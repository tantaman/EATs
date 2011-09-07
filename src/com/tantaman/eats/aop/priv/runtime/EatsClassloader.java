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

import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import com.tantaman.eats.aop.priv.common.config.JavaConfig;

// TODO: a few lines are ripped from JBoss AOP...
// is that ok?
public class EatsClassloader extends ClassLoader {
	private final ClassPool mPool;
	private final Eater mEater;
	private static final String NOT_FOUND_MSG = "Class not found";
	private boolean mInitialized = false;

	public EatsClassloader(ClassLoader pParent) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		super(pParent);
		mPool = new ClassPool();
		mPool.appendSystemPath();
		mEater = new Eater(JavaConfig.loadConfig(System.getProperty("eats.config")), this);
	}

	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve)
	throws ClassNotFoundException {
		// Have we already loaded the class?
		Class<?> clazz = findLoadedClass(name);
		if (clazz != null)
		{
			if (resolve) resolveClass(clazz);
			return clazz;
		}

		// Is it a jre class?
		clazz = loadClassByDelegation(name);
		if (clazz != null)
		{
			if (resolve) resolveClass(clazz);
			return clazz;
		}

		if (!mInitialized) {
			initialize();
		}
		
		CtClass ctClass;
		try {
			ctClass = mPool.get(name);
			mEater.process(ctClass);
			byte [] bytes = ctClass.toBytecode();
			return defineClass(name, bytes, 0, bytes.length);
		} catch (NotFoundException e) {
			throw new ClassNotFoundException(NOT_FOUND_MSG, e);
		} catch (IOException e) {
			throw new ClassNotFoundException(NOT_FOUND_MSG, e);
		} catch (CannotCompileException e) {
			throw new ClassNotFoundException(NOT_FOUND_MSG, e);
		}
	}

	private synchronized void initialize() {
		mInitialized = true;
		try {
			loadClass("com.tantaman.eats.aop.priv.common.ConfiguratorWrapper")
				.getMethod("configure")
				.invoke(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// This seems dumb...
	protected Class<?> loadClassByDelegation(String name)
	throws ClassNotFoundException
	{
		// TODO: Only works for Sun for now
		if (name.startsWith("java.") || name.startsWith("javax.")
				|| name.startsWith("sun.") || name.startsWith("com.sun.")
				|| name.startsWith("org.apache.xerces.") || name.startsWith("org.xml.sax.")
				|| name.startsWith("org.w3c.dom.") || name.startsWith("apple.")
				|| name.startsWith("com.apple."))
			return getParent().loadClass(name);

		return null;
	}
}
