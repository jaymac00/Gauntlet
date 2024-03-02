package tech.jmacsoftware.gauntlet.helpers;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import tech.jmacsoftware.gauntlet.events.GraveEvents;
import tech.jmacsoftware.gauntlet.helpers.deserializers.InventoryDeserializer;
import tech.jmacsoftware.gauntlet.helpers.serializers.InventorySerializer;
import tech.jmacsoftware.gauntlet.helpers.serializers.ItemMetaSerializer;
import tech.jmacsoftware.gauntlet.helpers.serializers.ItemStackSerializer;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class GravesHelper {

    private static final String FILE_NAME = "plugins/Gauntlet/resources/dynamic/inventories/graves.json";

    public static void loadGraves(Plugin plugin) {
        File graves = new File(FILE_NAME);
        if (graves.isFile()) {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(Inventory.class, new InventoryDeserializer());
            mapper.registerModule(module);
            try {
                JsonParser jsonParser = mapper.createParser(graves);
                JsonNode node = jsonParser.readValueAsTree();

                node.fields().forEachRemaining(entry -> {
                    try {
                        Inventory inventory = mapper.convertValue(entry.getValue(), Inventory.class);
                        OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(entry.getKey()));
                        Inventory grave = plugin.getServer().createInventory(null, inventory.getSize(), player.getName() + "\'s Grave");
                        grave.setContents(inventory.getContents());
                        GraveEvents.GRAVES.put(entry.getKey(), grave);
                    } catch (IllegalArgumentException e) {
                        plugin.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "[Gauntlet.GraveHelper_loadGraves] (key=" + entry.getKey() + ") " + ChatColor.RESET + e);
                    }
                });

                jsonParser.close();
            } catch (IOException e) {
	            plugin.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "[Gauntlet.GraveHelper_loadGraves] " + ChatColor.RESET + e);
            }
        }
    }

    public static void saveGraves(Plugin plugin) {
        File graves = new File(FILE_NAME);
        if (!graves.isFile()) {
            try {
                graves.getParentFile().mkdirs();
                graves.createNewFile();
            } catch (IOException e) {
                plugin.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "[Gauntlet.GraveHelper_saveGraves] " + ChatColor.RESET + e);
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Inventory.class, new InventorySerializer());
        module.addSerializer(ItemStack.class, new ItemStackSerializer());
        module.addSerializer(ItemMeta.class, new ItemMetaSerializer());
        mapper.registerModule(module);
        try {
            JsonGenerator jsonGenerator = mapper.createGenerator(graves, JsonEncoding.UTF8);
            jsonGenerator.writeStartObject();
            GraveEvents.GRAVES.forEach((key, inventory) -> {
                try {
                    jsonGenerator.writeObjectField(key, inventory);
                } catch (IOException e) {
                    plugin.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "[Gauntlet.GraveHelper_saveGraves] (key=" + key + ") " + ChatColor.RESET + e);
                }
            });
            jsonGenerator.writeEndObject();
            jsonGenerator.close();
        } catch (IOException e) {
            plugin.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "[Gauntlet.GraveHelper_saveGraves] " + ChatColor.RESET + e);
        }
    }
}
