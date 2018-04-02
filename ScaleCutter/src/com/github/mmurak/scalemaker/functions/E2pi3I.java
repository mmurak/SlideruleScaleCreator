package com.github.mmurak.scalemaker.functions;

public class E2pi3I extends Function {
	public double calculateFunction(double value) {
		return 1.0 - Math.log10(value * Math.PI * 2.0) / 3.0;
	}
}