package com.github.mmurak.scalemaker.functions;

public class CelsiusM40P500 extends Function {
	public double calculateFunction(double value) {
		return (value + 40.0) / 540.0;
	}
}
