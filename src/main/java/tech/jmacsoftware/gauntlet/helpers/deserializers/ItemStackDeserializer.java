package tech.jmacsoftware.gauntlet.helpers.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

import tech.jmacsoftware.gauntlet.Gauntlet;

import java.io.IOException;
import java.util.UUID;

public class ItemStackDeserializer extends StdDeserializer<ItemStack> {

	private Plugin plugin = Gauntlet.getPlugin(Gauntlet.class);

	public ItemStackDeserializer() {
		this(null);
	}

	public ItemStackDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public ItemStack deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
			throws IOException {

		JsonNode node = jsonParser.readValueAsTree();
		ItemStack itemStack = new ItemStack(Material.getMaterial(node.get("type").asText()));
		itemStack.setAmount(node.get("amount").asInt());

		if (node.has("itemMeta")) {
			ObjectMapper mapper = new ObjectMapper();
			SimpleModule module = new SimpleModule();
			module.addDeserializer(ItemMeta.class, new ItemMetaDeserializer());
			mapper.registerModule(module);
			itemStack.setItemMeta(mapper.convertValue(node.get("itemMeta"), ItemMeta.class));

			if (node.get("itemMeta").has("skullOwner")) {
				SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
				skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(node.get("itemMeta").get("skullOwner").asText())));
				itemStack.setItemMeta(skullMeta);
			}
		}

		return itemStack;
	}
}
