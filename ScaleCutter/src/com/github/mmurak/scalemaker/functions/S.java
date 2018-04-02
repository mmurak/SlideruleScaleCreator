package com.github.mmurak.scalemaker.functions;

public class S extends Function {
	public double calculateFunction(double value) {
		return Math.log10(Math.sin(value * Math.PI / 180.0) * 10.0);
	}
}
