package com.rageps.world.attr;

/**
 * Created by Jason M on 2017-02-13 at 3:55 PM
 * <p>
 * Intended to represent an attribute that is an object, is not a boxed primitive, and
 * cannot be persistent.
 */
public class ObjectAttribute<T> extends Attribute<T> {

	/**
	 * Creates a new attribute for the given value.
	 *
	 * @param value the value of the attribute.
	 */
	public ObjectAttribute(T value) {
		super(AttributeType.OBJECT, value);
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
