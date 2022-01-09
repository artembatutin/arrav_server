package com.rageps.world.attr;

/**
 * A definition for an {@link Attribute}.
 *
 * @param <T> The type of attribute.
 */
public final class AttributeDefinition<T> {

	/**
	 * The default value of the Attribute.
	 */
	private final T defaultValue;

	/**
	 * The persistence state of the Attribute.
	 */
	private final AttributePersistence persistence;

	/**
	 * The type of the Attribute.
	 */
	private final AttributeType type;

	/**
	 * Creates the AttributeDefinition.
	 *
	 * @param defaultValue The default value.
	 * @param persistence The {@link AttributePersistence}.
	 * @param type The {@link AttributeType}.
	 */
	public AttributeDefinition(T defaultValue, AttributePersistence persistence, AttributeType type) {
		this.defaultValue = defaultValue;
		this.persistence = persistence;
		this.type = type;
	}

	/**
	 * Creates an AttributeDefinition for a {@code boolean}.
	 *
	 * @param defaultValue The default value of the definition.
	 * @param persistence The {@link AttributePersistence} of the definition.
	 * @return The AttributeDefinition.
	 */
	public static AttributeDefinition<Boolean> forBoolean(boolean defaultValue, AttributePersistence persistence) {
		return new AttributeDefinition<>(defaultValue, persistence, AttributeType.BOOLEAN);
	}

	/**
	 * Creates an AttributeDefinition for a {@code double}.
	 *
	 * @param defaultValue The default value of the definition.
	 * @param persistence The {@link AttributePersistence} of the definition.
	 * @return The AttributeDefinition.
	 */
	public static AttributeDefinition<Double> forDouble(double defaultValue, AttributePersistence persistence) {
		return new AttributeDefinition<>(defaultValue, persistence, AttributeType.DOUBLE);
	}

	/**
	 * Creates an AttributeDefinition for an {@code int}.
	 *
	 * @param defaultValue The default value of the definition.
	 * @param persistence The {@link AttributePersistence} of the definition.
	 * @return The AttributeDefinition.
	 */
	public static AttributeDefinition<Integer> forInt(int defaultValue, AttributePersistence persistence) {
		return new AttributeDefinition<>(defaultValue, persistence, AttributeType.LONG);
	}

	/**
	 * Creates an AttributeDefinition for a {@code long}.
	 *
	 * @param defaultValue The default value of the definition.
	 * @param persistence The {@link AttributePersistence} of the definition.
	 * @return The AttributeDefinition.
	 */
	public static AttributeDefinition<Long> forLong(long defaultValue, AttributePersistence persistence) {
		return new AttributeDefinition<>(defaultValue, persistence, AttributeType.LONG);
	}

	/**
	 * Creates an AttributeDefinition for a String.
	 *
	 * @param defaultValue The default value of the definition.
	 * @param persistence The {@link AttributePersistence} of the definition.
	 * @return The AttributeDefinition.
	 */
	public static AttributeDefinition<String> forString(String defaultValue, AttributePersistence persistence) {
		return new AttributeDefinition<>(defaultValue, persistence, AttributeType.STRING);
	}

	/**
	 * Creates an AttributeDefinition for any given object type.
	 * @param defaultValue the default value for this given object type.
	 * @param <T> the type of value we're defining.
	 * @return the new definition of the object.
	 */
	public static <T> AttributeDefinition<T> emptyObjectDefinition(T defaultValue) {
		return new AttributeDefinition<T>(defaultValue, AttributePersistence.TRANSIENT, AttributeType.OBJECT);
	}
	/**
	 * Creates an AttributeDefinition for any given object type.
	 * @param defaultValue the default value for this given object type.
	 * @param <T> the type of value we're defining.
	 * @return the new definition of the object.
	 */
	public static <T> AttributeDefinition<T> emptyObjectDefinition(T defaultValue, AttributePersistence attributePersistence) {
		return new AttributeDefinition<T>(defaultValue, attributePersistence, AttributeType.OBJECT);
	}

	public static <T> AttributeDefinition<T> emptyObjectDefinition() {
		return emptyObjectDefinition(null);
	}

	/**
	 * Gets the default value of this AttributeDefinition.
	 *
	 * @return The default value.
	 */
	public T getDefault() {
		return defaultValue;
	}

	/**
	 * Gets the {@link AttributePersistence} of this AttributeDefinition.
	 *
	 * @return The AttributePersistence.
	 */
	public AttributePersistence getPersistence() {
		return persistence;
	}

	/**
	 * Gets the {@link AttributeType} of this AttributeDefinition
	 *
	 * @return The AttributeType.
	 */
	public AttributeType getType() {
		return type;
	}

}
