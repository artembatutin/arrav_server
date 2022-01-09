package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class MagicOnGroundItemPacketPacket extends Packet {

    private final int x, y, itemId, spellId;

    public MagicOnGroundItemPacketPacket(int x, int y, int itemId, int spellId) {
        this.x = x;
        this.y = y;
        this.itemId = itemId;
        this.spellId = spellId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getSpellId() {
        return spellId;
    }
}