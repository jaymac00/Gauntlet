package tech.jmacsoftware.gauntlet.items;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;
import tech.jmacsoftware.gauntlet.Gauntlet;

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
