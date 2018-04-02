package com.github.mmurak.scalemaker.functions;

public class Td extends Function {
	public double calculateFunction(double value) {
		return Math.log10((value / Math.tan(value * Math.PI / 180.0)) / 10.0);
	}
}
