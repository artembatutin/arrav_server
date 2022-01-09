package com.rageps.world.entity.actor.update;

import com.rageps.world.entity.actor.Actor;

/**
 * An enumerated type that holds all of the values representing update flags for {@link Actor}s.
 * @author Artem Batutin
 */
public enum UpdateFlag {
	APPEARANCE(0),
	CHAT(1),
	GRAPHIC(2),
	ANIMATION(3),
	FORCE_CHAT(4),
	FACE_ENTITY(5),
	FACE_COORDINATE(6),
	PRIMARY_HIT(7),
	SECONDARY_HIT(8),
	TRANSFORM(9),
	FORCE_MOVEMENT(10);
	
	private final int index;
	
	UpdateFlag(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
}
