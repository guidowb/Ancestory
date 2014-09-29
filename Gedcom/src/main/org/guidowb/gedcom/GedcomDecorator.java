package org.guidowb.gedcom;

public abstract class GedcomDecorator {

	protected GedcomRecord record;
	
	void setRecord(GedcomRecord record) { this.record = record; }
}
