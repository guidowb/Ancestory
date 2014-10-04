package org.guidowb.gedcom.decorators;

import java.util.ArrayList;
import java.util.List;

import org.guidowb.gedcom.GedcomDecorator;
import org.guidowb.gedcom.GedcomRecord;

public class Place extends GedcomDecorator {

	private String name;
	private List<GedcomRecord> events = new ArrayList<GedcomRecord>();

	public Place(String name) {
		this.name = name;
	}

	public void addEvent(GedcomRecord record) {
		events.add(record);
	}
	
	public String getName() { return name; }
	
	public Iterable<GedcomRecord> events() { return events; }
}
