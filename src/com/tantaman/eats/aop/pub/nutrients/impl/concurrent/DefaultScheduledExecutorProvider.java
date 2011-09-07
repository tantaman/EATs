package com.tantaman.eats.aop.pub.nutrients.impl.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.tantaman.commons.concurrent.IExecutorProvider;
import com.tantaman.commons.concurrent.IScheduledExecutorProvider;
import com.tantaman.commons.concurrent.NamedThreadFactory;
import com.tantaman.eats.aop.pub.annotations.concurrent.ScheduleExecution;

public class DefaultScheduledExecutorProvider implements IScheduledExecutorProvider<ScheduleExecution>, IExecutorProvider<ScheduleExecution> {
	public ScheduledExecutorService getExecutorForMethod(ScheduleExecution pAnnotation) {
		return Executors.newScheduledThreadPool(pAnnotation.numThreads(), 
				new NamedThreadFactory(pAnnotation.threadPrefix()));
	}
}
