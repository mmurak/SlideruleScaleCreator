package com.github.mmurak.scalemaker.functions;

public class LL02A extends Function {
	public double calculateFunction(double value) {
		return Math.log10(Math.log(value) * -10.0) / 2.0;
	}
}
