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

package com.tantaman.eats.aop.priv.common.nutrients;

import java.util.ArrayList;

import com.tantaman.eats.aop.pub.nutrients.Nutrient;

// TODO DRY!
public class NutrientProvider {
	private static final ArrayList<Nutrient> NUTRIENTS = new ArrayList<Nutrient>();
	
	public static Nutrient get(int pIndex) {
		return NUTRIENTS.get(pIndex);
	}
	
	// TODO: this is a hack (passing as an object instead of a nutrient)
	public static synchronized int put(Object pNutrient) {
		NUTRIENTS.add((Nutrient)pNutrient);
		return NUTRIENTS.size() - 1;
	}
}
