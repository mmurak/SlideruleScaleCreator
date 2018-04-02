package com.github.mmurak.scalemaker.functions;

public class E12 extends Function {
	public double calculateFunction(double value) {
		return Math.log10(value * 100000) / 12.0;
	}
}