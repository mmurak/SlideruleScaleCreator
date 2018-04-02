package com.github.mmurak.scalemaker.functions;

public class ST extends Function {
	public double calculateFunction(double value) {
		return Math.log10(Math.sin(value * Math.PI / 180.0) * 100.0);
	}
}
