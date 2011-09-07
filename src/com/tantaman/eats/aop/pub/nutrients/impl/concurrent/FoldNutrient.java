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

import java.util.concurrent.ExecutorService;

import com.tantaman.eats.aop.priv.common.MethodCall;
import com.tantaman.eats.aop.pub.annotations.concurrent.Fold;
import com.tantaman.eats.aop.pub.nutrients.Nutrient;
import com.tantaman.eats.tools.Constants;
import com.tantaman.eats.tools.cache.CacheKey;

public class FoldNutrient extends Nutrient {
	private final ExecutorService mService;
	private final RunnableCreator mRunnableCreator;
	
	public static Nutrient create(Object pAnnotation) {
		return new FoldNutrient((Fold)pAnnotation);
	}
	
	public FoldNutrient(Fold pAnnotation) {
		mRunnableCreator = new RunnableCreator();
		ExecutorService exec = null;
		try {
			exec = pAnnotation.executorProvider().newInstance().getExecutorForMethod(pAnnotation);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		mService = exec;
	}
	
	@Override
	public Object nourishAfter(MethodCall pMethodCall, Object pResult) {
		return null;
	}

	@Override
	public Object nourishBefore(MethodCall pMethodCall) {
		CacheKey key = new CacheKey(pMethodCall.getCalledObject(), pMethodCall.getParameters());
		
		Runnable task = mRunnableCreator.createRunnable(pMethodCall, getMethodSignature());
		CustomRunnable wrappedTask = new CustomRunnable(key, task);
		mService.submit(wrappedTask);
		
		return Constants.CONSTANT_OBJECT;
	}
	
	private static class CustomRunnable implements Runnable {
		private final CacheKey mKey;
		private final Runnable mRunnable;
		public CustomRunnable(CacheKey pKey, Runnable pRunnable) {
			mKey = pKey;
			mRunnable = pRunnable;
		}
		
		@Override
		public int hashCode() {
			return mKey.hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null) return false;
			if (obj.getClass() == CustomRunnable.class) {
				return ((CustomRunnable)obj).mKey.equals(mKey);
			}
			return false;
		}
		
		@Override
		public final void run() {
			mRunnable.run();
		}
	}
}
