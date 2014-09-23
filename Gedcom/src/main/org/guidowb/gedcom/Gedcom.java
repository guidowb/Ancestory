package org.guidowb.gedcom;

import java.util.ArrayList;
import java.util.List;

public class Gedcom {

	private List<GedcomRecord> recordsInFileOrder = new ArrayList<GedcomRecord>();
	private List<GedcomIndex> indices = new ArrayList<GedcomIndex>();

	public synchronized void addRecord(GedcomRecord record) {
		recordsInFileOrder.add(record);
		for (GedcomIndex index : indices) index.addRecord(record);
	}

	public synchronized void addIndex(GedcomIndex index) {
		indices.add(index);
		for (GedcomRecord record : recordsInFileOrder) index.addRecord(record);
	}
	
	@SuppressWarnings("unchecked")
	public synchronized <I extends GedcomIndex> I getIndex(Class<I> indexClass) throws InstantiationException, IllegalAccessException {
		for (GedcomIndex index : indices) {
			if (indexClass.isInstance(index)) return (I) index;
		}
		I index = indexClass.newInstance();
		addIndex(index);
		return index;
	}
	
	public Iterable<GedcomRecord> records() {
		return recordsInFileOrder;
	}
}
