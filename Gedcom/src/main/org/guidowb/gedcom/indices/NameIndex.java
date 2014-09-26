package org.guidowb.gedcom.indices;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.guidowb.gedcom.GedcomDecoration;
import org.guidowb.gedcom.GedcomIndex;
import org.guidowb.gedcom.GedcomRecord;

public class NameIndex implements GedcomIndex {

	private SortedMap<Name, GedcomRecord> individualsByName = new TreeMap<Name, GedcomRecord>();

	@SuppressWarnings("serial")
	public static class NameFormatException extends RuntimeException {
		NameFormatException(String name) { super("Illegal name format: " + name); }
		NameFormatException(String name, String reason) { super("Illegal name format (" + reason + "): " + name); }
	}

	public static class Name extends GedcomDecoration implements Comparable<Name> {
		public static class Surname {
			String original;
			String prefix;
			String root;
		}
		String nickname = null;
		String givenname = null;
		String prefix = null;
		Surname surname = new Surname();
		List<Surname> alternates = null;
		String postfix = null;
		GedcomRecord record;

		private Name(GedcomRecord record) { this.record = record; }
		
		public static Name parseName(GedcomRecord record) {
			Name name = new Name(record);
			String original = record.getField("NAME").getValue();
			String[] parts = original.split("/");
			if (parts.length > 0) name.givenname = parts[0].trim();
			if (parts.length > 1) name.surname.original = parts[1].trim();
			if (parts.length > 2) name.postfix = parts[2].trim();
			if (parts.length > 3) throw new NameFormatException(original, "too many slashes");
			if (name.givenname != null && name.givenname.equals("NN")) name.givenname = null;
			if (name.surname.original != null && name.surname.original.equals("NN")) name.surname.original = null;
			extractNickname(name);
			extractPrefix(name);
			extractAlternates(name);
			splitSurname(name.surname);
			return name;
		}
		
		private static void extractNickname(Name name) {
			String original = name.givenname;
			if (original == null) return;
			name.givenname = original.replaceAll("\\(.*\\)", "");
			if (original.contains("("))
				name.nickname = original.replaceAll("^.*\\(", "").replaceAll("\\).*$", "");
		}
		
		private static void extractPrefix(Name name) {
			String original = name.givenname;
			if (original == null) return;
			name.givenname = null;
			String parts[] = original.split(" ");
			for (String part : parts) {
				if (part.isEmpty()) continue;
				if (part.endsWith(".") || isLowerCase(part)) name.prefix = (name.prefix == null) ? part : (name.prefix + " " + part);
				else name.givenname = (name.givenname == null) ? part : (name.givenname + " " + part);
			}
		}
		
		private static void extractAlternates(Name name) {
			String original = name.surname.original;
			if (original == null) return;
			name.surname.original = original.replaceAll("\\(.*\\)", "");
			if (original.contains("(")) {
				name.alternates = new ArrayList<Surname>();
				String[] alternates = original.replaceAll("^.*\\(", "").replaceAll("\\).*$", "").split(", ");
				for (String alternate : alternates) {
					Surname surname = new Surname();
					surname.original = alternate;
					splitSurname(surname);
					name.alternates.add(surname);
				}
			}
		}
		
		private static void splitSurname(Surname surname) {
			if (surname.original == null) return;
			String parts[] = surname.original.split(" ");
			boolean inPrefix = parts.length > 1;
			for (String part : parts) {
				if (part.isEmpty()) continue;
				if (!isLowerCase(part)) inPrefix = false;
				if (inPrefix) surname.prefix = (surname.prefix == null) ? part : (surname.prefix + " " + part);
				else surname.root = (surname.root == null) ? part : (surname.root + " " + part);
			}
		}

		private static boolean isLowerCase(String part) {
			return Character.isLowerCase(part.replaceAll("^[\\(']", "").charAt(0));
		}
		
		public String toString() {
			String out = "";
			if (surname.root != null) out = surname.root; else out = "NN";
			if (surname.prefix != null) out += ", " + surname.prefix;
			if (alternates != null) for (Surname alternate : alternates) {
				out += " (" + alternate.root;
				if (alternate.prefix != null) out += ", " + alternate.prefix;
				out += ")";
			}
			if (givenname != null) out += ", " + givenname; else out += ", NN";
			if (postfix != null) out += " " + postfix;
			if (nickname != null) out += " (" + nickname + ")";
			if (prefix != null) out += ", " + prefix;
			return out;
		}

		private int compareStrings(String s1, String s2) {
			if (s1 == null)
				if (s2 == null) return 0;
				else return 1;
			else if (s2 == null) return -1;
			return s1.compareTo(s2);
		}
		
		@Override
		public int compareTo(Name other) {
			int result = compareStrings(this.surname.root, other.surname.root);
			if (result == 0) result = compareStrings(this.surname.prefix, other.surname.prefix);
			if (result == 0) result = compareStrings(this.givenname, other.givenname);
			if (result == 0) result = compareStrings(this.postfix, other.postfix);
			if (result == 0) result = this.record.compareTo(other.record);
			return result;
		}
	}

	@Override
	public void addRecord(GedcomRecord record) {
		if (!record.getTag().equals("INDI")) return;
		Name name = Name.parseName(record);
		record.addDecoration(name);
		individualsByName.put(name, record);
	}
	
	public Iterable<GedcomRecord> individuals() {
		return individualsByName.values();
	}
}
