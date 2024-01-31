package tech.jmacsoftware.gauntlet.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import tech.jmacsoftware.gauntlet.enums.PlayerColors;

import java.util.ArrayList;

public class EntityDrops implements Listener {

	public static ItemStack playerHead(Player player, Player killer) {

		ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta meta = (SkullMeta) head.getItemMeta();
		meta.setDisplayName(PlayerColors.resolveByPlayerName(player.getName()).getPrimaryColor() + player.getName() + "\'s Head");
		meta.setOwningPlayer(player);

		if (killer != null) {
			ArrayList<String> lore = new ArrayList<>();
			lore.add(ChatColor.GRAY + "Killed by " + ChatColor.RESET
					+ PlayerColors.resolveByPlayerName(killer.getName()).getSecondaryColor() + killer.getName());
			meta.setLore(lore);
		}

		head.setItemMeta(meta);
		return head;
	}
}
