package tech.jmacsoftware.gauntlet.items;

import com.google.common.collect.Multimap;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import tech.jmacsoftware.gauntlet.Gauntlet;
import tech.jmacsoftware.gauntlet.enums.CustomItems;

import java.nio.channels.MulticastChannel;
import java.util.jar.Attributes;

public class ToolRecipes implements Listener {

	private Plugin plugin = Gauntlet.getPlugin(Gauntlet.class);

	// Nice!
	public void redstonePickaxe() {
		ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE,1);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(ChatColor.WHITE + CustomItems.REDSTONE_PICKAXE.getName());
		meta.setLocalizedName(CustomItems.REDSTONE_PICKAXE.getName());
		meta.setCustomModelData(1);
		item.setItemMeta(meta);

		NamespacedKey namespacedKey = new NamespacedKey(plugin, CustomItems.REDSTONE_PICKAXE.getKey());
		ShapedRecipe shapedRecipe = new ShapedRecipe(namespacedKey, item);
		shapedRecipe.shape("$#$", " % ", " % ");
		shapedRecipe.setIngredient('#', Material.REDSTONE);
		shapedRecipe.setIngredient('$', Material.DIAMOND);
		shapedRecipe.setIngredient('%', Material.STICK);

		plugin.getServer().addRecipe(shapedRecipe);
	}

	public void senzaMisura(){

		ItemStack item = new ItemStack(Material.NETHERITE_SWORD, 1);
		AttributeModifier attackDamage = new AttributeModifier("attackDamage", 15, AttributeModifier.Operation.ADD_NUMBER);
		ItemMeta meta = item.getItemMeta();


		meta.setDisplayName("Senza Misura");
		meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, attackDamage);
		item.setItemMeta(meta);


		NamespacedKey namespacedKey = new NamespacedKey(plugin, CustomItems.SENZA_MISURA.getKey());
		ShapedRecipe shapedRecipe = new ShapedRecipe(namespacedKey, item);
		shapedRecipe.shape(" N ", " N ", "IBI");
		shapedRecipe.setIngredient('N', Material.NETHERITE_INGOT);
		shapedRecipe.setIngredient('B', Material.BLAZE_ROD);
		shapedRecipe.setIngredient('I', Material.IRON_NUGGET);

		plugin.getServer().addRecipe(shapedRecipe);
	}
}