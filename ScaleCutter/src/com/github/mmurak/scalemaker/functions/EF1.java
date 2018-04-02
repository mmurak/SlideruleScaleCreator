package com.github.mmurak.scalemaker.functions;

public class EF1 extends Function {
	public double calculateFunction(double value) {
		return 1.0 - Math.log10(Math.pow(value * Math.PI * 2.0, 2.0 / 3.0));
	}
}