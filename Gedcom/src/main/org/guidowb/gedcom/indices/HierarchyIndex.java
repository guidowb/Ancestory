package org.guidowb.gedcom.indices;

import java.util.ArrayList;
import java.util.List;

import org.guidowb.gedcom.GedcomIndex;
import org.guidowb.gedcom.GedcomRecord;

public class HierarchyIndex extends GedcomIndex {

	List<GedcomRecord> stack = new ArrayList<GedcomRecord>();

	@Override
	public void addRecord(GedcomRecord record) {
		while (stack.size() > record.getLevel()) stack.remove(0);
		if (record.getLevel() > 0) stack.get(0).addField(record);
		stack.add(0, record);
	}
}
