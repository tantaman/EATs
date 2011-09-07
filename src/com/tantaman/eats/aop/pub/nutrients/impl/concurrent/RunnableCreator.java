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

package com.tantaman.eats.aop.pub.nutrients.impl.concurrent;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

import com.tantaman.eats.aop.priv.common.MethodCall;

public class RunnableCreator {
	private AtomicReference<Method> mCachedMethodReference 
	= new AtomicReference<Method>(); 

	public Runnable createRunnable(final MethodCall pMethodCall, Class<?> [] pMethodSignature) {
		Method method = mCachedMethodReference.get();
		if (method == null) {
			synchronized (mCachedMethodReference) {
				method = mCachedMethodReference.get();
				if (method == null) {
					try {
						method = pMethodCall.getCalledObjectClass()
						.getDeclaredMethod(
								pMethodCall.getAlteredMethodName(),
								pMethodSignature);
						method.setAccessible(true);
						mCachedMethodReference.set(method);
					} catch (SecurityException e) {
						e.printStackTrace();
						return null;
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
						return null;
					}
				}
			}
		}

		final Method meth = method;
		Runnable task = new Runnable() {
			@Override
			public void run() {
				try {
					meth.invoke(pMethodCall.getCalledObject(), pMethodCall.getParameters());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		return task;
	}
}
