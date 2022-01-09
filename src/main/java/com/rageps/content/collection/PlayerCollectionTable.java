package com.rageps.content.collection;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Represents a collection that has been collected by a player
 * that will be displayed in the {@link CollectionLog}.
 * @author tamatea <tamateea@gmail.com>
 */
public class PlayerCollectionTable {

    /**
     * The type of table this is.
     */
    public final CollectionType type;

    /**
     * The name of this table as it's represented on the interface.
     */
    public final String name;

    /**
     * The items represented in this table.
     */
    public final ObjectArrayList<CollectableItem> items;

    /**
     * Constructs a {@link PlayerCollectionTable}.
     * @param type The type of table.
     * @param items The items in the table.
     * @param name The name of the table.
     */
    public PlayerCollectionTable(CollectionType type, ObjectArrayList<CollectableItem> items, String name) {
        this.type = type;
        this.items = items;
        this.name = name;
    }

    /**
     * Get's the name of this table.
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get's the type of collection table.
     * @return The type.
     */
    public CollectionType getType() {
        return type;
    }

}
