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

import junit.framework.Assert;

import org.junit.Test;

import com.tantaman.eats.aop.pub.annotations.concurrent.PutOnExecutor;
import com.tantaman.eats.aop.pub.nutrients.impl.concurrent.DefaultExecutorProvider;

public class PutOnExecutorTest {
	private ThreadLocal<String> mVerifier = new ThreadLocal<String>();
	
	@Test
	public void putOnDefaultExecutorTest() {
		TestClass tc = new TestClass();
		
		tc.methodToThreadOut();
		Assert.assertEquals(null, mVerifier.get());
	}
	
	@Test
	public void putOnCustomExectuorTest() {
		TestClass tc = new TestClass();
		
		tc.otherMethodToThreadOut();
		tc.otherMethodToThreadOut();
		tc.otherMethodToThreadOut();
		Assert.assertEquals(null, mVerifier.get());
	}
	
	private class TestClass {
		@PutOnExecutor(threadPrefix="SomePrefix")
		private void methodToThreadOut() {
			mVerifier.set("bleh");
			Assert.assertTrue(Thread.currentThread().getName().contains(
					"SomePrefix"));
		}
		
		@PutOnExecutor(executorProvider=DefaultExecutorProvider.class, numThreads=2, threadPrefix="SomeOtherPrefix")
		private void otherMethodToThreadOut() {
			mVerifier.set("blah");
			Assert.assertTrue(Thread.currentThread().getName().contains(
					"SomeOtherPrefix"));
		}
	}
}
