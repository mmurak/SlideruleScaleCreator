package com.github.mmurak.scalemaker.functions;

public class EI2pi12 extends Function {
	public double calculateFunction(double value) {
		return 1.0 - Math.log10(value * 10000 * Math.PI * 2.0)  / 12.0;
	}
}