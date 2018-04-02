package com.github.mmurak.scalemaker.functions;

public class SA extends Function {
	public double calculateFunction(double value) {
		return Math.log10(Math.pow(Math.sin(value * Math.PI / 180.0) * 100.0,  1.0 / 2.0));
	}
}
