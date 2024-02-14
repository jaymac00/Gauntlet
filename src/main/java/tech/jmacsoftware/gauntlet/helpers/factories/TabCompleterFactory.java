package tech.jmacsoftware.gauntlet.helpers.factories;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import tech.jmacsoftware.gauntlet.enums.Commands;
import tech.jmacsoftware.gauntlet.enums.CustomModels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabCompleterFactory implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

		List<String> tabList = new ArrayList<>();
		if (sender instanceof Player) {
			Player player = (Player) sender;

			switch (Commands.resolveByCommand(label)) {
				case SET_CUSTOM_MODEL_DATA -> Arrays.stream(CustomModels.values())
						.filter(customModels -> customModels.getOwners().length < 1 || Arrays.stream(customModels.getOwners()).toList().contains(player.getName()))
						.forEach(customModels -> tabList.add(customModels.getArg()));
			}
		}

		return tabList;
	}
}
