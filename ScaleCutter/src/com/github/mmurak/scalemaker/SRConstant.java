package com.github.mmurak.scalemaker;

import java.io.File;

public interface SRConstant {
	final String DEFPATH = "config" + File.separator;
	final String FONTPATH = "fonts" + File.separator;
	final String SCALEDEFPATH = "scaleDefs" + File.separator;
	final String PARMFILENAME = "config.dat";
	final String SCALEDEFFILEEXT = ".mrk";
	final float POINTS_PER_INCH = 72.0f;
	final float POINTS_PER_MM = 1.0f / (10.0f * 2.54f) * POINTS_PER_INCH;
	final long VALUEMAG = 1000000;
	//
	final String RULETYPE = "RULE-TYPE";
	final String CIRCULAR = "CIRCULAR";
	final String LINEAR = "LINEAR";
	final String DIAMETER = "DIAMETER";
	final String COREDIAMETER = "CORE-DIAMETER";
	final String PAPERWIDTH = "PAPER-WIDTH";
	final String PAPERHEIGHT = "PAPER-HEIGHT";
	final String LINEWIDTH = "LINE-WIDTH";
	final String LEFTMARGIN = "LEFT-MARGIN";
	final String LEFTLEADING = "LEFT-LEADING";
	final String RIGHTLEADING = "RIGHT-LEADING";
	final String LENGTH = "LENGTH";
	final String RIGHTMARGIN = "RIGHT-MARGIN";
	final String CUTMARK = "-cut mark-";
	final String CUTMARKSIZE = "CUT-MARK-SIZE";
	final String SEPARATOR = "-separator-";
	final String NOTCHSIZES = "NOTCH-SIZE-S";
	final String NOTCHSIZEM = "NOTCH-SIZE-M";
	final String NOTCHSIZEL = "NOTCH-SIZE-L";
	final String GAUGEMARKNOTCHSIZE = "GAUGE-MARK-NOTCH-SIZE";
	final String LEFT = "LEFT";
	final String CENTRE = "CENTRE";
	final String RIGHT = "RIGHT";
	final String UP = "UP";
	final String DOWN = "DOWN";
	final String COLOUR = "COLOUR";
	final String NIB = "NIB";
	final String SCALESET = "SCALE-SET";
	final String DEFAULTHEIGHT = "DEFAULT-HEIGHT";
}
