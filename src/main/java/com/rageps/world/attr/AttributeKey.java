package com.rageps.world.attr;

/**
 * Represents a key for an Attribute.
 */
public final class AttributeKey {

    private final String name;
    private final AttributeDefinition<?> definition;

    public AttributeKey(String name, AttributeDefinition<?> definition) {
        this.name = name.toUpperCase();
        this.definition = definition;
    }

    public AttributeDefinition<?> getDefinition() {
        return definition;
    }

    public String getName() {
        return name;
    }

}
