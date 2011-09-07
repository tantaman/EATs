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

package com.tantaman.eats.tools.debug;

import java.util.Collection;

public class StackTraceUtils {
	public static String toString(StackTraceElement [] pStackTrace, String pMessage) {
		StringBuilder sb = new StringBuilder();
		sb.append(pMessage + "\n");
		for (StackTraceElement elem : pStackTrace) {
			sb.append("\t" + elem + "\n");
		}
		
		return sb.toString();
	}
	
	public static String toString(Collection<StackInformation> pStackInformation) {
		StringBuilder sb = new StringBuilder();
		for (StackInformation stackInfo : pStackInformation) {
			sb.append(toString(stackInfo.getStackTrace(), stackInfo.getThreadName() + stackInfo.getMessage()));
		}
		
		return sb.toString(); 
	}
}
