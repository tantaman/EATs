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

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tantaman.eats.aop.priv.common.MethodCall;
import com.tantaman.eats.aop.pub.annotations.debug.concurrent.ConcurrentAccesses;
import com.tantaman.eats.aop.pub.nutrients.Nutrient;
import com.tantaman.eats.tools.debug.StackInformation;
import com.tantaman.eats.tools.debug.StackTraceUtils;

public class ConcurrentAccessesNutrient extends Nutrient {
	private static final Logger LOGGER = Logger.getLogger(ConcurrentAccessesNutrient.class);
	
	public static Nutrient create(Object pAnnotation) {
		return new ConcurrentAccessesNutrient();
	}

	private static final String MESSAGE_PRE = " had ";
	private static final String MESSAGE_POST = " concurrent accesses instead of ";
	
	private long mConcurrentAccesses = 0;
	private Map<Long, StackInformation> mAccessInformation = new HashMap<Long, StackInformation>();

	//private ReadWriteLock mRWLock = new ReentrantReadWriteLock();
	//private final AtomicLong mConcurrentAccesses = new AtomicLong(0);

	@Override
	public synchronized Object nourishBefore(MethodCall pMethodCall) {
		Thread currentThread = Thread.currentThread();
		StackTraceElement [] stackTrace = currentThread.getStackTrace();
		String threadName = currentThread.getName();
		StackInformation information = new StackInformation(stackTrace, threadName, "");
		addToAccessList(currentThread.getId(), information);

		int concurrencyLevel = ((ConcurrentAccesses)pMethodCall.getAnnotation()).concurrencyLevel();
		if (++mConcurrentAccesses > concurrencyLevel) {
			LOGGER.debug(pMethodCall.getMethodName() + MESSAGE_PRE + mConcurrentAccesses + MESSAGE_POST + concurrencyLevel + "\n" + StackTraceUtils.toString(mAccessInformation.values()));
		}
		
		return null;
	}

	@Override
	public synchronized Object nourishAfter(MethodCall pMethodCall, Object pResult) {
		removeFromAccessList(Thread.currentThread().getId());
		--mConcurrentAccesses;
		return null;
	}

	private void removeFromAccessList(long pThreadID) {
		mAccessInformation.remove(pThreadID);
	}

	private void addToAccessList(long pThreadID, StackInformation pStackInformation) {
		mAccessInformation.put(pThreadID, pStackInformation);
	}
}
