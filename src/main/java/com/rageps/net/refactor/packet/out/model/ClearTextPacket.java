package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.entity.actor.player.Player;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ClearTextPacket extends Packet {

    private final int start, count;

    Int2ObjectArrayMap<String> interfaceTexts;

    public ClearTextPacket(int start, int count, Int2ObjectArrayMap<String> interfaceTexts) {
        this.start = start;
        this.count = count;
        this.interfaceTexts = interfaceTexts;
    }

    public int getCount() {
        return count;
    }

    public Int2ObjectArrayMap<String> getInterfaceTexts() {
        return interfaceTexts;
    }

    public int getStart() {
        return start;
    }
}