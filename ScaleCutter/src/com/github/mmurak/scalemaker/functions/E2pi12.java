package com.github.mmurak.scalemaker.functions;

public class E2pi12 extends Function {
	public double calculateFunction(double value) {
		return Math.log10(value * 1000000 / Math.PI / 2.0) / 12.0;
	}
}