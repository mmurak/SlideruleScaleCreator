package com.github.mmurak.scalemaker.functions;

public class EI12 extends Function {
	public double calculateFunction(double value) {
		return 1.0 - (Math.log10(value * 100000) / 12.0);
	}
}