package com.github.mmurak.scalemaker.functions;

public class LL02 extends Function {
	public double calculateFunction(double value) {
		return Math.log10(Math.log(1.0 / value) * 10.0);
	}
}
