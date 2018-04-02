package com.github.mmurak.scalemaker.functions;

public class EI2piroot12 extends Function {
	public double calculateFunction(double value) {
		return 1.0 - ((Math.log10(value * 1000) - Math.log10(Math.sqrt(Math.PI * 2.0)))  / 12.0);
	}
}