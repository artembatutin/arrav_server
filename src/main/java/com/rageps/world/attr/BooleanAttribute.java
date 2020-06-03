package com.rageps.world.attr;

/**
 * An {@link Attribute} with a boolean value.
 */
public final class BooleanAttribute extends Attribute<Boolean> {

	/**
	 * Creates the boolean attribute.
	 *
	 * @param value The value.
	 */
	public BooleanAttribute(Boolean value) {
		super(AttributeType.BOOLEAN, value);
	}

	@Override
	public String toString() {
		return Boolean.toString(value);
	}

}
