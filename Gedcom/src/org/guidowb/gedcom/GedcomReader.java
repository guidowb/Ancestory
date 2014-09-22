package org.guidowb.gedcom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GedcomReader {

	private BufferedReader reader;

	GedcomReader(InputStreamReader reader) {
		this.reader = new BufferedReader(reader);
	}
	
	public Gedcom read() throws IOException {
		Gedcom gedcom = new Gedcom();
		GedcomLine line = new GedcomLine();
		GedcomRecord lastRecord = null;
		int lineno = 0;
		while (line.read(reader)) {
			GedcomRecord record = line.parse(gedcom, ++lineno);
			if (record.isContinuation()) {
				lastRecord.merge(record);
				continue;
			}
			if (lastRecord != null) gedcom.addRecord(lastRecord);
			lastRecord = record;
		}
		if (lastRecord != null) gedcom.addRecord(lastRecord);
		return gedcom;
	}
}
