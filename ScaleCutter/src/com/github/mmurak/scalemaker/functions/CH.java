package com.github.mmurak.scalemaker.functions;

public class CH extends Function {
	public double calculateFunction(double value) {
		return Math.log10((Math.exp(value) + Math.exp(-value)) / 2);
	}
}
