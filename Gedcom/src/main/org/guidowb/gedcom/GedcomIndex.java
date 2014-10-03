package org.guidowb.gedcom;

public abstract class GedcomIndex {

	Gedcom gedcom;
	
	protected <I extends GedcomIndex> I getIndex(Class<I> indexClass) { return gedcom.getIndex(indexClass); }

	public abstract void addRecord(GedcomRecord record);
}
