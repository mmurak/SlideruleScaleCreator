package com.github.mmurak.scalemaker.functions;

public class E6 extends Function {
	public double calculateFunction(double value) {
		return Math.log10(value * 10000) / 6.0;
	}
}