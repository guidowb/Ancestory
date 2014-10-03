package org.guidowb.gedcom.indices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.guidowb.gedcom.GedcomIndex;
import org.guidowb.gedcom.GedcomRecord;

public class TagIndex extends GedcomIndex {

	private Map<String, List<GedcomRecord>> recordsByTag = new HashMap<String, List<GedcomRecord>>();
	private List<GedcomRecord> emptyResult = new ArrayList<GedcomRecord>();

	@Override
	public void addRecord(GedcomRecord record) {
		List<GedcomRecord> records = recordsByTag.get(record.getTag());
		if (records == null) {
			records = new ArrayList<GedcomRecord>();
			recordsByTag.put(record.getTag(), records);
		}
		records.add(record);
	}
	
	public Iterable<GedcomRecord> getRecords(String tag) {
		Iterable<GedcomRecord> records = recordsByTag.get(tag);
		if (records != null) return records;
		else return emptyResult;
	}
}
