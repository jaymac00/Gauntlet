package tech.jmacsoftware.gauntlet;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import tech.jmacsoftware.gauntlet.events.TunnelingEvent;
import tech.jmacsoftware.gauntlet.items.ToolRecipes;

public class Gauntlet extends JavaPlugin {

	public void onEnable() {
		getServer().getPluginManager().registerEvents(new TunnelingEvent(), this);
		loadConfig();

		ToolRecipes toolRecipes = new ToolRecipes();
		toolRecipes.redstonePickaxe();

		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Reality is what I want it to be.");
	}

	public void onDisable() {
		getServer().getConsoleSender().sendMessage(ChatColor.RED + "Reality has been restored to its former state.");
	}

	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

}
