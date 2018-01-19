package net.arrav.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.arrav.util.json.JsonLoader;
import net.arrav.world.entity.item.ItemDefinition;
import net.arrav.world.entity.item.container.impl.EquipmentType;

import java.util.Objects;

/**
 * The {@link JsonLoader} implementation that loads all item definitions.
 * @author lare96 <http://github.com/lare96>
 * @author Artem Batutin<artembatutin@gmail.com>
 */
public final class ItemDefinitionLoader extends JsonLoader {
	
	/**
	 * Creates a new {@link ItemDefinitionLoader}.
	 */
	public ItemDefinitionLoader() {
		super("./data/def/item/item_definitions.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		int index = reader.get("id").getAsInt();
		String name = Objects.requireNonNull(reader.get("name").getAsString());
		boolean tradeable = reader.get("tradeable").getAsBoolean();
		boolean stackable = reader.get("stackable").getAsBoolean();
		boolean weapon = reader.get("weapon").getAsBoolean();
		boolean twoHanded = reader.get("twoHanded").getAsBoolean();
		boolean noted = reader.get("noted").getAsBoolean();
		boolean lended = reader.get("lended").getAsBoolean();
		boolean alchable = reader.get("alchable").getAsBoolean();
		int noteId = reader.get("noteId").getAsInt();
		int lendId = reader.get("lendId").getAsInt();
		double weight = reader.get("weight").getAsDouble();
		int highAlchValue = reader.get("highAlch").getAsInt();
		int lowAlchValue = reader.get("lowAlch").getAsInt();
		EquipmentType equipmentType = Objects.requireNonNull(builder.fromJson(reader.get("equip"), EquipmentType.class));
		String[] inventoryActions = null;
		if(reader.has("inv"))
			inventoryActions = builder.fromJson(reader.get("inv").getAsJsonArray(), String[].class);
		String[] groundActions = null;
		if(reader.has("ground"))
			groundActions = builder.fromJson(reader.get("ground").getAsJsonArray(), String[].class);
		int[] bonus = null;
		if(reader.has("bonus"))
			bonus = builder.fromJson(reader.get("bonus").getAsJsonArray(), int[].class);
		ItemDefinition.DEFINITIONS[index] = new ItemDefinition(index, name, equipmentType, tradeable, weapon, twoHanded, stackable, alchable, noted, noteId, lended, lendId, lowAlchValue, highAlchValue, weight, bonus, inventoryActions, groundActions);
	}
	
}