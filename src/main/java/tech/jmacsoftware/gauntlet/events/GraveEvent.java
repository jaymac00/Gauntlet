package tech.jmacsoftware.gauntlet.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
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
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Consumer;

import tech.jmacsoftware.gauntlet.Gauntlet;
import tech.jmacsoftware.gauntlet.enums.CustomBlocks;
import tech.jmacsoftware.gauntlet.enums.InventorySizes;

import java.util.HashMap;
import java.util.UUID;

public class GraveEvent implements Listener {

	private Plugin plugin = Gauntlet.getPlugin(Gauntlet.class);

	private NamespacedKey gravestoneKey = new NamespacedKey(plugin, CustomBlocks.GRAVESTONE.getKey());

	public static HashMap<String, Inventory> GRAVES = new HashMap<>();

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {

		Player player = event.getEntity();
		Inventory grave = plugin.getServer().createInventory(null, InventorySizes.GRAVE.getSize(), player.getName() + "\'s Grave");
		for (ItemStack item : event.getDrops()) {
			grave.addItem(item);
		}
		GRAVES.put(player.getUniqueId().toString(), grave);
		event.getDrops().clear();

		World world = player.getWorld();
		Location playerLocation = player.getLocation();
		Consumer<ArmorStand> armorStandConsumer = gravestone -> createGravestone(player, gravestone);
		world.spawn(new Location(world, playerLocation.getBlockX(), playerLocation.getBlockY(), playerLocation.getBlockZ(), playerLocation.getYaw(), playerLocation.getPitch()).add(0.5, -0.225, 0.5), ArmorStand.class, armorStandConsumer);
		world.setType(player.getLocation(), Material.RED_NETHER_BRICK_SLAB);

//		Player killer = player.getKiller();
//		ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
//		SkullMeta meta = (SkullMeta) head.getItemMeta();
//		meta.setOwningPlayer(player);
//		meta.setDisplayName(PlayerColors.resolveByPlayerName(player.getName()).getPrimaryColor() + player.getName() + "\'s Head");
//
//		ArrayList<String> lore = new ArrayList<>();
//		lore.add(ChatColor.GRAY + "Killed by " + ChatColor.RESET
//				+ (killer != null
//				? PlayerColors.resolveByPlayerName(killer.getName()).getSecondaryColor() + killer.getName()
//				: ChatColor.GRAY + "INADEQUACY"));
//		meta.setLore(lore);
//
//		head.setItemMeta(meta);
//		world.dropItemNaturally(player.getLocation().add(0.0, 1.0, 0.0), head);
	}

	@EventHandler
	public void onRetrieve(PlayerInteractEvent event) {

		Player player = event.getPlayer();
		Block gravestone = event.getClickedBlock();
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
				&& !player.isSneaking()
				&& gravestone.getType().equals(Material.RED_NETHER_BRICK_SLAB)) {

			World world = player.getWorld();
			world.getNearbyEntities(gravestone.getLocation(), 1.0, 1.0, 1.0,
					gravestoneHead -> gravestoneHead.getPersistentDataContainer().has(gravestoneKey, PersistentDataType.STRING))
					.stream().forEach(entity -> {

						String uuid = entity.getPersistentDataContainer().get(gravestoneKey, PersistentDataType.STRING);
						OfflinePlayer corpse = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
						Inventory grave = GRAVES.get(uuid);
						if (grave != null && gravestone.getLocation().equals(corpse.getLastDeathLocation())) {
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
		if (gravestone.getType().equals(Material.RED_NETHER_BRICK_SLAB)) {
			world.getNearbyEntities(gravestone.getLocation(), 1.0, 1.0, 1.0,
					gravestoneHead -> gravestoneHead.getPersistentDataContainer().has(gravestoneKey, PersistentDataType.STRING))
					.stream().forEach(entity -> {

						String uuid = entity.getPersistentDataContainer().get(gravestoneKey, PersistentDataType.STRING);
						Inventory grave = GRAVES.get(uuid);
						if (grave != null) {
							for (ItemStack item : grave.getContents()) {
								if (item != null) {
									world.dropItemNaturally(gravestone.getLocation(), item);
								}
							}
						}
						event.setDropItems(false);
						GRAVES.remove(uuid);
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
		armorStand.getPersistentDataContainer().set(gravestoneKey, PersistentDataType.STRING, player.getUniqueId().toString());

		armorStand.setCustomName(player.getName() + "\'s " + CustomBlocks.GRAVESTONE.getName());
		armorStand.setGravity(false);
		armorStand.setInvulnerable(true);
		armorStand.setSilent(true);
		armorStand.setSmall(true);
		armorStand.setVisible(false);
	}
}
