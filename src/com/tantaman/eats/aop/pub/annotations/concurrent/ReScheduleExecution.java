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

package com.tantaman.eats.aop.pub.annotations.concurrent;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

import com.tantaman.eats.aop.pub.annotations.concurrent.ScheduleExecution.Type;
import com.tantaman.eats.aop.pub.nutrients.impl.concurrent.DefaultReScheduleExecutorProvider;
import com.tantaman.eats.tools.concurrent.IScheduledExecutorProvider;

@Retention(RetentionPolicy.RUNTIME)
public @interface ReScheduleExecution {
	public long initialDelay() default 0;
	public long subsequentDelay() default 0;
	public Type type() default Type.SINGLE;
	public TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
	public Class<? extends IScheduledExecutorProvider> executorProvider() default DefaultReScheduleExecutorProvider.class;
	public String threadPrefix() default "ScheduleExecution";
	public int numThreads() default 1;
}
