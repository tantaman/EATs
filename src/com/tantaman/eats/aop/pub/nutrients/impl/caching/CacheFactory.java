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

package com.tantaman.eats.aop.pub.nutrients.impl.caching;

import com.tantaman.eats.aop.pub.annotations.caching.Cache;
import com.tantaman.eats.aop.pub.nutrients.Nutrient;
import com.tantaman.eats.tools.cache.data.ICacheDataStructure;
import com.tantaman.eats.tools.cache.data.RefCacheDataStructure;

public class CacheFactory {
	public static Nutrient create(Object pAnnotation) {
		Cache cacheAnnotation = (Cache)pAnnotation;
		
		return new SimpleCacheNutrient(
				getCacheDataStruct(cacheAnnotation));
	}
	
	private static ICacheDataStructure<Object, Object> 
		getCacheDataStruct(Cache pCacheAnnotation) {
		switch (pCacheAnnotation.cacheType()) {
		case REF:
			return new RefCacheDataStructure<Object, Object>(pCacheAnnotation);
		case STANDARD:
			return getStandardCacheDataStructure(pCacheAnnotation);
		}
		
		return null;
	}
	
	// TODO: the return result of this will change based on the eviction algorithm...
	private static ICacheDataStructure<Object, Object> getStandardCacheDataStructure(Cache pCacheAnnotation) {
		return null;
	}
}
