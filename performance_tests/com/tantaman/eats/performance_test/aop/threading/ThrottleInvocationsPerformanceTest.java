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

package com.tantaman.eats.performance_test.aop.threading;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import com.tantaman.commons.concurrent.NamedThreadFactory;
import com.tantaman.commons.concurrent.throttler.AccumulativeRunnable;
import com.tantaman.commons.concurrent.throttler.InvocationCombiner;
import com.tantaman.eats.aop.pub.annotations.concurrent.CombineInvocations;

public class ThrottleInvocationsPerformanceTest {
	private static final int NUM_OBJECTS = 500;
	private static final int NUM_INVOCATIONS = 1000;
	private static final int NUM_THREADS = 5;
	
	@Test
	public void testCombineAnnotationPerformanceNoPriming() {
		TestClass tc = new TestClass();
		
		
	}

	@Test
	public void testCombineAnnotationPerformanceWithPriming() {
		final AtomicLong totalTime = new AtomicLong(0);
		final TestClass o1 = new TestClass();
		final ClassA o2 = new ClassA();
		final ClassB o3 = new ClassB();
		
		final List<TestClass> tcs = new ArrayList<TestClass>();
		for (int i = 0; i < NUM_OBJECTS; ++i) {
			tcs.add(new TestClass());
			tcs.get(i).methodToCall(o1, o2, o3);
		}
		
		Runnable r = createCombinerAnnoRunnable(totalTime, tcs, o1, o2, o3);
		
		submitTasksAndWait(r);
		
		System.out.println("Test ran in total thread time of: " + totalTime.get());
	}
	
	@Test
	public void testCombinePerformance() {
		final AtomicLong totalTime = new AtomicLong(0);
		final TestClass o1 = new TestClass();
		final ClassA o2 = new ClassA();
		final ClassB o3 = new ClassB();
		
		final List<InvocationCombiner<Object []>> throts = new ArrayList<InvocationCombiner<Object []>>();
		for (int i = 0; i < NUM_OBJECTS; ++i) {
			final TestClass tc = new TestClass();
			throts.add(new InvocationCombiner<Object []>(
					new AccumulativeRunnable<Object []>() {
						@Override
						public void run(LinkedList<Object []> pParams) {
							Object [] params = pParams.getLast();
							// TODO: doing these casts isn't exactly fair.
							// change this.
							tc.unAnnotatedMethodToCall((TestClass)params[0], (ClassA)params[1], (ClassB)params[2]);
						}
					}
					, Executors.newScheduledThreadPool(1, new NamedThreadFactory("ThrottlePerfTest-Manual"))));
		}
		
		Runnable r = createThrottleRunnable(totalTime, throts, o1, o2, o3);
		
		submitTasksAndWait(r);
		
		System.out.println("NonAnnotated Test ran in total thread time of: " + totalTime.get());
	}
	
	private Runnable createCombinerAnnoRunnable(
			final AtomicLong totalTime,
			final List<TestClass> tcs,
			final TestClass o1, final ClassA o2, final ClassB o3) {
		return new Runnable() {
			@Override
			public void run() {
				long startTime = System.currentTimeMillis();
				for (int i = 0; i < NUM_INVOCATIONS; ++i) {
					for (TestClass tc : tcs) {
						tc.methodToCall(o1, o2, o3);
					}
				}
				long endTime = System.currentTimeMillis();
				addTime(totalTime, endTime - startTime);
			}
		};
	}
	
	private Runnable createThrottleRunnable(
			final AtomicLong totalTime,
			final List<InvocationCombiner<Object []>> throts,
			final Object o1, final Object o2, final Object o3) {
		return new Runnable() {
			@Override
			public void run() {
				long startTime = System.currentTimeMillis();
				for (int i = 0; i < NUM_INVOCATIONS; ++i) {
					for (InvocationCombiner<Object []> throt : throts) {
						Object [] params = {o1, o2, o3};
						throt.invoke(params);
					}
				}
				long endTime = System.currentTimeMillis();
				addTime(totalTime, endTime - startTime);
			}
		};
	}
	
	private void submitTasksAndWait(Runnable r) {
		ExecutorService threadPool = Executors.newFixedThreadPool(5);
		List<Future<?>> futures = new LinkedList<Future<?>>();
		for (int i = 0; i < NUM_THREADS; ++i) {
			Future<?> f = threadPool.submit(r);
			futures.add(f);
		}
		
		for (Future<?> f : futures) {
			try {
				f.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void addTime(AtomicLong pInitial, long pToAdd) {
		long initial;
		
		do {
			initial = pInitial.get();
		}
		while (!pInitial.compareAndSet(initial, initial + pToAdd));
	}
	
	private static class TestClass {
		@CombineInvocations
		private void methodToCall(TestClass o1, ClassA o2, ClassB o3) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		private void unAnnotatedMethodToCall(TestClass o1, ClassA o2, ClassB o3) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static class ClassA {
		
	}
	
	private static class ClassB extends ClassA {
		
	}
}
