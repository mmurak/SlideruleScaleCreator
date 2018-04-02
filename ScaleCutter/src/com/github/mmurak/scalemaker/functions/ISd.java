package com.github.mmurak.scalemaker.functions;

public class ISd extends Function {
	public double calculateFunction(double value) {
		return Math.log10((value / (Math.asin(value) * 180.0 / Math.PI)) * 100.0);
	}
}
