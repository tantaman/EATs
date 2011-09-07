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

import org.apache.log4j.Logger;

import com.tantaman.eats.aop.priv.common.MethodCall;
import com.tantaman.eats.aop.pub.annotations.concurrent.PutOnExecutor;
import com.tantaman.eats.aop.pub.nutrients.Nutrient;
import com.tantaman.eats.tools.Constants;

public class PutOnExecutorNutrient extends Nutrient {
	public static Nutrient create(Object pAnnotation) {
		return new PutOnExecutorNutrient((PutOnExecutor)pAnnotation);
	}
	
	private final ExecutorService mExecutor;
	private final RunnableCreator mRunnableCreator;
	private static final Logger LOGGER = Logger.getLogger(PutOnExecutorNutrient.class);
	private static final String ERROR_MSG = "Could not submit to executor.  Running task on calling thread.";

	public PutOnExecutorNutrient(PutOnExecutor pAnnotation) {
		ExecutorService exec = null;
		try {
			exec = pAnnotation.executorProvider().newInstance().getExecutorForMethod(pAnnotation);
		} catch (Exception e) {
			e.printStackTrace();
		}

		mExecutor = exec;
		
		mRunnableCreator = new RunnableCreator();
	}

	@Override
	public Object nourishBefore(final MethodCall pMethodCall) {
		Runnable task = mRunnableCreator.createRunnable(pMethodCall, getMethodSignature());
		if (task != null) {
			mExecutor.submit(task);
		} else {
			LOGGER.error(ERROR_MSG + " task: "
					+ pMethodCall.getCalledObjectClass().getName() + "."
					+ pMethodCall.getMethodName());
			return null;
		}
		return Constants.CONSTANT_OBJECT;
	}
	
	@Override
	public Object nourishAfter(MethodCall pMethodCall, Object pResult) {
		return null;
	}
}
