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

package com.tantaman.eats.test.aop.concurrent;

import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.Test;

import com.tantaman.eats.aop.pub.annotations.concurrent.ReScheduleExecution;

public class ReScheduleExecutionTest {
	private volatile int mRunCount;
	private class TestClass {
		@ReScheduleExecution(initialDelay=250, timeUnit=TimeUnit.MILLISECONDS)
		private void someMethod() {
			mRunCount++;
		}
	}
	
	// This test will certainly fail, one day, if it is just run enough times.
	@Test
	public void testReScheduling() {
		TestClass underTest = new TestClass();
		
		long prevtime = System.currentTimeMillis();
		int possibleAllowedRuns = 0;
		for (int i = 0; i < 100; ++i) {
			underTest.someMethod();
			if (System.currentTimeMillis() - prevtime >= 250) {
				++possibleAllowedRuns;
			}
			prevtime = System.currentTimeMillis();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		Assert.assertTrue("re-scheduler allowed an execution it should not have: " + mRunCount + " " + possibleAllowedRuns, mRunCount <= possibleAllowedRuns);
		
		mRunCount = 0;
		
		underTest.someMethod();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		underTest.someMethod();
		Assert.assertEquals(0, mRunCount);
	}
}
