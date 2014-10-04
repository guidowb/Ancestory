package org.guidowb.gedcom.decorators;

import org.guidowb.gedcom.GedcomDecorator;
import org.guidowb.gedcom.GedcomRecord;

public class Description extends GedcomDecorator {

	private String description = null;
	
	private Description(String description) {
		this.description = description;
	}

	public static synchronized Description getDescription(GedcomRecord record) {
		Description description = record.getDecorator(Description.class);
		if (description != null) return description;
		description = generateDescription(record);
		if (description != null) record.addDecorator(description);
		return description;
	}

	private static Description generateDescription(GedcomRecord record) {
		String tag = record.getTag();
		String description = "";
		if (tag.equals("INDI")) {
			description = Name.getName(record).getNormalized();
			String lifespan = Lifespan.getLifespan(record).toString();
			if (!lifespan.isEmpty()) description += " (" + lifespan + ")";
		}
		else if (tag.equals("FAM")) {
			GedcomRecord husband = record.resolveField("HUSB");
			GedcomRecord wife = record.resolveField("WIFE");
			if (husband != null) description += getDescription(husband).toString();
			else description += "UNKNOWN";
			description += " AND ";
			if (wife != null) description += getDescription(wife).toString();
			else description += "UNKNOWN";
		}
		else {
			description = tag + " OF " + getDescription(record.getContainer()).toString();
		}
		return new Description(description);
	}
	
	@Override
	public String toString() { return description; }
}