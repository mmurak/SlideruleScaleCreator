package com.github.mmurak.scalemaker;

import java.awt.Color;

public class NotchDef {
	long rangeStart;
	long rangeEnd;
	Color bgcolour;
	Color fgcolour;
	int offset;
	long increment;
	int mediumCycle;
	int largeCycle;
	public NotchDef(long rangeStart, long rangeEnd, Color bgcolour, Color fgcolour, int offset, long increment, int mediumCycle, int largeCycle) {
		this.rangeStart = rangeStart;
		this.rangeEnd = rangeEnd;
		this.bgcolour = bgcolour;
		this.fgcolour = fgcolour;
		this.offset = offset;
		this.increment = increment;
		this.mediumCycle = mediumCycle;
		this.largeCycle = largeCycle;
	}

}
