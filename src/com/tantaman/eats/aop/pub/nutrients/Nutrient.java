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
 *   limitations under the License.
 */

package com.tantaman.eats.aop.pub.nutrients;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.tantaman.eats.aop.priv.common.MethodCall;

// TODO: make non-blocking w/ compare and swaps?
/**
 * getMethodSignature is a part of nutrient and not
 * MethodCall because determining the method signature is a very expensive operation.
 */
public abstract class Nutrient {
	public abstract Object nourishBefore(MethodCall pMethodCall);
	public abstract Object nourishAfter(MethodCall pMethodCall, Object pResult);

	private Object [] mCtMethodSignature = null;
	private Class<?> [] mMethodSignature;
	private final Object mLockObject = new Object();
	
	
	private static final Map<String, Class<?>> PRIMITIVES = new HashMap<String, Class<?>>();
	
	static {
		PRIMITIVES.put(int.class.getName(), int.class);
		PRIMITIVES.put(byte.class.getName(), byte.class);
		PRIMITIVES.put(long.class.getName(), long.class);
		PRIMITIVES.put(char.class.getName(), char.class);
		PRIMITIVES.put(short.class.getName(), short.class);
		PRIMITIVES.put(float.class.getName(), float.class);
		PRIMITIVES.put(double.class.getName(), double.class);
	};

	public void setCtMethodSig(Object pSig) {
		synchronized (mLockObject) {
			mCtMethodSignature = (Object [])pSig;
		}
	}

	public Class<?> [] getMethodSignature() {
		synchronized (mLockObject) {
			if (mMethodSignature == null) {
				initMethodSignature();
			}
		}

		return mMethodSignature;
	}

	private void initMethodSignature() {
		mMethodSignature = new Class<?> [mCtMethodSignature.length];
		if (mCtMethodSignature.length > 0) {
			Method getNameMethod;
			try {
				getNameMethod = mCtMethodSignature[0].getClass().getMethod("getName");
				for (int i = 0; i < mCtMethodSignature.length; ++i) {
						try {
							// TODO: need to make class loader smarter so it can handle primitives...
							String className = (String)getNameMethod.invoke(mCtMethodSignature[i]);
							Class<?> primitive = PRIMITIVES.get(className);
							if (primitive != null) {
								mMethodSignature[i] = primitive;
							} else {
								mMethodSignature[i] = Class.forName(className);
							}
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
				}
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			}
		}

		mCtMethodSignature = null;
	}
}
