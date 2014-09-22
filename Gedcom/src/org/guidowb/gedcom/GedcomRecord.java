package org.guidowb.gedcom;

public class GedcomRecord {

	private Gedcom gedcom;
	private int lineno;
	private int level;
	private String id;
	private String tag;
	private String xref;
	private String value;
	
	GedcomRecord(Gedcom gedcom, int lineno, int level, String id, String tag, String xref, String value) {
		this.gedcom = gedcom;
		this.lineno = lineno;
		this.level = level;
		this.id = id;
		this.tag = tag;
		this.xref = xref;
		this.value = value;
	}
	
	public boolean isContinuation() {
		return (tag.equals("CONC") || tag.equals("CONT"));
	}
	
	public void merge(GedcomRecord record) {
		if (record.tag.equals("CONC")) this.value += record.value;
		else if (record.tag.equals("CONT")) this.value += "\n" + record.value;
	}
}
