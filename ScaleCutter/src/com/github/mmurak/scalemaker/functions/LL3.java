package com.github.mmurak.scalemaker.functions;

public class LL3 extends Function {
	public double calculateFunction(double value) {
		return Math.log10(Math.log(value));
	}
}
