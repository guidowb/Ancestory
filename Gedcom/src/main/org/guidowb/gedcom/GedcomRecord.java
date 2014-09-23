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
		if (value != null) out.append(" " + splitValue(value, pretty));
		return out.toString();
	}
	
	private String splitValue(String value, boolean pretty) {
		value = value.replaceAll("\n", String.format("\n%d CONT ", level+1));
		if (pretty) return value;
		final int maxlen = 200;
		String[] lines = value.split("\n");
		value = "";
		for (String line : lines) {
			while (line.length() > maxlen) {
				String line1 = line.substring(0, maxlen);
				String line2 = line.substring(maxlen);
				while (line2.startsWith(" ") || line1.endsWith(" ")) {
					line2 = line1.substring(line1.length() - 1) + line2;
					line1 = line1.substring(0, line1.length() - 1);
				}
				value += line1 + String.format("\n%d CONC ", level + 1);
				line = line2;
			}
			value += line;
		}
		return value;
	}
}
