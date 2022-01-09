package com.rageps.util.json.impl;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rageps.content.item.Requirement;
import com.rageps.content.skill.SkillData;
import com.rageps.util.json.JsonLoader;

import java.util.Objects;

/**
 * The {@link JsonLoader} implementation that loads all equipment level
 * requirements.
 * @author lare96 <http://github.com/lare96>
 */
public final class EquipmentRequirementLoader extends JsonLoader {
	
	/**
	 * Creates a new {@link EquipmentRequirementLoader}.
	 */
	public EquipmentRequirementLoader() {
		super("./data/def/combat/level_requirements.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		int[] ids = Objects.requireNonNull(builder.fromJson(reader.get("id"), int[].class));
		int[] skills = Objects.requireNonNull(builder.fromJson(reader.get("levelId"), int[].class));
		int[] levels = Objects.requireNonNull(builder.fromJson(reader.get("requiredLevel"), int[].class));
		Requirement[] requirements = new Requirement[skills.length];
		Preconditions.checkState(requirements.length > 0);
		for(int skill = 0; skill < skills.length; skill++) {
			requirements[skill] = new Requirement(levels[skill], SkillData.values()[skills[skill]]);
		}
		for(int id : ids) {
			Requirement.REQUIREMENTS.put(id, requirements);
		}
	}
}
