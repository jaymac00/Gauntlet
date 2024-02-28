package tech.jmacsoftware.gauntlet;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import tech.jmacsoftware.gauntlet.events.GraveEvents;
import tech.jmacsoftware.gauntlet.events.HeadEvents;
import tech.jmacsoftware.gauntlet.events.TunnelingEvents;
import tech.jmacsoftware.gauntlet.helpers.AutoSaveHelper;
import tech.jmacsoftware.gauntlet.helpers.GraveHelper;
import tech.jmacsoftware.gauntlet.items.ToolRecipes;

public class Gauntlet extends JavaPlugin {

	public void onEnable() {
		getServer().getPluginManager().registerEvents(new GraveEvents(), this);
		getServer().getPluginManager().registerEvents(new HeadEvents(), this);
		getServer().getPluginManager().registerEvents(new TunnelingEvents(), this);
		loadConfig();

		GraveHelper.loadGraves(this);

		ToolRecipes toolRecipes = new ToolRecipes();
		toolRecipes.redstonePickaxe();

		toolRecipes.craftSaddle();

		getServer().getScheduler().runTaskTimer(this, new AutoSaveHelper(), 5184000, 5184000);

		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Reality is what I want it to be.");
	}

	public void onDisable() {
		GraveHelper.saveGraves(this);
		getServer().getConsoleSender().sendMessage(ChatColor.RED + "Reality has been restored to its former state.");
	}

	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
}
