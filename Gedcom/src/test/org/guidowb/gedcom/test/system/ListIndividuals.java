package org.guidowb.gedcom.test.system;

import java.io.IOException;

import org.guidowb.gedcom.Gedcom;
import org.guidowb.gedcom.GedcomReader;
import org.guidowb.gedcom.GedcomRecord;
import org.guidowb.gedcom.indices.RootRecordIndex;

public class ListIndividuals {

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println("Usage: ListIndividuals <gedfile>");
			System.exit(1);
		}
		Gedcom gedcom = GedcomReader.load(args[0]);
		for (GedcomRecord individual : gedcom.getIndex(RootRecordIndex.class).getRecords("INDI")) {
			System.out.println(individual.getName());
		}
	}

}
