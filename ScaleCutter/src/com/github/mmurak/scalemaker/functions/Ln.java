package com.github.mmurak.scalemaker.functions;

public class Ln extends Function {
	public double calculateFunction(double value) {
		return Math.log10(Math.exp(value));
	}
}
