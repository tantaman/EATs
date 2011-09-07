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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.log4j.Logger;

import com.google.common.collect.MapMaker;
import com.tantaman.eats.aop.priv.common.MethodCall;
import com.tantaman.eats.aop.pub.annotations.concurrent.CombineInvocations;
import com.tantaman.eats.aop.pub.nutrients.Nutrient;
import com.tantaman.eats.tools.Constants;
import com.tantaman.eats.tools.concurrent.IScheduledExecutorProvider;
import com.tantaman.eats.tools.concurrent.throttler.AccumulativeRunnable;
import com.tantaman.eats.tools.concurrent.throttler.InvocationCombiner;

// TODO: make a configurable version that can pass the entire list of parameters
// instead of just the last one?
public class CombineInvocationsNutrient extends Nutrient {
	private AtomicReference<Method> mCachedMethodReference;
	private final Map<Object, InvocationCombiner<Object []>> mThrottlers;
	private static final Logger LOGGER = Logger.getLogger(CombineInvocationsNutrient.class);
	private static final String ERROR_MSG = "Could not throttle.  Executing in calling thread.";

	public static Nutrient create(Object pAnnotation) {
		return new CombineInvocationsNutrient((CombineInvocations)pAnnotation);
	}

	public CombineInvocationsNutrient(CombineInvocations pAnnotation) {
		mCachedMethodReference = new AtomicReference<Method>();
		
		mThrottlers = new MapMaker().weakKeys().makeMap();
	}

	@Override
	public Object nourishBefore(final MethodCall pMethodCall) {
		Method m = mCachedMethodReference.get();

		if (m == null) {
			initialize(pMethodCall);
		}
		
		InvocationCombiner<Object []> throttler = mThrottlers.get(pMethodCall.getCalledObject());
		if (throttler == null) {
			// TODO: what is the best to synchronize on?
			// Not safe to synch on pMethodCall.getCalledObject() since
			// we have no idea if anyone else is synching on that object.
			synchronized (mCachedMethodReference) {
				throttler = mThrottlers.get(pMethodCall.getCalledObject());
				if (throttler == null) {
					try {
						throttler = createThrottler(pMethodCall, mCachedMethodReference.get());
						mThrottlers.put(pMethodCall.getCalledObject(), throttler);
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		if (throttler != null) {
			throttler.invoke(pMethodCall.getParameters());
			return Constants.CONSTANT_OBJECT;
		} else {
			LOGGER.error(ERROR_MSG);
		}

		return null;
	}

	private void initialize(final MethodCall pMethodCall) {
		synchronized (mCachedMethodReference) {
			Method m = mCachedMethodReference.get();
			if (m == null) {
				try {
					m = pMethodCall.getCalledObjectClass()
					.getDeclaredMethod(
							pMethodCall.getAlteredMethodName(),
							getMethodSignature());
					m.setAccessible(true);
					mCachedMethodReference.set(m);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
				
				InvocationCombiner<Object[]> throttler;
				try {
					throttler = createThrottler(pMethodCall, m);
					mThrottlers.put(pMethodCall.getCalledObject(), throttler);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private final InvocationCombiner<Object []> createThrottler(
			final MethodCall pMethodCall,
			final Method meth) throws InstantiationException, IllegalAccessException {
		CombineInvocations annotation = ((CombineInvocations)pMethodCall.getAnnotation());
		String threadPrefix = annotation.threadPrefix();
		if (threadPrefix == CombineInvocations.DEFAULT_THREAD_PREFIX) {
			threadPrefix += "-" + pMethodCall.getCalledObjectClass().getName() + "." + pMethodCall.getMethodName();
		}
		IScheduledExecutorProvider<CombineInvocations> executorProvider
			= annotation.executorProvider().newInstance();
		ScheduledExecutorService executor = executorProvider.getExecutorForMethod(annotation);
		
		InvocationCombiner<Object []> throttler = 
			new InvocationCombiner<Object []>(
					new AccumulativeRunnable<Object []>() {
						@Override
						public void run(LinkedList<Object []> pParams) {
							try {
								if (getMethodSignature().length > 0)
									meth.invoke(pMethodCall.getCalledObject(), pParams.getLast());
								else
									meth.invoke(pMethodCall.getCalledObject());
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						}
					}, executor
					);
			
			return throttler;
	}

	@Override
	public Object nourishAfter(MethodCall pMethodCall, Object pResult) {
		return null;
	}
}
