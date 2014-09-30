package org.guidowb.gedcom.indices;

import java.util.SortedMap;
import java.util.TreeMap;
import org.guidowb.gedcom.GedcomIndex;
import org.guidowb.gedcom.GedcomRecord;
import org.guidowb.gedcom.decorators.Individual;
import org.guidowb.gedcom.decorators.Name;

public class NameIndex implements GedcomIndex {

	private SortedMap<Name, Individual> names = new TreeMap<Name, Individual>();
	private SortedMap<Name, Individual> individuals = new TreeMap<Name, Individual>();

	@Override
	public void addRecord(GedcomRecord record) {
		if (!record.getTag().equals("INDI")) return;
		Individual individual = Individual.getIndividual(record);
		Name name = individual.getName();
		if (name == null) return;
		individuals.put(name, individual);
		names.put(name, individual);
		for (Name alias : name.aliases()) names.put(alias, individual);
	}
	
	public Iterable<Name> names() { return names.keySet(); }
	public Iterable<Individual> individuals() { return individuals.values(); }
	public Individual getIndividual(String name) { return names.get(name); }
}
