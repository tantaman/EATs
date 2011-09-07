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

package com.tantaman.eats.test.aop.caching;

import org.junit.Assert;
import org.junit.Test;

import com.tantaman.commons.ref.EReferenceType;
import com.tantaman.eats.aop.pub.annotations.caching.Cache;
import com.tantaman.eats.tools.cache.ECacheType;

public class WeakRefCacheTest {
	/*
	 * Test when params hash code / equals changes
	 */
	@Test
	public void testNoParamCaching() {
		TestClass testObj = new TestClass(1);
		
		Matter m = testObj.noParams();
		Assert.assertEquals(0, m.mInitialValue);
		
		m.mInitialValue = 1;
		
		m = testObj.noParams();
		Assert.assertEquals(1, m.mInitialValue);
		
		TestClass testObj2 = new TestClass(1);
		
		m = testObj2.noParams();
		Assert.assertEquals(1, m.mInitialValue);
	}
	
	@Test
	public void testNoParamCachingModifyHash() {
		TestClass testObj = new TestClass(1);
		
		Matter m = testObj.noParams();
		m.mInitialValue = 1;
		
		testObj.mId = 2;
		m = testObj.noParams();
		Assert.assertEquals(0, m.mInitialValue);
	}
	
	@Test
	public void testParamCaching() {
		TestClass testObj = new TestClass(0);
		
		Matter param1 = new Matter(1);
		Matter param2 = new Matter(2);
		
		Matter m = testObj.withParams(param1, param2);
		Assert.assertEquals(0, m.mInitialValue);
		
		m.mInitialValue = 1;
		m = testObj.withParams(param1, param2);
		Assert.assertEquals(1, m.mInitialValue);
		
		param1.mInitialValue = 3;
		m = testObj.withParams(param1, param2);
		Assert.assertEquals(0, m.mInitialValue);
		
		m.mInitialValue = 1;
		m = testObj.withParams(param1, param2);
		Assert.assertEquals(1, m.mInitialValue);
		
		m = testObj.withParams(param2, param1);
		Assert.assertEquals(0, m.mInitialValue);
		m.mInitialValue = 1;
		m = testObj.withParams(param2, param1);
		Assert.assertEquals(1, m.mInitialValue);
	}
	
	public void testCacheCleanup() {
		
	}
	
	private static class Matter {
		private int mInitialValue = 0;
		
		public Matter() {
		}
		
		public Matter(int pInitialValue) {
			mInitialValue = pInitialValue;
		}
		
		@Override
		public int hashCode() {
			return mInitialValue;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Matter) {
				return mInitialValue == ((Matter)obj).mInitialValue;
			}
			
			return false;
		}
	}
	
	private static class TestClass {
		private int mId;
		public TestClass(int pId) {
			mId = pId;
		}
		
		@Cache(cacheType=ECacheType.REF, referenceType=EReferenceType.WEAK)
		private Matter noParams() {
			Matter m = new Matter();
			
			return m;
		}
		
		@Cache(cacheType=ECacheType.REF, referenceType=EReferenceType.WEAK)
		private Matter withParams(Object pParam1, Object pParam2) {
			Matter m = new Matter();
			
			return m;
		}
		
		@Override
		public int hashCode() {
			return mId;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof TestClass) {
				return mId == ((TestClass)obj).mId;
			}
			
			return false;
		}
	}
}
