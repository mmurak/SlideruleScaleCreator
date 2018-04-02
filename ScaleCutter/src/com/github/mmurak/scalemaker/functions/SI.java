package com.github.mmurak.scalemaker.functions;

public class SI extends Function {
	public double calculateFunction(double value) {
		return 1.0 - Math.log10(Math.sin(value * Math.PI / 180.0) * 10.0);
	}
}
