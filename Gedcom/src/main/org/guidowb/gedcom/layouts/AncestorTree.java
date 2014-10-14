package org.guidowb.gedcom.layouts;

import java.util.ArrayList;

import org.guidowb.gedcom.decorators.Individual;

public class AncestorTree {

	public class Branch {
		private int generation;
		private int position;
		private Individual individual;
		private String prefix;
		
		public Individual individual() { return individual; }
		public int position() { return position; }
		public int generation() { return generation; }
		public String prefix() {
			String out = "";
			for (char c : prefix.toCharArray()) {
				switch (c) {
				case ' ': out += prefixes[0]; break;
				case 'F': out += prefixes[1]; break;
				case 'f': out += prefixes[2]; break;
				case 'm': out += prefixes[3]; break;
				case 'M': out += prefixes[4]; break;
				}
			}
			return out;
		}
	};
	
	private ArrayList<Branch> branches = new ArrayList<Branch>();
	private String[] prefixes = null;

	public static AncestorTree create(Individual root) { return create(root, null); }
	public static AncestorTree create(Individual root, String[] prefixes) {
		AncestorTree tree = new AncestorTree();
		if (prefixes != null) {
			if (prefixes.length != 5) throw new RuntimeException("Must provide 5 prefix strings");
			tree.prefixes = prefixes;
		}
		tree.addAncestors(root);
		return tree;
	}

	private void addAncestors(Individual individual) { addAncestors(individual, "", "", ""); }
	private void addAncestors(Individual individual, String at, String before, String after) {
		Individual father = individual.getFather();
		Individual mother = individual.getMother();
		if (father != null) addAncestors(father, before + "F", before + " ", before + "f");
		addBranch(individual, at);
		if (mother != null) addAncestors(mother, after + "M", after + "m", after + " ");
	}
	
	private void addBranch(Individual individual, String prefix) {
		Branch branch = new Branch();
		branch.generation = prefix.length();
		branch.position = branches.size();
		branch.individual = individual;
		branch.prefix = prefix;
		branches.add(branch);
	}
	
	public Iterable<Branch> branches() { return branches; }
}
