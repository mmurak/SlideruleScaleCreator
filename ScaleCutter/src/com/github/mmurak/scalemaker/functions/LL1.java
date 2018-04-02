package com.github.mmurak.scalemaker.functions;

public class LL1 extends Function {
	public double calculateFunction(double value) {
		return Math.log10(Math.log(value) * 100.0);
	}
}
