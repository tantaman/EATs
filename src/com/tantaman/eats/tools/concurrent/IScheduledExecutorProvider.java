package com.tantaman.eats.tools.concurrent;

import java.util.concurrent.ScheduledExecutorService;

// TODO: allow getExecutorForCall?
public interface IScheduledExecutorProvider<ANNOTATION_TYPE> {
	public ScheduledExecutorService getExecutorForMethod(ANNOTATION_TYPE pAnnotation);
}
