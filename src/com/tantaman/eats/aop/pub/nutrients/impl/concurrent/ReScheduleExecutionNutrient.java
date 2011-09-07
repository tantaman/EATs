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

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import com.google.common.collect.MapMaker;
import com.tantaman.eats.aop.priv.common.MethodCall;
import com.tantaman.eats.aop.pub.annotations.concurrent.ReScheduleExecution;
import com.tantaman.eats.aop.pub.nutrients.Nutrient;
import com.tantaman.eats.tools.Constants;

public class ReScheduleExecutionNutrient extends Nutrient {
	private final Map<Object, FutureHolder> mFutures;
	private final RunnableCreator mRunnableCreator;
	private final ScheduledExecutorService mExecutor;
	private final ReScheduleExecution mAnnotation;
	
	private static class FutureHolder {
		private ScheduledFuture<?> mFuture;
	}
	
	public static Nutrient create(Object pAnnotation) {
		return new ReScheduleExecutionNutrient((ReScheduleExecution)pAnnotation);
	}
	
	public ReScheduleExecutionNutrient(ReScheduleExecution pAnnotation) {
		mAnnotation = pAnnotation;
		mFutures = new MapMaker().weakKeys().makeMap();
		mRunnableCreator = new RunnableCreator();
		
		ScheduledExecutorService exec = null;
		try {
			exec = pAnnotation.executorProvider().newInstance().getExecutorForMethod(pAnnotation);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		mExecutor = exec;
	}

	@Override
	public Object nourishAfter(MethodCall pMethodCall, Object pResult) {
		return null;
	}

	@Override
	public Object nourishBefore(MethodCall pMethodCall) {
		FutureHolder holder = mFutures.get(pMethodCall.getCalledObject());
		if (holder == null) {
			// Sycnhing on pMethodCall.getCalledObject would be the fastest thing
			// but it would cause Eats to impact the synchronization policies of client code.
			synchronized (mFutures) {
				holder = new FutureHolder();
				mFutures.put(pMethodCall.getCalledObject(), holder);
			}
		}
		
		synchronized (holder) {
			holder.mFuture = reSchedule(pMethodCall, holder.mFuture);
		}
		
		return Constants.CONSTANT_OBJECT;
	}
	
	// TODO: this code is just like scheduleExecution.
	// make them share a mixin or common base.
	// TODO: appropriately handle null cases
	private ScheduledFuture<?> reSchedule(MethodCall pMethodCall, ScheduledFuture<?> pFuture) {
		if (pFuture != null) {
			pFuture.cancel(true);
		}
		
		ScheduledFuture<?> scheduledFuture = null;
		
		Runnable task = mRunnableCreator.createRunnable(pMethodCall, getMethodSignature());
		
		if (task != null) {
			switch (mAnnotation.type()) {
			case FIXED_DELAY:
				scheduledFuture = mExecutor
					.scheduleWithFixedDelay(task, mAnnotation.initialDelay(), mAnnotation.subsequentDelay(), mAnnotation.timeUnit());
				break;
			case FIXED_RATE:
				scheduledFuture = mExecutor
					.scheduleAtFixedRate(task, mAnnotation.initialDelay(), mAnnotation.subsequentDelay(), mAnnotation.timeUnit());
				break;
			case SINGLE:
				scheduledFuture = mExecutor.schedule(task, mAnnotation.initialDelay(), mAnnotation.timeUnit());
				break;
			}
		}
		
		return scheduledFuture;
	}
}
