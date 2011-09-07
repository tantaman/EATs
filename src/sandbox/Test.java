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


package sandbox;

import java.util.LinkedList;
import java.util.List;

import sandbox.classes.ExampleClass;

public class Test {
	public static void main(String[] args) throws ClassNotFoundException {
		System.out.println("main");
		final ExampleClass all = new ExampleClass();
		
		all.doGuiStuff();
		all.onThreads();
		
		new Thread() {
			public void run() {
				all.nonThreadSafeMethod();
			};
		}.start();
		
		all.nonThreadSafeMethod();
		
		List<String> vals = testCache(all);
		
		System.gc();
		
		vals = testCache(all);
		vals.clear();
		
		System.gc();
		
		testCache(all);
	}
	
	private static List<String> testCache(ExampleClass obj) {
		String p1 = "a";
		String p2 = "b";
		
		List<String> vals = new LinkedList<String>();
		for (int i = 0; i < 10; ++i) {
			if (i % 2 == 0) {
				p1 += p1;
				p2 += p2;
			}
			
			long beforeCall = System.currentTimeMillis();
			String val = obj.cached(p1, p2);
			System.out.println("Call: " + val);
			System.out.println("Time: " + (System.currentTimeMillis() - beforeCall));
			
			vals.add(val);
		}
		
		return vals;
	}
}