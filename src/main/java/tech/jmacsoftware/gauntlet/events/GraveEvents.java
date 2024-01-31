package tech.jmacsoftware.gauntlet.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class GraveEvents implements Listener {

	private Plugin plugin = Gauntlet.getPlugin(Gauntlet.class);

	private final NamespacedKey namespaced_key = new NamespacedKey(plugin, CustomItems.PLAYER_HEAD.getKey());

	public static HashMap<String, Inventory> GRAVES = new HashMap<>();

	private static final Set<Material> PICKAXES = Set.of(Material.WOODEN_PICKAXE, Material.STONE_PICKAXE,
			Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE);

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {

		Player player = event.getEntity();
		Inventory inventory = plugin.getServer().createInventory(null,
				InventorySizes.GRAVE.getSize(), player.getName() + "\'s Grave");
		for (ItemStack item : event.getDrops()) {
			inventory.addItem(item);
		}
		GRAVES.put(player.getUniqueId().toString(), inventory);
		event.getDrops().clear();

		World world = player.getWorld();
		Location location = player.getLocation();
		world.setType(location, Material.RED_NETHER_BRICK_SLAB);
		Consumer<ItemFrame> itemFrameConsumer = itemFrame -> createItemFrame(player, itemFrame, BlockDrops.gravestone());
		world.spawn(location, ItemFrame.class, itemFrameConsumer);


//		Consumer<ArmorStand> armorStandConsumer = gravestone -> createGravestoneHead(player, gravestone);
//		world.spawn(new Location(world, playerLocation.getBlockX(), playerLocation.getBlockY(), playerLocation.getBlockZ(),
//				playerLocation.getYaw(), playerLocation.getPitch()).add(0.5, -0.225, 0.5), ArmorStand.class, armorStandConsumer);

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
					.filter(itemFrame -> itemFrame.getLocation().equals(block.getLocation())).toList();
			if (!itemFrames.isEmpty()) {

				String uuid = itemFrames.stream().findAny().get().getPersistentDataContainer().get(
						namespaced_key, PersistentDataType.STRING);
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
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
		ItemStack tool = player.getInventory().getItemInMainHand();
		if (block.getType().equals(Material.RED_NETHER_BRICK_SLAB)
				&& tool != null
				&& PICKAXES.contains(tool.getType())) {

			World world = block.getWorld();
			Location location = block.getLocation();
			Collection<ItemFrame> itemFrames = world.getEntitiesByClass(ItemFrame.class).stream()
					.filter(itemFrame -> itemFrame.getLocation().equals(location)).toList();
			if (!itemFrames.isEmpty()) {

				ItemFrame itemFrame = itemFrames.stream().findAny().get();
				world.dropItemNaturally(location, itemFrame.getItem());

				String uuid = itemFrame.getPersistentDataContainer().get(namespaced_key, PersistentDataType.STRING);
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
				Inventory inventory = GRAVES.get(uuid);
				if (inventory != null && location.equals(offlinePlayer.getLastDeathLocation())) {
					for (ItemStack itemStack : inventory) {
						if (itemStack != null) {
							world.dropItemNaturally(location, itemStack);
						}
					}
					GRAVES.remove(uuid);
				}

				itemFrames.forEach(Entity::remove);
				event.setDropItems(false);
			}
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {

		ItemStack itemStack = event.getItemInHand();
		if (itemStack.getType().equals(Material.RED_NETHER_BRICK_SLAB)
				&& itemStack.hasItemMeta()
				&& itemStack.getItemMeta().getLocalizedName().equals(CustomBlocks.GRAVESTONE.getName())) {

			ItemStack clone = itemStack.clone();
			Player player = event.getPlayer();
			Block block = event.getBlockPlaced();
			Consumer<ItemFrame> itemFrameConsumer = itemFrame -> createItemFrame(player, itemFrame, clone);
			player.getWorld().spawn(block.getLocation(), ItemFrame.class, itemFrameConsumer);
		}
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
}
