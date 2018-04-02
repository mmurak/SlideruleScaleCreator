package com.github.mmurak.scalemaker.functions;

public class LL00 extends Function {
	public double calculateFunction(double value) {
		return Math.log10(Math.log(1.0 / value) * 1000.0);
	}
}
