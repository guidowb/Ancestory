package org.guidowb.gedcom.decorators;

import org.guidowb.gedcom.GedcomDecorator;
import org.guidowb.gedcom.GedcomRecord;

public class Lifespan extends GedcomDecorator {

	public String birthYear = null;
	public String deathYear = null;

	public static synchronized Lifespan getLifespan(GedcomRecord record) {
		Lifespan lifespan = record.getDecorator(Lifespan.class);
		if (lifespan != null) return lifespan;
		lifespan = determineLifespan(record);
		if (lifespan != null) record.addDecorator(lifespan);
		return lifespan;
	}

	private static Lifespan determineLifespan(GedcomRecord record) {
		if (!record.getTag().equals("INDI")) return null;
		Lifespan lifespan = new Lifespan();
		lifespan.birthYear = getEventYear(record, "BIRT BAPM");
		// TODO - if birth is not known, guess based on other events
		lifespan.deathYear = getEventYear(record, "DEAT BURI");
		// TODO - if death is now known, guess based on other events
		return lifespan;
	}

	private static String getEventYear(GedcomRecord record, String events) {
		if (record == null) return null;
		String[] tags = events.split(" ");
		for (String tag : tags) {
			GedcomRecord eventField = record.getField(tag);
			if (eventField == null) continue;
			GedcomRecord dateField = eventField.getField("DATE");
			if (dateField == null) continue;
			String year = dateField.getValue().replaceAll("^.* ", "");
			return year;
		}
		return null;
	}

	@Override
	public String toString() {
		String out = "";
		if (birthYear != null) out += birthYear;
		if (birthYear != null || deathYear != null) out += " - ";
		if (deathYear != null) out += deathYear;
		return out;
	}
}