package com.github.mmurak.scalemaker.functions;

public class EI6 extends Function {
	public double calculateFunction(double value) {
		return 1.0 - (Math.log10(value * 10000) / 6.0);
	}
}