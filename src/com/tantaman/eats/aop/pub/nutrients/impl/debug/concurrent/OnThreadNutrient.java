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
import com.tantaman.eats.aop.pub.annotations.debug.concurrent.OnThread;
import com.tantaman.eats.aop.pub.nutrients.Nutrient;
import com.tantaman.eats.tools.debug.StackTraceUtils;

public class OnThreadNutrient extends Nutrient {
	private static final Logger LOGGER = Logger.getLogger(OnThreadNutrient.class);
	
	private static final String MESSAGE_PRE = "Had access from thread: ";
	private static final String MESSAGE_POST = ", expected thread: ";
	
	public static Nutrient create(Object pAnnotation) {
		return new OnThreadNutrient();
	}
	
	@Override
	public Object nourishBefore(MethodCall pMethodCall) {
		OnThread anno = (OnThread)pMethodCall.getAnnotation();
		Thread currentThread = Thread.currentThread();
		if (!currentThread.getName().startsWith(anno.value())) {
			LOGGER.debug(StackTraceUtils.toString(currentThread.getStackTrace(), 
					MESSAGE_PRE + currentThread.getName() + MESSAGE_POST + anno.value()));
		}
		
		return null;
	}
	
	@Override
	public final Object nourishAfter(MethodCall pMethodCall, Object pResult) {
		return null;
	}
}
