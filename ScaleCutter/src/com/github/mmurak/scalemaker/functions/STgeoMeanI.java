package com.github.mmurak.scalemaker.functions;

public class STgeoMeanI extends Function {
	public double calculateFunction(double value) {
		return 1.0 - Math.log10(Math.sqrt(Math.sin(value * Math.PI / 180.0) * Math.tan(value * Math.PI / 180.0)) * 100.0);
	}
}
