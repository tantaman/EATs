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
*  limitations under the License.
*/

package com.tantaman.eats.aop.priv.common.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.tantaman.eats.aop.pub.annotations.caching.Cache;
import com.tantaman.eats.aop.pub.annotations.concurrent.CombineInvocations;
import com.tantaman.eats.aop.pub.annotations.concurrent.Fold;
import com.tantaman.eats.aop.pub.annotations.concurrent.PutOnEDT;
import com.tantaman.eats.aop.pub.annotations.concurrent.PutOnExecutor;
import com.tantaman.eats.aop.pub.annotations.concurrent.ReScheduleExecution;
import com.tantaman.eats.aop.pub.annotations.debug.concurrent.ConcurrentAccesses;
import com.tantaman.eats.aop.pub.annotations.debug.concurrent.NotOnThread;
import com.tantaman.eats.aop.pub.annotations.debug.concurrent.NotOnThreads;
import com.tantaman.eats.aop.pub.annotations.debug.concurrent.OnThread;
import com.tantaman.eats.aop.pub.annotations.debug.concurrent.OnThreads;
import com.tantaman.eats.aop.pub.nutrients.impl.caching.CacheFactory;
import com.tantaman.eats.aop.pub.nutrients.impl.concurrent.CombineInvocationsNutrient;
import com.tantaman.eats.aop.pub.nutrients.impl.concurrent.FoldNutrient;
import com.tantaman.eats.aop.pub.nutrients.impl.concurrent.PutOnEDTNutrient;
import com.tantaman.eats.aop.pub.nutrients.impl.concurrent.PutOnExecutorNutrient;
import com.tantaman.eats.aop.pub.nutrients.impl.concurrent.ReScheduleExecutionNutrient;
import com.tantaman.eats.aop.pub.nutrients.impl.debug.concurrent.ConcurrentAccessesNutrient;
import com.tantaman.eats.aop.pub.nutrients.impl.debug.concurrent.NotOnThreadNutrient;
import com.tantaman.eats.aop.pub.nutrients.impl.debug.concurrent.NotOnThreadsNutrient;
import com.tantaman.eats.aop.pub.nutrients.impl.debug.concurrent.OnThreadNutrient;
import com.tantaman.eats.aop.pub.nutrients.impl.debug.concurrent.OnThreadsNutrient;

public class JavaConfig extends Config {
	public static final Map<String, Class<?>> DEFAULT_NUTRIENTS;
	
	static {
		// TODO: we should be able to fix this to not have to use strings
		// for the annotations...
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put(ConcurrentAccesses.class.getName(), ConcurrentAccessesNutrient.class);
		map.put(OnThread.class.getName(), OnThreadNutrient.class);
		map.put(OnThreads.class.getName(), OnThreadsNutrient.class);
		map.put(NotOnThread.class.getName(), NotOnThreadNutrient.class);
		map.put(NotOnThreads.class.getName(), NotOnThreadsNutrient.class);
		map.put(Cache.class.getName(), CacheFactory.class);
		map.put(PutOnExecutor.class.getName(), PutOnExecutorNutrient.class);
		map.put(PutOnEDT.class.getName(), PutOnEDTNutrient.class);
		map.put(CombineInvocations.class.getName(), CombineInvocationsNutrient.class);
		map.put(ReScheduleExecution.class.getName(), ReScheduleExecutionNutrient.class);
		map.put(Fold.class.getName(), FoldNutrient.class);
		
		DEFAULT_NUTRIENTS = Collections.unmodifiableMap(map);
	}
	
	@Override
	public Map<String, Class<?>> getAnnotationMapping() {
		return DEFAULT_NUTRIENTS;
	}
	
	public static Config loadConfig(String pClassName) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		if (pClassName == null)
			return new JavaConfig();
		else
			return (Config)Class.forName(pClassName).newInstance();
	}
}
