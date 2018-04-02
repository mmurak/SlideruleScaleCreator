package com.github.mmurak.scalemaker.functions;

public class CIFpi extends Function {
	public double calculateFunction(double value) {
		return Math.log10(100.0 / (value * Math.PI));
	}
}
