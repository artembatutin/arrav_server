package com.rageps.world.entity.actor.combat.projectile;

import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.CombatImpact;
import com.rageps.world.model.Animation;
import com.rageps.world.model.Graphic;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

import java.util.Optional;
import java.util.OptionalInt;

public final class CombatProjectile {
	
	public static Object2ObjectArrayMap<String, CombatProjectile> DEFINITIONS;
	
	private final String name;
	private final int maxHit;
	private final CombatImpact effect;
	private final Animation animation;
	private final Graphic start;
	private final Graphic end;
	public final ProjectileBuilder projectile;
	
	private final int hitDelay;
	private final int hitsplatDelay;
	
	public CombatProjectile(String name, int maxHit, CombatImpact effect, Animation animation, Graphic start, Graphic end, ProjectileBuilder projectile, int hitDelay, int hitsplatDelay) {
		this.name = name;
		this.maxHit = maxHit;
		this.effect = effect;
		this.animation = animation;
		this.start = start;
		this.end = end;
		this.projectile = projectile;
		this.hitDelay = hitDelay;
		this.hitsplatDelay = hitsplatDelay;
	}
	
	public void sendProjectile(Actor attacker, Actor defender, boolean magic) {
		if(projectile != null) {
			projectile.send(attacker, defender, magic);
		}
	}
	
	public static CombatProjectile getDefinition(String name) {
		return DEFINITIONS.get(name);
	}
	
	public String getName() {
		return name;
	}
	
	public int getMaxHit() {
		return maxHit;
	}
	
	public Optional<CombatImpact> getEffect() {
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
	
}
