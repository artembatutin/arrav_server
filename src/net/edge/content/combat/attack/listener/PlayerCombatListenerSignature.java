package net.edge.content.combat.attack.listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Listens for item listeners or some shit lol
 *
 * @author Michael | Chex
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PlayerCombatListenerSignature {

	/**
	 * The item ids to attach this listener to.
	 *
	 * @return the item ids
	 */
	int[] items();

}
