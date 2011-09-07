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

package com.tantaman.eats.tools.cache.cleanup;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.tantaman.commons.concurrent.NamedThreadFactory;

public class RefQueueCleanupService<REF_TYPE extends Reference<VAL_TYPE>, VAL_TYPE> implements ICleanupService<REF_TYPE, VAL_TYPE> {
	private final ReferenceQueue<VAL_TYPE> mRefQueue;
	private final ExecutorService mCleaningExecutor;
	private Future<?> mFuture;

	public RefQueueCleanupService() {
		mRefQueue = new ReferenceQueue<VAL_TYPE>();
		mCleaningExecutor = Executors
			.newSingleThreadExecutor(new NamedThreadFactory(this.getClass().getSimpleName()));
	}
	
	public ReferenceQueue<VAL_TYPE> getReferenceQueue() {
		return mRefQueue;
	}

	@Override
	public synchronized Future<?> start(final ICleaner<REF_TYPE> pCleaner) {
		if (mFuture != null) return mFuture;
		
		mFuture = mCleaningExecutor.submit(new Runnable() {
			@Override
			public void run() {
				clean(pCleaner);
			}
		});
		
		return mFuture;
	}

	private void clean(ICleaner<REF_TYPE> pCleaner) {
		Reference<? extends VAL_TYPE> ref;
		try {
			while (!Thread.interrupted()) {
				ref = mRefQueue.remove();
				
				pCleaner.clean((REF_TYPE)ref);
			}
		} catch (InterruptedException e) {
			return;
		}
	}
}
