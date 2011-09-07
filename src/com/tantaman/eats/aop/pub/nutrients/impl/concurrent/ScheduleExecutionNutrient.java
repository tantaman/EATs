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

import java.util.concurrent.ScheduledExecutorService;

import com.tantaman.eats.aop.priv.common.MethodCall;
import com.tantaman.eats.aop.pub.annotations.concurrent.ScheduleExecution;
import com.tantaman.eats.aop.pub.nutrients.Nutrient;
import com.tantaman.eats.tools.Constants;

public class ScheduleExecutionNutrient extends Nutrient {
	private final RunnableCreator mRunnableCreator;
	private final ScheduledExecutorService mExecutor;
	private final ScheduleExecution mAnnotation;
	
	public static Nutrient create(Object pAnnotation) {
		return new ScheduleExecutionNutrient((ScheduleExecution)pAnnotation);
	}
	
	public ScheduleExecutionNutrient(ScheduleExecution pAnnotation) {
		mRunnableCreator = new RunnableCreator();
		mAnnotation = pAnnotation;
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
	public Object nourishBefore(MethodCall pMethodCall) {
		Runnable r = mRunnableCreator.createRunnable(pMethodCall, getMethodSignature());
		
		if (r != null) {
			switch (mAnnotation.type()) {
			case FIXED_DELAY:
				mExecutor
					.scheduleWithFixedDelay(r, mAnnotation.initialDelay(), mAnnotation.subsequentDelay(), mAnnotation.timeUnit());
				break;
			case FIXED_RATE:
				mExecutor
					.scheduleAtFixedRate(r, mAnnotation.initialDelay(), mAnnotation.subsequentDelay(), mAnnotation.timeUnit());
				break;
			case SINGLE:
				mExecutor.schedule(r, mAnnotation.initialDelay(), mAnnotation.timeUnit());
				break;
			}
		}
		
		return Constants.CONSTANT_OBJECT;
	}

	@Override
	public Object nourishAfter(MethodCall pMethodCall, Object pResult) {
		// TODO Auto-generated method stub
		return null;
	}
}
