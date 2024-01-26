package tech.jmacsoftware.gauntlet.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import tech.jmacsoftware.gauntlet.Gauntlet;
import tech.jmacsoftware.gauntlet.enums.CustomBlocks;
import tech.jmacsoftware.gauntlet.enums.PlayerColors;

import java.util.ArrayList;

public class BlockDrops implements Listener {

	private Plugin plugin = Gauntlet.getPlugin(Gauntlet.class);

	public static final NamespacedKey PRIMARY_PLAYER = new NamespacedKey(Gauntlet.getPlugin(Gauntlet.class), "primary_player");

	public ItemStack gravestone(Player player, Player killer) {

		ItemStack gravestone = new ItemStack(Material.RED_NETHER_BRICK_SLAB, 1);
		ItemMeta meta = gravestone.getItemMeta();
		meta.setDisplayName(PlayerColors.resolveByPlayerName(player.getName()).getPrimaryColor() + player.getName() + "\'s Gravestone");
		meta.setLocalizedName(CustomBlocks.GRAVESTONE.getName());

		ArrayList<String> lore = new ArrayList<>();
		lore.add(ChatColor.GRAY + "Killed by " + ChatColor.RESET
				+ (killer != null
				? PlayerColors.resolveByPlayerName(killer.getName()).getSecondaryColor() + killer.getName() + ChatColor.RESET
				: ChatColor.GRAY + "INADEQUACY"));
		meta.setLore(lore);

		meta.getPersistentDataContainer().set(PRIMARY_PLAYER, PersistentDataType.STRING, player.getUniqueId().toString());

		meta.setCustomModelData(1);
		gravestone.setItemMeta(meta);
		return gravestone;
	}
}
