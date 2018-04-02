package com.github.mmurak.scalemaker;

public class MarkDef {

	long val;
	NibDef nib;
	String mark;
	boolean isGauge;
	public MarkDef(long val, NibDef nib, String mark, boolean isGauge) {
		this.val = val;
		this.nib = nib;
		this.mark = mark;
		this.isGauge = isGauge;
	}

}
