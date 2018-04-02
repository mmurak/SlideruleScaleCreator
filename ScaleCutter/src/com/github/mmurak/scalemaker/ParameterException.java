package com.github.mmurak.scalemaker;

public class ParameterException extends Exception {
	private static final long serialVersionUID = 9196973277554100212L;
	String msg;
	public ParameterException(String msg) {
		this.msg = msg;
	}

}
