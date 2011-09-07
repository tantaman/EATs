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


import com.tantaman.eats.aop.priv.common.MethodCall;
import com.tantaman.eats.aop.pub.nutrients.Nutrient;
import com.tantaman.eats.test.core.InstrumentationTest;

public class TestNutrient extends Nutrient {

	@Override
	public Object nourishAfter(MethodCall pMethodCall, Object pResult) {
		InstrumentationTest.nourishedAfter();
		return null;
	}

	@Override
	public Object nourishBefore(MethodCall pMethodCall) {
		InstrumentationTest.nourishedBefore();
		return null;
	}
}
