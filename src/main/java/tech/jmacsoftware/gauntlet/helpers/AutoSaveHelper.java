package tech.jmacsoftware.gauntlet.helpers;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

import tech.jmacsoftware.gauntlet.Gauntlet;

import java.util.concurrent.TimeUnit;

public class AutoSaveHelper implements Runnable {

	private Plugin plugin = Gauntlet.getPlugin(Gauntlet.class);

	@Override
	public void run() {

		Server server = plugin.getServer();
		server.dispatchCommand(server.getConsoleSender(), "save-off");

		try {
			TimeUnit.SECONDS.sleep(2);
			server.dispatchCommand(server.getConsoleSender(), "save-all");
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			server.getConsoleSender().sendMessage(ChatColor.DARK_RED + "[Gauntlet.AutoSaveHelper] " + ChatColor.RESET + e);
		}

		server.dispatchCommand(server.getConsoleSender(), "save-on");
	}
}
