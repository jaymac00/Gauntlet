package tech.jmacsoftware.gauntlet.enums;

public enum CustomModels {

	VANILLA("vanilla", 0, new String[]{}),
	SERVER_OWNER("server_owner", 1, new String[]{Players.LONE_LAD.getName()});

	private String arg;

	private int value;

	private String[] owners;

	CustomModels(String arg, int value, String[] owners) {
		this.arg = arg;
		this.value = value;
		this.owners = owners;
	}

	public static CustomModels resolveByArg(String arg) {
		for (CustomModels customModels : CustomModels.values()) {
			if (arg != null && arg.equals(customModels.arg)) {
				return customModels;
			}
		}
		return VANILLA;
	}

	public static CustomModels resolveByValue(int value) {
		for (CustomModels customModels : CustomModels.values()) {
			if (value > 0 && value == customModels.value) {
				return customModels;
			}
		}
		return VANILLA;
	}

	public String getArg() {
		return this.arg;
	}

	public int getValue() {
		return this.value;
	}

	public String[] getOwners() {
		return this.owners;
	}
}
