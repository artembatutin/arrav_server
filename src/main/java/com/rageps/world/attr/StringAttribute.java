package com.rageps.world.attr;

/**
 * An {@link Attribute} with a string value.
 */
public final class StringAttribute extends Attribute<String> {

	/**
	 * Creates the string attribute.
	 *
	 * @param value The value.
	 */
	public StringAttribute(String value) {
		super(AttributeType.STRING, value);
	}

	@Override
	public String toString() {
		return value;
	}

}
