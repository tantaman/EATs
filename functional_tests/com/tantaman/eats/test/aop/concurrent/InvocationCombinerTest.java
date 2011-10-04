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
 *  limitations under the License.
 */

package com.tantaman.eats.test.aop.concurrent;

import junit.framework.Assert;

import org.junit.Test;

import com.tantaman.eats.aop.pub.annotations.concurrent.CombineInvocations;

/**
 * @author mlaw
 *
 */
public class InvocationCombinerTest {
	private final ThreadLocal<Integer> mVerifier = new ThreadLocal<Integer>();
	private final Object mCoordinator = new Object();
	private final Object mCoordinator2 = new Object();
	private volatile static int numInvocations = 0;
	private volatile static int lastParamReceived = 0;
	private volatile static boolean ran = false;
	private volatile static boolean complete = false;

	@Test
	public void singleThreadedThrottleTest() {
		TestClass tc = new TestClass();

		// Keep the first invocation blocked while
		// we submit subsequent invocations
		synchronized (mCoordinator2) {
			
			// Wait for the first invocation to start before 
			// submitting more invocations.  Otherwise,
			// our invocations may come quick enough
			// to collapse into just 1 invocation instead of 2.
			synchronized (mCoordinator) {
				tc.noParamMethodToThrottle();
				while (!ran) {
					try {
						mCoordinator.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}


			for (int i = 0; i < 9; ++i) {
				tc.noParamMethodToThrottle();
			}
			
			// Wait for the second invocation to complete...
			while (!complete) {
				try {
					mCoordinator2.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		// Sleep to try to make sure there are no additional invocations.
		// not sure if there is any other way to accomplish this without
		// actually getting a reference to the combiner.
		// yeah... this is bad, but is there really a better way w/o
		// modifying the code under test?  The issue is that, if things aren't working,
		// there could be more invocations queued up in the pipe.
		// The only way to test that without a sleep
		// would be to add an operation to get the queue size from the combiner
		// but the combiner is added via an annotation, so we have no way to get it.
		try {
			Thread.sleep(150);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Assert.assertEquals(null, mVerifier.get());
		System.out.println(numInvocations);
		Assert.assertTrue(numInvocations == 2);

		numInvocations = 0;
		ran = false;
		complete = false;
		synchronized (mCoordinator2) {
			// Wait for the first invocation to 
			// start running
			synchronized (mCoordinator) {
				tc.paramedMethodToThrottle(0, null);
				while (!ran) {
					try {
						mCoordinator.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
			// submit additional invocations
			for (int i = 1; i < 10; ++i) {
				tc.paramedMethodToThrottle(i, null);
			}
			
			while (!complete) {
				try {
					mCoordinator2.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		try {
			// yeah... this is bad, but is there really a better way w/o
			// modifying the code under test?  The issue is that, if things aren't working,
			// there could be more invocations queued up in the pipe.
			// The only way to test that without a sleep
			// would be to add an operation to get the queue size from the combiner
			// but the combiner is added via an annotation, so we have no way to get it.
			Thread.sleep(150);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Assert.assertEquals(null, mVerifier.get());
		Assert.assertTrue(numInvocations == 2);
		Assert.assertEquals(9, lastParamReceived);

		numInvocations = lastParamReceived = 0;
	}

	@Test
	public void multiThreadedThrottleTest() {
		final TestClass tc = new TestClass();
		Runnable r = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 10; ++i) {

				}
			}
		};
		Thread t = new Thread();
	}

	private class TestClass {
		@CombineInvocations
		public void noParamMethodToThrottle() {
			synchronized (mCoordinator) {
				ran = true;
				mCoordinator.notifyAll();				
			}

			synchronized (mCoordinator2) {
				Integer currVal = mVerifier.get();
				if (currVal == null)
					currVal = 0;

				mVerifier.set(++currVal);
				++numInvocations;
				if (numInvocations > 1) {
					complete = true;
					mCoordinator2.notifyAll();
				}
			}
		}

		@CombineInvocations
		public void paramedMethodToThrottle(int pInt, Object pObject) {
			synchronized (mCoordinator) {
				mCoordinator.notifyAll();
				ran = true;
			}
			
			synchronized (mCoordinator2) {
				lastParamReceived = pInt;
				mVerifier.set(1);
				++numInvocations;
				
				if (numInvocations > 1) {
					complete = true;
					mCoordinator2.notifyAll();
				}
			}
		}
	}
}
