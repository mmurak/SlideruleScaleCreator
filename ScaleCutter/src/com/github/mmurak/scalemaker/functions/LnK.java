package com.github.mmurak.scalemaker.functions;

public class LnK extends Function {
	public double calculateFunction(double value) {
		return Math.log10(Math.exp(value)) / 3.0;
	}
}
