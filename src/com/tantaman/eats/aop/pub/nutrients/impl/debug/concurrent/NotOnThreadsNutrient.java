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

package com.tantaman.eats.aop.pub.nutrients.impl.debug.concurrent;

import org.apache.log4j.Logger;

import com.tantaman.eats.aop.priv.common.MethodCall;
import com.tantaman.eats.aop.pub.annotations.debug.concurrent.NotOnThreads;
import com.tantaman.eats.aop.pub.nutrients.Nutrient;
import com.tantaman.eats.tools.debug.StackTraceUtils;

public class NotOnThreadsNutrient extends Nutrient {
	private static final Logger LOGGER = Logger.getLogger(OnThreadNutrient.class);
	private static final String PREFIX = " was run on thread ";
	
	public static NotOnThreadsNutrient create() {
		return new NotOnThreadsNutrient();
	}
	
	@Override
	public Object nourishBefore(MethodCall pMethodCall) {
		NotOnThreads anno = (NotOnThreads)pMethodCall.getAnnotation();
		
		String currThreadName = Thread.currentThread().getName();
		for (String threadName : anno.value()) {
			if (currThreadName.startsWith(threadName)) {
				reportError(threadName, pMethodCall.getMethodName());
				return null;
			}
		}
		
		return null;
	}
	
	private void reportError(String pOffendingThread, String pMethodName) {
		LOGGER.debug(StackTraceUtils.toString(Thread.currentThread().getStackTrace(), "\n" 
				+ pMethodName + PREFIX + pOffendingThread));
	}
	
	@Override
	public Object nourishAfter(MethodCall pMethodCall, Object pResult) {
		return null;
	}
}
