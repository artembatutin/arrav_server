package com.rageps.content.itemBoxes;

import com.rageps.util.rand.Chance;
import com.rageps.world.entity.item.Item;

/**
 * @author Tamatea <tamateeea@gmail.com>
 * Represents a loot and it's chance from a {@link ItemBox}
 */
public class BoxLoot {

    /**
     * The item represented in the loot.
     */
    private Item item;

    /**
     * The chance of the item.
     */
    private Chance chance;

    private boolean rare;

    /**
     * Constructs a {@link BoxLoot} object.
     * @param item The item represented in the boxloot.
     * @param chance The chance of the item being drawn.
     */
    public BoxLoot(Item item, Chance chance, boolean rare) {
        this.item = item;
        this.chance = chance;
        this.rare = rare;
    }

    public Chance getChance() {
        return chance;
    }

    public Item getItem() {
        return item;
    }

    public boolean isRare() {
        return rare;
    }

    @Override
    public String toString() {
        return "id =["+item.getId()+"] amount=["+item.getAmount()+"] chance=["+chance+"]";
    }
}
