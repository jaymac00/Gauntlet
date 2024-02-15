package tech.jmacsoftware.gauntlet.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.block.Lectern;
import org.bukkit.block.data.type.Slab;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Consumer;

import tech.jmacsoftware.gauntlet.Gauntlet;
import tech.jmacsoftware.gauntlet.enums.CustomBlocks;
import tech.jmacsoftware.gauntlet.enums.CustomItems;
import tech.jmacsoftware.gauntlet.enums.InventorySizes;
import tech.jmacsoftware.gauntlet.items.BlockDrops;
import tech.jmacsoftware.gauntlet.items.EntityDrops;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class GraveEvents implements Listener {

	private Plugin plugin = Gauntlet.getPlugin(Gauntlet.class);

	private final NamespacedKey namespaced_key = new NamespacedKey(plugin, CustomItems.PLAYER_HEAD.getKey());

	public static HashMap<String, Inventory> GRAVES = new HashMap<>();

	private static final Set<Material> PICKAXES = Set.of(Material.WOODEN_PICKAXE, Material.STONE_PICKAXE,
			Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE);

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {

		Player player = event.getEntity();
		World world = player.getWorld();
		Location location = player.getLocation();
		if (validateLocation(world.getEnvironment(), location)) {

			Inventory inventory = plugin.getServer().createInventory(null,
					InventorySizes.GRAVE.getSize(), player.getName() + "\'s Grave");
			for (ItemStack item : event.getDrops()) {
				inventory.addItem(item);
			}
			GRAVES.put(player.getUniqueId().toString(), inventory);
			event.getDrops().clear();

			world.getEntitiesByClass(ArmorStand.class).stream()
					.filter(armorStand -> armorStand.getEyeLocation().getBlock().getLocation().equals(location.getBlock().getLocation())
							&& armorStand.getPersistentDataContainer().has(namespaced_key, PersistentDataType.STRING))
					.forEach(ArmorStand::remove);

			Consumer<ArmorStand> armorStandConsumer = armorStand -> createArmorStand(player, armorStand);
			world.spawn(new Location(world, location.getBlockX(), location.getBlockY(), location.getBlockZ(),
					location.getYaw(), location.getPitch()).add(0.5, -0.225, 0.5), ArmorStand.class, armorStandConsumer);
		}
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {

		Player player = event.getPlayer();
		World world = player.getWorld();
		Location location = player.getLastDeathLocation();
		if (event.getRespawnReason().equals(PlayerRespawnEvent.RespawnReason.DEATH)
				&& validateLocation(world.getEnvironment(), location)) {

			Block block = world.getBlockAt(location);
			switch (block.getType()) {
				case CHEST, TRAPPED_CHEST -> ((Chest) block.getState()).getBlockInventory()
						.forEach(itemStack -> world.dropItemNaturally(location, itemStack));
				case HOPPER -> ((Hopper) block.getState()).getInventory()
						.forEach(itemStack -> world.dropItemNaturally(location, itemStack));
				case LECTERN -> ((Lectern) block.getState()).getInventory()
						.forEach(itemStack -> world.dropItemNaturally(location, itemStack));
				case RED_NETHER_BRICK_SLAB -> {
					Collection<ItemFrame> itemFrames = world.getEntitiesByClass(ItemFrame.class).stream()
							.filter(itemFrame -> itemFrame.getLocation().getBlock().getLocation().equals(location.getBlock().getLocation())).toList();
					if (!itemFrames.isEmpty()) {

						ItemFrame itemFrame = itemFrames.stream().findAny().get();
						world.dropItemNaturally(location, itemFrame.getItem());

						String uuid = itemFrame.getPersistentDataContainer().get(namespaced_key, PersistentDataType.STRING);
						OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(uuid != null ? uuid : ""));
						Inventory inventory = GRAVES.get(uuid);
						if (uuid != null && !uuid.equals(player.getUniqueId().toString())
								&& inventory != null && location.equals(offlinePlayer.getLastDeathLocation())) {
							for (ItemStack itemStack : inventory) {
								if (itemStack != null) {
									world.dropItemNaturally(location, itemStack);
								}
							}
							GRAVES.remove(uuid);
						}

						itemFrames.forEach(ItemFrame::remove);
						world.setType(location, Material.AIR);
					}
				}
			}
			block.getDrops().forEach(itemStack -> world.dropItemNaturally(location, itemStack));
			world.setType(location, Material.RED_NETHER_BRICK_SLAB);

			Consumer<ItemFrame> itemFrameConsumer = itemFrame -> createItemFrame(player, itemFrame, BlockDrops.gravestone());
			world.spawn(location, ItemFrame.class, itemFrameConsumer);
		}
	}

	@EventHandler
	public void onRetrieve(PlayerInteractEvent event) {

		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
				&& !player.isSneaking()
				&& block.getType().equals(Material.RED_NETHER_BRICK_SLAB)) {

			World world = player.getWorld();
			Collection<ItemFrame> itemFrames = world.getEntitiesByClass(ItemFrame.class).stream()
					.filter(itemFrame -> itemFrame.getLocation().getBlock().getLocation().equals(block.getLocation())
							&& itemFrame.getPersistentDataContainer().has(namespaced_key, PersistentDataType.STRING)).toList();
			if (!itemFrames.isEmpty()) {

				String uuid = itemFrames.stream().findAny().get().getPersistentDataContainer().get(
						namespaced_key, PersistentDataType.STRING);
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(uuid != null ? uuid : ""));
				Inventory inventory = GRAVES.get(uuid);
				if (inventory != null && block.getLocation().equals(offlinePlayer.getLastDeathLocation())) {
					event.setUseItemInHand(Event.Result.DENY);
					player.openInventory(inventory);
				}
			}
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {

		Block block = event.getBlock();
		Player player = event.getPlayer();
		if (block.getType().equals(Material.RED_NETHER_BRICK_SLAB)) {

			World world = block.getWorld();
			Location location = block.getLocation();
			Collection<ItemFrame> itemFrames = world.getEntitiesByClass(ItemFrame.class).stream()
					.filter(itemFrame -> itemFrame.getLocation().getBlock().getLocation().equals(location)).toList();
			if (!itemFrames.isEmpty()) {

				ItemFrame itemFrame = itemFrames.stream().findAny().get();
				ItemStack tool = player.getInventory().getItemInMainHand();
				if (tool != null && PICKAXES.contains(tool.getType())) {
					world.dropItemNaturally(location, itemFrame.getItem());
				}

				String uuid = itemFrame.getPersistentDataContainer().get(namespaced_key, PersistentDataType.STRING);
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(uuid != null ? uuid : ""));
				Inventory inventory = GRAVES.get(uuid);
				if (inventory != null && location.equals(offlinePlayer.getLastDeathLocation())) {
					for (ItemStack itemStack : inventory) {
						if (itemStack != null) {
							world.dropItemNaturally(location, itemStack);
						}
					}
					GRAVES.remove(uuid);
				}

				itemFrames.forEach(ItemFrame::remove);
				event.setDropItems(false);

				world.getEntitiesByClass(ArmorStand.class).stream()
						.filter(armorStand -> armorStand.getEyeLocation().getBlock().getLocation().equals(location)
								&& armorStand.getPersistentDataContainer().has(namespaced_key, PersistentDataType.STRING))
						.forEach(ArmorStand::remove);
			}
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {

		ItemStack itemStack = event.getItemInHand();
		Block blockPlaced = event.getBlockPlaced();
		Block blockAgainst = event.getBlockAgainst();
		World world = blockPlaced.getWorld();
		Location location = blockPlaced.getLocation();
		if (itemStack.hasItemMeta()
				&& itemStack.getItemMeta().hasLocalizedName()
				&& itemStack.getItemMeta().getLocalizedName().equals(CustomBlocks.GRAVESTONE.getName())) {

			Slab slab = (Slab) blockPlaced.getBlockData();
			Player player = event.getPlayer();
			ItemStack clone = itemStack.clone();
			if (slab.getType().equals(Slab.Type.DOUBLE)) {
				event.setCancelled(true);

				if (blockPlaced.equals(blockAgainst)) {
					if (player.getLocation().getPitch() < 0.0
							&& location.add(0.0, -1.0, 0.0).getBlock().getType().isAir()) {

						world.setType(location, blockPlaced.getType());
						itemStack.setAmount(itemStack.getAmount() - 1);
					} else if (location.add(0.0, 1.0, 0.0).getBlock().getType().isAir()) {
						world.setType(location, blockPlaced.getType());
						itemStack.setAmount(itemStack.getAmount() - 1);
					}

					Consumer<ItemFrame> itemFrameConsumer = itemFrame -> createItemFrame(player, itemFrame, clone);
					world.spawn(location, ItemFrame.class, itemFrameConsumer);
				}
				return;
			}
			slab.setType(Slab.Type.BOTTOM);
			blockPlaced.setBlockData(slab);

			Consumer<ItemFrame> itemFrameConsumer = itemFrame -> createItemFrame(player, itemFrame, clone);
			world.spawn(location, ItemFrame.class, itemFrameConsumer);

		} else if (itemStack.getType().equals(Material.RED_NETHER_BRICK_SLAB)) {
			if (!world.getEntitiesByClass(ItemFrame.class).stream()
					.filter(itemFrame -> itemFrame.getLocation().getBlock().getLocation().equals(location))
					.toList().isEmpty()) {

				Slab slab = (Slab) blockPlaced.getBlockData();
				if (slab.getType().equals(Slab.Type.DOUBLE)) {
					event.setCancelled(true);

					if (blockPlaced.equals(blockAgainst)
							&& location.add(0.0, 1.0, 0.0).getBlock().getType().isAir()) {

						world.setType(location, blockPlaced.getType());
						itemStack.setAmount(itemStack.getAmount() - 1);
					}
				}
			}
		}
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {

		event.blockList().forEach(block -> {
			if (block.getType().equals(Material.RED_NETHER_BRICK_SLAB)) {

				World world = block.getWorld();
				Location location = block.getLocation();
				Collection<ItemFrame> itemFrames = world.getEntitiesByClass(ItemFrame.class).stream()
						.filter(itemFrame -> itemFrame.getLocation().getBlock().getLocation().equals(location)).toList();
				if (!itemFrames.isEmpty()) {

					ItemFrame itemFrame = itemFrames.stream().findAny().get();
					world.dropItemNaturally(location, itemFrame.getItem());

					String uuid = itemFrame.getPersistentDataContainer().get(namespaced_key, PersistentDataType.STRING);
					OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(uuid != null ? uuid : ""));
					Inventory inventory = GRAVES.get(uuid);
					if (inventory != null && location.equals(offlinePlayer.getLastDeathLocation())) {
						for (ItemStack itemStack : inventory) {
							if (itemStack != null) {
								world.dropItemNaturally(location, itemStack);
							}
						}
						GRAVES.remove(uuid);
					}

					itemFrames.forEach(ItemFrame::remove);
					world.setType(location, Material.AIR);

					world.getEntitiesByClass(ArmorStand.class).stream()
							.filter(armorStand -> armorStand.getEyeLocation().getBlock().getLocation().equals(location)
									&& armorStand.getPersistentDataContainer().has(namespaced_key, PersistentDataType.STRING))
							.forEach(ArmorStand::remove);
				}
			}
		});
	}

	@EventHandler
	public void onBlockExplode(BlockExplodeEvent event) {

		event.blockList().forEach(block -> {
			if (block.getType().equals(Material.RED_NETHER_BRICK_SLAB)) {

				World world = block.getWorld();
				Location location = block.getLocation();
				Collection<ItemFrame> itemFrames = world.getEntitiesByClass(ItemFrame.class).stream()
						.filter(itemFrame -> itemFrame.getLocation().getBlock().getLocation().equals(location)).toList();
				if (!itemFrames.isEmpty()) {

					ItemFrame itemFrame = itemFrames.stream().findAny().get();
					world.dropItemNaturally(location, itemFrame.getItem());

					String uuid = itemFrame.getPersistentDataContainer().get(namespaced_key, PersistentDataType.STRING);
					OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(uuid != null ? uuid : ""));
					Inventory inventory = GRAVES.get(uuid);
					if (inventory != null && location.equals(offlinePlayer.getLastDeathLocation())) {
						for (ItemStack itemStack : inventory) {
							if (itemStack != null) {
								world.dropItemNaturally(location, itemStack);
							}
						}
						GRAVES.remove(uuid);
					}

					itemFrames.forEach(ItemFrame::remove);
					world.setType(location, Material.AIR);

					world.getEntitiesByClass(ArmorStand.class).stream()
							.filter(armorStand -> armorStand.getEyeLocation().getBlock().getLocation().equals(location)
									&& armorStand.getPersistentDataContainer().has(namespaced_key, PersistentDataType.STRING))
							.forEach(ArmorStand::remove);
				}
			}
		});
	}

	@EventHandler
	public void onPush(BlockPistonExtendEvent event) {
		Collection<ItemFrame> itemFrames = event.getBlock().getWorld().getEntitiesByClass(ItemFrame.class);
		if (!event.getBlocks().stream()
				.filter(block -> !itemFrames.stream()
						.filter(itemFrame -> itemFrame.getPersistentDataContainer().has(namespaced_key, PersistentDataType.STRING)
								&& itemFrame.getLocation().getBlock().getLocation()
								.equals(block.getLocation()))
						.toList().isEmpty())
				.toList().isEmpty()) {

			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPull(BlockPistonRetractEvent event) {
		Collection<ItemFrame> itemFrames = event.getBlock().getWorld().getEntitiesByClass(ItemFrame.class);
		if (event.isSticky()
				&& !event.getBlocks().stream()
				.filter(block -> !itemFrames.stream()
						.filter(itemFrame -> itemFrame.getPersistentDataContainer().has(namespaced_key, PersistentDataType.STRING)
								&& itemFrame.getLocation().getBlock().getLocation()
								.equals(block.getLocation()))
						.toList().isEmpty())
				.toList().isEmpty()) {

			event.setCancelled(true);
		}
	}

	private boolean validateLocation(World.Environment environment, Location location) {
		if (environment.equals(World.Environment.NORMAL)
				&& (location.getY() > -64.0 && location.getY() < 320.0)) {
			return true;
		}
		if ((environment.equals(World.Environment.NETHER) || environment.equals(World.Environment.THE_END))
				&& (location.getY() > 0.0 && location.getY() < 256.0)) {
			return true;
		}
		return false;
	}

	private void createItemFrame(Player player, ItemFrame itemFrame, ItemStack itemStack) {

		itemFrame.getPersistentDataContainer().set(namespaced_key, PersistentDataType.STRING, player.getUniqueId().toString());

		itemFrame.setFixed(true);
		itemFrame.setFacingDirection(BlockFace.UP);
		itemFrame.setInvulnerable(true);
		itemFrame.setItem(itemStack);
		itemFrame.setSilent(true);
		itemFrame.setVisible(false);
	}

	private void createArmorStand(Player player, ArmorStand armorStand) {

		armorStand.getPersistentDataContainer().set(namespaced_key, PersistentDataType.STRING, player.getUniqueId().toString());

		armorStand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
		armorStand.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING);
		armorStand.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.ADDING);
		armorStand.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.ADDING);

		armorStand.getEquipment().setHelmet(EntityDrops.playerHead(player, player.getKiller()));

		armorStand.setGravity(false);
		armorStand.setInvulnerable(true);
		armorStand.setSilent(true);
		armorStand.setSmall(true);
		armorStand.setVisible(false);
	}
}
