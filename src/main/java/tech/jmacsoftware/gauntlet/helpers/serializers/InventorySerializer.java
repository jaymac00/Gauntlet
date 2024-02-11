package tech.jmacsoftware.gauntlet.helpers.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import tech.jmacsoftware.gauntlet.Gauntlet;

import java.io.IOException;
import java.util.Arrays;

public class InventorySerializer extends StdSerializer<Inventory> {

	private Plugin plugin = Gauntlet.getPlugin(Gauntlet.class);

	public InventorySerializer() {
		this(null);
	}

	public InventorySerializer(Class<Inventory> t) {
		super(t);
	}

	@Override
	public void serialize(Inventory inventory, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException {

		jsonGenerator.writeStartObject();
		jsonGenerator.writeNumberField("size", inventory.getSize());

		jsonGenerator.writeArrayFieldStart("contents");
		Arrays.stream(inventory.getContents()).forEach(itemStack -> {
			try {
				if (itemStack != null) {
					jsonGenerator.writeObject(itemStack);
				}
			} catch (IOException e) {
				plugin.getServer().getConsoleSender().sendMessage("[Gauntlet.InventorySerializer_contents] " + e);
			}
		});
		jsonGenerator.writeEndArray();

		jsonGenerator.writeEndObject();
	}
}
