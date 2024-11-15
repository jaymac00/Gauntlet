package tech.jmacsoftware.gauntlet.helpers.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.bukkit.ChatColor;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

import tech.jmacsoftware.gauntlet.Gauntlet;

import java.io.IOException;

public class ItemMetaSerializer extends StdSerializer<ItemMeta> {

	private Plugin plugin = Gauntlet.getPlugin(Gauntlet.class);

	public ItemMetaSerializer() {
		this(null);
	}

	public ItemMetaSerializer(Class<ItemMeta> t) {
		super(t);
	}

	@Override
	public void serialize(ItemMeta itemMeta, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException {

		jsonGenerator.writeStartObject();
		if (itemMeta.hasCustomModelData()) {
			jsonGenerator.writeNumberField("customModelData", itemMeta.getCustomModelData());
		}
		if (itemMeta.hasDisplayName()) {
			jsonGenerator.writeStringField("displayName", itemMeta.getDisplayName());
		}
		if (itemMeta.hasItemName()) {
			jsonGenerator.writeStringField("itemName", itemMeta.getItemName());
		}

		if (itemMeta.hasAttributeModifiers()) {
			jsonGenerator.writeObjectFieldStart("attributes");
			itemMeta.getAttributeModifiers().asMap().forEach((attribute, attributeModifiers) -> {
				try {
					jsonGenerator.writeArrayFieldStart(attribute.name());
					attributeModifiers.forEach(attributeModifier -> {
						try {
							jsonGenerator.writeObject(attributeModifier);
						} catch (IOException e) {
							plugin.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "[Gauntlet.ItemMetaSerializer_attributes] " + ChatColor.RESET + e);
						}
					});
					jsonGenerator.writeEndArray();
				} catch (IOException e) {
					plugin.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "[Gauntlet.ItemMetaSerializer_attributes] " + ChatColor.RESET + e);
				}
			});
			jsonGenerator.writeEndObject();
		}

		if (itemMeta.hasEnchants()) {
			jsonGenerator.writeObjectFieldStart("enchantments");
			itemMeta.getEnchants().forEach((enchantment, level) -> {
				try {
					jsonGenerator.writeNumberField(enchantment.getKey().getKey(), level);
				} catch (IOException e) {
					plugin.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "[Gauntlet.ItemMetaSerializer_enchantments] " + ChatColor.RESET + e);
				}
			});
			jsonGenerator.writeEndObject();
		}

		if (!itemMeta.getItemFlags().isEmpty()) {
			jsonGenerator.writeArrayFieldStart("itemFlags");
			itemMeta.getItemFlags().forEach(itemFlag -> {
				try {
					jsonGenerator.writeString(itemFlag.name());
				} catch (IOException e) {
					plugin.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "[Gauntlet.ItemMetaSerializer_itemFlags] " + ChatColor.RESET + e);
				}
			});
			jsonGenerator.writeEndArray();
		}

		if (itemMeta.hasLore()) {
			jsonGenerator.writeArrayFieldStart("lore");
			itemMeta.getLore().forEach(string -> {
				try {
					jsonGenerator.writeString(string);
				} catch (IOException e) {
					plugin.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "[Gauntlet.ItemMetaSerializer_lore] " + ChatColor.RESET + e);
				}
			});
			jsonGenerator.writeEndArray();
		}

		try {
			SkullMeta skullMeta = (SkullMeta) itemMeta;
			jsonGenerator.writeStringField("skullOwner", skullMeta.getOwningPlayer().getUniqueId().toString());
		} catch (Exception ignored) {}

		jsonGenerator.writeEndObject();
	}
}
