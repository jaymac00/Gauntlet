package tech.jmacsoftware.gauntlet.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import tech.jmacsoftware.gauntlet.enums.CustomBlocks;

public class BlockDrops implements Listener {

	public static ItemStack gravestone() {

		ItemStack gravestone = new ItemStack(Material.RED_NETHER_BRICK_SLAB, 1);
		ItemMeta meta = gravestone.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + "Gravestone");
		meta.setItemName(CustomBlocks.GRAVESTONE.getName());
		meta.setCustomModelData(CustomBlocks.GRAVESTONE.getCustomModelData());
		gravestone.setItemMeta(meta);
		return gravestone;
	}
}
