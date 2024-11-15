package tech.jmacsoftware.gauntlet.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import tech.jmacsoftware.gauntlet.Gauntlet;
import tech.jmacsoftware.gauntlet.enums.CustomItems;

public class ToolRecipes implements Listener {

	private Plugin plugin = Gauntlet.getPlugin(Gauntlet.class);

	// Nice!
	public void redstonePickaxe() {
		ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE,1);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(ChatColor.WHITE + CustomItems.REDSTONE_PICKAXE.getName());
		meta.setItemName(CustomItems.REDSTONE_PICKAXE.getName());
		meta.setCustomModelData(CustomItems.REDSTONE_PICKAXE.getCustomModelData());
		item.setItemMeta(meta);

		NamespacedKey namespacedKey = new NamespacedKey(plugin, CustomItems.REDSTONE_PICKAXE.getKey());
		ShapedRecipe shapedRecipe = new ShapedRecipe(namespacedKey, item);
		shapedRecipe.shape("$#$", " % ", " % ");
		shapedRecipe.setIngredient('#', Material.REDSTONE);
		shapedRecipe.setIngredient('$', Material.DIAMOND);
		shapedRecipe.setIngredient('%', Material.STICK);

		plugin.getServer().addRecipe(shapedRecipe);
	}
}
