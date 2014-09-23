package org.guidowb.gedcom;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class GedcomWriter {

	public static void save(Gedcom gedcom, String filename) throws IOException { save(gedcom, new File(filename)); }
	public static void save(Gedcom gedcom, File file) throws IOException { save(gedcom, new FileOutputStream(file)); }
	public static void save(Gedcom gedcom, OutputStream output) throws IOException { save(gedcom, new OutputStreamWriter(output)); }
	public static void save(Gedcom gedcom, OutputStreamWriter target) throws IOException {
		GedcomWriter writer = new GedcomWriter(target);
		writer.write(gedcom);
	}
	
	public static void print(Gedcom gedcom) throws IOException { print(gedcom, System.out); }
	public static void print(Gedcom gedcom, OutputStream target) throws IOException { print(gedcom, new OutputStreamWriter(target)); }
	public static void print(Gedcom gedcom, OutputStreamWriter target) throws IOException {
		GedcomWriter writer = new GedcomWriter(target);
		writer.write(gedcom, true);
	}

	private OutputStreamWriter writer;

	private GedcomWriter(OutputStreamWriter writer) {
		this.writer = writer;
	}
	
	private void write(Gedcom gedcom) throws IOException { write(gedcom, false); }
	private void write(Gedcom gedcom, boolean pretty) throws IOException {
		for (GedcomRecord record : gedcom.records()) {
			writer.write(record.toString(pretty));
		}
		writer.close();
	}
}
