package com.rageps.content.skill.action;

/**
 * Acts as a transformable object which switches between states.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class TransformableObject {
	
	/**
	 * The original state of this object.
	 */
	private final int objectId;
	
	/**
	 * The transformable states this object can transform into.
	 */
	private final int transformable;
	
	/**
	 * Constructs a new {@link TransformableObject}.
	 * @param objectId {@link #objectId}.
	 * @param transformable {@link #transformable}.
	 */
	public TransformableObject(int objectId, int transformable) {
		this.objectId = objectId;
		this.transformable = transformable;
	}
	
	/**
	 * @return {@link #objectId}.
	 */
	public int getObjectId() {
		return objectId;
	}
	
	/**
	 * @return {@link #transformable}.
	 */
	public int getTransformable() {
		return transformable;
	}
}
