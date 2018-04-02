package com.github.mmurak.scalemaker.functions;

public class R1 extends Function {
	public double calculateFunction(double value) {
		return Math.log10(value * value);
	}
}
