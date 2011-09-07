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

package com.tantaman.eats.aop.priv.common;

import java.util.HashMap;
import java.util.Map;

public class WrappedAnnotation {
	private final Map<String, String> mAttributes;
	private final String mStringRep;
	private final Class<?> mAnnotationClass;
	
	public WrappedAnnotation(String pAnnotationAsString) throws ClassNotFoundException {
		mAttributes = new HashMap<String, String>();
		mStringRep = pAnnotationAsString;
		mAnnotationClass = determineClass(pAnnotationAsString);
		
		//explodeAttributes();
	}
	
	private static Class<?> determineClass(String pAnnotationAsString) throws ClassNotFoundException {
		String annotationClass = determineClassName(pAnnotationAsString);
		
		return Class.forName(annotationClass);
	}
	
	public static String determineClassName(String pAnnotationAsString) {
		int endIndex = pAnnotationAsString.indexOf("(");
		if (endIndex < 0) {
			endIndex = pAnnotationAsString.length();
		}
		
		return pAnnotationAsString.substring(1, endIndex);
	}
	
	@Override
	public String toString() {
		return mStringRep;
	}
	
	public Class<?> getAnnotationClass() {
		return mAnnotationClass;
	}
	
	public String get(String pAttribute) {
		return mAttributes.get(pAttribute);
	}
	
	public String [] getAsStringArray(String pAttribute) {
		return mAttributes.get(pAttribute).split(",");
	}
	
	public int getAsInt(String pAttribute) {
		return Integer.parseInt(mAttributes.get(pAttribute));
	}
	
	public long getAsLong(String pAttribute) {
		return Long.parseLong(mAttributes.get(pAttribute));
	}
	
	public float getAsFloat(String pAttribute) {
		return Float.parseFloat(mAttributes.get(pAttribute));
	}
	
	public double getAsDouble(String pAttribute) {
		return Double.parseDouble(mAttributes.get(pAttribute));
	}
}
