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

package com.tantaman.eats.aop.pub.nutrients.impl.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tantaman.eats.aop.pub.annotations.concurrent.Fold;
import com.tantaman.eats.aop.pub.annotations.concurrent.Fold.EFoldingPolicy;
import com.tantaman.eats.tools.concurrent.IExecutorProvider;
import com.tantaman.eats.tools.concurrent.NamedThreadFactory;
import com.tantaman.eats.tools.concurrent.executors.FoldingExecutor;

public class DefaultFoldingExecutorProvider implements IExecutorProvider<Fold> {
	@Override
	public ExecutorService getExecutorForMethod(Fold pAnnotation) {
		return new FoldingExecutor(Executors.newFixedThreadPool(pAnnotation.numThreads(), 
				new NamedThreadFactory(pAnnotation.threadPrefix())), (pAnnotation.foldingPolicy() == EFoldingPolicy.REJECT) ? true : false);
	}
}
