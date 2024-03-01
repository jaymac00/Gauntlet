package tech.jmacsoftware.gauntlet.helpers.factories;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import tech.jmacsoftware.gauntlet.enums.Commands;
import tech.jmacsoftware.gauntlet.enums.CustomModels;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TabCompleterFactory implements TabCompleter {

	public static Multimap<String, HashMap<String, List<String>>> TAB_LISTS;

	private static final String DIRECTORY = "plugins/Gauntlet/resources/static/tabLists/";

	public TabCompleterFactory(Plugin plugin) {
		super();
		TAB_LISTS = ArrayListMultimap.create();

		File tabLists = new File(DIRECTORY);
		if (tabLists.isDirectory()) {
			ObjectMapper mapper = new ObjectMapper();
			Arrays.stream(tabLists.listFiles()).forEach(file -> {
				try {
					JsonParser jsonParser = mapper.createParser(file);
					JsonNode node = jsonParser.readValueAsTree();

					node.fields().forEachRemaining(entry -> {
						TAB_LISTS.put(file.getName(), mapper.convertValue(entry, HashMap.class));
					});

					jsonParser.close();
				} catch (IOException e) {
					plugin.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "[Gauntlet.TabCompleterFactory] (file=" + file.getName() + ") " + ChatColor.RESET + e);
				}
			});
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

		List<String> tabList = new ArrayList<>();
		if (sender instanceof Player) {
			Player player = (Player) sender;

			switch (Commands.resolveByCommand(label)) {
				case SET_CUSTOM_MODEL_DATA -> Arrays.stream(CustomModels.values())
						.filter(customModels -> customModels.getOwners().length < 1 || Arrays.stream(customModels.getOwners()).toList().contains(player.getName()))
						.forEach(customModels -> tabList.add(customModels.getArg()));
				// reference TAB_LISTS
			}
		}

		return tabList;
	}
}
