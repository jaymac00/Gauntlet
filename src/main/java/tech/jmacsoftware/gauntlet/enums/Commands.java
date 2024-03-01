package tech.jmacsoftware.gauntlet.enums;

public enum Commands {

	INVALID_COMMAND("invalidcommand"),
	RELOAD_TAB_LISTS("reloadtablists"),
	SET_CUSTOM_MODEL_DATA("setmodel"),
	SET_CUSTOM_MODEL_DATA_FORCE("setmodelforce");

	private String command;

	Commands(String command) {
		this.command = command;
	}

	public static Commands resolveByCommand(String command) {
		for (Commands commands : Commands.values()) {
			if (command != null && command.equals(commands.command)) {
				return commands;
			}
		}
		return INVALID_COMMAND;
	}

	public String getCommand() {
		return this.command;
	}
}
