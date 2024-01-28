package tech.jmacsoftware.gauntlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import tech.jmacsoftware.gauntlet.events.GraveEvent;
import tech.jmacsoftware.gauntlet.events.TunnelingEvent;
import tech.jmacsoftware.gauntlet.items.ToolRecipes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class Gauntlet extends JavaPlugin {

	private final ObjectMapper object_mapper = new ObjectMapper();

	public void onEnable() {
		getServer().getPluginManager().registerEvents(new TunnelingEvent(), this);
		getServer().getPluginManager().registerEvents(new GraveEvent(), this);
		loadConfig();
		loadGraves();

		ToolRecipes toolRecipes = new ToolRecipes();
		toolRecipes.redstonePickaxe();

		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Reality is what I want it to be.");
	}

	public void onDisable() {
		saveGraves();
		getServer().getConsoleSender().sendMessage(ChatColor.RED + "Reality has been restored to its former state.");
	}

	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	private void loadGraves() {
		try {
			GraveEvent.GRAVES = object_mapper.readValue(new File(System.getProperty("user.dir").concat("/resources/inventories/graves.json")), HashMap.class);
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			getServer().getConsoleSender().sendMessage("Unable to load graves...\n" + e);
		}
	}

	private void saveGraves() {
		try {
			object_mapper.writeValue(new File(System.getProperty("user.dir").concat("/resources/inventories/graves.json")), GraveEvent.GRAVES);
		} catch (IOException e) {
			getServer().getConsoleSender().sendMessage("Unable to save graves...\n" + e);
		}
	}

}
