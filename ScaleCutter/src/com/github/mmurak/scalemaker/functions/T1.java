package com.github.mmurak.scalemaker.functions;

public class T1 extends Function {
	public double calculateFunction(double value) {
		return Math.log10(Math.tan(value * Math.PI / 180.0) * 10.0);
	}
}
