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
import com.tantaman.eats.aop.pub.annotations.debug.concurrent.NotOnThread;
import com.tantaman.eats.aop.pub.nutrients.Nutrient;
import com.tantaman.eats.tools.debug.StackTraceUtils;

public class NotOnThreadNutrient extends Nutrient {
	private static final Logger LOGGER = Logger.getLogger(NotOnThreadNutrient.class);
	private static final String PREFIX = " was run on thread ";
	
	public static Nutrient create(Object pAnnotation) {
		return new NotOnThreadNutrient();
	}
	
	@Override
	public Object nourishBefore(MethodCall pMethodCall) {
		NotOnThread anno = (NotOnThread)pMethodCall.getAnnotation();
		Thread currentThread = Thread.currentThread();
		if (currentThread.getName().startsWith(anno.value())) {
			String stackTrace = StackTraceUtils.toString(currentThread.getStackTrace(),
					pMethodCall.getMethodName() + PREFIX + currentThread.getName());
			LOGGER.debug("\n" + stackTrace);
		}
		
		return null;
	}
	
	@Override
	public Object nourishAfter(MethodCall pMethodCall, Object pResult) {
		return null;
	}
}
