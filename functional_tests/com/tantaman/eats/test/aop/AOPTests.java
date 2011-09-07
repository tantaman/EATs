package com.tantaman.eats.test.aop;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.tantaman.eats.test.aop.caching.WeakRefCacheTest;
import com.tantaman.eats.test.aop.concurrent.FoldingTest;
import com.tantaman.eats.test.aop.concurrent.InvocationCombinerTest;
import com.tantaman.eats.test.aop.concurrent.PutOnEDTTest;
import com.tantaman.eats.test.aop.concurrent.PutOnExecutorTest;
import com.tantaman.eats.test.aop.concurrent.ReScheduleExecutionTest;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	WeakRefCacheTest.class,
	PutOnEDTTest.class,
	PutOnExecutorTest.class,
	InvocationCombinerTest.class,
	ReScheduleExecutionTest.class,
	FoldingTest.class
})
public class AOPTests {
}
