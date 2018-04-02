package com.github.mmurak.scalemaker.functions;

public class TI2 extends Function {
	public double calculateFunction(double value) {
		return 1.0 - Math.log10(Math.tan(value * Math.PI / 180.0));
	}
}
