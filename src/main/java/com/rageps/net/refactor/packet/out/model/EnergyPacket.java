package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class EnergyPacket extends Packet {

    private final int energy;

    public EnergyPacket(int energy) {
        this.energy = energy;
    }

    public int getEnergy() {
        return energy;
    }
}