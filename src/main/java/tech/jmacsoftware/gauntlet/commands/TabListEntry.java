package tech.jmacsoftware.gauntlet.commands;

import java.util.List;

public class TabListEntry {

	private String keyword;

	private int value;

	private List<String> owners;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public List<String> getOwners() {
		return owners;
	}

	public void setOwners(List<String> owners) {
		this.owners = owners;
	}
}
