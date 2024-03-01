package tech.jmacsoftware.gauntlet.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import tech.jmacsoftware.gauntlet.helpers.factories.TabCompleterFactory;

public class CustomModelDataCommands {

	public static void setCustomModelData(Player player, String[] args) {

		TabListEntry data;
		if (args.length > 0) {
			data = TabCompleterFactory.CUSTOM_MODEL_DATA_TAB_LIST.get(args[0].toUpperCase());
		} else {
			data = TabCompleterFactory.CUSTOM_MODEL_DATA_TAB_LIST.get("VANILLA");
		}

		if (data != null && (data.getOwners().isEmpty() || data.getOwners().contains(player.getName()))) {

			ItemStack item = player.getEquipment() != null ? player.getEquipment().getItemInMainHand() : null;
			if (item == null || item.getType().equals(Material.AIR)) {
				player.sendMessage(ChatColor.RED + "Unable to set CustomModelData...");
				return;
			}

			if (!item.hasItemMeta()) {
				if (data.getKeyword().equalsIgnoreCase("VANILLA")) {
					player.sendMessage(ChatColor.GREEN + "No changes made to CustomModelData for " + item.getType().name() + "...");
					return;
				}

				ItemStack temp = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);
				item.setItemMeta(temp.getItemMeta());
			}

			ItemMeta meta = item.getItemMeta();
			if (meta.hasLocalizedName()) {
				player.sendMessage(ChatColor.RED + "Not permitted to change CustomModelData for custom item with special functionality...");
				return;
			}

			if (data.getKeyword().equalsIgnoreCase("VANILLA")) {
				player.sendMessage(ChatColor.GREEN + "Reverting to default CustomModelData for " + item.getType().name() + "...");
				meta.setCustomModelData(null);
			} else {
				meta.setCustomModelData(data.getValue());
			}
			item.setItemMeta(meta);
			player.sendMessage(ChatColor.GREEN + "Set CustomModelData for " + item.getType().name() + " to " + data.getValue() + " (" + data.getKeyword().toUpperCase() + ")" + "!");
		} else {
			player.sendMessage(ChatColor.RED + "Not permitted to change CustomModelData for custom item belonging to another player...");
		}
	}
}
