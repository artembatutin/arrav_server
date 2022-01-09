package com.rageps.world.attr;

/**
 * An attribute belonging to an entity.
 *
 * @param <T> The type of attribute.
 */
public abstract class Attribute<T> {

	/**
	 * The type of this attribute.
	 */
	protected final AttributeType type;

	/**
	 * The value of this attribute.
	 */
	protected final T value;

	/**
	 * Creates the attribute with the specified {@link AttributeType} and value.
	 *
	 * @param type The type.
	 * @param value The value.
	 */
	protected Attribute(AttributeType type, T value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * Gets the type of this attribute.
	 *
	 * @return The type.
	 */
	public AttributeType getType() {
		return type;
	}

	/**
	 * Gets the type this attribute is saved as.
	 * @return The type.
	 */
	public String getSavedType() {
		return this.type == AttributeType.OBJECT ? getValue().getClass().getName() : this.type.name();
	}

	/**
	 * Gets the value of this attribute.
	 *
	 * @return The value.
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Returns the String representation of this Attribute. Will be used to write this Attribute as a String, if
	 * required.
	 */
	@Override
	public abstract String toString();

}
