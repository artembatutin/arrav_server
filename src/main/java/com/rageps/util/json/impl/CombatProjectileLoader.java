package com.rageps.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rageps.world.Animation;
import com.rageps.world.Graphic;
import com.rageps.world.entity.actor.combat.CombatImpact;
import com.rageps.world.entity.actor.combat.magic.MagicImpact;
import com.rageps.world.entity.actor.combat.projectile.CombatProjectile;
import com.rageps.world.entity.actor.combat.projectile.ProjectileBuilder;
import com.rageps.world.entity.actor.combat.ranged.RangedEffects;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import com.rageps.util.json.JsonLoader;

/**
 * The {@link JsonLoader} implementation that loads all combat projectile
 * definitions.
 * @author Artem Batutin
 */
public final class CombatProjectileLoader extends JsonLoader {

	/**
	 * Constructs a new {@link CombatProjectileLoader}.
	 */
	public CombatProjectileLoader() {
		super("./data/def/combat/projectile_definitions.json");
	}

	@Override
	protected void initialize(int size) {
		if(CombatProjectile.DEFINITIONS == null) {
			CombatProjectile.DEFINITIONS = new Object2ObjectArrayMap<>(size);
		}
	}

	@Override
	public void load(JsonObject reader, Gson builder) {
		String name = reader.get("name").getAsString();

		int hitDelay = -1;
		if(reader.has("hit-delay")) {
			hitDelay = reader.get("hit-delay").getAsInt();
		}

		int hitsplatDelay = -1;
		if(reader.has("hitsplat-delay")) {
			hitsplatDelay = reader.get("hitsplat-delay").getAsInt();
		}

		int maxHit = 0;
		if(reader.has("max-hit")) {
			maxHit = reader.get("max-hit").getAsInt();
		}

		CombatImpact effect = null;
		if(reader.has("magic-effect")) {
			String effectName = reader.get("magic-effect").getAsString();
			effect = MagicImpact.valueOf(effectName).getEffect();
		} else if(reader.has("ranged-effect")) {
			String effectName = reader.get("ranged-effect").getAsString();
			effect = RangedEffects.valueOf(effectName).getEffect();
		}

		Animation animation = null;
		if(reader.has("animation")) {
			animation = builder.fromJson(reader.get("animation"), Animation.class);
		}

		Graphic start = null;
		if(reader.has("start")) {
			start = builder.fromJson(reader.get("start"), Graphic.class);
		}

		Graphic end = null;
		if(reader.has("end")) {
			end = builder.fromJson(reader.get("end"), Graphic.class);
		}

		ProjectileBuilder projectile = null;
		if(reader.has("projectile")) {
			projectile = builder.fromJson(reader.get("projectile"), ProjectileBuilder.class);
		}

		CombatProjectile.DEFINITIONS.put(name, new CombatProjectile(name, maxHit, effect, animation, start, end, projectile, hitDelay, hitsplatDelay));
	}
}
