package com.tantaman.eats.aop.priv.common;

public final class MethodCall {
	private final String mMethodName;
	private final String mAlteredMethodName;
	private final Object mCalledObject;
	private final Object [] mParameters;
	private final Object mAnnotation;
	
	public MethodCall(Object pAnno, Object pCalledObject,
			Object[] pArgs, String pMethodName,
			String pAlteredMethodName) {
		mMethodName = pMethodName;
		mCalledObject = pCalledObject;
		mParameters = pArgs;
		
		mAnnotation = pAnno;
		mAlteredMethodName = pAlteredMethodName;
	}
	
	public final String getMethodName() {
		return mMethodName;
	}
	
	public final String getAlteredMethodName() {
		return mAlteredMethodName;
	}
	
	public final Class<?> getCalledObjectClass() {
		return mCalledObject.getClass();
	}
	
	public final Object getCalledObject() {
		return mCalledObject;
	}
	
	public final Object [] getParameters() {
		return mParameters;
	}
	
	public final Object getAnnotation() {
		return mAnnotation;
	}
}
