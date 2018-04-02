package com.github.mmurak.scalemaker.functions;

public class K extends Function {
	public double calculateFunction(double value) {
		return Math.log10(Math.pow(value, 1.0 / 3.0));
	}
}
