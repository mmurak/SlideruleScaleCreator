package com.github.mmurak.scalemaker.functions;

public class R2 extends Function {
	public double calculateFunction(double value) {
		return Math.log10(value * value) - 1.0;
	}
}
