package org.guidowb.gedcom.indices;

import java.util.HashMap;
import java.util.Map;

import org.guidowb.gedcom.GedcomIndex;
import org.guidowb.gedcom.GedcomRecord;

public class RecordIndex extends GedcomIndex {

	private Map<String, GedcomRecord> recordsById = new HashMap<String, GedcomRecord>();

	@Override
	public void addRecord(GedcomRecord record) {
		if (record.getId() != null) recordsById.put(record.getId(), record);
	}

	public GedcomRecord getRecord(String id) {
		return recordsById.get(id);
	}
	
	public GedcomRecord resolve(GedcomRecord record) {
		if (record == null) return null;
		String xref = record.getXref();
		while (xref != null) {
			record = getRecord(xref);
			xref = record.getXref();
		}
		return record;
	}
}
