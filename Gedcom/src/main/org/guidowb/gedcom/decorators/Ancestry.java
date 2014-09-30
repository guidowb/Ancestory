package org.guidowb.gedcom.decorators;

import java.util.ArrayList;
import java.util.List;

import org.guidowb.gedcom.GedcomDecorator;
import org.guidowb.gedcom.GedcomRecord;

public class Ancestry extends GedcomDecorator {
	private GedcomRecord father = null;
	private GedcomRecord mother = null;
	private List<GedcomRecord> children = new ArrayList<GedcomRecord>();
	
	public static Ancestry getAncestry(GedcomRecord record) {
		if (!record.getTag().equals("INDI")) return null;
		Ancestry ancestry = record.getDecorator(Ancestry.class);
		if (ancestry != null) return ancestry;
		ancestry = determineAncestry(record);
		record.addDecorator(ancestry);
		return ancestry;
	}
	
	private static Ancestry determineAncestry(GedcomRecord record) {
		Ancestry ancestry = new Ancestry();
		GedcomRecord familyInWhichChild = record.resolveField("FAMC");
		if (familyInWhichChild != null) {
			ancestry.father = familyInWhichChild.resolveField("HUSB");
			ancestry.mother = familyInWhichChild.resolveField("WIFE");
		}
		GedcomRecord familyInWhichSpouse = record.resolveField("FAMS");
		if (familyInWhichSpouse != null) {
			ancestry.children = familyInWhichSpouse.resolveFields("CHIL");
		}
		return ancestry;
	}
	
	public GedcomRecord getFather() { return father; }
	public GedcomRecord getMother() { return mother; }
	public Iterable<GedcomRecord> getChildren() { return children; }
}
