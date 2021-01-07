package com.rageps.content.object.cannon;

import com.rageps.net.packet.out.SendObjectAnimation;
import com.rageps.combat.strategy.basic.RangedStrategy;
import com.rageps.content.skill.Skills;
import com.rageps.net.refactor.packet.out.model.ObjectAnimationPacket;
import com.rageps.task.Task;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.model.Direction;
import com.rageps.world.model.Projectile;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.CombatType;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.combat.hit.HitIcon;
import com.rageps.world.entity.actor.combat.hit.Hitsplat;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.GroundItem;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.region.Region;

/**
 * A {@link Task} handling the {@link Multicannon} shooting.
 * @author Artem Batutin
 */
public class MulticannonTask extends Task {
	
	/**
	 * The cannon shooting.
	 */
	private final Multicannon cannon;
	
	MulticannonTask(Multicannon cannon) {
		super(1, false);
		this.cannon = cannon;
	}
	
	@Override
	protected void execute() {
		if(cannon.isDisabled()) {
			cancel();
			return;
		}
		if(cannon.getPosition().getDistance(cannon.player.getPosition()) > 30) {
			cannon.player.message("Your cannon is now on the floor because you weren't close enough to repair it in time.");
			cancel();
			cannon.remove();
			Region r = cannon.getRegion();
			if(r != null) {
				if(cannon.getElements() > 0) {
					r.register(new GroundItem(new Item(2, cannon.getElements()), cannon.getPosition(), cannon.player));
				}
				r.register(new GroundItem(new Item(6), cannon.getPosition(), cannon.player));
				r.register(new GroundItem(new Item(8), cannon.getPosition(), cannon.player));
				r.register(new GroundItem(new Item(10), cannon.getPosition(), cannon.player));
				r.register(new GroundItem(new Item(12), cannon.getPosition(), cannon.player));
			}
		} else if(cannon.getElements() < 1) {
			cannon.player.message("Your cannon has run out of ammo!");
			cancel();
		} else {
			if(cannon.facing == null) {
				cannon.facing = Direction.NORTH;
				rotate(cannon);
				fire();
				return;
			}
			switch(cannon.facing) {
				case NORTH: // north
					cannon.facing = Direction.NORTH_EAST;
					break;
				case NORTH_EAST: // north-east
					cannon.facing = Direction.EAST;
					break;
				case EAST: // east
					cannon.facing = Direction.SOUTH_EAST;
					break;
				case SOUTH_EAST: // south-east
					cannon.facing = Direction.SOUTH;
					break;
				case SOUTH: // south
					cannon.facing = Direction.SOUTH_WEST;
					break;
				case SOUTH_WEST: // south-west
					cannon.facing = Direction.WEST;
					break;
				case WEST: // west
					cannon.facing = Direction.NORTH_WEST;
					break;
				case NORTH_WEST: // north-west
					cannon.facing = null;
					break;
			}
			
			rotate(cannon);
			fire();
		}
	}
	
	/**
	 * Rotate the cannon and change the object animation based on the direction
	 * we are facing.
	 */
	private void rotate(Multicannon cannon) {
		Player p = cannon.player;
		switch(cannon.facing) {
			case NORTH: // north
				p.send(new ObjectAnimationPacket(p, cannon.getPosition(), 516, cannon.getObjectType(), -1));
				break;
			case NORTH_EAST: // north-east
				p.send(new ObjectAnimationPacket(p, cannon.getPosition(), 517, cannon.getObjectType(), -1));
				break;
			case EAST: // east
				p.send(new ObjectAnimationPacket(p, cannon.getPosition(), 518, cannon.getObjectType(), -1));
				break;
			case SOUTH_EAST: // south-east
				p.send(new ObjectAnimationPacket(p, cannon.getPosition(), 519, cannon.getObjectType(), -1));
				break;
			case SOUTH: // south
				p.send(new ObjectAnimationPacket(p, cannon.getPosition(), 520, cannon.getObjectType(), -1));
				break;
			case SOUTH_WEST: // south-west
				p.send(new ObjectAnimationPacket(p, cannon.getPosition(), 521, cannon.getObjectType(), -1));
				break;
			case WEST: // west
				p.send(new ObjectAnimationPacket(p, cannon.getPosition(), 514, cannon.getObjectType(), -1));
				break;
			case NORTH_WEST: // north-west
				p.send(new ObjectAnimationPacket(p, cannon.getPosition(), 515, cannon.getObjectType(), -1));
				cannon.facing = null;
				break;
		}
	}
	
	/**
	 * Applies damage to the victim.
	 * 4* exp multiplier is based on:
	 * {@link RangedStrategy#BASE_EXPERIENCE_MULTIPLIER}
	 */
	
	private void fire() {
		Actor victim = getVictim();
		if(victim == null)
			return;
		int damage = RandomUtils.inclusive(300);
		cannon.setElements(cannon.getElements() - 1);
		new Projectile(cannon.getPosition().move(1, 1), victim.getCenterPosition(), (victim.isPlayer() ? -victim.getSlot() - 1 : victim.getSlot() + 1), 53, 60, 20, 35, 30, cannon.player.getInstance(), CombatType.RANGED).sendProjectile();
		//Hit data = CombatUtil.calculateSoaking(victim, CombatType.RANGED, new Hit(damage, Hitsplat.NORMAL, HitIcon.CANNON, true, cannon.player.getSlot()));
		Hit data = new Hit(damage, Hitsplat.NORMAL, HitIcon.CANNON);
		
		int exp = 0;
		exp += damage;
		exp = Math.round(exp / 10F);
		exp *= 4;
		Skills.experience(cannon.player, exp / 2, Skills.RANGED);
		
		new Task(2, false) {
			@Override
			protected void execute() {
				victim.damage(data);
				if(victim.isAutoRetaliate() && !victim.getCombat().isAttacking()) {
					victim.getCombat().attack(cannon.player);
				}
				this.cancel();
			}
		}.submit();
	}
	
	private Actor getVictim() {
		int myX = cannon.getPosition().getX();
		int myY = cannon.getPosition().getY();
		if(cannon.player.inMulti()) {
			for(Mob m : cannon.player.getLocalMobs()) {
				if(m.isDead())
					continue;
				if(!m.getDefinition().isAttackable())
					continue;
				if(m.getPosition().getDistance(cannon.getPosition()) > 8)
					continue;
				if(check(m, myX, myY)) {
					return m;
				}
			}
		} else if(cannon.player.getCombat().isUnderAttack()) {
			Actor actor = cannon.player.getCombat().getLastAttacker();
			if(check(actor, myX, myY)) {
				return actor;
			}
		}
		return null;
	}
	
	private boolean check(Actor a, int myX, int myY) {
		int theirX = a.getPosition().getX();
		int theirY = a.getPosition().getY();
		
		if(cannon.facing == null) {
			cannon.facing = Direction.NORTH;
		}
		
		switch(cannon.facing) {
			case NORTH:
				if(theirY > myY && theirX >= myX - 1 && theirX <= myX + 1)
					return true;
				break;
			case NORTH_EAST:
				if(theirX >= myX + 1 && theirY >= myY + 1)
					return true;
				break;
			case EAST:
				if(theirX > myX && theirY >= myY - 1 && theirY <= myY + 1)
					return true;
				break;
			case SOUTH_EAST:
				if(theirY <= myY - 1 && theirX >= myX + 1)
					return true;
				break;
			case SOUTH:
				if(theirY < myY && theirX >= myX - 1 && theirX <= myX + 1)
					return true;
				break;
			case SOUTH_WEST:
				if(theirX <= myX - 1 && theirY <= myY - 1)
					return true;
				break;
			case WEST:
				if(theirX < myX && theirY >= myY - 1 && theirY <= myY + 1)
					return true;
				break;
			case NORTH_WEST:
				if(theirX <= myX - 1 && theirY >= myY + 1)
					return true;
				break;
		}
		return false;
	}
	
	@Override
	protected void onCancel() {
		cannon.firing = false;
	}
}