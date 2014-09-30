package org.guidowb.gedcom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.guidowb.gedcom.indices.HierarchyIndex;

public class Gedcom {

	private List<GedcomRecord> recordsInFileOrder = new ArrayList<GedcomRecord>();
	private List<GedcomIndex> indices = new ArrayList<GedcomIndex>();
	private Map<String, GedcomRecord> recordsById = new HashMap<String, GedcomRecord>();

	Gedcom() {
		// Add the non-optional indices so that they are invoked as the
		// records are added.
		addIndex(HierarchyIndex.class);
	}

	public synchronized void addRecord(GedcomRecord record) {
		recordsInFileOrder.add(record);
		if (record.getId() != null) recordsById.put(record.getId(), record);
		for (GedcomIndex index : indices) index.addRecord(record);
	}

	public synchronized void addIndex(GedcomIndex index) {
		indices.add(index);
		for (GedcomRecord record : recordsInFileOrder) index.addRecord(record);
	}
	
	public <I extends GedcomIndex> I addIndex(Class<I> indexClass) {
		try {
			I index;
			index = indexClass.newInstance();
			addIndex(index);
			return index;
		} catch (Exception e) {
			// If we get an exception here, it really means that someone has written code
			// to request an index that isn't in the class path. I don't want to expect
			// all callers to have to deal with that eventuality or declare these types
			// of exceptions. So I'm translating it to a RuntimeException here. This is
			// a case where the best thing to do might be to just crash, rather than
			// pollute all of the rest of the code.
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public synchronized <I extends GedcomIndex> I getIndex(Class<I> indexClass) {
		for (GedcomIndex index : indices) {
			if (indexClass.isInstance(index)) return (I) index;
		}
		return addIndex(indexClass);
	}
	
	public Iterable<GedcomRecord> records() {
		return recordsInFileOrder;
	}
	
	public GedcomRecord getRecord(String id) {
		return recordsById.get(id);
	}
}
