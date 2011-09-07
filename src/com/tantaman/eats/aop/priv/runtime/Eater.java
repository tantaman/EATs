/*
*   Copyright 2010 Matthew Crinklaw-Vogt
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*  limitations under the License.
*/

package com.tantaman.eats.aop.priv.runtime;

import java.lang.reflect.Method;
import java.util.Map;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;

import com.tantaman.eats.aop.priv.common.AnnotationProvider;
import com.tantaman.eats.aop.priv.common.WrappedAnnotation;
import com.tantaman.eats.aop.priv.common.config.Config;
import com.tantaman.eats.aop.priv.common.nutrients.NutrientInvoker;
import com.tantaman.eats.aop.priv.common.nutrients.NutrientProvider;

public class Eater {
	// TODO: don't keep the keys as strings.
	private final Map<String, Class<?>> mNutrients;
	private final EatsClassloader mClassLoader;
	private Method mNutrientSetSigMethod;
	
	public Eater(Config pConfig, EatsClassloader pClassLoader) throws ClassNotFoundException {
		mNutrients = pConfig.getAnnotationMapping();
		mClassLoader = pClassLoader;
	}
	
	public void process(CtClass pClazz) throws ClassNotFoundException {
		CtMethod[] methods = pClazz.getDeclaredMethods();
		
		/*
		 * Get annotations on the class
		 * Get Implementers
		 * Get Methods implemented by implementer.
		 */
		
		//CtMethod[] methodsCopy = Arrays.copyOf(methods, methods.length);
		for (CtMethod method : methods) {
			processMethod(pClazz, method);
		}
		
//		try {
//			CtClass [] nestedClasses = pClazz.getNestedClasses();
//			
//			for (CtClass clazz : nestedClasses) {
//				if (clazz != pClazz)
//					process(clazz);
//			}
//		} catch (NotFoundException e) {
//			e.printStackTrace();
//		}
	}
	
	private void processMethod(CtClass pClazz, CtMethod pMethod) throws ClassNotFoundException {
		if (pClazz.isInterface())
			return;
		for (Object annotation : pMethod.getAnnotations()) {
			try {
				String annotationAsString =
					(String)(annotation.getClass().getMethod("toString").invoke(annotation));
				String annotationName = WrappedAnnotation.determineClassName(annotationAsString);
				
				Class<?> nutrientFactoryClass = mNutrients.get(annotationName);
				Object nutrient = null;
				
				if (nutrientFactoryClass != null) {
					try {
						nutrient = mClassLoader
							.loadClass(nutrientFactoryClass.getName())
							.getMethod("create", Object.class)
							.invoke(null, annotation);
					} catch (Exception e) {
						//e.printStackTrace();
						nutrient = mClassLoader
							.loadClass(nutrientFactoryClass.getName())
							.newInstance();
					}
					// TODO eats class loader...
					// TODO: some nutrient are pretty static in nature
					// so we shouldn't always create one of these per method.
					instrument(nutrient, pClazz, pMethod,
							annotation);
					
					if (mNutrientSetSigMethod == null) {
						mNutrientSetSigMethod = mClassLoader
							.loadClass("com.tantaman.eats.aop.pub.nutrients.Nutrient")
							.getMethod("setCtMethodSig", Object.class);
					}
					
					mNutrientSetSigMethod.invoke(nutrient, (Object)pMethod.getParameterTypes());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void instrument(Object pNutrient, CtClass pClazz, CtMethod pMethod,
			Object pAnnotation) {
		try {
			int annotationID = (Integer)mClassLoader.loadClass(AnnotationProvider.class.getName())
				.getMethod("put", Object.class).invoke(null, pAnnotation);
			int nutrientID = (Integer)mClassLoader.loadClass(NutrientProvider.class.getName())
				.getMethod("put", Object.class).invoke(null, pNutrient);
			
			int previousModifiers = pMethod.getModifiers();
			String previousName = pMethod.getName();
			pMethod.setModifiers(Modifier.PRIVATE);
			String newName = "__EATS__" + pMethod.getName();
			pMethod.setName(newName);
			
			String newMethodBodySrc = "{Object instruRetVal = ";
			newMethodBodySrc += NutrientInvoker.class.getName() + ".invokeBefore(" 
				+ nutrientID + ", " + annotationID + ", $0, $args, \"" + previousName + 
				"\", \"" + newName + "\");";
			newMethodBodySrc += "if (instruRetVal != null) return ($r)instruRetVal;";
			newMethodBodySrc += "Object retVal = ($r)" + newName + "($$);";
			newMethodBodySrc += "instruRetVal = " + NutrientInvoker.class.getName() + ".invokeAfter(" 
				+ nutrientID + ", " + annotationID + ", $0, $args, retVal, \"" + previousName + 
				"\", \"" + newName + "\");";
			newMethodBodySrc += "if (instruRetVal != null) return ($r)instruRetVal;";
			newMethodBodySrc += "return ($r)retVal; }";
			
			CtMethod wrapperMethod = CtNewMethod.make(
					previousModifiers,
					pMethod.getReturnType(),
					previousName,
					pMethod.getParameterTypes(),
					pMethod.getExceptionTypes(),
					newMethodBodySrc,
					pClazz
					);
			pClazz.addMethod(wrapperMethod);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
