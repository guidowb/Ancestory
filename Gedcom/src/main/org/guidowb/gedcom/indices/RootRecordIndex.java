package org.guidowb.gedcom.indices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.guidowb.gedcom.GedcomIndex;
import org.guidowb.gedcom.GedcomRecord;

public class RootRecordIndex implements GedcomIndex {

	private Map<String, List<GedcomRecord>> rootRecordsByTag = new HashMap<String, List<GedcomRecord>>();
	private List<GedcomRecord> emptyResult = new ArrayList<GedcomRecord>();

	@Override
	public void addRecord(GedcomRecord record) {
		if (record.getLevel() > 0) return;
		List<GedcomRecord> records = rootRecordsByTag.get(record.getTag());
		if (records == null) {
			records = new ArrayList<GedcomRecord>();
			rootRecordsByTag.put(record.getTag(), records);
		}
		records.add(record);
	}
	
	public Iterable<GedcomRecord> getRecords(String tag) {
		Iterable<GedcomRecord> records = rootRecordsByTag.get(tag);
		if (records != null) return records;
		else return emptyResult;
	}
}
