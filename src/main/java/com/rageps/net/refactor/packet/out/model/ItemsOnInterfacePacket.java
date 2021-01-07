package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemsOnInterfacePacket extends Packet {

    private final int id;
    private final Item[] items;
    private final int capacity;
    private final Player player;

    public ItemsOnInterfacePacket(Player player, int id, int capacity, Item... items) {
        this.id = id;
        this.player = player;
        this.items = items;
        this.capacity = capacity;
    }
    public ItemsOnInterfacePacket(Player player, int id, Item... items) {
        this(player, id, items.length, items);
    }

    public Player getPlayer() {
        return player;
    }

    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public Item[] getItems() {
        return items;
    }
}