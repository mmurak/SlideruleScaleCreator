package com.github.mmurak.scalemaker;

import java.awt.Color;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.regex.Pattern;

import com.github.mmurak.scalemaker.functions.Function;

import java.util.Vector;

public class ScaleSetManager {

	Vector<ScaleDef> scaleDefinitions;
	
	public ScaleSetManager(String filename, ParameterManager pm) {
		scaleDefinitions = new Vector<ScaleDef>();
		Pattern spaces = Pattern.compile("^\\s*$");
		Pattern numbers = Pattern.compile("^-?[\\d\\.]*$");
		try (BufferedReader br = 
				Files.newBufferedReader(Paths.get(filename), StandardCharsets.UTF_8)) {
			while (true) {
				String line = br.readLine();
				if (line == null) {
					break;
				}
				line = line.replaceAll("[ \t]*#.*", "");	// remove comment contents
				if (spaces.matcher(line).matches()) {
					continue;
				}
				if (line.equals("-")) {		// cut mark (begins with '-')
					scaleDefinitions.add(new ScaleDef(SRConstant.CUTMARK, "", 0.0f, 0.0f, 0.0f, null, null, null, false, ""));
					continue;
				}
				if (line.substring(0, 3).equals("---")) {	// separator line (begins with '---')
					String[] lcolour = line.split(":");
					if (lcolour.length != 2) {
						throw new ParameterException("Separotor colour error:" + line);
					}
					scaleDefinitions.add(new ScaleDef(SRConstant.SEPARATOR, "", 0.0f, 0.0f, 0.0f, pm.getColour(lcolour[1]), null, null, true, ""));
					continue;
				}
				String[] elem = line.split(":");
				if (elem.length != 10) {
					throw new ParameterException("Parameter format error: " + line);
				}
				// Numeric items
				float ugap, height, lgap;
				if (elem[2].equals("-")) {					// if upper gap == "-" then overlay print
					elem[2] = Float.toString(-pm.getProperty(SRConstant.DEFAULTHEIGHT) / SRConstant.POINTS_PER_MM);
				}
				if (numbers.matcher(elem[2]).matches() &&	// upper gap
					numbers.matcher(elem[3]).matches() &&	// height
					numbers.matcher(elem[4]).matches()) {	// lower gap
					if (elem[2].equals("")) {
						ugap = 0.0f;
					} else {
						ugap = Float.parseFloat(elem[2]) * SRConstant.POINTS_PER_MM;
					}
					if (elem[3].equals("")) {
						height = pm.getProperty(SRConstant.DEFAULTHEIGHT);
					} else {
						height = Float.parseFloat(elem[3]) * SRConstant.POINTS_PER_MM;
					}
					if (elem[4].equals("")) {
						lgap = 0.0f;
					} else {
						lgap = Float.parseFloat(elem[4]) * SRConstant.POINTS_PER_MM;
					}
				} else {
					throw new ParameterException("Gap/height is not numeric: " + line);
				}
				// Coloured items
				Color bg = (elem[5].equals("")) ? null : pm.getColour(elem[5]);
				NibDef fg = pm.getNibDef(elem[6]);
				// Function name
				Function func = (Function)Class.forName("com.github.mmurak.scalemaker.functions." + elem[7]).newInstance();
				// Notch direction (up or down)
				String temp = elem[8].toUpperCase();
				boolean notchDir;
				if (temp.equals(SRConstant.UP)) {
					notchDir = true;
				} else if (temp.equals(SRConstant.DOWN)) {
					notchDir = false;
				} else {
					throw new ParameterException("Notch direction is not UP/DOWN: " + elem[7]);
				}
				scaleDefinitions.add(new ScaleDef(elem[0], elem[1],	// Label
						ugap, height, lgap,							// Gaps
						bg, fg,										// Colours
						func,										// Function
						notchDir,									// Notch direction
						elem[9]));									// Scale def file name
			}
			br.close();
		} catch (NumberFormatException e) {
			System.err.println("Number Format Illegal.");
			System.exit(1);
		} catch (ParameterException e) {
			System.err.println(e.msg);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("IO exception: " + e.getMessage());
			System.exit(1);
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
			System.exit(1);
		}
	}
//	public int numberOfScaleDefinitions() {
//		return scaleDefinitions.size();
//	}
//	public ScaleDef getScaleDefinition(int i) {
//		return scaleDefinitions.elementAt(i);
//	}
	public Vector<ScaleDef> getScaleDefinitions() {
		return scaleDefinitions;
	}
}
