package com.rageps.world.entity.actor.combat.effect;

import com.google.common.collect.ImmutableSet;
import com.rageps.world.entity.actor.combat.effect.impl.CombatAntifireEffect;
import com.rageps.world.entity.actor.combat.effect.impl.CombatTeleblockEffect;
import com.rageps.world.entity.actor.player.assets.AntifireDetails;
import com.rageps.world.entity.actor.combat.effect.impl.CombatPoisonEffect;
import com.rageps.world.entity.actor.combat.effect.impl.CombatSkullEffect;

/**
 * The enumerated type whose values represent the collection of different combat
 * effect types.
 * @author lare96 <http://github.org/lare96>
 */
public enum CombatEffectType {
	
	/**
	 * The combat poison effect, handles the poisoning of characters.
	 */
	POISON(new CombatPoisonEffect()),
	
	/**
	 * The combat skull effect, handles the skulling of players.
	 */
	SKULL(new CombatSkullEffect()),
	
	/**
	 * The combat teleblock effect, handles the teleblocking of players.
	 */
	TELEBLOCK(new CombatTeleblockEffect()),
	
	/**
	 * The combat antifire potion effect, handles the effect of drinking the potion.
	 */
	ANTIFIRE_POTION(new CombatAntifireEffect(AntifireDetails.AntifireType.REGULAR)),
	
	/**
	 * The combat super antifire potion effect, handles the effect of drinking the potion.
	 */
	SUPER_ANTIFIRE_POTION(new CombatAntifireEffect(AntifireDetails.AntifireType.SUPER));
	
	/**
	 * The immutable set that holds the elements of this enumerated type, to
	 * prevent repeated calls to {@code values()}.
	 */
	public static final ImmutableSet<CombatEffectType> TYPES = ImmutableSet.copyOf(values());
	
	/**
	 * The combat effect that contains the data for this type.
	 */
	private final CombatEffect effect;
	
	/**
	 * Creates a new {@link CombatEffectType}.
	 * @param effect the combat effect for this type.
	 */
	private CombatEffectType(CombatEffect effect) {
		this.effect = effect;
	}
	
	/**
	 * Gets the combat effect that contains the data for this type.
	 * @return the combat effect for this type.
	 */
	public final CombatEffect getEffect() {
		return effect;
	}
}
