package tech.jmacsoftware.gauntlet.events;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Consumer;

import tech.jmacsoftware.gauntlet.Gauntlet;
import tech.jmacsoftware.gauntlet.enums.CustomBlocks;
import tech.jmacsoftware.gauntlet.enums.InventorySizes;
import tech.jmacsoftware.gauntlet.enums.PlayerColors;

import java.util.ArrayList;
import java.util.HashMap;

public class GraveEvent implements Listener {

	private Plugin plugin = Gauntlet.getPlugin(Gauntlet.class);

	private NamespacedKey gravestoneKey = new NamespacedKey(plugin, CustomBlocks.GRAVESTONE.getKey());

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
		GRAVES.put(player.getName(), grave);

		World world = player.getWorld();
		Location playerLocation = player.getLocation();
		Consumer<ArmorStand> armorStandConsumer = gravestone -> createGravestone(player, gravestone);
		world.spawn(new Location(world, playerLocation.getBlockX(), playerLocation.getBlockY(), playerLocation.getBlockZ(), playerLocation.getYaw(), playerLocation.getPitch()).add(0.5, -0.225, 0.5), ArmorStand.class, armorStandConsumer);
		world.setType(player.getLocation(), Material.STONE_SLAB);

		Player killer = player.getKiller();
		ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta meta = (SkullMeta) head.getItemMeta();
		meta.setOwningPlayer(player);
		meta.setDisplayName(PlayerColors.resolveByPlayerName(player.getName()).getPrimaryColor() + player.getName() + "\'s Head");

		ArrayList<String> lore = new ArrayList<>();
		lore.add(ChatColor.GRAY + "Killed by " + ChatColor.RESET
				+ (killer != null
				? PlayerColors.resolveByPlayerName(killer.getName()).getSecondaryColor() + killer.getName()
				: ChatColor.GRAY + "INADEQUACY"));
		meta.setLore(lore);

		head.setItemMeta(meta);
		world.dropItemNaturally(player.getLocation().add(0.0, 1.0, 0.0), head);
	}

	@EventHandler
	public void onRetrieve(PlayerInteractEvent event) {

		Player player = event.getPlayer();
		Block gravestone = event.getClickedBlock();
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
				&& !player.isSneaking()
				&& gravestone.getType().equals(Material.STONE_SLAB)
				&& gravestone.getLocation().equals(player.getLastDeathLocation())) {

			World world = player.getWorld();
			world.getNearbyEntities(gravestone.getLocation(), 1.0, 1.0, 1.0,
					gravestoneHead -> gravestoneHead.getPersistentDataContainer().has(gravestoneKey, PersistentDataType.STRING))
					.stream().forEach(entity -> {

				String corpseName = entity.getPersistentDataContainer().get(gravestoneKey, PersistentDataType.STRING);
				Inventory grave = GRAVES.get(corpseName);
				if (grave != null) {
					event.setUseItemInHand(Event.Result.DENY);
					player.openInventory(grave);
				}
			});
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {

		Block gravestone = event.getBlock();
		Player player = event.getPlayer();
		World world = player.getWorld();
		if (gravestone.getType().equals(Material.STONE_SLAB)) {
			world.getNearbyEntities(gravestone.getLocation(), 1.0, 1.0, 1.0,
					gravestoneHead -> gravestoneHead.getPersistentDataContainer().has(gravestoneKey, PersistentDataType.STRING))
					.stream().forEach(entity -> {

				String corpseName = entity.getPersistentDataContainer().get(gravestoneKey, PersistentDataType.STRING);
				Inventory grave = GRAVES.get(corpseName);
				if (grave != null) {
					for (ItemStack item : grave.getContents()) {
						if (item != null) {
							world.dropItemNaturally(gravestone.getLocation(), item);
						}
					}
				}
				GRAVES.remove(corpseName);
				entity.remove();
			});
		}
	}

	private void createGravestone(Player player, ArmorStand armorStand) {

		ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta meta = (SkullMeta) head.getItemMeta();
		meta.setOwningPlayer(player);
		head.setItemMeta(meta);

		armorStand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
		armorStand.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING);
		armorStand.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.ADDING);
		armorStand.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.ADDING);

		armorStand.getEquipment().setHelmet(head);
		armorStand.getPersistentDataContainer().set(gravestoneKey, PersistentDataType.STRING, player.getName());

		armorStand.setCustomName(player.getName() + "\'s " + CustomBlocks.GRAVESTONE.getName());
		armorStand.setGravity(false);
		armorStand.setInvulnerable(true);
		armorStand.setSilent(true);
		armorStand.setSmall(true);
		armorStand.setVisible(false);
	}
}
