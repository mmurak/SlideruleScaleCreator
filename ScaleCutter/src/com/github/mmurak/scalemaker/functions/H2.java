package com.github.mmurak.scalemaker.functions;

public class H2 extends Function {
	public double calculateFunction(double value) {
		return Math.log10(Math.sqrt((value * value) - 1.0));
	}
}
