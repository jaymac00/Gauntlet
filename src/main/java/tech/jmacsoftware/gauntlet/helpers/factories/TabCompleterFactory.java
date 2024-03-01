package tech.jmacsoftware.gauntlet.helpers.factories;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import tech.jmacsoftware.gauntlet.commands.TabListEntry;
import tech.jmacsoftware.gauntlet.enums.Commands;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TabCompleterFactory implements TabCompleter {

	public static HashMap<String, TabListEntry> CUSTOM_MODEL_DATA_TAB_LIST = new HashMap<>();

	private static final String FILE_NAME = "plugins/Gauntlet/resources/static/tabLists/customModels.json";

	public TabCompleterFactory(Plugin plugin) {
		super();

		File tabList = new File(FILE_NAME);
		if (tabList.isFile()) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				JsonParser jsonParser = mapper.createParser(tabList);
				JsonNode node = jsonParser.readValueAsTree();

				node.fields().forEachRemaining(entry -> {
					CUSTOM_MODEL_DATA_TAB_LIST.put(entry.getKey(), mapper.convertValue(entry.getValue(), TabListEntry.class));
				});

				jsonParser.close();
			} catch (IOException e) {
				plugin.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "[Gauntlet.TabCompleterFactory] (file=" + tabList.getName() + ") " + ChatColor.RESET + e);
			}
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

		List<String> tabList = new ArrayList<>();
		if (sender instanceof Player player) {

			switch (Commands.resolveByCommand(label)) {
				case SET_CUSTOM_MODEL_DATA -> CUSTOM_MODEL_DATA_TAB_LIST.forEach((key, entry) -> {
					if (entry.getOwners().size() < 1 || entry.getOwners().contains(player.getName())) {
						tabList.add(entry.getKeyword());
					}
				});
			}
		}

		return tabList;
	}
}
