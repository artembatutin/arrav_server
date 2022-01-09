package com.rageps.content.rewardslist;

import com.rageps.world.entity.item.Item;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;


/**
 * Represents a drop table or list items rewardable by XX event/content to be displayed on the
 * reward lookup interface.
 * @author Tamatea <tamateea@gmail.com>
 */
public interface Rewardable {

    /**
     * Gets the list of items to be displayed on the interface.
     * @return The items.
     */
    ObjectArrayList<Item> getitems();

    /**
     * The name of the drop table displayed on the interface.
     * @return The name.
     */
    String name();


    /**
     * The placeholder item that is represented on the table column of the interface.
     * @return
     */
    int placeholderItem();
}
