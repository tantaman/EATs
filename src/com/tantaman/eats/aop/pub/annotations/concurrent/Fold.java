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

import com.tantaman.eats.aop.pub.nutrients.impl.concurrent.DefaultFoldingExecutorProvider;
import com.tantaman.eats.tools.concurrent.IExecutorProvider;

/**
 * Hold map of running invocations of the method...
 * Would then only be applicable to void methods?
 * Or we can block and return the result of the running computation!
 * @author mlaw
 *
 */
public @interface Fold {
	// await completion of currently running task?
	//public boolean awaitCompletion();
	
	public EFoldingPolicy foldingPolicy() default EFoldingPolicy.REPLACE;
	public int numThreads() default 1;
	public String threadPrefix() default "Fold";
	public Class<? extends IExecutorProvider> executorProvider() default DefaultFoldingExecutorProvider.class;

	public static enum EFoldingPolicy {
		REPLACE,
		REJECT;
	}
}
