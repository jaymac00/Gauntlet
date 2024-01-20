package tech.jmacsoftware.gauntlet.enums;

public enum CustomBlocks {

	GRAVESTONE("Gravestone", "gravestone");

	private String name;

	private String key;

	CustomBlocks(String name, String key) {
		this.name = name;
		this.key = key;
	}

	public String getName() {
		return this.name;
	}

	public String getKey() {
		return this.key;
	}
}
