package tech.jmacsoftware.gauntlet.items;

import com.google.common.collect.Multimap;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import tech.jmacsoftware.gauntlet.Gauntlet;
import tech.jmacsoftware.gauntlet.enums.CustomItems;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemRecipes implements Listener {

    private Plugin plugin = Gauntlet.getPlugin(Gauntlet.class);


    public void craftSaddle() {

        ItemStack item = new ItemStack(Material.SADDLE, 1);

        ShapedRecipe saddle = new ShapedRecipe(Material.SADDLE.getKey(), item);
        saddle.shape("LLL", "S S", "I I");
        saddle.setIngredient('L', Material.LEATHER);
        saddle.setIngredient('S', Material.STRING);
        saddle.setIngredient('I', Material.IRON_INGOT);

        plugin.getServer().addRecipe(saddle);
    }

    public void craftDHorseArmor() {

        ItemStack item = new ItemStack(Material.DIAMOND_HORSE_ARMOR, 1);

        ShapedRecipe diaHorseArmor = new ShapedRecipe(Material.DIAMOND_HORSE_ARMOR.getKey(), item);
        diaHorseArmor.shape("  D", "DYD", "D D");
        diaHorseArmor.setIngredient('D', Material.DIAMOND);
        diaHorseArmor.setIngredient('Y', Material.YELLOW_WOOL);

        plugin.getServer().addRecipe(diaHorseArmor);
    }

    public void craftGHorseArmor() {

        ItemStack item = new ItemStack(Material.GOLDEN_HORSE_ARMOR, 1);

        ShapedRecipe goldHorseArmor = new ShapedRecipe(Material.GOLDEN_HORSE_ARMOR.getKey(), item);
        goldHorseArmor.shape("  G", "GBG", "G G");
        goldHorseArmor.setIngredient('G', Material.GOLD_INGOT);
        goldHorseArmor.setIngredient('B', Material.BROWN_WOOL);

        plugin.getServer().addRecipe(goldHorseArmor);
    }

    public void craftIHorseArmor() {

        ItemStack item = new ItemStack(Material.IRON_HORSE_ARMOR, 1);

        ShapedRecipe ironHorseArmor = new ShapedRecipe(Material.IRON_HORSE_ARMOR.getKey(), item);
        ironHorseArmor.shape("  I", "IBI", "I I");
        ironHorseArmor.setIngredient('I', Material.IRON_INGOT);
        ironHorseArmor.setIngredient('B', Material.BLACK_WOOL);

        plugin.getServer().addRecipe(ironHorseArmor);
    }
}
