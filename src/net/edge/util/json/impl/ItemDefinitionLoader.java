package net.edge.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.edge.util.json.JsonLoader;
import net.edge.content.container.impl.EquipmentType;
import net.edge.world.node.item.ItemDefinition;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.Arrays;
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
		super("./data/json/items/item_definitions.json");
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
	
	public void dump() {
		JSONArray all = new JSONArray();
		for(ItemDefinition d : ItemDefinition.DEFINITIONS) {
			JSONObject o = new JSONObject();
			o.put("id", d.getId());
			o.put("name", d.getName());
			o.put("tradeable", d.isTradable());
			o.put("stackable", d.isStackable());
			o.put("weapon", d.isWeapon());
			o.put("twoHanded", d.isTwoHanded());
			o.put("noted", d.isNoted());
			o.put("lended", d.isLended());
			o.put("alchable", d.isAlchable());
			o.put("noteId", d.getNoted());
			o.put("lendId", d.getLend());
			o.put("weight", d.getWeight());
			o.put("highAlch", d.getHighAlchValue());
			o.put("lowAlch", d.getLowAlchValue());
			o.put("equip", d.getEquipmentType() + "");
			JSONArray inv = new JSONArray();
			JSONArray gr = new JSONArray();
			JSONArray bon = new JSONArray();
			if(d.getInventoryActions() != null) {
				for(String b : d.getInventoryActions()) {
					inv.add(b);
				}
				o.put("inv", inv);
			}
			if(d.getGroundActions() != null) {
				for(String b : d.getGroundActions()) {
					gr.add(b);
				}
				o.put("ground", gr);
			}
			if(d.getBonus() != null) {
				for(double b : d.getBonus()) {
					bon.add(b);
				}
				o.put("bonus", bon);
			}
			all.add(o);
		}
		try (FileWriter file = new FileWriter("./idefs.json")) {
			
			file.write(all.toJSONString());
			file.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}