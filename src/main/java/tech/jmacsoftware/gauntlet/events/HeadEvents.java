package tech.jmacsoftware.gauntlet.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Consumer;

import tech.jmacsoftware.gauntlet.Gauntlet;
import tech.jmacsoftware.gauntlet.items.EntityDrops;

import java.util.Collection;

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

		ItemStack itemStack = event.getItemInHand();
		if (itemStack.getType().equals(Material.PLAYER_HEAD)) {

			ItemStack clone = itemStack.clone();
			SkullMeta meta = (SkullMeta) clone.getItemMeta();
			meta.setCustomModelData(1);
			clone.setItemMeta(meta);

			Player player = event.getPlayer();
			Block block = event.getBlockPlaced();
			Consumer<ItemFrame> itemFrameConsumer = itemFrame -> createItemFrame(itemFrame, block.getFace(event.getBlockAgainst()) , clone);
			player.getWorld().spawn(block.getLocation(), ItemFrame.class, itemFrameConsumer);
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {

		Block block = event.getBlock();
		if (block.getType().equals(Material.PLAYER_HEAD)) {
			World world = block.getWorld();
			Location location = block.getLocation();

			Collection<ItemFrame> itemFrames = world.getEntitiesByClass(ItemFrame.class).stream().filter(itemFrame -> itemFrame.getLocation().equals(location)).toList();
			if (!itemFrames.isEmpty()) {

				ItemStack clone = itemFrames.stream().findAny().get().getItem();
				SkullMeta meta = (SkullMeta) clone.getItemMeta();
				meta.setCustomModelData(null);
				clone.setItemMeta(meta);

				world.dropItemNaturally(location, clone);
				event.setDropItems(false);
			}
		}
	}

	private void createItemFrame(ItemFrame itemFrame, BlockFace blockFace, ItemStack itemStack) {

		itemFrame.setFixed(true);
		itemFrame.setFacingDirection(blockFace);
		itemFrame.setInvulnerable(true);
		itemFrame.setItem(itemStack);
		itemFrame.setSilent(true);
		itemFrame.setVisible(false);
	}
}
