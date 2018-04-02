package com.github.mmurak.scalemaker.functions;

public class LL2 extends Function {
	public double calculateFunction(double value) {
		return Math.log10(Math.log(value) * 10.0);
	}
}
