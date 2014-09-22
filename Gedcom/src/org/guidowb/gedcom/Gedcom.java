package org.guidowb.gedcom;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Gedcom {

	private List<GedcomRecord> recordsInFileOrder = new ArrayList<GedcomRecord>();

	public void addRecord(GedcomRecord record) {
		recordsInFileOrder.add(record);
	}

	public static Gedcom load(String fileName) throws IOException {
		File gedcomFile = new File(fileName);
		return load(gedcomFile);
	}
	
	public static Gedcom load(File file) throws IOException {
		InputStream input = new FileInputStream(file);
		return load(input);
	}
	
	public static Gedcom load(InputStream input) throws IOException {
		InputStreamReader reader = new InputStreamReader(input);
		return load(reader);
	}

	public static Gedcom load(InputStreamReader source) throws IOException {
		GedcomReader reader = new GedcomReader(source);
		return reader.read();
	}
}
