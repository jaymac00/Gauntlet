package tech.jmacsoftware.gauntlet.enums;

public enum CustomBlocks {

	GRAVESTONE("Gravestone", "gravestone", 1);

	private String name;

	private String key;


	private int customModelData;

	CustomBlocks(String name, String key, int customModelData) {
		this.name = name;
		this.key = key;
		this.customModelData = customModelData;
	}

	public String getName() {
		return this.name;
	}

	public String getKey() {
		return this.key;
	}

	public int getCustomModelData() {
		return this.customModelData;
	}
}
