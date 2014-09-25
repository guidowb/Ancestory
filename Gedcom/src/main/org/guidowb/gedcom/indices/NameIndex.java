package org.guidowb.gedcom.indices;

import java.util.SortedMap;
import java.util.TreeMap;

import org.guidowb.gedcom.GedcomDecoration;
import org.guidowb.gedcom.GedcomIndex;
import org.guidowb.gedcom.GedcomRecord;

public class NameIndex implements GedcomIndex {

	private SortedMap<String, GedcomRecord> individualsByName = new TreeMap<String, GedcomRecord>();

	@SuppressWarnings("serial")
	public static class NameFormatException extends RuntimeException {
		NameFormatException(String name) { super("Illegal name format: " + name); }
		NameFormatException(String name, String reason) { super("Illegal name format (" + reason + "): " + name); }
	}

	public static class Name extends GedcomDecoration {
		String nickname = null;
		String givenname = null;
		String prefix = null;
		String surname = null;
		String surname_prefix = null;
		String surname_root = null;
		String surname_alternates = null;
		String postfix = null;

		public static Name parse(String original) {
			Name name = new Name();
			String[] parts = original.split("/");
			if (parts.length > 1) name.givenname = parts[0].trim();
			if (parts.length > 1) name.surname = parts[1].trim(); else name.surname = parts[0].trim();
			if (parts.length > 2) name.postfix = parts[2].trim();
			if (parts.length > 3) throw new NameFormatException(original, "too many slashes");
			extractNickname(name);
			extractPrefix(name);
			extractAlternates(name);
			splitSurname(name);
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
			String original = name.surname;
			if (original == null) return;
			name.surname = original.replaceAll("\\(.*\\)", "");
			if (original.contains("("))
				name.surname_alternates = original.replaceAll("^.*\\(", "").replaceAll("\\).*$", "");
		}
		
		private static void splitSurname(Name name) {
			String original = name.surname;
			String parts[] = original.split(" ");
			for (String part : parts) {
				if (part.isEmpty()) continue;
				if (isLowerCase(part)) name.surname_prefix = (name.surname_prefix == null) ? part : (name.surname_prefix + " " + part);
				else name.surname_root = (name.surname_root == null) ? part : (name.surname_root + " " + part);
			}
		}

		private static boolean isLowerCase(String part) {
			return Character.isLowerCase(part.replaceAll("^[\\(']", "").charAt(0));
		}
		
		public String toString() {
			String out = surname_root;
			if (postfix != null) out += " " + postfix;
			if (surname_prefix != null) out += ", " + surname_prefix;
			if (surname_alternates != null) out += " (" + surname_alternates + ")";
			if (givenname != null) out += ", " + givenname;
			if (nickname != null) out += " (" + nickname + ")";
			if (prefix != null) out += ", " + prefix;
			return out;
		}
	}

	@Override
	public void addRecord(GedcomRecord record) {
		if (!record.getTag().equals("INDI")) return;
		Name name = Name.parse(record.getField("NAME").getValue());
		record.addDecoration(name);
		individualsByName.put(name.toString(), record);
	}
	
	public Iterable<GedcomRecord> individuals() {
		return individualsByName.values();
	}
}
