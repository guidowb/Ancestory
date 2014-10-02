package org.guidowb.gedcom.decorators;

import java.util.ArrayList;
import java.util.List;

import org.guidowb.gedcom.GedcomDecorator;
import org.guidowb.gedcom.GedcomRecord;

public class Individual extends GedcomDecorator {
	private Name name = null;
	private Ancestry ancestry = null;
	private List<Individual> children = null;
	private AncestorCount ancestorCount = null;

	public synchronized Name getName() {
		if (name != null) return name;
		name = Name.getName(record);
		return name;
	}

	private synchronized Ancestry getAncestry() {
		if (ancestry != null) return ancestry;
		ancestry = Ancestry.getAncestry(record);
		return ancestry;
	}

	public synchronized AncestorCount getAncestorCount() {
		if (ancestorCount != null) return ancestorCount;
		ancestorCount = AncestorCount.getAncestorCount(record);
		return ancestorCount;
	}

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
