package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class InterfaceAnimationPacket extends Packet {

    private final int id, animation;

    public InterfaceAnimationPacket(int id, int animation) {
        this.id = id;
        this.animation = animation;
    }

    public int getId() {
        return id;
    }

    public int getAnimation() {
        return animation;
    }
}