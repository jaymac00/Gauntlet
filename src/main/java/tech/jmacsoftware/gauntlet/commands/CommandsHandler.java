package tech.jmacsoftware.gauntlet.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tech.jmacsoftware.gauntlet.enums.Commands;

public class CommandsHandler implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player) {
			Player player = (Player) sender;

			switch (Commands.resolveByCommand(label)) {
				case INVALID_COMMAND -> sender.sendMessage(ChatColor.RED + "Command \"" + label + "\" unknown...");
				case RELOAD_TAB_LISTS -> sender.sendMessage(ChatColor.YELLOW + "Command \"" + label + "\" not implemented yet...");
				case SET_CUSTOM_MODEL_DATA -> CustomModelDataCommands.setCustomModelData(player, args);
				case SET_CUSTOM_MODEL_DATA_FORCE -> CustomModelDataCommands.setCustomModelDataForce(player, args);
			}
		}

		return true;
	}
}
