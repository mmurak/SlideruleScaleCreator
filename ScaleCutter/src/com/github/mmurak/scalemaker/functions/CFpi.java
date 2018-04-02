package com.github.mmurak.scalemaker.functions;

public class CFpi extends Function {
	public double calculateFunction(double value) {
		return Math.log10(value / Math.PI);
	}
}