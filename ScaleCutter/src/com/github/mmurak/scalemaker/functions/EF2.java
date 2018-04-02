package com.github.mmurak.scalemaker.functions;

public class EF2 extends Function {
	public double calculateFunction(double value) {
		return 2.0 - Math.log10(Math.pow(value * Math.PI * 2.0, 2.0 / 3.0));
	}
}