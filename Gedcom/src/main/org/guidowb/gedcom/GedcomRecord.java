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
	
	public void mergeContinuation(GedcomRecord record) {
		if (record.tag.equals("CONC")) this.value += record.value;
		else if (record.tag.equals("CONT")) this.value += "\n" + record.value;
	}
	
	public String toString() { return toString(false); }
	public String toString(boolean pretty) {
		StringBuffer out = new StringBuffer();
		if (pretty) out.append(String.format("%6d ", lineno));
		if (pretty) for (int i = 0; i < level; i++) out.append("  ");
		out.append(String.format("%d", level));
		if (id != null) out.append(String.format(" @%s@", id));
		out.append(" " + tag);
		if (xref != null) out.append(String.format(" @%s@", xref));
		if (value != null) out.append(" " + value);
		out.append("\n");
		return out.toString();
	}
}
