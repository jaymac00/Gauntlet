package tech.jmacsoftware.gauntlet.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
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
import org.bukkit.util.Vector;

import tech.jmacsoftware.gauntlet.Gauntlet;
import tech.jmacsoftware.gauntlet.enums.CustomBlocks;
import tech.jmacsoftware.gauntlet.enums.InventorySizes;
import tech.jmacsoftware.gauntlet.items.BlockDrops;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class GraveEvent implements Listener {

	private Plugin plugin = Gauntlet.getPlugin(Gauntlet.class);

	private final NamespacedKey gravestoneKey = new NamespacedKey(plugin, CustomBlocks.GRAVESTONE.getKey());

	private BlockDrops blockDrops = new BlockDrops();

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
		world.setType(playerLocation, Material.RED_NETHER_BRICK_SLAB);
		Consumer<ItemFrame> itemFrameConsumer = gravestone -> createGravestoneBlockOnDeath(player, player.getKiller(), gravestone);
		world.spawn(playerLocation, ItemFrame.class, itemFrameConsumer);
		Consumer<ArmorStand> armorStandConsumer = gravestone -> createGravestoneHead(player, gravestone);
		world.spawn(new Location(world, playerLocation.getBlockX(), playerLocation.getBlockY(), playerLocation.getBlockZ(), playerLocation.getYaw(), playerLocation.getPitch()).add(0.5, -0.225, 0.5), ArmorStand.class, armorStandConsumer);
	}

	@EventHandler
	public void onRetrieve(PlayerInteractEvent event) {

		Player player = event.getPlayer();
		Block gravestone = event.getClickedBlock();
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
				&& !player.isSneaking()
				&& gravestone.getType().equals(Material.RED_NETHER_BRICK_SLAB)) {

			World world = player.getWorld();
			Collection<Entity> nearbyEntities = world.getNearbyEntities(gravestone.getLocation(), 0.5, 0.5, 0.5,
					gravestoneHead -> gravestoneHead.getPersistentDataContainer().has(gravestoneKey, PersistentDataType.STRING));
			if (!nearbyEntities.isEmpty()) {
				String uuid = nearbyEntities.stream().findAny().get().getPersistentDataContainer().get(gravestoneKey, PersistentDataType.STRING);
				OfflinePlayer corpse = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
				Inventory grave = GRAVES.get(uuid);
				if (grave != null && gravestone.getLocation().equals(corpse.getLastDeathLocation())) {
					event.setUseItemInHand(Event.Result.DENY);
					player.openInventory(grave);
				}
			}
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {

		Block gravestone = event.getBlock();
		if (gravestone.getType().equals(Material.RED_NETHER_BRICK_SLAB)) {
			Player player = event.getPlayer();
			World world = player.getWorld();
			Collection<Entity> nearbyEntities = world.getNearbyEntities(gravestone.getLocation(), 0.5, 0.5, 0.5,
					gravestoneHead -> gravestoneHead.getPersistentDataContainer().has(gravestoneKey, PersistentDataType.STRING));
			if (!nearbyEntities.isEmpty()) {

				String uuid = nearbyEntities.stream().findAny().get().getPersistentDataContainer().get(gravestoneKey, PersistentDataType.STRING);
				Inventory grave = GRAVES.get(uuid);
				if (grave != null) {
					for (ItemStack item : grave.getContents()) {
						if (item != null) {
							world.dropItemNaturally(gravestone.getLocation(), item);
						}
					}
				}
				GRAVES.remove(uuid);

				nearbyEntities.forEach(entity -> {
					try {
						world.dropItemNaturally(gravestone.getLocation(), ((ItemFrame) entity).getItem());
					} catch (Exception e) {
						// not sure if there is a better way of doing this, short of creating/searching for 2 different tags
					}
					entity.remove();
				});
				event.setDropItems(false);
			}
		}
	}

	@EventHandler
	private void onPlace(PlayerInteractEvent event) {

		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
				&& event.isBlockInHand()
				&& !event.useItemInHand().equals(Event.Result.DENY)) {

			Player player = event.getPlayer();
			ItemStack blockUsed = player.getInventory().getItem(event.getHand());
			if (blockUsed.hasItemMeta()
					&& blockUsed.getItemMeta().hasLocalizedName()
					&& blockUsed.getItemMeta().getLocalizedName().equals(CustomBlocks.GRAVESTONE.getName())
					&& blockUsed.getType().equals(Material.RED_NETHER_BRICK_SLAB)
					&& blockUsed.getItemMeta().getPersistentDataContainer().has(BlockDrops.PRIMARY_PLAYER, PersistentDataType.STRING)) {

				World world = player.getWorld();
				Block blockClicked = event.getClickedBlock();
				Location blockLocation = blockClicked.getLocation();
				Vector faceVector = event.getBlockFace().getDirection().normalize();

				world.setType(blockLocation.add(faceVector), Material.RED_NETHER_BRICK_SLAB);
				Consumer<ItemFrame> itemFrameConsumer = gravestone -> createGravestoneBlockOnPlace(player, blockUsed, gravestone);
				world.spawn(blockLocation, ItemFrame.class, itemFrameConsumer);
				Consumer<ArmorStand> armorStandConsumer = gravestone -> createGravestoneHead(player, gravestone);
				world.spawn((blockLocation).add(0.5, -0.225, 0.5), ArmorStand.class, armorStandConsumer);
				blockUsed.setAmount(blockUsed.getAmount() - 1);
			}
		}
	}

	private void createGravestoneHead(Player player, ArmorStand armorStand) {

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

	private void createGravestoneBlockOnDeath(Player player, Player killer, ItemFrame itemFrame) {

		itemFrame.getPersistentDataContainer().set(gravestoneKey, PersistentDataType.STRING, player.getUniqueId().toString());

		itemFrame.setFixed(true);
		itemFrame.setFacingDirection(BlockFace.UP);
		itemFrame.setInvulnerable(true);
		itemFrame.setItem(blockDrops.gravestone(player, killer));
		itemFrame.setSilent(true);
		itemFrame.setVisible(false);
	}

	private void createGravestoneBlockOnPlace(Player player, ItemStack gravestone, ItemFrame itemFrame) {

		itemFrame.getPersistentDataContainer().set(gravestoneKey, PersistentDataType.STRING, player.getUniqueId().toString());

		itemFrame.setFixed(true);
		itemFrame.setFacingDirection(BlockFace.UP);
		itemFrame.setInvulnerable(true);
		itemFrame.setItem(gravestone);
		itemFrame.setSilent(true);
		itemFrame.setVisible(false);
	}
}
