package com.rageps.content.skill.summoning.specials;

import com.rageps.world.model.Animation;
import com.rageps.world.model.Graphic;

import java.util.Optional;

public abstract class FamiliarSpecialAttack {

	/**
	 * The delay before this player can cast this special attack again.
	 * @return the identifier for the delay.
	 */
	public abstract int getDelay();

	/**
	 * The delay before the special attack hits.
	 * @return the identifier for the delay.
	 */
	public abstract int getHitDelay();

	/**
	 * The amount of special attack points this special attack drains.
	 * @return the identifier for the special attack points.
	 */
	public abstract int getSpecialAttackPoints();

	/**
	 * The player animation played when the special attack is activated.
	 * @return the animation wrapped in an optional.
	 */
	public abstract Optional<Animation> getPlayerAnimation();

	/**
	 * The player graphic played when the special attack is activated.
	 * @return the graphic wrapped in an optional.
	 */
	public abstract Optional<Graphic> getPlayerGraphic();

	/**
	 * The npc animation played when the special attack is activated.
	 * @return the animation wrapped in an optional.
	 */
	public abstract Optional<Animation> getNpcAnimation();

	/**
	 * The npc graphic played when the special attack is activated.
	 * @return the graphic wrapped in an optional.
	 */
	public abstract Optional<Graphic> getNpcGraphic();

}
