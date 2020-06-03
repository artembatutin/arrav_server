package com.rageps.world.attr;

/**
 * An {@link Attribute} with a numerical value.
 */
public final class NumericalAttribute extends Attribute<Number> {

	/**
	 * Creates the number attribute.
	 *
	 * @param value The value of this attribute.
	 */
	public NumericalAttribute(Number value) {
		super(typeOf(value), value);
	}

	/**
	 * Gets the {@link AttributeType} of number this attribute is.
	 *
	 * @param value The value of this attribute.
	 * @return The type.
	 */
	private static AttributeType typeOf(Number value) {
		return value instanceof Double ? AttributeType.DOUBLE : AttributeType.LONG;
	}

	@Override
	public String toString() {
		return type == AttributeType.DOUBLE ? Double.toString((double) value) : Long.toString((long) value);
	}

}
