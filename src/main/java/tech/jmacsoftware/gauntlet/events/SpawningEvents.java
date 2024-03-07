package tech.jmacsoftware.gauntlet.events;

import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Collections;
import java.util.List;


public class SpawningEvents implements Listener {

    public int zombieKillCounter = 0;

    @EventHandler
    public void onZombieDeath(EntityDeathEvent event) {

        if (event.getEntity().getType() == EntityType.ZOMBIE) {
            zombieKillCounter++;
            System.out.println(zombieKillCounter);
            if (zombieKillCounter >= 51) {
                spawnZombieKing(event.getEntity().getLocation());
                zombieKillCounter = 0;
                System.out.println(zombieKillCounter);
            }

        }

    }

    public void spawnZombieKing(Location location) {

        Zombie zombieKing = (Zombie) location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
        zombieKing.setCustomName(ChatColor.RED + "Zombie King");
        zombieKing.setCustomNameVisible(true);
        zombieKing.setMaxHealth(100);
        zombieKing.setHealth(100);
        zombieKing.damage(10);



        ItemStack zKingCrown = new ItemStack(Material.GOLDEN_HELMET, 1);
        ItemMeta zKingCrownMeta = zKingCrown.getItemMeta();

        zKingCrownMeta.setDisplayName(ChatColor.GOLD + "Zombie King's Crown");
        zKingCrownMeta.setLore(Collections.singletonList("This Belongs to the Zombie King."));
        zKingCrown.setItemMeta(zKingCrownMeta);

        zombieKing.getEquipment().setHelmet(zKingCrown);

    }
}
