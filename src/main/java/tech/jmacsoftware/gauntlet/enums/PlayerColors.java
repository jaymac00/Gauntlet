package tech.jmacsoftware.gauntlet.enums;

import org.bukkit.ChatColor;

public enum PlayerColors {

	DEFAULT("Default", ChatColor.DARK_GRAY, ChatColor.GRAY),
	AXMAN_JKC("AxMan_JKC", ChatColor.DARK_RED, ChatColor.RED),
	LONE_LAD("Lone_Lad", ChatColor.DARK_GREEN, ChatColor.GREEN),
	REXTER_01("Rexter_01", ChatColor.DARK_BLUE, ChatColor.BLUE);

	private String name;

	private ChatColor primaryColor;

	private ChatColor secondaryColor;

	PlayerColors(String name, ChatColor primaryColor, ChatColor secondaryColor) {
		this.name = name;
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
	}

	public static PlayerColors resolveByPlayerName(String name) {
		for (PlayerColors playerColors : PlayerColors.values()) {
			if (name != null && name.equals(playerColors.name)) {
				return playerColors;
			}
		}
		return DEFAULT;
	}

	public String getName() {
		return this.name;
	}

	public ChatColor getPrimaryColor() {
		return this.primaryColor;
	}

	public ChatColor getSecondaryColor() {
		return this.secondaryColor;
	}
}
