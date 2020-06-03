package com.rageps.world.attr;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A helper-class for recursively initializing global Attributes.
 */
public final class Attributes {
    private static final Logger logger = LogManager.getLogger(Attributes.class);

    /**
     * The map of attribute names to AttributeKeys.
     */
    private static final Map<String, AttributeKey> attributeKeys = new HashMap<>();

    /**
     * The map of attribute keys to definitions.
     */
    private static final Map<AttributeKey, AttributeDefinition<?>> definitions = new HashMap<>();

    /* Strings */
    public static AttributeKey definePersistent(String name, String defaultValue) {
        return define(name, defaultValue, AttributePersistence.PERSISTENT);
    }

    public static AttributeKey define(String name, String defaultValue) {
        return define(name, defaultValue, AttributePersistence.TRANSIENT);
    }

    private static AttributeKey define(String name, String defaultValue, AttributePersistence persistence) {
        return define(name, AttributeDefinition.forString(defaultValue, persistence));
    }

    /* Integers */
    public static AttributeKey definePersistent(String name, int defaultValue) {
        return define(name, defaultValue, AttributePersistence.PERSISTENT);
    }

    public static AttributeKey define(String name, int defaultValue) {
        return define(name, defaultValue, AttributePersistence.TRANSIENT);
    }

    private static AttributeKey define(String name, int defaultValue, AttributePersistence persistence) {
        return define(name, AttributeDefinition.forInt(defaultValue, persistence));
    }

    /* Boolean */
    public static AttributeKey definePersistent(String name, boolean defaultValue) {
        return define(name, defaultValue, AttributePersistence.PERSISTENT);
    }

    public static AttributeKey define(String name, boolean defaultValue) {
        return define(name, defaultValue, AttributePersistence.TRANSIENT);
    }

    private static AttributeKey define(String name, boolean defaultValue, AttributePersistence persistence) {
        return define(name, AttributeDefinition.forBoolean(defaultValue, persistence));
    }

    /* Doubles */
    public static AttributeKey definePersistent(String name, double defaultValue) {
        return define(name, defaultValue, AttributePersistence.PERSISTENT);
    }

    public static AttributeKey define(String name, double defaultValue) {
        return define(name, defaultValue, AttributePersistence.TRANSIENT);
    }

    private static AttributeKey define(String name, double defaultValue, AttributePersistence persistence) {
        return define(name, AttributeDefinition.forDouble(defaultValue, persistence));
    }

    /* Longs */
    public static AttributeKey definePersistent(String name, long defaultValue) {
        return define(name, defaultValue, AttributePersistence.PERSISTENT);
    }

    public static AttributeKey define(String name, long defaultValue) {
        return define(name, defaultValue, AttributePersistence.TRANSIENT);
    }

    private static AttributeKey define(String name, long defaultValue, AttributePersistence persistence) {
        return define(name, AttributeDefinition.forLong(defaultValue, persistence));
    }

    /* Objects */
    public static <T> AttributeKey defineEmpty(String name) {
        return define(name, (T) null);
    }

    public static <T> AttributeKey define(String name, T defaultValue) {
        return define(name, AttributeDefinition.emptyObjectDefinition(defaultValue));
    }
    public static <T> AttributeKey definePersistentObject(String name, T defaultValue) {
        return define(name, AttributeDefinition.emptyObjectDefinition(defaultValue, AttributePersistence.PERSISTENT));
    }

    /**
     * Defines an AttributeKey with the specified name and definition.
     *
     * @param name       The name of the AttributeKey.
     * @param definition The AttributeDefinition.
     * @return The created AttributeKey.
     */
    private static AttributeKey define(String name, AttributeDefinition<?> definition) {
        return new AttributeKey(name, definition);
    }

    /**
     * Gets the {@link AttributeDefinition} with the specified name, or {@code null} if it is not defined.
     *
     * @param key The key of the attribute.
     * @return The attribute definition.
     */
    @SuppressWarnings("unchecked")
    public static <T> AttributeDefinition<T> getDefinition(AttributeKey key) {
        return (AttributeDefinition<T>) definitions.get(key);
    }

    /**
     * Gets the {@link AttributeDefinition}s, as a {@link Map}.
     *
     * @return The map of attribute names to definitions.
     */
    public static Map<AttributeKey, AttributeDefinition<?>> getDefinitions() {
        return new HashMap<>(definitions);
    }

    /**
     * Gets the {@link AttributeKey} with the specified name, or {@code null} if it is not defined.
     *
     * @param name The name of the attribute.
     * @return The attribute definition.
     */
    public static AttributeKey getAttributeKey(String name) {
        return attributeKeys.get(name);
    }

    public static void init() {
        Reflections reflections = new Reflections("com.rageps", new FieldAnnotationsScanner());
        Set<Field> fields = reflections.getFieldsAnnotatedWith(Attr.class);

        for (Field field : fields) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }

            AttributeKey key;
            try {
                key = (AttributeKey) field.get(null);
            } catch (IllegalAccessException cause) {
                throw new AssertionError("Unable to get field: " + field, cause);
            }

            String name = field.getName();
            if (attributeKeys.containsKey(name)) {
                throw new IllegalArgumentException(name + " already exists as AttributeKey.");
            }
            attributeKeys.put(name, key);
            definitions.put(key, key.getDefinition());
        }

        logger.info("Defined {} attributes.", fields.size());
    }
}
