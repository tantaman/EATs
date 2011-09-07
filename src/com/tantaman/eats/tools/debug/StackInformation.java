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

public class StackInformation {
	private final StackTraceElement [] mStackTrace;
	private final String mThreadName;
	private final String mMessage;
	
	public StackInformation(StackTraceElement [] pStackTrace, String pThreadName, String pMessage) {
		mStackTrace = pStackTrace;
		mThreadName = pThreadName;
		mMessage = pMessage;
	}
	
	public StackTraceElement [] getStackTrace() {
		return mStackTrace;
	}
	
	public String getThreadName() {
		return mThreadName;
	}
	
	public String getMessage() {
		return mMessage;
	}
}
