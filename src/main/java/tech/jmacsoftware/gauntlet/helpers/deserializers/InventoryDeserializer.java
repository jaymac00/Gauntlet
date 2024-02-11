package tech.jmacsoftware.gauntlet.helpers.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import tech.jmacsoftware.gauntlet.Gauntlet;

import java.io.IOException;

public class InventoryDeserializer extends StdDeserializer<Inventory> {

	private Plugin plugin = Gauntlet.getPlugin(Gauntlet.class);

	public InventoryDeserializer() {
		this(null);
	}

	public InventoryDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public Inventory deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
			throws IOException {

		JsonNode node = jsonParser.readValueAsTree();
		int size = node.get("size").asInt();
		Inventory inventory = plugin.getServer().createInventory(null, size);

		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(ItemStack.class, new ItemStackDeserializer());
		mapper.registerModule(module);

		node.get("contents").forEach(itemStack -> {
			inventory.addItem(mapper.convertValue(itemStack, ItemStack.class));
		});

		return inventory;
	}
}
