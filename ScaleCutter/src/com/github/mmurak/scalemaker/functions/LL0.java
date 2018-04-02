package com.github.mmurak.scalemaker.functions;

public class LL0 extends Function {
	public double calculateFunction(double value) {
		return Math.log10(Math.log(value) * 1000.0);
	}
}
