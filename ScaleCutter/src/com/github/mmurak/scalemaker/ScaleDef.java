package com.github.mmurak.scalemaker;

import com.github.mmurak.scalemaker.functions.*;
import java.awt.Color;

public class ScaleDef {
    public String header;
    public String trailer;
    public float upperGap;
    public float height;
    public float lowerGap;
    public Color bgcolour;
    public NibDef nib;
    public Function functionHandler;
    public boolean isUpward;
    public String scaleDefFile;
    public ScaleDef() {
        super();
    }
    public ScaleDef(String header, String trailer,
    		float upperGap, float height, float lowerGap,
                Color bgcolour, NibDef nib,
                Function functionHandler, boolean isUpward,
                String scaleDefFile) {
        this.header = header;
        this.trailer = trailer;
        this.upperGap = upperGap;
        this.height = height;
        this.lowerGap = lowerGap;
        this.bgcolour = bgcolour;
        this.nib = nib;
        this.functionHandler = functionHandler;
        this.isUpward = isUpward;
        this.scaleDefFile = scaleDefFile;
    }
}
