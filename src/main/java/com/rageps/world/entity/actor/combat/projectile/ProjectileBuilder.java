package com.rageps.world.entity.actor.combat.projectile;

import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.CombatType;
import com.rageps.world.model.Projectile;

/**
 * Represents a stored projectile.
 */
public final class ProjectileBuilder {
	private short id;
	private byte delay;
	private byte speed;
	private byte startHeight;
	private byte endHeight;

	public void send(Actor attacker, Actor defender, boolean magic) {
		Projectile projectile = new Projectile(attacker, defender, id, speed, delay, startHeight, endHeight, magic ? CombatType.MAGIC : CombatType.RANGED);
		projectile.sendProjectile();
	}

}
