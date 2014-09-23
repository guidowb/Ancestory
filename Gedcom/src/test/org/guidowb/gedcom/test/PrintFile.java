package org.guidowb.gedcom.test;

import java.io.IOException;

import org.guidowb.gedcom.Gedcom;
import org.guidowb.gedcom.GedcomReader;
import org.guidowb.gedcom.GedcomWriter;

public class PrintFile {

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println("Usage: PrintFile <gedfile>");
			System.exit(1);
		}
		Gedcom gedcom = GedcomReader.load(args[0]);
		GedcomWriter.print(gedcom);
	}

}
