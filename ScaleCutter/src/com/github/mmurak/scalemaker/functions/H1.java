package com.github.mmurak.scalemaker.functions;

public class H1 extends Function {
	public double calculateFunction(double value) {
		return Math.log10(Math.sqrt((value * value) - 1.0) * 10.0);
	}
}
