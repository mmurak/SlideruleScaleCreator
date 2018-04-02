package com.github.mmurak.scalemaker.functions;

public class LL03 extends Function {
	public double calculateFunction(double value) {
		return Math.log10(Math.log(1.0 / value));
	}
}
