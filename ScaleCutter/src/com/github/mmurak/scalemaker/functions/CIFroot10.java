package com.github.mmurak.scalemaker.functions;

public class CIFroot10 extends Function {
	public double calculateFunction(double value) {
		return Math.log10(100.0 / (value * Math.sqrt(10.0)));
	}
}
