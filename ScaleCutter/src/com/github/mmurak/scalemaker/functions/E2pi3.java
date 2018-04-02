package com.github.mmurak.scalemaker.functions;

public class E2pi3 extends Function {
	public double calculateFunction(double value) {
		return Math.log10(value / (Math.PI * 2.0)) / 3.0;
	}
}