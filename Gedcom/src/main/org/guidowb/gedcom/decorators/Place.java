package org.guidowb.gedcom.decorators;

import java.util.ArrayList;
import java.util.List;

import org.guidowb.gedcom.GedcomDecorator;
import org.guidowb.gedcom.GedcomRecord;

public class Place extends GedcomDecorator {

	private String name;
	private List<GedcomRecord> references = new ArrayList<GedcomRecord>();

	public Place(String name) {
		this.name = name;
	}

	public void addReference(GedcomRecord record) {
		references.add(record);
	}
	
	public String getName() { return name; }
}
