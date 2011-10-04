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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.Test;

import com.tantaman.eats.aop.pub.annotations.concurrent.Fold;
import com.tantaman.eats.aop.pub.annotations.concurrent.Fold.EFoldingPolicy;

public class FoldingTest {
	private final Object mCoordinator = new Object();
	private static int invocations = 0;
	private final CountDownLatch latch = new CountDownLatch(4);
	
	@Test
	public void testFolding() {
		TestClass tc = new TestClass();
		synchronized (mCoordinator) {
			for (int i = 0; i < 20; ++i) {
				tc.foldMe(i / 5, mCoordinator);
			}
		}
		
		try {
			latch.await(150, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Assert.assertEquals(4, invocations);
	}
	
	private class TestClass {
		@Fold(foldingPolicy=EFoldingPolicy.REJECT)
		public void foldMe(int bleh, Object blah) {
			synchronized (mCoordinator) {
				++invocations;
				latch.countDown();
			}
		}
	}
}
