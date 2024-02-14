package tech.jmacsoftware.gauntlet.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import tech.jmacsoftware.gauntlet.enums.CustomModels;

import java.util.Arrays;
import java.util.List;

public class CustomModelDataCommands {

	public static void setCustomModelData(Player player, String[] args) {

		CustomModels data = null;
		if (args.length > 0) {
			try {
				data = CustomModels.resolveByValue(Integer.parseInt(args[0]));
			} catch (Exception e) {
				data = CustomModels.resolveByArg(args[0].toLowerCase());
			}
		} else {
			data = CustomModels.VANILLA;
		}

		List<String> owners = Arrays.stream(data.getOwners()).toList();
		if (owners.isEmpty() || owners.contains(player.getName())) {

			ItemStack item = player.getEquipment() != null ? player.getEquipment().getItemInMainHand() : null;
			if (item == null || item.getType().equals(Material.AIR)) {
				player.sendMessage(ChatColor.RED + "Unable to set CustomModelData...");
				return;
			}

			if (!item.hasItemMeta()) {
				if (data.equals(CustomModels.VANILLA)) {
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

			if (data.equals(CustomModels.VANILLA)) {
				player.sendMessage(ChatColor.GREEN + "Reverting to default CustomModelData for " + item.getType().name() + "...");
				meta.setCustomModelData(null);
			} else {
				meta.setCustomModelData(data.getValue());
			}
			item.setItemMeta(meta);
			player.sendMessage(ChatColor.GREEN + "Set CustomModelData for " + item.getType().name() + " to " + data.getValue() + " (" + data.getArg() + ")" + "!");
		} else {
			player.sendMessage(ChatColor.RED + "Not permitted to change CustomModelData for custom item belonging to another player...");
		}
	}
}
