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
*   limitations under the License.
*/

package sandbox;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.tantaman.eats.tools.concurrent.executors.FoldingExecutor;

public class FoldingFun {
	public static void main(String[] args) {
		FoldingExecutor folder = new FoldingExecutor(Executors.newFixedThreadPool(3), false);
		
		for (int i = 0; i < 10; ++i) {
			folder.submit(new TaskToFold());
		}
		
		folder.shutdown();
	}
	
	private static class TaskToFold implements Runnable {
		private static final AtomicInteger INSTANCE_COUNT = new AtomicInteger(0);
		
		private final int mInstanceNum;
		public TaskToFold() {
			mInstanceNum = INSTANCE_COUNT.incrementAndGet();
		}
		
		@Override
		public void run() {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("task is running " + mInstanceNum);
		}
		
		@Override
		public String toString() {
			return Integer.toString(mInstanceNum);
		}
		
		@Override
		public int hashCode() {
			return 1;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof TaskToFold))
				return false;
			return true;
		}
	}
}
