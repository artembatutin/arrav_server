package net.arrav.world.entity.actor.combat.effect.impl;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.arrav.net.packet.out.SendConfig;
import net.arrav.world.PoisonType;
import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.effect.CombatEffect;
import net.arrav.world.entity.actor.combat.hit.Hit;
import net.arrav.world.entity.actor.combat.hit.HitIcon;
import net.arrav.world.entity.actor.combat.hit.Hitsplat;
import net.arrav.world.entity.actor.mob.MobDefinition;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;

import java.util.Optional;

/**
 * The combat effect applied when a character needs to be poisoned.
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatPoisonEffect extends CombatEffect {
	
	/**
	 * The collection of weapons mapped to their respective poison types.
	 */
	public static final Int2ObjectArrayMap<PoisonType> TYPES = new Int2ObjectArrayMap<>();
	
	/**
	 * The amount of times this player has been hit.
	 */
	private int amount;
	
	/**
	 * Creates a new {@link CombatPoisonEffect}.
	 */
	public CombatPoisonEffect() {
		super(30);
	}
	
	@Override
	public boolean apply(Actor t) {
		if(t.isPoisoned() || t.getPoisonType() == null)
			return false;
		if(t.isPlayer()) {
			Player player = (Player) t;
			if(player.getPoisonImmunity().get() > 0 || t.isDead())
				return false;
			player.out(new SendConfig(174, 1));
			player.message("You have been poisoned!");//u dont have tosend message
		}
		t.getPoisonDamage().set(t.getPoisonType().getDamage());
		return true;
	}
	
	@Override
	public boolean removeOn(Actor t) {
		return !t.isPoisoned() || t.isDead();
	}
	
	@Override
	public void process(Actor t) {
		if(t.getPoisonDamage().get() <= 0) {
			if(t.isPlayer()) {
				t.toPlayer().out(new SendConfig(174, 0));
			}
			this.removeOn(t);
			return;
		}
		t.damage(new Hit(t.getPoisonDamage().get() * 10, Hitsplat.POISON, HitIcon.NONE));
		t.getPoisonDamage().decrementAndGet();
		//		amount--;
		//		t.damage(new Hit(t.getPoisonDamage().get() * 10, Hitsplat.POISON, HitIcon.NONE));
		//		if(amount == 0) {
		//			amount = 4;
		//			int val = t.getPoisonDamage().decrementAndGet();
		//
		//			if(val < 1) {
		//				t.getPoisonDamage().set(0);//clear poison.
		//			}
		//		}
	}
	
	@Override
	public boolean onLogin(Actor t) {
		return t.isPoisoned();
	}
	
	/**
	 * Gets the {@link PoisonType} for {@code item} wrapped in an optional. If a
	 * poison type doesn't exist for the item then an empty optional is
	 * returned.
	 * @param item the item to get the poison type for.
	 * @return the poison type for this item wrapped in an optional, or an empty
	 * optional if no poison type exists.
	 */
	public static Optional<PoisonType> getPoisonType(Item item) {
		if(item == null || item.getId() < 1 || item.getAmount() < 1)
			return Optional.empty();
		return Optional.ofNullable(TYPES.get(item.getId()));
	}
	
	/**
	 * Gets the {@link PoisonType} for {@code npc} wrapped in an optional. If a
	 * poison type doesn't exist for the NPC then an empty optional is returned.
	 * @param npc the NPC to get the poison type for.
	 * @return the poison type for this NPC wrapped in an optional, or an empty
	 * optional if no poison type exists.
	 */
	public static Optional<PoisonType> getPoisonType(int npc) {
		MobDefinition def = MobDefinition.DEFINITIONS[npc];
		if(def == null || !def.isAttackable() || !def.poisonous())
			return Optional.empty();
		if(def.getCombatLevel() < 75)
			return Optional.of(PoisonType.DEFAULT_NPC);
		if(def.getCombatLevel() < 200)
			return Optional.of(PoisonType.STRONG_NPC);
		return Optional.of(PoisonType.SUPER_NPC);
	}
}
