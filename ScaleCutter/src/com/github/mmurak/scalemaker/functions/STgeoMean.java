package com.github.mmurak.scalemaker.functions;

public class STgeoMean extends Function {
	public double calculateFunction(double value) {
		return Math.log10(Math.sqrt(Math.sin(value * Math.PI / 180.0) * Math.tan(value * Math.PI / 180.0)) * 100.0);
	}
}
