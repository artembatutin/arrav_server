package net.edge.content.combat.attack.listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Listens for npc listeners or some shit lol
 *
 * @author Michael | Chex
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NpcCombatListenerSignature {

    /**
     * The NPC ids to attach this listener to.
     * @return the npc ids
     */
    int[] npcs();

}
