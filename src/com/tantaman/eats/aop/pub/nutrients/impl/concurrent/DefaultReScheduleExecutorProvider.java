package com.tantaman.eats.aop.pub.nutrients.impl.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.tantaman.eats.aop.pub.annotations.concurrent.ReScheduleExecution;
import com.tantaman.eats.tools.concurrent.IExecutorProvider;
import com.tantaman.eats.tools.concurrent.IScheduledExecutorProvider;
import com.tantaman.eats.tools.concurrent.NamedThreadFactory;

public class DefaultReScheduleExecutorProvider implements IScheduledExecutorProvider<ReScheduleExecution>, IExecutorProvider<ReScheduleExecution> {
	public ScheduledExecutorService getExecutorForMethod(ReScheduleExecution pAnnotation) {
		return Executors.newScheduledThreadPool(pAnnotation.numThreads(), 
				new NamedThreadFactory(pAnnotation.threadPrefix()));
	}
}
