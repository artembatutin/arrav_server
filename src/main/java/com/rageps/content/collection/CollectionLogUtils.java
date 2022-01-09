package com.rageps.content.collection;

public class CollectionLogUtils {

    /**
     * Get's the table as collected by the player.
     * @param items The items the players has collected.
     * @return
     */
    /*public PlayerCollectionTable getMergedTable(ObjectArrayList<CollectableItem> items) {
        for(CollectableItem item : this.items) {
            if(items.contains(item)) {
                items.add(item);
                continue;
            }
            CollectableItem collected = items.stream().filter(collectableItem -> collectableItem.getId() == item.getId()).findFirst().get();
            if(item.getAmount() != 0)
                item.setAmountCollected(collected.getAmount());
        }
        return new PlayerCollectionTable(this.type, items, this.name);
    }*/
}
