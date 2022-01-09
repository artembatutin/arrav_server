package com.rageps.content.rewardslist.impl;

import com.rageps.content.itemBoxes.BoxLoot;
import com.rageps.content.itemBoxes.ItemBox;
import com.rageps.content.itemBoxes.ItemBoxHandler;
import com.rageps.content.rewardslist.Rewardable;
import com.rageps.world.entity.item.Item;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.stream.Collectors;


/**
 * Creates a {@link Rewardable} from any of the {@link ItemBox}'s
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemBoxRewardable implements Rewardable {

    private ItemBox itemBox;

    public ItemBoxRewardable(ItemBox itemBox) {
        this.itemBox = itemBox;
    }

    @Override
    public ObjectArrayList<Item> getitems() {
        return (itemBox.getItems().stream().map(BoxLoot::getItem).collect(Collectors.toCollection(ObjectArrayList::new)));
    }

    @Override
    public String name() {
        return itemBox.name();
    }

    @Override
    public int placeholderItem() {
        return ItemBoxHandler.getID(itemBox);
    }
}
