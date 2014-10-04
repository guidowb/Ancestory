package org.guidowb.gedcom.decorators;

import java.util.ArrayList;
import java.util.List;

import org.guidowb.gedcom.GedcomDecorator;
import org.guidowb.gedcom.GedcomRecord;

public class Individual extends GedcomDecorator {

	private List<Individual> children = null;

	public Name getName() { return Name.getName(record); }
	public String getDescription() { return Description.getDescription(record).toString(); }
	public Lifespan getLifespan() { return Lifespan.getLifespan(record); }
	private Ancestry getAncestry() { return Ancestry.getAncestry(record); }
	public AncestorCount getAncestorCount() { return AncestorCount.getAncestorCount(record); }

	public Individual getFather() { return getIndividual(getAncestry().getFather()); }
	public Individual getMother() { return getIndividual(getAncestry().getMother()); }

	public synchronized Iterable<Individual> getChildren() {
		if (children != null) return children;
		children = new ArrayList<Individual>();
		for (GedcomRecord child : getAncestry().getChildren()) {
			children.add(getIndividual(child));
		}
		return children;
	}

	public static synchronized Individual getIndividual(GedcomRecord record) {
		if (record == null) return null;
		if (!record.getTag().equals("INDI")) return null;
		Individual individual = record.getDecorator(Individual.class);
		if (individual != null) return individual;
		individual = new Individual();
		record.addDecorator(individual);
		return individual;
	}
}
