package org.guidowb.gedcom.test.system;

import java.io.IOException;

import org.guidowb.gedcom.Gedcom;
import org.guidowb.gedcom.GedcomReader;
import org.guidowb.gedcom.GedcomRecord;
import org.guidowb.gedcom.indices.NameIndex;

public class ListIndividuals {

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println("Usage: ListIndividuals <gedfile>");
			System.exit(1);
		}
		Gedcom gedcom = GedcomReader.load(args[0]);
		for (GedcomRecord individual : gedcom.getIndex(NameIndex.class).individuals()) {
			String original = individual.getField("NAME").getValue();
			String parsed = individual.getDecoration(NameIndex.Name.class).toString();
			System.out.println(parsed + " [" + original + "]");
		}
	}

}
