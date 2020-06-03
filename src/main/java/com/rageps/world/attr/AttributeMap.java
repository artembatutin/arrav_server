package com.rageps.world.attr;

import com.google.common.base.Preconditions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link Map} wrapper used to store {@link Attribute}s and their {@link AttributeDefinition definitions}.
 */
public final class AttributeMap {
	/**
	 * The Logger for this class.
	 */
	private static final Logger logger = LogManager.getLogger(AttributeMap.class);

	/**
	 * The default size of the map.
	 */
	private static final int DEFAULT_MAP_SIZE = 2;

	/**
	 * The map of attribute keys to attributes.
	 */
	private final Map<AttributeKey, Attribute<?>> attributes = new HashMap<>(DEFAULT_MAP_SIZE);

	/**
	 * Gets the appropriate {@link Attribute} from the specified {@code T value}.
	 *
	 * @param value The value of the Attribute.
	 * @param <T> The Attributes type.
	 * @return A new {@link Attribute} from the specified type, if possible. Otherwise an {@link IllegalArgumentException} is thrown.
	 */
	@SuppressWarnings("unchecked")
	private static <T> Attribute<T> toAttribute(T value) {
		if (value == null) {
			return (Attribute<T>) createAttribute(null, AttributeType.OBJECT);
		}

		Class<?> clazz = value.getClass();

		if (Primitives.isTwosComplementPrimitive(clazz)) {
			return (Attribute<T>) createAttribute(value, AttributeType.LONG);
		} else if (Primitives.isFloatingPointPrimitive(clazz)) {
			return (Attribute<T>) createAttribute(value, AttributeType.DOUBLE);
		} else if (clazz == String.class) {
			return (Attribute<T>) createAttribute(value, AttributeType.STRING);
		} else if (clazz == Boolean.class) {
			return (Attribute<T>) createAttribute(value, AttributeType.BOOLEAN);
		}

		return (Attribute<T>) createAttribute(value, AttributeType.OBJECT);
	}

	/**
	 * Creates an {@link Attribute} with the specified value and {@link AttributeType}.
	 *
	 * @param value The value of the Attribute.
	 * @param type The AttributeType.
	 * @return The Attribute.
	 */
	private static <T> Attribute<?> createAttribute(T value, AttributeType type) {
		switch (type) {
			case LONG:
				return new NumericalAttribute(((Number) value).longValue());
			case DOUBLE:
				return new NumericalAttribute((Double) value);
			case STRING:
				return new StringAttribute((String) value);
			case BOOLEAN:
				return new BooleanAttribute((Boolean) value);
			case OBJECT:
				return new ObjectAttribute<>(value);
		}

		throw new IllegalArgumentException("Unrecognised type " + type + ".");
	}

	/**
	 * Gets the {@link Attribute} with the specified key.
	 *
	 * @param key The key of the attribute.
	 * @return The attribute.
	 */
	@SuppressWarnings("unchecked")
	public <T> Attribute<T> get(AttributeKey key) {
		AttributeDefinition<T> definition = Attributes.getDefinition(key);
		Preconditions.checkNotNull(definition, "Attributes must be defined before their value can be retrieved.");

		return (Attribute<T>) attributes.computeIfAbsent(key, __ -> createAttribute(definition.getDefault(), definition.getType()));
	}

	/**
	 * Gets the {@link Attribute} int value from the specified key.
	 *
	 * @param key The key of the attribute.
	 * @return The attribute value.
	 */
	public int getInt(AttributeKey key) {
		return (int) getLong(key);
	}

	/**
	 * Gets the {@link Attribute} long value from the specified key.
	 *
	 * @param key The key of the attribute.
	 * @return The attribute value.
	 */
	public long getLong(AttributeKey key) {
		Preconditions.checkArgument(key.getDefinition().getType() == AttributeType.LONG, "AttributeKey is not of type long!");
		return (long) get(key).getValue();
	}

	/**
	 * Gets the {@link Attribute} double value from the specified key.
	 *
	 * @param key The key of the attribute.
	 * @return The attribute value.
	 */
	public double getDouble(AttributeKey key) {
		Preconditions.checkArgument(key.getDefinition().getType() == AttributeType.DOUBLE, "AttributeKey is not of type double!");
		return (double) get(key).getValue();
	}

	/**
	 * Gets the {@link Attribute} boolean value from the specified key.
	 *
	 * @param key The key of the attribute.
	 * @return The attribute value.
	 */
	public boolean getBoolean(AttributeKey key) {
		Preconditions.checkArgument(key.getDefinition().getType() == AttributeType.BOOLEAN, "AttributeKey is not of type boolean!");
		return (boolean) get(key).getValue();
	}

	/**
	 * Gets the {@link Attribute} String value from the specified key.
	 *
	 * @param key The key of the attribute.
	 * @return The attribute value.
	 */
	public String getString(AttributeKey key) {
		Preconditions.checkArgument(key.getDefinition().getType() == AttributeType.STRING, "AttributeKey is not of type string!");
		return (String) get(key).getValue();
	}

	/**
	 * Retrieves an object for the given key.
	 *
	 * @param key the key for the object.
	 * @param <T> the type of value we should expect.
	 * @return the object of the given type.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getObject(AttributeKey key) {
		return (T) get(key).getValue();
	}

	public void plus(AttributeKey key, Number increment) {
		Attribute<?> attribute = get(key);
		AttributeType type = attribute.getType();
		Preconditions.checkArgument(type == AttributeType.DOUBLE || type == AttributeType.LONG);

		Number value = (Number) attribute.getValue();

		if (type == AttributeType.DOUBLE) {
			set(key, value.doubleValue() + increment.doubleValue());
		} else {
			set(key, value.longValue() + increment.longValue());
		}
	}

	public void minus(AttributeKey key, Number decrement) {
		Attribute<?> attribute = get(key);
		AttributeType type = attribute.getType();
		Preconditions.checkArgument(type == AttributeType.DOUBLE || type == AttributeType.LONG);

		Number value = (Number) attribute.getValue();

		if (type == AttributeType.DOUBLE) {
			set(key, value.doubleValue() - decrement.doubleValue());
		} else {
			set(key, value.longValue() - decrement.longValue());
		}
	}

	/**
	 * Gets the {@link Map} of {@link Attribute}s.
	 *
	 * @return The attributes.
	 */
	public Map<AttributeKey, Attribute<?>> getAttributes() {
		return attributes;
	}

	/**
	 * Sets the value of the {@link Attribute} with the specified name.
	 *
	 * @param key The key of the attribute.
	 * @param attribute The attribute.
	 */
	public void set(AttributeKey key, Attribute<?> attribute) {
		if (Attributes.getDefinition(key) == null) {
			logger.warn("Attribute with key: {} is not defined.", key.getName());
		} else {
			attributes.put(key, attribute);
		}
	}

	/**
	 * Sets the value of the {@link Attribute} with the specified name.
	 *
	 * @param key The key of the attribute.
	 * @param value The value of the attribute.
	 */
	public <T> void set(AttributeKey key, T value) {
		set(key, toAttribute(value));
	}

	/**
	 * Sets the value for the key to the default value.
	 *
	 * @param key the key being modified.
	 */
	public void reset(AttributeKey key) {
		set(key, key.getDefinition().getDefault());
	}

	/**
	 * Clears this AttributeMap completely.
	 */
	public void clear() {
		attributes.clear();
	}

}
