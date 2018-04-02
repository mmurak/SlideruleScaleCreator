package com.github.mmurak.scalemaker;

import com.github.mmurak.scalemaker.functions.*;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.util.Matrix;

public class SlideRuleCreator {

	public static void main(String[] args) {

		System.out.println("Slide rule creator ver. 1.04");
		if (args.length != 1) {		// Argument should be a directory-name for slide-rule configuration.
			System.err.println("usage error: specify folder name in config directory.");
			System.exit(1);
		}
		String filename = args[0];

		System.out.println("  processing " + filename + "...");	// the directory should be placed in 'config'.

		// Read config.dat and create HashTables for parameters.
		ParameterManager pm = new ParameterManager(SRConstant.DEFPATH + filename + File.separator + SRConstant.PARMFILENAME);

		try {
			if (pm.isCircular()) {
				System.out.println("  rule-type: circular");	// Process for circular slide-rule
				createCircularRule(pm, filename);
			} else {
				System.out.println("  rule-type: linear");	// Process for linear slide-rule
				createLinearRule(pm, filename);
			}
		} catch (ParameterException e) {
			System.err.println(e.msg);
			System.exit(1);
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
			System.exit(1);
		}
	}

	static void createCircularRule(ParameterManager pm, String filename) throws Exception {
		for(String inputFilename : pm.getScaleDefinitionFiles()) {
			String inputFilepath = SRConstant.DEFPATH + filename + File.separator + inputFilename;
			int ptr = inputFilename.lastIndexOf(".");
			String pdfname = (ptr >= 0) ? inputFilename.substring(0, ptr) : inputFilename;
			pdfname = pdfname + ".pdf";
			System.out.println(pdfname);

			ScaleSetManager ssm = new ScaleSetManager(inputFilepath, pm);

			PDDocument document = new PDDocument();
			pm.loadTTF(document);
			PDPage page = new PDPage(new PDRectangle(pm.getProperty(SRConstant.PAPERWIDTH),	pm.getProperty(SRConstant.PAPERHEIGHT)));
			float vmax = page.getMediaBox().getHeight();
			float hmax = page.getMediaBox().getWidth();  // prepared but never used
			float absCentreY = (float)(vmax / 2.0);
			float absCentreX = (float)(hmax / 2.0);
			document.addPage(page);
			PDPageContentStream contents = new PDPageContentStream(document, page);

		    contents.setStrokingColor(new Color(0, 0, 0));
		    contents.setLineWidth(pm.getProperty(SRConstant.LINEWIDTH));
		    float outerR = pm.getProperty(SRConstant.DIAMETER) / 2.0f;
			drawCircle(contents, absCentreX, absCentreY, outerR);

			float verticalOffset = outerR;
			for(ScaleDef sd : ssm.getScaleDefinitions()) {
				if (sd.header.equals(SRConstant.CUTMARK)) {
					contents.setStrokingColor(new Color(0, 0, 0));
					contents.setLineWidth(pm.getProperty(SRConstant.LINEWIDTH));
					drawCircle(contents, absCentreX, absCentreY, verticalOffset);
					continue;
				}
				if (sd.header.equals(SRConstant.SEPARATOR)) {
					contents.setStrokingColor(sd.bgcolour);
					contents.setLineWidth(pm.getProperty(SRConstant.LINEWIDTH));
					drawCircle(contents, absCentreX, absCentreY, verticalOffset);
					continue;
				}
				verticalOffset -= sd.upperGap;
				// entire colour of the scale
				if (sd.bgcolour != null) {
					contents.setNonStrokingColor(sd.bgcolour);
					fillCircle(contents, absCentreX, absCentreY, verticalOffset);
					contents.setNonStrokingColor(new Color(255, 255, 255));
					fillCircle(contents, absCentreX, absCentreY, verticalOffset - sd.height);
				}
				// notch & mark process
				ScaleFileManager sfm = new ScaleFileManager(SRConstant.DEFPATH + filename + File.separator +
						SRConstant.SCALEDEFPATH, sd.scaleDefFile + SRConstant.SCALEDEFFILEEXT, pm);
				// notch process
				for (NotchDef nd : sfm.getNotchDefinitions()) {
					// range colour of the scale
					if (nd.bgcolour != null) {
						contents.setNonStrokingColor(nd.bgcolour);
						float theta1 = (float)(Math.PI * 2 - sd.functionHandler.calculateFunction((double)nd.rangeStart / SRConstant.VALUEMAG) * Math.PI * 2.0);
						float theta2 = (float)(Math.PI * 2 - sd.functionHandler.calculateFunction((double)nd.rangeEnd / SRConstant.VALUEMAG) * Math.PI * 2.0);
						rangeFill(contents, theta1, theta2, verticalOffset,
							    nd.bgcolour, absCentreX, absCentreY);
						rangeFill(contents, theta1, theta2, verticalOffset - sd.height,
							    new Color(255, 255, 255), absCentreX, absCentreY);
					}
					int notchCount = nd.offset;
					contents.setStrokingColor(nd.fgcolour);
					contents.setLineWidth(pm.getProperty(SRConstant.LINEWIDTH));
					for (long nindex = nd.rangeStart; nindex < nd.rangeEnd; nindex += nd.increment) {
						float notchSize;
						if ((notchCount % nd.largeCycle) == 0) {
							notchSize = pm.getProperty(SRConstant.NOTCHSIZEL);
						} else if ((notchCount % nd.mediumCycle) == 0) {
							notchSize = pm.getProperty(SRConstant.NOTCHSIZEM);
						} else {
							notchSize = pm.getProperty(SRConstant.NOTCHSIZES);
						}
						float startPoint;
						if (sd.isUpward) {
							startPoint = verticalOffset - sd.height;
						} else {
							startPoint = verticalOffset - notchSize;
						}
						float theta = (float)(Math.PI * 2 - sd.functionHandler.calculateFunction((double)nindex / SRConstant.VALUEMAG) * Math.PI * 2.0);
						drawNotchAtPolarCoordinate(contents, theta, startPoint, notchSize, absCentreX, absCentreY);
						notchCount++;
					}
				}
				// mark process
				for (MarkDef md : sfm.getMarkDefinitions()) {
					NibDef nib = md.nib;
					contents.setFont(nib.font, nib.size);
					contents.setNonStrokingColor(nib.colour);
					float adjustV = 0.0f;
					if (sd.isUpward) {
						adjustV = verticalOffset - sd.height + pm.getProperty(SRConstant.NOTCHSIZEL) + nib.vOffset;
					} else {
						adjustV = verticalOffset - pm.getProperty(SRConstant.NOTCHSIZEL) 
								- adjustVPos(md.mark, nib.font, nib.size) - nib.vOffset;
					}
					float theta = (float)(Math.PI * 2 - sd.functionHandler.calculateFunction((double)md.val / SRConstant.VALUEMAG) * Math.PI * 2.0);
					drawStringAtPolarCoordinate(contents, theta, adjustV, -adjustHPos(md.mark, nib.font, nib.size, nib.alignment, nib.hOffset), md.mark, absCentreX, absCentreY);
					if (md.isGauge) {
						if (sd.isUpward) {
							adjustV = verticalOffset - sd.height + pm.getProperty(SRConstant.NOTCHSIZEL);
						} else {
							adjustV = verticalOffset - pm.getProperty(SRConstant.NOTCHSIZEL)
									+ pm.getProperty(SRConstant.GAUGEMARKNOTCHSIZE);
						}
						contents.setStrokingColor(nib.colour);
						contents.setLineWidth(pm.getProperty(SRConstant.LINEWIDTH));
						theta = (float)(Math.PI * 2 - sd.functionHandler.calculateFunction((double)md.val / SRConstant.VALUEMAG) * Math.PI * 2.0);
						drawNotchAtPolarCoordinate(contents, theta, adjustV, -pm.getProperty(SRConstant.GAUGEMARKNOTCHSIZE), absCentreX, absCentreY);
					}
				}
				// header & trailer
				NibDef nib = sd.nib;
				contents.setFont(nib.font, nib.size);
				contents.setNonStrokingColor(nib.colour);
				drawStringAtPolarCoordinate(contents, 0.0f, verticalOffset - (sd.height / 2.0f), -pm.getProperty(SRConstant.LEFTLEADING)-adjustHPos(sd.header, nib.font, nib.size, nib.alignment, nib.hOffset), sd.header, absCentreX, absCentreY);
				drawStringAtPolarCoordinate(contents, 0.0f, verticalOffset - (sd.height / 2.0f), pm.getProperty(SRConstant.RIGHTLEADING)-adjustHPos(sd.trailer, nib.font, nib.size, nib.alignment, nib.hOffset), sd.trailer, absCentreX, absCentreY);
				verticalOffset -= (sd.height + sd.lowerGap);
			}
			float coreR = pm.getProperty(SRConstant.COREDIAMETER) / 2.0f;
		    contents.setStrokingColor(new Color(0, 0, 0));
			drawCircle(contents, absCentreX, absCentreY, coreR);
			contents.moveTo(absCentreX - coreR, absCentreY);
			contents.lineTo(absCentreX + coreR, absCentreY);
			contents.stroke();
			contents.moveTo(absCentreX, absCentreY - coreR);
			contents.lineTo(absCentreX, absCentreY + coreR);
			contents.stroke();
			
			contents.close();
			document.save(pdfname);
			document.close();

		}
	}

	public static void drawNotchAtPolarCoordinate(PDPageContentStream contents, double theta, double r,
			double length, float absCentreX, float absCentreY)
		throws IOException {
		float fromx = (float)(Math.cos(theta) * r);
		float fromy = (float)(Math.sin(theta) * r);
		float tox = (float)(Math.cos(theta) * (r + length));
		float toy = (float)(Math.sin(theta) * (r + length));
		fromx += absCentreX;
		fromy += absCentreY;
		tox += absCentreX;
		toy += absCentreY;
		contents.moveTo(fromx, fromy); contents.lineTo(tox, toy); contents.stroke();
	}

	public static void drawStringAtPolarCoordinate(PDPageContentStream contents, double theta, double r, 
			float adjH, String text, float absCentreX, float absCentreY)
		throws IOException {
		float atx = (float)(Math.cos(theta) * r);
		float aty = (float)(Math.sin(theta) * r);
		contents.beginText();
		float fixy = (float)(adjH * Math.cos(theta));
		float fixx = (float)(adjH * Math.sin(theta));
		contents.setTextMatrix(Matrix.getRotateInstance(theta - (Math.PI/2.0), atx + absCentreX - fixx, aty + absCentreY + fixy));
		contents.showText(text);
		contents.endText();
	}


	static void drawCircle(PDPageContentStream contents, float cx, float cy, float r) throws Exception {
	    final float k = 0.552284749831f;	// (4 * sqrt(2) - 1) / 3
	    contents.moveTo(cx - r, cy);
	    contents.curveTo(cx - r, cy + k * r, cx - k * r, cy + r, cx, cy + r);
	    contents.curveTo(cx + k * r, cy + r, cx + r, cy + k * r, cx + r, cy);
	    contents.curveTo(cx + r, cy - k * r, cx + k * r, cy - r, cx, cy - r);
	    contents.curveTo(cx - k * r, cy - r, cx - r, cy - k * r, cx - r, cy);
	    contents.stroke();
	}

	static void fillCircle(PDPageContentStream contents, float cx, float cy, float r) throws Exception {
	    final float k = 0.552284749831f;	// (4 * sqrt(2) - 1) / 3
	    contents.moveTo(cx - r, cy);
	    contents.curveTo(cx - r, cy + k * r, cx - k * r, cy + r, cx, cy + r);
	    contents.curveTo(cx + k * r, cy + r, cx + r, cy + k * r, cx + r, cy);
	    contents.curveTo(cx + r, cy - k * r, cx + k * r, cy - r, cx, cy - r);
	    contents.curveTo(cx - k * r, cy - r, cx - r, cy - k * r, cx - r, cy);
	    contents.fill();
	}

	public static void rangeFill(PDPageContentStream contents,
		    float theta1, float theta2, float verticalOffset,
		    Color colour, float absCentreX, float absCentreY) throws IOException {
		if (theta1 > theta2) {
			float w = theta1;
			theta1 = theta2;
			theta2 = w;
		}
		contents.setNonStrokingColor(colour);
		float rightAngle = (float)(Math.PI / 2.0);
		for (float subrange = theta1; subrange < theta2; subrange += rightAngle) {
			float endRange = ((theta2 - subrange) > rightAngle) ? rightAngle : theta2 - subrange;
			endRange += subrange;
			List<Float> smallArc = createSmallArc(verticalOffset, subrange, endRange);
			contents.moveTo(absCentreX, absCentreY);
			contents.lineTo(smallArc.get(0) + absCentreX, smallArc.get(1) + absCentreY);
			contents.curveTo(smallArc.get(2) + absCentreX, smallArc.get(3) + absCentreY,
					smallArc.get(4) + absCentreX, smallArc.get(5) + absCentreY,
					smallArc.get(6) + absCentreX, smallArc.get(7) + absCentreY);
			contents.closePath();
			contents.fill();
		}
	}

	/**
     *  From https://hansmuller-flex.blogspot.com/2011/10/more-about-approximating-circular-arcs.html
     * 		( http://stackoverflow.com/questions/40781610/how-to-draw-a-pie-chart-using-pdfbox )
     *  Cubic bezier approximation of a circular arc centered at the origin, 
     *  from (radians) a1 to a2, where a2-a1 &lt; pi/2.  The arc's radius is r.
     * 
     *  Returns a list with 4 points, where x1,y1 and x4,y4 are the arc's end points
     *  and x2,y2 and x3,y3 are the cubic bezier's control points.
     * 
     *  This algorithm is based on the approach described in:
     *  Aleksas Riškus, "Approximation of a Cubic Bezier Curve by Circular Arcs and Vice Versa," 
     *  Information Technology and Control, 35(4), 2006 pp. 371-378.
     */
    private static List<Float> createSmallArc(double r, double a1, double a2)
    {
        // Compute all four points for an arc that subtends the same total angle
        // but is centered on the X-axis
        double a = (a2 - a1) / 2;
        double x4 = r * Math.cos(a);
        double y4 = r * Math.sin(a);
        double x1 = x4;
        double y1 = -y4;
        double q1 = x1*x1 + y1*y1;

        double q2 = q1 + x1*x4 + y1*y4;
        double k2 = 4/3d * (Math.sqrt(2 * q1 * q2) - q2) / (x1 * y4 - y1 * x4);
        double x2 = x1 - k2 * y1;
        double y2 = y1 + k2 * x1;
        double x3 = x2; 
        double y3 = -y2;

        // Find the arc points' actual locations by computing x1,y1 and x4,y4 
        // and rotating the control points by a + a1

        double ar = a + a1;
        double cos_ar = Math.cos(ar);
        double sin_ar = Math.sin(ar);

        List<Float> list = new ArrayList<Float>();
        list.add((float) (r * Math.cos(a1)));
        list.add((float) (r * Math.sin(a1))); 
        list.add((float) (x2 * cos_ar - y2 * sin_ar)); 
        list.add((float) (x2 * sin_ar + y2 * cos_ar)); 
        list.add((float) (x3 * cos_ar - y3 * sin_ar)); 
        list.add((float) (x3 * sin_ar + y3 * cos_ar)); 
        list.add((float) (r * Math.cos(a2))); 
        list.add((float) (r * Math.sin(a2)));
        return list;
    }

	
	static void createLinearRule(ParameterManager pm, String filename) throws Exception {
		for(String inputFilename : pm.getScaleDefinitionFiles()) {
			String inputFilepath = SRConstant.DEFPATH + filename + File.separator + inputFilename;
			int ptr = inputFilename.lastIndexOf(".");
			String pdfname = (ptr >= 0) ? inputFilename.substring(0, ptr) : inputFilename;
			pdfname = pdfname + ".pdf";
			System.out.println(pdfname);

			ScaleSetManager ssm = new ScaleSetManager(inputFilepath, pm);

			PDDocument document = new PDDocument();
			pm.loadTTF(document);
			PDPage page = new PDPage(new PDRectangle(pm.getProperty(SRConstant.PAPERWIDTH),	pm.getProperty(SRConstant.PAPERHEIGHT)));
			float vmax = page.getMediaBox().getHeight();
//			float hmax = page.getMediaBox().getWidth();  // prepared but never used
			document.addPage(page);
			PDPageContentStream contents = new PDPageContentStream(document, page);

			float verticalOffset = vmax;
			for(ScaleDef sd : ssm.getScaleDefinitions()) {
				if (sd.header.equals(SRConstant.CUTMARK)) {
					contents.setStrokingColor(new Color(0, 0, 0));
					contents.setLineWidth(pm.getProperty(SRConstant.LINEWIDTH));
					contents.moveTo(0, verticalOffset);
					contents.lineTo(pm.getProperty(SRConstant.CUTMARKSIZE),verticalOffset);
					contents.stroke();
					contents.moveTo(pm.getProperty(SRConstant.PAPERWIDTH), verticalOffset);
					contents.lineTo(pm.getProperty(SRConstant.PAPERWIDTH) - pm.getProperty(SRConstant.CUTMARKSIZE),verticalOffset);
					contents.stroke();
					continue;
				}
				if (sd.header.equals(SRConstant.SEPARATOR)) {
					contents.setStrokingColor(sd.bgcolour);
					contents.setLineWidth(pm.getProperty(SRConstant.LINEWIDTH));
					contents.moveTo(0, verticalOffset);
					contents.lineTo(pm.getProperty(SRConstant.PAPERWIDTH),verticalOffset);
					contents.stroke();
					continue;
				}
				verticalOffset -= sd.upperGap;
				if (sd.bgcolour != null) {
					contents.setNonStrokingColor(sd.bgcolour);
					contents.addRect(0, verticalOffset - sd.height, 
							pm.getProperty(SRConstant.LEFTMARGIN) + pm.getProperty(SRConstant.LENGTH) + pm.getProperty(SRConstant.RIGHTMARGIN), 
							sd.height);
					contents.fill();
				}
				// notch & mark process
				ScaleFileManager sfm = new ScaleFileManager(SRConstant.DEFPATH + filename + File.separator +
						SRConstant.SCALEDEFPATH, sd.scaleDefFile + SRConstant.SCALEDEFFILEEXT, pm);
				// notch process
				for (NotchDef nd : sfm.getNotchDefinitions()) {
					if (nd.bgcolour != null) {
						contents.setNonStrokingColor(nd.bgcolour);
						float s1 = getPosition(sd.functionHandler, nd.rangeEnd, pm);
						float s2 = getPosition(sd.functionHandler, nd.rangeStart, pm);
						if (s1 > s2) {		// for Invert scale
							float w = s1;
							s1 = s2;
							s2 = w;
						}
						contents.addRect(s1, verticalOffset - sd.height, s2 - s1, sd.height);
						contents.fill();
					}
					int notchCount = nd.offset;
					contents.setStrokingColor(nd.fgcolour);
					contents.setLineWidth(pm.getProperty(SRConstant.LINEWIDTH));
					for (long nindex = nd.rangeStart; nindex < nd.rangeEnd; nindex += nd.increment) {
						float notchSize;
						if ((notchCount % nd.largeCycle) == 0) {
							notchSize = pm.getProperty(SRConstant.NOTCHSIZEL);
						} else if ((notchCount % nd.mediumCycle) == 0) {
							notchSize = pm.getProperty(SRConstant.NOTCHSIZEM);
						} else {
							notchSize = pm.getProperty(SRConstant.NOTCHSIZES);
						}
						float startPoint;
						if (sd.isUpward) {
							startPoint = verticalOffset - sd.height;
						} else {
							startPoint = verticalOffset - notchSize;
						}
						float pos = getPosition(sd.functionHandler, nindex, pm);
						contents.moveTo(pos, startPoint);
						contents.lineTo(pos, startPoint + notchSize);
						contents.stroke();
						notchCount++;
					}
				}
				// mark process
				for (MarkDef md : sfm.getMarkDefinitions()) {
					NibDef nib = md.nib;
					contents.setFont(nib.font, nib.size);
					contents.setNonStrokingColor(nib.colour);
					contents.beginText();
					float adjustV = 0.0f;
					if (sd.isUpward) {
						adjustV = verticalOffset - sd.height + pm.getProperty(SRConstant.NOTCHSIZEL) + nib.vOffset;
					} else {
						adjustV = verticalOffset - pm.getProperty(SRConstant.NOTCHSIZEL) 
								- adjustVPos(md.mark, nib.font, nib.size) - nib.vOffset;
					}
					float adjustH = getPosition(sd.functionHandler, md.val, pm) 
							+ adjustHPos(md.mark, nib.font, nib.size, nib.alignment, nib.hOffset);
					contents.setTextMatrix(new Matrix(1, 0, nib.shear, 1.0f,
							adjustH,
							adjustV));
					contents.showText(md.mark);
					contents.endText();
					if (md.isGauge) {
						if (sd.isUpward) {
							adjustV = verticalOffset - sd.height + pm.getProperty(SRConstant.NOTCHSIZEL);
						} else {
							adjustV = verticalOffset - pm.getProperty(SRConstant.NOTCHSIZEL)
									+ pm.getProperty(SRConstant.GAUGEMARKNOTCHSIZE);
						}
						adjustH = getPosition(sd.functionHandler, md.val, pm);
						contents.setStrokingColor(nib.colour);
						contents.setLineWidth(pm.getProperty(SRConstant.LINEWIDTH));
						contents.moveTo(adjustH, adjustV);
						contents.lineTo(adjustH, adjustV - pm.getProperty(SRConstant.GAUGEMARKNOTCHSIZE));
						contents.stroke();
					}
				}
				// header & trailer
				NibDef nib = sd.nib;
				contents.setFont(nib.font, nib.size);
				contents.setNonStrokingColor(nib.colour);
				contents.beginText();
				contents.setTextMatrix(new Matrix(1, 0, nib.shear, 1.0f, 
						pm.getProperty(SRConstant.LEFTLEADING) + adjustHPos(sd.header, nib.font, nib.size, nib.alignment, nib.hOffset), 
						verticalOffset - (sd.height / 2.0f) - adjustVPos(sd.header, nib.font, nib.size) / 2.0f));
				contents.showText(sd.header);
				contents.setTextMatrix(new Matrix(1, 0, nib.shear, 1.0f, 
						pm.getProperty(SRConstant.LEFTMARGIN) + pm.getProperty(SRConstant.LENGTH) + pm.getProperty(SRConstant.RIGHTLEADING) + adjustHPos(sd.trailer, nib.font, nib.size, nib.alignment, nib.hOffset), 
						verticalOffset - (sd.height / 2.0f) - adjustVPos(sd.trailer, nib.font, nib.size) / 2.0f));
				contents.showText(sd.trailer);
				contents.endText();
				verticalOffset -= (sd.height + sd.lowerGap);
			}
			contents.close();
			document.save(pdfname);
			document.close();
		}
	}

	static float getPosition(Function f, double value, ParameterManager pm) throws ParameterException {
		return (float)(f.calculateFunction((double)value / SRConstant.VALUEMAG) * pm.getProperty(SRConstant.LENGTH)) + pm.getProperty(SRConstant.LEFTMARGIN);
	}
	static float adjustHPos(String str, PDFont font, float size, String align, float hOffset) throws Exception {
		float textWidth = font.getStringWidth(str) / 1000 * size;
		switch (align) {
		case SRConstant.LEFT :
			return -textWidth - hOffset;
		case SRConstant.CENTRE :
			return textWidth / -2.0f;
		default:
			return 0.0f + hOffset;
		}
	}
	static float adjustVPos(String str, PDFont font, float size) throws Exception {
		return font.getFontDescriptor().getCapHeight() / 1000 * size;
	}
}
