package tech.jmacsoftware.gauntlet.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

import tech.jmacsoftware.gauntlet.Gauntlet;
import tech.jmacsoftware.gauntlet.items.EntityDrops;

public class HeadEvents implements Listener {

	private Plugin plugin = Gauntlet.getPlugin(Gauntlet.class);

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {

		Player player = event.getEntity();
		Player killer = player.getKiller();
		if (killer != null) {
			player.getWorld().dropItemNaturally(player.getLocation(), EntityDrops.playerHead(player, killer));
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {

		if (event.getItemInHand().getType().equals(Material.PLAYER_HEAD)) {
			event.setCancelled(true);
		}
	}
}
