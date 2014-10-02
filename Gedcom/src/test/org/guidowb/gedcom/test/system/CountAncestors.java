package org.guidowb.gedcom.test.system;

import java.io.IOException;
import java.util.List;

import org.guidowb.gedcom.Gedcom;
import org.guidowb.gedcom.GedcomReader;
import org.guidowb.gedcom.decorators.AncestorCount;
import org.guidowb.gedcom.decorators.Individual;
import org.guidowb.gedcom.indices.NameIndex;
import org.guidowb.gedcom.indices.NameIndex.Match;

public class CountAncestors {

	public static void main(String[] args) throws IOException {
		if (args.length < 2) {
			System.err.println("Usage: FindAncestors <gedfile> <individual>");
			System.exit(1);
		}
		Gedcom gedcom = GedcomReader.load(args[0]);
		List<Match> matches = gedcom.getIndex(NameIndex.class).findIndividuals(args[1]);
		if (matches.size() < 1) {
			System.err.println(args[1] + " doesn't match anyone in this file");
			System.exit(1);
		}
		if (matches.size() > 1) {
			System.err.println(args[1] + " matches more than one individual:");
			for (Match match : matches) {
				System.err.println("    " + match.name.getNormalized());
			}
			System.exit(1);
		}
		Individual individual = matches.get(0).individual;
		System.out.println("Ancestor counts of " + individual.getName().getNormalized() + ":");
		AncestorCount count = individual.getAncestorCount();
		for (int i = 0; i < count.getGenerations(); i++) {
			System.out.println(String.format("   %2d %4d (%.0f%%)", i+1, count.getCount(i), count.getPercentage(i)));
		}
	}
}
