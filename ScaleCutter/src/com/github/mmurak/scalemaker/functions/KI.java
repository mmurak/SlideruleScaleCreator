package com.github.mmurak.scalemaker.functions;

public class KI extends Function {
	public double calculateFunction(double value) {
		return 1.0 - Math.log10(Math.pow(value, 1.0 / 3.0));
	}
}
