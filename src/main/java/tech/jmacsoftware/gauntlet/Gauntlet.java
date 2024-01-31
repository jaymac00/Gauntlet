package tech.jmacsoftware.gauntlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import tech.jmacsoftware.gauntlet.events.GraveEvents;
import tech.jmacsoftware.gauntlet.events.TunnelingEvents;
import tech.jmacsoftware.gauntlet.items.ToolRecipes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class Gauntlet extends JavaPlugin {

	private final ObjectMapper object_mapper = new ObjectMapper();

	public void onEnable() {
		getServer().getPluginManager().registerEvents(new TunnelingEvents(), this);
		getServer().getPluginManager().registerEvents(new GraveEvents(), this);
		loadConfig();
		//loadGraves();

		ToolRecipes toolRecipes = new ToolRecipes();
		toolRecipes.redstonePickaxe();

		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Reality is what I want it to be.");
	}

	public void onDisable() {
		//saveGraves();
		getServer().getConsoleSender().sendMessage(ChatColor.RED + "Reality has been restored to its former state.");
	}

	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	private void loadGraves() {
		File inventories = new File(System.getProperty("user.dir").concat("/resources/inventories/graves/"));
		if (inventories.isDirectory()) {
			Arrays.stream(inventories.listFiles()).forEach(grave -> {
				try {
					GraveEvents.GRAVES.put(grave.getName().substring(0, grave.getName().length() - 5), object_mapper.readValue(grave, Inventory.class));
				} catch (IOException e) {
					getServer().getConsoleSender().sendMessage(ChatColor.RED + "Unable to load grave from " + grave.getName() + "...\n" + e);
				}
			});
		}
	}

	private void saveGraves() {
		GraveEvents.GRAVES.forEach((key, inventory) -> {
			try {
				object_mapper.writeValue(new File(System.getProperty("user.dir").concat("/resources/inventories/graves/" + key + ".json")), inventory);
			} catch (FileNotFoundException e) {
				File grave = new File(System.getProperty("user.dir").concat("/resources/inventories/graves/" + key + ".json"));
				grave.getParentFile().mkdirs();
				try {
					grave.createNewFile();
					object_mapper.writeValue(grave, inventory);
					getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Successfully saved grave for " + key + "!");
				} catch (IOException e2) {
					getServer().getConsoleSender().sendMessage(ChatColor.RED + "Unable to save grave for " + key + "...\n" + e2);
				}
			} catch (IOException e) {
				getServer().getConsoleSender().sendMessage(ChatColor.RED + "Unable to save grave for " + key + "...\n" + e);
			}
		});
	}

}
