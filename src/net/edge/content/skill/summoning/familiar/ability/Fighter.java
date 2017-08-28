package net.edge.content.skill.summoning.familiar.ability;

import net.edge.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.world.entity.actor.player.Player;

/**
 * This class doesn't need to hold functionality since all it does is
 * let the familiar enter combat which is done separately.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public class Fighter extends FamiliarAbility {

	/**
	 * Constructs the new Fighter ability.
	 */
	public Fighter() {
		super(FamiliarAbilityType.FIGHTER);
	}

	@Override
	public void initialise(Player player) {
		// TODO
	}
}
