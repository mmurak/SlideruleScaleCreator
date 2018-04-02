package com.github.mmurak.scalemaker.functions;

public class Q1 extends Function {
	public double calculateFunction(double value) {
		return Math.log10(value) * 3.0;
	}
}
