package org.guidowb.gedcom.indices;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.guidowb.gedcom.GedcomIndex;
import org.guidowb.gedcom.GedcomRecord;

public class SourceIndex extends GedcomIndex {

	private File source;
	private List<GedcomRecord> recordsInFileOrder = new ArrayList<GedcomRecord>();

	@Override
	public void addRecord(GedcomRecord record) {
		recordsInFileOrder.add(record);
	}

	public Iterable<GedcomRecord> records() {
		return recordsInFileOrder;
	}
	
	public void setSource(File source) { this.source = source; }
	public File getSource() { return source; }
}
