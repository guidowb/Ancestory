package org.guidowb.gedcom.indices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.guidowb.gedcom.GedcomIndex;
import org.guidowb.gedcom.GedcomRecord;
import org.guidowb.gedcom.decorators.Individual;
import org.guidowb.gedcom.decorators.Name;

public class NameIndex extends GedcomIndex {

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

	public static class Match {
		public Name name;
		public Individual individual;
		Match(Name name, Individual individual) {
			this.name = name;
			this.individual = individual;
		}
	}

	public List<Match> findIndividuals(String pattern) {
		String regex = pattern.replaceAll(" ", ".* .*");
		List<Match> results = new ArrayList<Match>();
		for (Entry<Name, Individual> entry : names.entrySet()) {
			Name name = entry.getKey();
			if (name.getNormalized().matches(regex)) {
				Match match = new Match(name, entry.getValue());
				results.add(match);
			}
		}
		return results;
	}
}
