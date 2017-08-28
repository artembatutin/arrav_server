package net.edge.content.combat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.edge.content.combat.content.MagicEffects;
import net.edge.content.combat.content.RangedEffects;
import net.edge.util.json.JsonLoader;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.Projectile;
import net.edge.world.entity.actor.Actor;

import java.util.HashMap;
import java.util.Optional;
import java.util.OptionalInt;

public final class CombatProjectileDefinition {

	private final String name;
	private int maxHit;
	private CombatEffect effect;
	private Animation animation;
	private Graphic start;
	private Graphic end;
	private ProjectileBuilder projectile;

	private int hitDelay;
	private int hitsplatDelay;

	private CombatProjectileDefinition(String name) {
		this.name = name;
	}

	private static HashMap<String, CombatProjectileDefinition> DEFINITIONS;

	public String getName() {
		return name;
	}

	public int getMaxHit() {
		return maxHit;
	}

	public Optional<CombatEffect> getEffect() {
		return Optional.ofNullable(effect);
	}

	public Optional<Animation> getAnimation() {
		return Optional.ofNullable(animation);
	}

	public Optional<Graphic> getStart() {
		return Optional.ofNullable(start);
	}

	public Optional<Graphic> getEnd() {
		return Optional.ofNullable(end);
	}

	public OptionalInt getHitDelay() {
		if(hitDelay == -1) {
			return OptionalInt.empty();
		}
		return OptionalInt.of(hitDelay);
	}

	public OptionalInt getHitsplatDelay() {
		if(hitsplatDelay == -1) {
			return OptionalInt.empty();
		}
		return OptionalInt.of(hitsplatDelay);
	}

	public void sendProjectile(Actor attacker, Actor defender, boolean magic) {
		if(projectile != null) {
			projectile.send(attacker, defender, magic);
		}
	}

	public static CombatProjectileDefinition getDefinition(String name) {
		return DEFINITIONS.get(name);
	}

	public static JsonLoader createLoader() {
		return new JsonLoader("./data/def/combat/projectile_definitions.json") {

			@Override
			protected void initialize(int size) {
				if(DEFINITIONS == null || DEFINITIONS.size() != size) {
					DEFINITIONS = new HashMap<>(size);
				}
			}

			@Override
			public void load(JsonObject reader, Gson builder) {
				CombatProjectileDefinition definition = new CombatProjectileDefinition(reader.get("name").getAsString());

				definition.hitDelay = -1;
				if(reader.has("hit-delay")) {
					definition.hitDelay = reader.get("hit-delay").getAsInt();
				}

				definition.hitsplatDelay = -1;
				if(reader.has("hitsplat-delay")) {
					definition.hitsplatDelay = reader.get("hitsplat-delay").getAsInt();
				}

				definition.maxHit = 0;
				if(reader.has("max-hit")) {
					definition.maxHit = reader.get("max-hit").getAsInt();
				}

				definition.effect = null;
				if(reader.has("magic-effect")) {
					String name = reader.get("magic-effect").getAsString();
					definition.effect = MagicEffects.valueOf(name).getEffect();
				} else if(reader.has("ranged-effect")) {
					String name = reader.get("ranged-effect").getAsString();
					definition.effect = RangedEffects.valueOf(name).getEffect();
				}

				definition.animation = null;
				if(reader.has("animation")) {
					definition.animation = builder.fromJson(reader.get("animation"), Animation.class);
				}

				definition.start = null;
				if(reader.has("start")) {
					definition.start = builder.fromJson(reader.get("start"), Graphic.class);
				}

				definition.end = null;
				if(reader.has("end")) {
					definition.end = builder.fromJson(reader.get("end"), Graphic.class);
				}

				definition.projectile = null;
				if(reader.has("projectile")) {
					definition.projectile = builder.fromJson(reader.get("projectile"), ProjectileBuilder.class);
				}

				DEFINITIONS.put(definition.name, definition);
			}
		};
	}

	private static final class ProjectileBuilder {
		short id;
		byte delay;
		byte duration;
		byte startHeight;
		byte endHeight;
		byte curve;

		public void send(Actor attacker, Actor defender, boolean magic) {
			Projectile projectile = new Projectile(attacker, defender, id, duration, delay, startHeight, endHeight, curve, magic ? CombatType.MAGIC : CombatType.RANGED);
			projectile.sendProjectile();
		}

	}

}
