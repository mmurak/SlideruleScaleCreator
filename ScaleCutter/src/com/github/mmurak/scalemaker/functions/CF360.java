package com.github.mmurak.scalemaker.functions;

public class CF360 extends Function {
	public double calculateFunction(double value) {
		return Math.log10(value / 3.6);
	}
}