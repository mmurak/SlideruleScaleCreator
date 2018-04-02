package com.github.mmurak.scalemaker.functions;

public class P extends Function {
	public double calculateFunction(double value) {
		return Math.log10(Math.sqrt(1.0 - (value * value)) * 10.0);
	}
}
