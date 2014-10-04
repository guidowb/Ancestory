package org.guidowb.gedcom.test.system;

import java.io.IOException;

import org.guidowb.gedcom.Gedcom;
import org.guidowb.gedcom.GedcomReader;
import org.guidowb.gedcom.decorators.Individual;
import org.guidowb.gedcom.decorators.Lifespan;
import org.guidowb.gedcom.decorators.Name;
import org.guidowb.gedcom.indices.NameIndex;

public class ListIndividuals {

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.err.println("Usage: ListIndividuals <gedfile>");
			System.exit(1);
		}
		Gedcom gedcom = GedcomReader.load(args[0]);
		for (Individual individual : gedcom.getIndex(NameIndex.class).individuals()) {
			Name name = individual.getName();
			Lifespan lifespan = individual.getLifespan();
			System.out.print(name.getCanonical());
			if (!lifespan.toString().isEmpty()) System.out.print(" (" + lifespan.toString() + ")");
			System.out.println();
			for (Name alias : name.aliases()) {
				System.out.println("    " + alias.getCanonical());
			}
		}
	}
}
