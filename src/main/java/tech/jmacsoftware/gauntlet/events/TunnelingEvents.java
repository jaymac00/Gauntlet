package tech.jmacsoftware.gauntlet.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import tech.jmacsoftware.gauntlet.enums.CustomItems;
import tech.jmacsoftware.gauntlet.enums.FacingPlane;

import java.util.Collection;

public class TunnelingEvents implements Listener {

	@EventHandler
	public void onBreak(BlockBreakEvent event) {

		Block block = event.getBlock();
		Player player = event.getPlayer();
		ItemStack tool = player.getEquipment().getItemInMainHand();

		if (checkBlockTag(block)
				&& tool != null
				&& tool.hasItemMeta()
				&& tool.getItemMeta().hasItemName()
				&& tool.getItemMeta().getItemName().equals(CustomItems.REDSTONE_PICKAXE.getName())) {

			Location playerLocation = player.getLocation();
			Location blockLocation = block.getLocation();
			World world = block.getWorld();

			switch (FacingPlane.resolveByPitchAndYaw(playerLocation.getPitch(), playerLocation.getYaw())) {
				case XY_PLANE -> handleRelativeXY(blockLocation, world, tool);
				case YZ_PLANE -> handleRelativeYZ(blockLocation, world, tool);
				case XZ_PLANE -> handleRelativeXZ(blockLocation, world, tool);
			}
		}
	}

	private void handleRelativeXY(Location location, World world, ItemStack tool) {
		int xCoord, yCoord;
		int zCoord = (int) Math.floor(location.getZ());
		Block block;
		for (int x = -1; x < 2; ++x) {
			for (int y = -1; y < 2; ++y) {
				xCoord = (int) Math.floor(location.getX()) + x;
				yCoord = (int) Math.floor(location.getY()) + y;
				block = world.getBlockAt(xCoord, yCoord, zCoord);
				if (checkBlockTag(block) && !(x == 0 && y == 0)) {
					Collection<ItemStack> blockDrops = block.getDrops(tool);
					for (ItemStack item : blockDrops) {
						world.dropItemNaturally(location, item);
					}
					world.setType(xCoord, yCoord, zCoord, Material.AIR);
				}
			}
		}
	}

	private void handleRelativeYZ(Location location, World world, ItemStack tool) {
		int xCoord = (int) Math.floor(location.getX());
		int yCoord, zCoord;
		Block block;
		for (int y = -1; y < 2; ++y) {
			for (int z = -1; z < 2; ++z) {
				yCoord = (int) Math.floor(location.getY()) + y;
				zCoord = (int) Math.floor(location.getZ()) + z;
				block = world.getBlockAt(xCoord, yCoord, zCoord);
				if (checkBlockTag(block) && !(y == 0 && z == 0)) {
					Collection<ItemStack> blockDrops = block.getDrops(tool);
					for (ItemStack item : blockDrops) {
						world.dropItemNaturally(location, item);
					}
					world.setType(xCoord, yCoord, zCoord, Material.AIR);
				}
			}
		}
	}

	private void handleRelativeXZ(Location location, World world, ItemStack tool) {
		int yCoord = (int) Math.floor(location.getY());
		int xCoord, zCoord;
		Block block;
		for (int x = -1; x < 2; ++x) {
			for (int z = -1; z < 2; ++z) {
				xCoord = (int) Math.floor(location.getX()) + x;
				zCoord = (int) Math.floor(location.getZ()) + z;
				block = world.getBlockAt(xCoord, yCoord, zCoord);
				if (checkBlockTag(block) && !(x == 0 && z == 0)) {
					Collection<ItemStack> blockDrops = block.getDrops(tool);
					for (ItemStack item : blockDrops) {
						world.dropItemNaturally(location, item);
					}
					world.setType(xCoord, yCoord, zCoord, Material.AIR);
				}
			}
		}
	}

	private boolean checkBlockTag(Block block) {
		return (Tag.BASE_STONE_OVERWORLD.isTagged(block.getType()) || Tag.BASE_STONE_NETHER.isTagged(block.getType()));
	}
}
