package com.rageps.content.collection;

import com.rageps.world.entity.item.Item;

/**
 * Represents an item and it's collected value present in a
 * {@link PlayerCollectionTable}.
 *
 * @author tamatea <tamateea@gmail.com>
 */
public class CollectableItem {

    /**
     * The {@link Item} that is displayed.
     */
    private final Item item;

    /**
     * Used to identify the {@link PlayerCollectionTable} that this item
     * is used on.
     */
    private final String table;

    /**
     * Constructs a {@link CollectableItem} that has not
     * been collected with it's given id.
     * @param itemId The id provided for this.
     */
    public CollectableItem(int itemId, String table) {
        this.item = new Item(itemId, 0);
        this.table = table;
    }

    /**
     * Constructs a {@link CollectableItem} with it's given id.
     * @param itemId The id provided for this.
     * @param amount The amount of times it's been collected.
     */
    public CollectableItem(int itemId, int amount, String table) {
        this.item =  new Item(itemId);
        this.item.setAmount(amount);
        this.table = table;
    }

    /**
     * Increments the amount of this item which has been collected.
     * @param amount The amount to increment it by.
     */
    public void incrementCollected(int amount) {
        item.setAmount(item.getAmount() + amount);
    }

    public void setAmountCollected(int amount) {
        item.setAmount(amount);
    }

    /**
     * Checks if this item has been collected before.
     * @return If the item has been collected.
     */
    public boolean hasBeenCollected() {
        return item.getAmount() > 0;
    }

    /**
     * Get's the amount of times this item has been collected.
     * @return The amount.
     */
    public int getAmount() {
        return item.getAmount();
    }

    /**
     * Get's the item with is collected amount.
     * @return The item.
     */
    public Item getItem() {
        return item;
    }

    public int getId() {
        return item.getId();
    }

    public String getTable() {
        return table;
    }
}
