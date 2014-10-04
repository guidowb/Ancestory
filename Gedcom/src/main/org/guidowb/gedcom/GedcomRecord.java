package org.guidowb.gedcom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.guidowb.gedcom.indices.RecordIndex;

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

	public int getLevel() { return level; }
	public String getId() { return id; }
	public String getTag() { return tag; }
	public String getXref() { return xref; }
	public String getValue() { return value; }

	private GedcomRecord container = null;
	private List<GedcomRecord> fieldsInFileOrder = new ArrayList<GedcomRecord>();
	private Map<String, List<GedcomRecord>> fieldsByTag = new HashMap<String, List<GedcomRecord>>();

	public GedcomRecord getContainer() { return container; }

	public void addField(GedcomRecord field) {
		field.container = this;
		fieldsInFileOrder.add(field);
		List<GedcomRecord> fields = fieldsByTag.get(field.getTag());
		if (fields == null) {
			fields = new ArrayList<GedcomRecord>();
			fieldsByTag.put(field.getTag(), fields);
		}
		fields.add(field);
	}
	
	public GedcomRecord getField(String tag) {
		List<GedcomRecord> fields = fieldsByTag.get(tag);
		if (fields == null) return null;
		return fields.get(0);	
	}

	public Iterable<GedcomRecord> getFields(String tag) {
		Iterable<GedcomRecord> fields = fieldsByTag.get(tag);
		if (fields == null) return new ArrayList<GedcomRecord>();
		return fields;
	}

	public GedcomRecord resolveField(String tag) {
		return gedcom.getIndex(RecordIndex.class).resolve(getField(tag));
	}

	public List<GedcomRecord> resolveFields(String tag) {
		List<GedcomRecord> fields = new ArrayList<GedcomRecord>();
		for (GedcomRecord field : getFields(tag)) {
			fields.add(gedcom.getIndex(RecordIndex.class).resolve(field));
		}
		return fields;
	}

	@SuppressWarnings("rawtypes")
	private Map<Class, GedcomDecorator> decorators = new HashMap<Class, GedcomDecorator>();

	@SuppressWarnings("unchecked")
	public <D extends GedcomDecorator> D getDecorator(Class<D> decoratorClass) {
		return (D) decorators.get(decoratorClass);
	}
	
	public <D extends GedcomDecorator> void addDecorator(D decorator) {
		decorator.setRecord(this);
		decorators.put(decorator.getClass(), decorator);
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

	public int compareTo(GedcomRecord other) {
		if (this.lineno < other.lineno) return -1;
		if (this.lineno > other.lineno) return 1;
		return 0;
	}
}
