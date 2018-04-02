package com.github.mmurak.scalemaker.functions;

public class CFroot10 extends Function {
	public double calculateFunction(double value) {
		return Math.log10(value / Math.sqrt(10.0));
	}
}