package com.github.mmurak.scalemaker;

import java.awt.Color;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.regex.Pattern;
import java.util.Vector;

public class ScaleFileManager {

	Vector<NotchDef> notchDefinitions;
	Vector<MarkDef> markDefinitions;
	
	public ScaleFileManager(String path, String filename, ParameterManager pm) {
		notchDefinitions = new Vector<NotchDef>();
		markDefinitions = new Vector<MarkDef>();
		Pattern spaces = Pattern.compile("^\\s*$");
		Pattern numbers = Pattern.compile("^-?\\d*$");
		String fullpath;
		if (new File(path + filename).exists()) {
			fullpath = path + filename;			
		} else {
			fullpath = SRConstant.DEFPATH + SRConstant.SCALEDEFPATH + filename;
		}
		System.out.println("  " + fullpath);
		try (BufferedReader br = 
				Files.newBufferedReader(Paths.get(fullpath), StandardCharsets.UTF_8)) {
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
				switch (elem.length) {
				case 8 :
					if (numbers.matcher(elem[0]).matches() &&		// rangeStart
						numbers.matcher(elem[1]).matches() &&		// rangeEnd
						numbers.matcher(elem[4]).matches() &&		// offset
						numbers.matcher(elem[5]).matches() &&		// increment
						numbers.matcher(elem[6]).matches() &&		// mediumCycle
						numbers.matcher(elem[7]).matches())	{		// largeCycle
						Color bg;
						if (elem[2].equals("")) {
							bg = null;
						} else {
							bg = pm.getColour(elem[2]);
						}
						Color fg = pm.getColour(elem[3]);
						notchDefinitions.add(new NotchDef(Long.parseLong(elem[0]),
								Long.parseLong(elem[1]),
								bg, fg, Integer.parseInt(elem[4]),
								Long.parseLong(elem[5]),
								Integer.parseInt(elem[6]),
								Integer.parseInt(elem[7])));
					} else {
						throw new ParameterException("Mark file error: " + line);
					}
					break;
				case 3 :
				case 4 :
					boolean isGauge = (elem.length == 3) ? false : true;
					if (numbers.matcher(elem[0]).matches()) {
						markDefinitions.add(new MarkDef(Long.parseLong(elem[0]),
								pm.getNibDef(elem[1]), elem[2], isGauge));
					}
					break;
				default :
					throw new ParameterException("Mark file number of items error (" + elem.length + ") : " + line);
				}
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
	public Vector<NotchDef> getNotchDefinitions() {
		return notchDefinitions;
	}
	public Vector<MarkDef> getMarkDefinitions() {
		return markDefinitions;
	}
}
