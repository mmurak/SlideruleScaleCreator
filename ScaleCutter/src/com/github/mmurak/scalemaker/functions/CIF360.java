package com.github.mmurak.scalemaker.functions;

public class CIF360 extends Function {
	public double calculateFunction(double value) {
		return Math.log10(100.0 / (value * 3.6));
	}
}
