package tech.jmacsoftware.gauntlet.helpers.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import tech.jmacsoftware.gauntlet.Gauntlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class ItemMetaDeserializer extends StdDeserializer<ItemMeta> {

	private Plugin plugin = Gauntlet.getPlugin(Gauntlet.class);

	public ItemMetaDeserializer() {
		this(null);
	}

	public ItemMetaDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public ItemMeta deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
			throws IOException {

		JsonNode node = jsonParser.readValueAsTree();
		ItemStack itemStack = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);
		ItemMeta itemMeta = itemStack.getItemMeta();

		itemMeta.setCustomModelData(node.has("customModelData") ?
				node.get("customModelData").asInt() : null);
		itemMeta.setDisplayName(node.has("displayName") ?
				node.get("displayName").asText() : null);
		itemMeta.setLocalizedName(node.has("localizedName") ?
				node.get("localizedName").asText() : null);

		if (node.has("attributes")) {
			Multimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
			node.get("attributes").fields().forEachRemaining(entry -> {
				entry.getValue().forEach(attribute -> {
					AttributeModifier attributeModifier = new AttributeModifier(
							UUID.fromString(attribute.get("uniqueId").asText()), attribute.get("name").asText(),
							attribute.get("amount").asDouble(), AttributeModifier.Operation.valueOf(attribute.get("operation").asText()),
							EquipmentSlot.valueOf(attribute.get("slot").asText()));
					attributes.put(Attribute.valueOf(entry.getKey()), attributeModifier);
				});
			});
			itemMeta.setAttributeModifiers(attributes);
		}

		if (node.has("enchantments")) {
			node.get("enchantments").fields().forEachRemaining(entry -> {
				itemMeta.addEnchant(Registry.ENCHANTMENT.get(NamespacedKey.fromString(entry.getKey())),
						entry.getValue().asInt(), true);
			});
		}

		if (node.has("itemFlags")) {
			node.get("itemFlags").forEach(itemFlag -> {
				itemMeta.addItemFlags(ItemFlag.valueOf(itemFlag.asText()));
			});
		}

		if (node.has("lore")) {
			ArrayList<String> lore = new ArrayList<>();
			node.get("lore").forEach(string -> {
				lore.add(string.asText());
			});
			itemMeta.setLore(lore);
		}

		return itemMeta;
	}
}
