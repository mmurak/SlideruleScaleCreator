package com.github.mmurak.scalemaker.functions;

public class CI extends Function {
	public double calculateFunction(double value) {
		return Math.log10(10.0 / value);
	}
}
