package tech.jmacsoftware.gauntlet;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import tech.jmacsoftware.gauntlet.commands.CommandsHandler;
import tech.jmacsoftware.gauntlet.events.GraveEvents;
import tech.jmacsoftware.gauntlet.events.HeadEvents;
import tech.jmacsoftware.gauntlet.events.TunnelingEvents;
import tech.jmacsoftware.gauntlet.helpers.AutoSaveHelper;
import tech.jmacsoftware.gauntlet.helpers.CommandsHelper;
import tech.jmacsoftware.gauntlet.helpers.GravesHelper;
import tech.jmacsoftware.gauntlet.helpers.factories.TabCompleterFactory;
import tech.jmacsoftware.gauntlet.items.ItemRecipes;
import tech.jmacsoftware.gauntlet.items.ToolRecipes;

public class Gauntlet extends JavaPlugin {

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new GraveEvents(), this);
		getServer().getPluginManager().registerEvents(new HeadEvents(), this);
		getServer().getPluginManager().registerEvents(new TunnelingEvents(), this);
		loadConfig();

		CommandsHelper.registerCommands(new CommandsHandler(), new TabCompleterFactory(this), this);
		GravesHelper.loadGraves(this);

		ToolRecipes toolRecipes = new ToolRecipes();
		toolRecipes.redstonePickaxe();

		ItemRecipes itemRecipes = new ItemRecipes();
		itemRecipes.craftSaddle();
		itemRecipes.craftDHorseArmor();
		itemRecipes.craftGHorseArmor();
		itemRecipes.craftIHorseArmor();

		getServer().getScheduler().runTaskTimer(this, new AutoSaveHelper(), 5184000, 5184000);

		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Reality is what I want it to be.");
	}

	@Override
	public void onDisable() {
		GravesHelper.saveGraves(this);
		getServer().getConsoleSender().sendMessage(ChatColor.RED + "Reality has been restored to its former state.");
	}

	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
}
