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

package com.tantaman.eats.test.supportingClasses;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;



import com.tantaman.eats.aop.priv.common.config.Config;
import com.tantaman.eats.aop.priv.common.config.JavaConfig;


public class TestConfig extends Config {
	private static final Map<String, Class<?>> annotationsMapping;
	
	static {
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.putAll(JavaConfig.DEFAULT_NUTRIENTS);
		map.put(TestAnnotation.class.getName(), TestNutrient.class);
		
		annotationsMapping = Collections.unmodifiableMap(map);
	}
	
	@Override
	public Map<String, Class<?>> getAnnotationMapping() {
		return annotationsMapping;
	}

}
