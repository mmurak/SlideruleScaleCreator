package com.github.mmurak.scalemaker;

import java.awt.Color;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.Vector;

public class ParameterManager {

	private Hashtable<String, Float> properties;
	private Hashtable<String, Color> colourDictionary;
	private Hashtable<String, NibDef> nibDictionary;
	private Vector<String> scaleDefinitionFiles;
	private Hashtable<String, File> ttfDictionary;
	enum RULETYPE {LINEAR, CIRCULAR};
	private RULETYPE ruletype;
	
	public ParameterManager(String filename) {
		properties = new Hashtable<String, Float>();
		colourDictionary = new Hashtable<String, Color>();
		nibDictionary = new Hashtable<String, NibDef>();
		scaleDefinitionFiles = new Vector<String>();
		ttfDictionary = new Hashtable<String, File>();
		Pattern spaces = Pattern.compile("^\\s*$");
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
				String[] elem = line.split(":");
				if (elem.length != 2) {
					throw new ParameterException("Unrecognisable parameter: " + line);
				}
				String name = elem[0].toUpperCase();
				switch (name) {
				case SRConstant.COLOUR :
					String[] w = elem[1].split("=");
					if (w.length != 2) {
						throw new ParameterException("Colour spedification error: " + elem[1]);
					}
					String[] p = w[1].split(",");
					if (p.length != 3) {
						throw new ParameterException("Colour spedification parameter error: " + elem[1]);
					}
					String wkey = w[0].toUpperCase();
					if (colourDictionary.containsKey(wkey)) {
						throw new ParameterException("Colour name already defined: " + w[0]);
					}
					colourDictionary.put(wkey, 
						new Color(Integer.parseInt(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2])));
					break;
				case SRConstant.NIB :
					w = elem[1].split("=");
					if (w.length != 2) {
						throw new ParameterException("Nib spedification error: " + elem[1]);
					}
					p = w[1].split(",");
					if (p.length != 7) {
						throw new ParameterException("Nib spedification parameter error: " + elem[1]);
					}
					wkey = w[0].toUpperCase();
					if (nibDictionary.containsKey(wkey)) {
						throw new ParameterException("Nib name already defined: " + w[0]);
					}
					PDFont font;
					String ttfName = "";
					switch (p[0].toUpperCase()) {
					case "COURIER" :
						font = PDType1Font.COURIER;
						break;
					case "COURIER_BOLD" :
						font = PDType1Font.COURIER_BOLD;
						break;
					case "COURIER_BOLD_OBLIQUE" :
						font = PDType1Font.COURIER_BOLD_OBLIQUE;
						break;
					case "COURIER_OBLIQUE" :
						font = PDType1Font.COURIER_OBLIQUE;
						break;
					case "HELVETICA" :
						font = PDType1Font.HELVETICA;
						break;
					case "HELVETICA_BOLD" :
						font = PDType1Font.HELVETICA_BOLD;
						break;
					case "HELVETICA_BOLD_OBLIQUE" :
						font = PDType1Font.HELVETICA_BOLD_OBLIQUE;
						break;
					case "HELVETICA_OBLIQUE" :
						font = PDType1Font.HELVETICA_OBLIQUE;
						break;
					case "SYMBOL" :
						font = PDType1Font.SYMBOL;
						break;
					case "TIMES_ROMAN" :
						font = PDType1Font.TIMES_ROMAN;
						break;
					case "TIMES_BOLD" :
						font = PDType1Font.TIMES_BOLD;
						break;
					case "TIMES_BOLD_ITALIC" :
						font = PDType1Font.TIMES_BOLD_ITALIC;
						break;
					case "TIMES_ITALIC" :
						font = PDType1Font.TIMES_ITALIC;
						break;
					case "ZAPF_DINGBATS" :
						font = PDType1Font.ZAPF_DINGBATS;
						break;
					default :
						if (p[0].startsWith(SRConstant.FONTPATH)) {
							font = null;
							ttfName = p[0];
							if (!ttfDictionary.contains(ttfName)) {
								ttfDictionary.put(ttfName, new File(ttfName));
							}
						} else {
							throw new ParameterException("Nib font name invalud:" + p[0]);
						}
					}
					String align = p[4].toUpperCase();
					if (colourDictionary.get(p[2]) == null) {
						throw new ParameterException("Colour not defined: " + p[2]);
					}
					if (align.equals(SRConstant.CENTRE) || align.equals(SRConstant.LEFT) || align.equals(SRConstant.RIGHT)) {
						nibDictionary.put(wkey, 
								new NibDef(font,					// Font
										ttfName,					// TTF name
										Float.parseFloat(p[1]),		// size
										colourDictionary.get(p[2]),	// Colour
										Float.parseFloat(p[3]),		// vOffset
										p[4],						// alignment
										Float.parseFloat(p[5]),		// hOffset
										Float.parseFloat(p[6])));	// shearing factor
					} else {
						throw new ParameterException("Nib alignment error: " + p[4]);
					}
					break;
				case SRConstant.SCALESET :
					scaleDefinitionFiles.add(elem[1]);
					break;
				case SRConstant.LENGTH :
				case SRConstant.DIAMETER :
				case SRConstant.COREDIAMETER :
				case SRConstant.PAPERWIDTH :
				case SRConstant.PAPERHEIGHT :
				case SRConstant.LEFTMARGIN :
				case SRConstant.LEFTLEADING :
				case SRConstant.RIGHTMARGIN :
				case SRConstant.RIGHTLEADING :
				case SRConstant.DEFAULTHEIGHT :
				case SRConstant.LINEWIDTH :
				case SRConstant.NOTCHSIZES :
				case SRConstant.NOTCHSIZEM :
				case SRConstant.NOTCHSIZEL :
				case SRConstant.GAUGEMARKNOTCHSIZE :
				case SRConstant.CUTMARKSIZE :
					properties.put(name,  Float.parseFloat(elem[1]) * SRConstant.POINTS_PER_MM);
					break;
				case SRConstant.RULETYPE :
					if (elem[1].equals(SRConstant.LINEAR)) {
						ruletype = RULETYPE.LINEAR;
					} else if (elem[1].equals(SRConstant.CIRCULAR)) {
						ruletype = RULETYPE.CIRCULAR;
					} else {
						throw new ParameterException("Unrecognisable rule type: " + line);
					}
					break;
				default :
					throw new ParameterException("Unrecognisable parameter-key: " + line);
				}
			}
			br.close();
		} catch (ParameterException e) {
			System.err.println(e.msg);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("IO exception: " + e.getMessage());
			System.exit(1);
		}
	}
	public void loadTTF(PDDocument doc) throws IOException {
		for (Map.Entry<String, File> ettf : ttfDictionary.entrySet()) {
			String filename = ettf.getKey();
			PDFont font = PDType0Font.load(doc, ettf.getValue());
			for (Map.Entry<String, NibDef> enib : nibDictionary.entrySet()) {
				NibDef nibdef = enib.getValue();
				if (nibdef.ttfName.equals(filename)) {
					nibdef.font = font;
				}
			}
		}
	}
	public Color getColour(String colourName) throws ParameterException {
		if (colourDictionary.containsKey(colourName)) {
			return colourDictionary.get(colourName);
		} else {
			throw new ParameterException("Colour not defined: " + colourName);
		}
	}
	public Enumeration<String> getColourDictionaryKeys() {		// for debug use
		return colourDictionary.keys();
	}
	public NibDef getNibDef(String nibName) throws ParameterException {
		if (nibDictionary.containsKey(nibName)) {
			return nibDictionary.get(nibName);
		} else {
			throw new ParameterException("Nib not defined: " + nibName);
		}
	}
	public Enumeration<String> getNibDictionaryKeys() {		// for debug use
		return nibDictionary.keys();
	}
	public float getProperty(String p) throws ParameterException {
		if (properties.containsKey(p)) {
			return properties.get(p);
		} else {
			throw new ParameterException("Property not defined:" + p);
		}
	}
	public Enumeration<String> getPropertyKeys() {		// for debug use
		return properties.keys();
	}
	public Vector<String> getScaleDefinitionFiles() {
		return scaleDefinitionFiles;
	}
	public boolean isCircular() {
		return ruletype == RULETYPE.CIRCULAR; 
	}

}
