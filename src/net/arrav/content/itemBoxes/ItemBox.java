package net.arrav.content.itemBoxes;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.arrav.util.rand.Chance;
import net.arrav.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 * Represents and item which can be opened to yield a random item, or group of items.
 */
public interface ItemBox {


    /**
     * The amount of free slots required to open the box.
     * @return the amount.
     */
    int freeSlotsRequired();

    /**
     * The amount of rewards the box will give.
     * @param player The player opening the box.
     * @return the amount.
     */
    int lootCount(Player player);

    /**
     * The {@link Chance} members present in the box.
     * @return The chances.
     */
    ObjectArrayList<Chance> getChances();

    /**
     * The list of items, and their chances available from the box.
     * @return the list.
     */
    ObjectArrayList<BoxLoot> getItems();

    /**
     * The name of the box.
     * @return the name.
     */
    String name();


}
