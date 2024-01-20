package tech.jmacsoftware.gauntlet.events;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import tech.jmacsoftware.gauntlet.Gauntlet;
import tech.jmacsoftware.gauntlet.enums.CustomBlocks;
import tech.jmacsoftware.gauntlet.enums.InventorySizes;
import tech.jmacsoftware.gauntlet.enums.PlayerColors;

import java.util.ArrayList;
import java.util.HashMap;

public class GraveEvent implements Listener {

	private Plugin plugin = Gauntlet.getPlugin(Gauntlet.class);

	public static HashMap<String, Inventory> GRAVES = new HashMap<>();

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {

		Player player = event.getEntity();
		Inventory grave = plugin.getServer().createInventory(null, InventorySizes.GRAVE.getSize(), player.getName() + "\'s Grave");
		PlayerInventory playerInventory = player.getInventory();

		for (ItemStack item : playerInventory.getContents()) {
			if (item != null) {
				grave.addItem(item);
			}
		}
		playerInventory.clear();
		GRAVES.put(player.getName(), grave);

		World world = player.getWorld();
		world.setType(player.getLocation(), Material.CHEST);
		Block gravestone = world.getBlockAt(player.getLocation());
		Chest gravestoneState = (Chest) gravestone.getState();
		gravestoneState.setCustomName(player.getName() + "\'s" + CustomBlocks.GRAVESTONE.getName());
		gravestoneState.setMetadata(CustomBlocks.GRAVESTONE.getKey(), new FixedMetadataValue(plugin, CustomBlocks.GRAVESTONE.getName()));
		gravestoneState.update();

		Player killer = player.getKiller();
		if (killer != null) {
			ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
			SkullMeta meta = (SkullMeta) head.getItemMeta();
			meta.setOwningPlayer(player);
			meta.setDisplayName(PlayerColors.resolveByPlayerName(player.getName()).getPrimaryColor() + player.getName());

			ArrayList<String> lore = new ArrayList<>();
			lore.add(PlayerColors.resolveByPlayerName(killer.getName()).getSecondaryColor() + killer.getName());
			meta.setLore(lore);

			head.setItemMeta(meta);
			world.dropItemNaturally(player.getLocation().add(0.0, 1.0, 0.0), head);
		}
	}

	@EventHandler
	public void onRetrieve(PlayerInteractEvent event) {

		Action click = event.getAction();
		Block gravestone = event.getClickedBlock();
		if (click.equals(Action.RIGHT_CLICK_BLOCK)
				&& gravestone.getType().equals(Material.CHEST)) {

			Player player = event.getPlayer();
			Chest gravestoneState = (Chest) gravestone.getState();
			Inventory grave = GRAVES.get(player.getName());
			if (!gravestoneState.getMetadata(CustomBlocks.GRAVESTONE.getKey()).isEmpty()
					&& grave != null) {
				player.openInventory(grave);
			}
		}
	}
}
