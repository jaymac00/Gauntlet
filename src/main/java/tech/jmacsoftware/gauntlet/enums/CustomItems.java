package tech.jmacsoftware.gauntlet.enums;

public enum CustomItems {

	REDSTONE_PICKAXE("Redstone Pickaxe", "redstone_pickaxe", 1),
	PLAYER_HEAD("Player Head", "player_head", 0);

	private String name;

	private String key;

	private int customModelData;

	CustomItems(String name, String key, int customModelData) {
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
