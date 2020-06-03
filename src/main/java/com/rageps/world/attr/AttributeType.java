package com.rageps.world.attr;

/**
 * The type of attribute. The functionality of this enum (and other classes) is dependent on the ordering of the values
 * - the expected order is {@link #BOOLEAN}, {@link #DOUBLE}, {@link #LONG}, {@link #STRING}.
 */
public enum AttributeType {

	/**
	 * The boolean attribute type.
	 */
	BOOLEAN,

	/**
	 * The double attribute type.
	 */
	DOUBLE,

	/**
	 * The long attribute type.
	 */
	LONG,

	/**
	 * The string attribute type.
	 */
	STRING,

	/**
	 * An attribute type that is a object reference.
	 */
	OBJECT;

	/**
	 * Gets the type with the specified ordinal.
	 *
	 * @param ordinal The ordinal.
	 * @return The type.
	 */
	public static AttributeType valueOf(int ordinal) {
		return values()[ordinal];
	}

	public static boolean isPrimitiveType(String value) {
		for(AttributeType type : values()) {
			if(type.name().equalsIgnoreCase(value)) {
				return true;
			}
		}
		return false;
	}

}
