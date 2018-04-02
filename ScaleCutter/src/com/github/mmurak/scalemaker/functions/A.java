package com.github.mmurak.scalemaker.functions;

public class A extends Function {
	public double calculateFunction(double value) {
		return Math.log10(Math.pow(value, 1.0 / 2.0));
	}
}
