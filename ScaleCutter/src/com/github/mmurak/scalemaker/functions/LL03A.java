package com.github.mmurak.scalemaker.functions;

public class LL03A extends Function {
	public double calculateFunction(double value) {
		return Math.log10(Math.log(value / 10.0e6) * -0.1) / 2.0;
	}
}
