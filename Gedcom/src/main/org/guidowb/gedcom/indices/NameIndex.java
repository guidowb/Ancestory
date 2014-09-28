package org.guidowb.gedcom.indices;

import java.util.SortedSet;
import java.util.TreeSet;

import org.guidowb.gedcom.GedcomIndex;
import org.guidowb.gedcom.GedcomRecord;
import org.guidowb.gedcom.decorators.Name;

public class NameIndex implements GedcomIndex {

	private SortedSet<Name> names = new TreeSet<Name>();
	private SortedSet<Name> individuals = new TreeSet<Name>();
	public static final SortedSet<Name> emptySet = new TreeSet<Name>();

	@Override
	public void addRecord(GedcomRecord record) {
		if (!record.getTag().equals("INDI")) return;
		Name name = Name.parseName(record);
		record.addDecorator(name);
		individuals.add(name);
		names.add(name);
		for (Name alias : name.aliases()) names.add(alias);
	}
	
	public Iterable<Name> names() { return names; }
	public Iterable<Name> individuals() { return individuals; }
}
