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

import com.tantaman.eats.aop.priv.common.AnnotationProvider;
import com.tantaman.eats.aop.priv.common.MethodCall;
import com.tantaman.eats.aop.pub.nutrients.Nutrient;

// TODO: DRY!
public class NutrientInvoker {
	public static Object invokeBefore(
			int pNutrientID,
			int pAnnotationID,
			Object pThis,
			Object [] pArgs,
			String pMethodName,
			String pAlteredMethodName) {
		try {
			Object anno = AnnotationProvider.get(pAnnotationID);
			Nutrient nutrient = NutrientProvider.get(pNutrientID);
			
			MethodCall methodCall = new MethodCall(
					anno, pThis, pArgs, pMethodName, pAlteredMethodName);
			
			return nutrient.nourishBefore(methodCall);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Object invokeAfter(
			int pNutrientID,
			int pAnnotationID,
			Object pThis,
			Object [] pArgs,
			Object pResult,
			String pMethodName, 
			String pAlteredMethodName) {
		try {
			Object anno = AnnotationProvider.get(pAnnotationID);
			Nutrient nutrient = NutrientProvider.get(pNutrientID);
			
			MethodCall methodCall = new MethodCall(
					anno, pThis, pArgs, pMethodName, pAlteredMethodName);
			
			return nutrient.nourishAfter(methodCall, pResult);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
