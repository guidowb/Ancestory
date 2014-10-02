package org.guidowb.gedcom.decorators;

import java.util.ArrayList;

import org.guidowb.gedcom.GedcomDecorator;
import org.guidowb.gedcom.GedcomRecord;

public class AncestorCount extends GedcomDecorator {

	private ArrayList<Integer> knownAncestors = new ArrayList<Integer>();

	public int getGenerations() { return knownAncestors.size(); }

	public int getCount(int generation) {
		if (generation < knownAncestors.size()) return knownAncestors.get(generation);
		else return 0;
	}

	public double getPercentage(int generation) {
		return getCount(generation) * 100 / Math.pow(2, generation + 1);
	}

	public class GenerationCount {
		int count;
		double percentage;
		GenerationCount(int count, double percentage) { this.count = count; this.percentage = percentage; }
	}
	
	public ArrayList<GenerationCount> getCounts() {
		ArrayList<GenerationCount> counts = new ArrayList<GenerationCount>();
		for (int i = 0; i < knownAncestors.size(); i++) {
			counts.add(new GenerationCount(getCount(i), getPercentage(i)));
		}
		return counts;
	}

	public static AncestorCount getAncestorCount(GedcomRecord record) {
		if (record == null) return null;
		if (!record.getTag().equals("INDI")) return null;
		AncestorCount count = record.getDecorator(AncestorCount.class);
		if (count != null) return count;
		count = countAncestors(record);
		record.addDecorator(count);
		return count;
	}
	
	private static AncestorCount countAncestors(GedcomRecord record) {
		Ancestry ancestry = Ancestry.getAncestry(record);
		AncestorCount count = new AncestorCount();
		AncestorCount fathersCount = getAncestorCount(ancestry.getFather());
		AncestorCount mothersCount = getAncestorCount(ancestry.getMother());
		
		count.mergeAncestorCounts(fathersCount);
		count.mergeAncestorCounts(mothersCount);
		
		return count;
	}
	
	private void mergeAncestorCounts(AncestorCount other) {
		if (other == null) return;
		increaseAncestorCount();
		for (int i = 0; i < other.knownAncestors.size(); i++) {
			increaseAncestorCount(i+1, other.knownAncestors.get(i));
		}
	}
	
	private void increaseAncestorCount() { increaseAncestorCount(0); }
	private void increaseAncestorCount(int generation) { increaseAncestorCount(generation, 1); }
	private void increaseAncestorCount(int generation, int increase) {
		if (generation > knownAncestors.size()) throw new IndexOutOfBoundsException();
		if (generation == knownAncestors.size()) knownAncestors.add(0);
		knownAncestors.set(generation, knownAncestors.get(generation) + increase);
	}
}
