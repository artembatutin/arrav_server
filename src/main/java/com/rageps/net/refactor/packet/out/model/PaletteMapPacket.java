package com.rageps.net.refactor.packet.out.model;

import com.rageps.content.skill.construction.Palette;
import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class PaletteMapPacket extends Packet {

    private final Palette palette;

    private final Player player;

    public PaletteMapPacket(Player player, Palette palette) {
        this.player = player;
        this.palette = palette;
    }

    public Player getPlayer() {
        return player;
    }

    public Palette getPalette() {
        return palette;
    }
}