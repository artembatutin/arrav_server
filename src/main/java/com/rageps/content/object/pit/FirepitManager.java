package com.rageps.content.object.pit;

import com.rageps.content.skill.firemaking.FireLighter;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.object.GameObject;

/**
 * The manager class for the fire pit event objects.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class FirepitManager {
	
	/**
	 * The fire pit event for the world.
	 */
	private static final FirepitManager FIRE_PIT_EVENT = new FirepitManager();
	
	/**
	 * Represents the constant id for the red fire pit object.
	 */
	static final int RED_FIRE_PIT_OBJECT_ID = 38828;
	
	/**
	 * Represents the event time in ticks.
	 */
	public static final int EVENT_TIME_IN_TICKS = 30_000; //5 hour
	
	/**
	 * Represents the a fire pit object.
	 */
	private FirepitObject firepit = new FirepitObject();
	
	/**
	 * Gets the fire pit.
	 * @return {@link FirepitObject}.
	 */
	public FirepitObject getFirepit() {
		return firepit;
	}
	
	/**
	 * Attempts to fire the fire pit object.
	 * @param player the player firing the fire pit.
	 * @param object the object representing the fire pit.
	 * @param item the item that was used on the fire pit.
	 * @return {@code true} if the pit was fired, {@code false} otherwise.
	 */
	public boolean fire(Player player, GameObject object, Item item) {
		FirepitObject pit = firepit.getPosition().same(object.getPosition()) && firepit.getId() == object.getId() ? firepit : null;
		if(pit == null) {
			return false;
		}
		FireLighter lighter = FireLighter.getDefinition(item.getId()).orElse(null);
		if(lighter == null) {
			player.message("You can only fire the pit with a fire lighter.");
			return false;
		}
		pit.fire(player);
		return true;
	}
	
	/**
	 * Registers the two fire pits on the world.
	 */
	public void register() {
		firepit.publish();
	}
	
	/**
	 * Returns the fire pit event manager.
	 */
	public static FirepitManager get() {
		return FIRE_PIT_EVENT;
	}
	
}
