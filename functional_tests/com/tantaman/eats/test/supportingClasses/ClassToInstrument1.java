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

import com.tantaman.eats.test.core.InstrumentationTest;


public class ClassToInstrument1 implements IClassToInstrument {
	@TestAnnotation
	public void pubVoid() {
		InstrumentationTest.methodInvoked();
	}
	
	public void callPrivVoid() {
		privVoid();
	}
	
	public void callProtVoid() {
		protVoid();
	}
	
	public Object callPrivObject() {
		return privObject();
	}
	
	public Object callProtObject() {
		return protObject();
	}
	
	@TestAnnotation
	private void privVoid() {
		InstrumentationTest.methodInvoked();
	}
	
	@TestAnnotation
	protected void protVoid() {
		InstrumentationTest.methodInvoked();
	}
	
	@TestAnnotation
	public Object pubObject() {
		InstrumentationTest.methodInvoked();
		return null;
	}
	
	@TestAnnotation
	private Object privObject() {
		InstrumentationTest.methodInvoked();
		return null;
	}
	
	@TestAnnotation
	protected Object protObject() {
		InstrumentationTest.methodInvoked();
		return null;
	}
}
