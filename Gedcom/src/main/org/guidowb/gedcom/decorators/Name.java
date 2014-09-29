package org.guidowb.gedcom.decorators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.guidowb.gedcom.GedcomDecorator;
import org.guidowb.gedcom.GedcomRecord;

public class Name extends GedcomDecorator implements Comparable<Name> {
	private String original = null;
	private String prefix = null;
	private String givenname = null;
	private String nickname = null;
	private String surname_prefix = null;
	private String surname = null;
	private List<Name> aliases = null;
	private Name isAliasFor = null;
	private String postfix = null;

	public String getOriginal() { return original; }
	public String getNormalized() { return toString(); }

	public boolean isAlias() { return isAliasFor != null; }
	public Iterable<Name> aliases() { return (aliases != null) ? aliases : emptyList; }
	
	@SuppressWarnings("serial")
	public static class NameFormatException extends RuntimeException {
		NameFormatException(String name) { super("Illegal name format: " + name); }
		NameFormatException(String name, String reason) { super("Illegal name format (" + reason + "): " + name); }
	}

	public static Name parseName(GedcomRecord record) {
		Iterator<GedcomRecord> nameFields = record.getFields("NAME").iterator();
		if (!nameFields.hasNext()) return null;
		Name name = parseName(nameFields.next().getValue());
		while (nameFields.hasNext()) {
			if (name.aliases == null) name.aliases = new ArrayList<Name>();
			name.aliases.add(parseName(nameFields.next().getValue()));
		}
		return name;
	}

	private static Name parseName(String original) {
		Name name = new Name();
		name.original = original;
		String[] parts = original.split("/");
		if (parts.length > 0) name.givenname = parts[0].trim();
		if (parts.length > 1) name.surname = parts[1].trim();
		if (parts.length > 2) name.postfix = parts[2].trim();
		if (parts.length > 3) throw new NameFormatException(original, "too many slashes");
		if (name.givenname != null && name.givenname.equals("NN")) name.givenname = null;
		if (name.surname != null && name.surname.equals("NN")) name.surname = null;
		extractNickname(name);
		extractAliases(name);
		extractSurnamePrefix(name);
		return name;
	}

	private static void extractNickname(Name name) {
		String original = name.givenname;
		if (original == null) return;
		name.givenname = original.replaceAll(" \\(.*\\)", "");
		if (original.contains("("))
			name.nickname = original.replaceAll("^.* \\(", "").replaceAll("\\).*$", "");
	}
	
	private static void extractAliases(Name name) {
		String original = name.surname;
		if (original == null) return;
		name.surname = original.replaceAll(" \\(.*\\)", "");
		if (original.contains("(")) {
			name.aliases = new ArrayList<Name>();
			String[] aliases = original.replaceAll("^.* \\(", "").replaceAll("\\).*$", "").split(", ");
			for (String alias : aliases) addAlias(name, alias);
		}
	}

	private static void addAlias(Name name, String alternateSurname) {
		Name alias = new Name();
		alias.prefix = name.prefix;
		alias.givenname = name.givenname;
		alias.nickname = name.nickname;
		alias.surname_prefix = null;
		alias.surname = alternateSurname;
		alias.isAliasFor = name;
		alias.postfix = null;
		alias.record = name.record;
		extractSurnamePrefix(alias);
		name.aliases.add(alias);
	}

	private static void extractSurnamePrefix(Name name) {
		if (name.surname_prefix != null) return;
		if (name.surname == null) return;
		String parts[] = name.surname.split(" ");
		name.surname = null;
		boolean inPrefix = parts.length > 1;
		for (String part : parts) {
			if (part.isEmpty()) continue;
			if (!isLowerCase(part)) inPrefix = false;
			if (inPrefix) name.surname_prefix = (name.surname_prefix == null) ? part : (name.surname_prefix + " " + part);
			else name.surname = (name.surname == null) ? part : (name.surname + " " + part);
		}
	}

	private static boolean isLowerCase(String part) {
		return Character.isLowerCase(part.replaceAll("^[\\(']", "").charAt(0));
	}
	
	public String toString() {
		String out = "";
		if (surname != null) out += surname; else out += "NN";
		if (surname_prefix != null) out += ", " + surname_prefix;
		if (givenname != null) out += ", " + givenname; else out += ", NN";
		if (postfix != null) out += " " + postfix;
		if (nickname != null) out += " (" + nickname + ")";
		if (prefix != null) out += ", " + prefix;
		return out;
	}

	private static final List<Name> emptyList = new ArrayList<Name>();

	private int compareStrings(String s1, String s2) {
		if (s1 == null)
			if (s2 == null) return 0;
			else return 1;
		else if (s2 == null) return -1;
		return s1.compareTo(s2);
	}
	
	@Override
	public int compareTo(Name other) {
		int result = compareStrings(this.surname, other.surname);
		if (result == 0) result = compareStrings(this.surname_prefix, other.surname_prefix);
		if (result == 0) result = compareStrings(this.givenname, other.givenname);
		if (result == 0) result = compareStrings(this.postfix, other.postfix);
		if (result == 0) result = this.record.compareTo(other.record);
		return result;
	}
}