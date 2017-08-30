package net.edge.world.entity.actor.combat.projectile;

import net.edge.world.Projectile;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.CombatType;

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
