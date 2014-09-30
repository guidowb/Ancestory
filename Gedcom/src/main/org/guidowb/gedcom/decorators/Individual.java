package org.guidowb.gedcom.decorators;

import org.guidowb.gedcom.GedcomDecorator;
import org.guidowb.gedcom.GedcomRecord;

public class Individual extends GedcomDecorator {
	private Name name = null;

	public Name getName() {
		if (name != null) return name;
		name = Name.getName(record);
		return name;
	}

	public static synchronized Individual getIndividual(GedcomRecord record) {
		if (!record.getTag().equals("INDI")) return null;
		Individual individual = record.getDecorator(Individual.class);
		if (individual != null) return individual;
		individual = new Individual();
		record.addDecorator(individual);
		return individual;
	}
}
