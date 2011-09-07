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

package sandbox.classes;

import com.tantaman.commons.ref.EReferenceType;
import com.tantaman.eats.aop.pub.annotations.caching.Cache;
import com.tantaman.eats.aop.pub.annotations.debug.concurrent.ConcurrentAccesses;
import com.tantaman.eats.aop.pub.annotations.debug.concurrent.OnThread;
import com.tantaman.eats.aop.pub.annotations.debug.concurrent.OnThreads;
import com.tantaman.eats.tools.cache.ECacheType;

public class ExampleClass {
	@OnThread("AWT-EventQueue")
	public void doGuiStuff() {
	}
	
	@OnThreads({"AWT-EventQueue", "main"})
	public void onThreads() {
	}
	
	@ConcurrentAccesses(concurrencyLevel = 1)
	public void nonThreadSafeMethod() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Cache(cacheType=ECacheType.REF, referenceType=EReferenceType.WEAK)
	public String cached(String p1, String p2) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p1 + p2;
	}
}
