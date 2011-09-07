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

package com.tantaman.eats.aop.pub.nutrients.impl.concurrent;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.tantaman.eats.aop.priv.common.MethodCall;
import com.tantaman.eats.aop.pub.annotations.concurrent.PutOnEDT;
import com.tantaman.eats.aop.pub.nutrients.Nutrient;
import com.tantaman.eats.tools.Constants;

public class PutOnEDTNutrient extends Nutrient {
	private final RunnableCreator mRunnableCreator;
	private final boolean mInvokeLater;
	private static final Logger LOGGER = Logger.getLogger(PutOnEDTNutrient.class);
	private static final String ERROR_MSG = "Could not submit to EDT.  Not running task.";
	
	public static Nutrient create(Object pAnnotation) {
		return new PutOnEDTNutrient((PutOnEDT)pAnnotation);
	}
	
	public PutOnEDTNutrient(PutOnEDT pAnnotation) {
		mRunnableCreator = new RunnableCreator();
		mInvokeLater = pAnnotation.invokeLater();
	}
	
	@Override
	public Object nourishBefore(MethodCall pMethodCall) {
		if (SwingUtilities.isEventDispatchThread() && !mInvokeLater)
			return null;
		
		Runnable task = mRunnableCreator.createRunnable(pMethodCall, getMethodSignature());
		
		if (task != null) {
			if (mInvokeLater)
				SwingUtilities.invokeLater(task);
			else {
				try {
					SwingUtilities.invokeAndWait(task);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		} else {
			LOGGER.error(ERROR_MSG + " Task: "
					+ pMethodCall.getCalledObjectClass().getName()
					+ "." +pMethodCall.getMethodName());
		}
		
		return Constants.CONSTANT_OBJECT;
	}
	
	@Override
	public Object nourishAfter(MethodCall pMethodCall, Object pResult) {
		return null;
	}
}
