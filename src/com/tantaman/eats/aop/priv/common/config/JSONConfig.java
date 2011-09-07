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

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class JSONConfig {
	private Mapping [] mappings;
	
	public static JSONConfig loadConfig(String pConfigFileLocation) throws JsonParseException, FileNotFoundException {
		if (pConfigFileLocation == null)
			pConfigFileLocation = "src/com/tantaman/eats/pub/config.json";
		Gson gson = new Gson();
		JSONConfig cfg = gson.fromJson(new FileReader(pConfigFileLocation), JSONConfig.class);
		
		return cfg;
	}
	
	public JSONConfig() {
	}

	public Mapping [] getMappings() {
		return mappings;
	}

	public static class Mapping {
		private String annotation;
		private String factory;
		
		public Mapping() {
		}
		
		public String getAnnotationClassName() {
			return annotation;
		}
		
		public String getFactoryClassName() {
			return factory;
		}
		
		public Class<?> getFactoryClass() throws ClassNotFoundException {
			return Class.forName(factory);
		}
	}
}