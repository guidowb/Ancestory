package org.guidowb.gedcom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GedcomReader {

	public static Gedcom load(String filename) throws IOException { return load(new File(filename)); }
	public static Gedcom load(File file) throws IOException { return load(new FileInputStream(file)); }
	public static Gedcom load(InputStream input) throws IOException { return load(new InputStreamReader(input)); }
	public static Gedcom load(InputStreamReader source) throws IOException {
		GedcomReader reader = new GedcomReader(source);
		return reader.read();
	}

	private BufferedReader reader;

	private GedcomReader(InputStreamReader reader) {
		this.reader = new BufferedReader(reader);
	}
	
	private Gedcom read() throws IOException {
		Gedcom gedcom = new Gedcom();
		GedcomLine line = new GedcomLine();
		GedcomRecord lastRecord = null;
		int lineno = 0;
		while (line.read(reader)) {
			GedcomRecord record = line.parse(gedcom, ++lineno);
			if (record.isContinuation()) {
				lastRecord.mergeContinuation(record);
				continue;
			}
			if (lastRecord != null) gedcom.addRecord(lastRecord);
			lastRecord = record;
		}
		if (lastRecord != null) gedcom.addRecord(lastRecord);
		return gedcom;
	}
}
