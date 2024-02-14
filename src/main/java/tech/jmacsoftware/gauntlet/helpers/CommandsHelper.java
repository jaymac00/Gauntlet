package tech.jmacsoftware.gauntlet.helpers;

import org.bukkit.plugin.java.JavaPlugin;

import tech.jmacsoftware.gauntlet.commands.CommandsHandler;
import tech.jmacsoftware.gauntlet.enums.Commands;
import tech.jmacsoftware.gauntlet.helpers.factories.TabCompleterFactory;

public class CommandsHelper {

	public static void registerCommands(CommandsHandler commandsHandler, TabCompleterFactory tabCompleterFactory, JavaPlugin plugin) {
		for (Commands commands : Commands.values()) {
			plugin.getCommand(commands.getCommand()).setExecutor(commandsHandler);
			plugin.getCommand(commands.getCommand()).setTabCompleter(tabCompleterFactory);
		}
	}
}
