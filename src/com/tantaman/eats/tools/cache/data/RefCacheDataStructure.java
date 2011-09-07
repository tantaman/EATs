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

package com.tantaman.eats.tools.cache.data;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tantaman.commons.ref.IKeyedReference;
import com.tantaman.commons.ref.KeyedSoftReference;
import com.tantaman.commons.ref.KeyedWeakReference;
import com.tantaman.eats.aop.pub.annotations.caching.Cache;
import com.tantaman.eats.tools.cache.cleanup.ICleaner;
import com.tantaman.eats.tools.cache.cleanup.RefQueueCleanupService;


// TODO: subclass this thing for the diff types of caches so we can actually get type sfatey
// on the weak/soft reference stuff
public class RefCacheDataStructure<K, V> implements ICacheDataStructure<K, V>{
	private final Map<K, Reference<V>> mCache;
	private final ReferenceFactory<K, V> mRefFactory;
	private final RefQueueCleanupService<Reference<V>, V> mCleanupSrvc;
	private final ReferenceQueue<V> mRefQueue;
	private static final String ILLEGAL_REF_TYPE = "Only weak/soft are valid";

	public RefCacheDataStructure(Cache pCacheAnnotation) {
		mCache = createMap(pCacheAnnotation);
		mRefFactory = createReferenceFactory(pCacheAnnotation);
		mCleanupSrvc = new RefQueueCleanupService<Reference<V>, V>();
		mRefQueue = mCleanupSrvc.getReferenceQueue();
		
		mCleanupSrvc.start(new ICleaner<Reference<V>>() {
			@Override
			public void clean(Reference<V> pExpiredValue) {
				IKeyedReference<K, V> val = (IKeyedReference<K, V>)pExpiredValue;
				
				mCache.remove(val.getKey());
			}
		});
	}

	@SuppressWarnings("unchecked")
	private Map<K, Reference<V>> createMap(Cache pCacheAnnotation) {
		switch (pCacheAnnotation.referenceType()) {
		case SOFT:
			return (Map)new ConcurrentHashMap<K, KeyedSoftReference<K, V>>();
		case WEAK:
			return (Map)new ConcurrentHashMap<K, KeyedWeakReference<K, V>>();
		default:
			throw new IllegalArgumentException(ILLEGAL_REF_TYPE);
		}
	}

	@Override
	public V get(K pKey) {
		Reference<V> val = mCache.get(pKey);
		if (val == null) return null;
		return val.get();
	}

	@Override
	public void put(K pKey, V pValue) {
		mCache.put(pKey, mRefFactory.createReference(pKey, pValue, mRefQueue));
	}
	
	public int size() {
		return mCache.size();
	}
	
	private ReferenceFactory<K, V> createReferenceFactory(Cache pCacheAnnotation) {
		switch (pCacheAnnotation.referenceType()) {
		case SOFT:
			return new ReferenceFactory<K, V>() {
				@Override
				public Reference<V> createReference(K pKey, V pValue, ReferenceQueue<V> pQueue) {
					return new KeyedSoftReference<K, V>(pKey, pValue, pQueue);
				}
			};
		case WEAK:
			return new ReferenceFactory<K, V>() {
				@Override
				public Reference<V> createReference(K pKey, V pValue, ReferenceQueue<V> pQueue) {
					return new KeyedWeakReference<K, V>(pKey, pValue, pQueue);
				}
			};
		default:
			throw new IllegalArgumentException(ILLEGAL_REF_TYPE);
		}
	}

	private static interface ReferenceFactory<K, V> {
		public Reference<V> createReference(K pKey, V pValue, ReferenceQueue<V> pQueue);
	}
}
