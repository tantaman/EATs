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

package com.tantaman.eats.test.core;

import org.junit.Assert;
import org.junit.Test;

import com.tantaman.eats.test.supportingClasses.ClassToInstrument1;
import com.tantaman.eats.test.supportingClasses.IClassToInstrument;
import com.tantaman.eats.test.supportingClasses.TestAnnotation;



public class InstrumentationTest {
	private static boolean nourishedBefore = false;
	private static boolean nourishedAfter = false;
	private static boolean methodInvoked = false;

	public static void nourishedBefore() {
		nourishedBefore = true;
	}

	public static void nourishedAfter() {
		nourishedAfter = true;
	}

	public static void methodInvoked() {
		methodInvoked = true;
	}

	private void resetInstrumentationInfo() {
		nourishedAfter = false;
		nourishedBefore = false;
		methodInvoked = false;
	}

	private void assertAllRan() {
		Assert.assertTrue(nourishedBefore);
		Assert.assertTrue(methodInvoked);
		Assert.assertTrue(nourishedAfter);
	}

	@Test
	public void testTestClass1() {
		runDefaultTestsOn(new ClassToInstrument1());
	}

	@Test
	public void testTestClass2() {
		runDefaultTestsOn(new ClassToInstrument2());
	}
	
	private void runDefaultTestsOn(IClassToInstrument pObject) {
		pObject.pubVoid();
		assertAllRan();
		resetInstrumentationInfo();
		
		pObject.pubObject();
		assertAllRan();
		resetInstrumentationInfo();
		
		pObject.callPrivVoid();
		assertAllRan();
		resetInstrumentationInfo();
		
		pObject.callPrivObject();
		assertAllRan();
		resetInstrumentationInfo();
		
		pObject.callProtVoid();
		assertAllRan();
		resetInstrumentationInfo();
		
		pObject.callProtObject();
		assertAllRan();
		resetInstrumentationInfo();
	}

	@Test
	public void testTestClass3() {
		runDefaultTestsOn(new ClassToInstrument3());
	}

	@Test
	public void testTestClass4() {
		runDefaultTestsOn(new ClassToInstrument4());
	}
	
	public void testTestVar1() {
		testVar1.doStuff();
		assertAllRan();
		resetInstrumentationInfo();
	}
	
	public void testTestVar2() {
		testVar2.doStuff();
		assertAllRan();
		resetInstrumentationInfo();
	}

	private static class ClassToInstrument3 implements IClassToInstrument {
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

	private class ClassToInstrument4 implements IClassToInstrument {

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

	public static interface Interface {
		@TestAnnotation
		public void doStuff();
	}

	private Interface testVar1 = new Interface() {
		@Override
		public void doStuff() {
			InstrumentationTest.methodInvoked();
		}
	};

	private Interface testVar2 = new Interface() {
		@TestAnnotation
		@Override
		public void doStuff() {
			InstrumentationTest.methodInvoked();
		}
	};
}

class ClassToInstrument2 implements IClassToInstrument {
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