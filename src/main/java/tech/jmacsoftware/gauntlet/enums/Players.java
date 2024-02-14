package tech.jmacsoftware.gauntlet.enums;

import org.bukkit.ChatColor;

public enum Players {

	DEFAULT("Default", ChatColor.DARK_GRAY, ChatColor.GRAY),
	AXMAN_JKC("AxMan_JKC", ChatColor.DARK_RED, ChatColor.RED),
	EGGPLANT0505("eggplant0505", ChatColor.DARK_PURPLE, ChatColor.LIGHT_PURPLE),
	LONE_LAD("Lone_Lad", ChatColor.DARK_GREEN, ChatColor.GREEN),
	REXTER_01("Rexter_01", ChatColor.DARK_BLUE, ChatColor.BLUE),
	TAVROS1577("Tavros1577", ChatColor.GOLD, ChatColor.YELLOW),
	THEPANMAN02("thepanman02", ChatColor.DARK_AQUA, ChatColor.AQUA);

	private String name;

	private ChatColor primaryColor;

	private ChatColor secondaryColor;

	Players(String name, ChatColor primaryColor, ChatColor secondaryColor) {
		this.name = name;
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
	}

	public static Players resolveByPlayerName(String name) {
		for (Players playerColors : Players.values()) {
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
