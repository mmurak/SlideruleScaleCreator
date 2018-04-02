package com.github.mmurak.scalemaker.functions;

public class Q2 extends Function {
	public double calculateFunction(double value) {
		return Math.log10(value) * 3.0 - 1.0;
	}
}
