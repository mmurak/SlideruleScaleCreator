package com.github.mmurak.scalemaker;

import java.awt.Color;
import org.apache.pdfbox.pdmodel.font.PDFont;

public class NibDef {

	PDFont font;
	String ttfName;
	float size;
	Color colour;
	float vOffset;
	String alignment;
	float hOffset;
	float shear;

	public NibDef(PDFont font, String ttfName, float size, Color colour, float vOffset, String alignment, float hOffset, float shear) {
		this.font = font;
		this.ttfName = ttfName;
		this.size = size;
		this.colour = colour;
		this.vOffset = vOffset;
		this.alignment = alignment;
		this.hOffset = hOffset;
		this.shear = shear;
	}

}
