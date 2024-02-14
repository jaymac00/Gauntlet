package tech.jmacsoftware.gauntlet.helpers.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import tech.jmacsoftware.gauntlet.Gauntlet;

import java.io.IOException;

public class ItemStackSerializer extends StdSerializer<ItemStack> {

	private Plugin plugin = Gauntlet.getPlugin(Gauntlet.class);

	public ItemStackSerializer() {
		this(null);
	}

	public ItemStackSerializer(Class<ItemStack> t) {
		super(t);
	}

	@Override
	public void serialize(ItemStack itemStack, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException {

		jsonGenerator.writeStartObject();
		jsonGenerator.writeStringField("type", itemStack.getType().name());
		jsonGenerator.writeNumberField("amount", itemStack.getAmount());

		if (itemStack.hasItemMeta()) {
			jsonGenerator.writeObjectField("itemMeta", itemStack.getItemMeta());
		}

		jsonGenerator.writeEndObject();
	}
}
