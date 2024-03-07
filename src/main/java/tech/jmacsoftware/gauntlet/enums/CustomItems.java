package tech.jmacsoftware.gauntlet.enums;

public enum CustomItems {

	REDSTONE_PICKAXE("Redstone Pickaxe", "redstone_pickaxe"),
	PLAYER_HEAD("Player Head", "player_head"),
	SENZA_MISURA("Senza Misura", "senza_misura");

	private String name;
	private String key;

	CustomItems(String name, String key) {
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
