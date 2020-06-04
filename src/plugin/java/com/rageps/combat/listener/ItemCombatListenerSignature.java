package com.rageps.combat.listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Listens for item listeners.
 * @author Michael | Chex
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ItemCombatListenerSignature {

	/**
	 * The item ids to attach this listener to.
	 * @return the item ids
	 */
	int[] items();

}
