package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.entity.actor.player.assets.Rights;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class YellPacket extends Packet {


    private final String author, message;
    private final Rights rank;

    public YellPacket(String author, String message, Rights rank) {
        this.author = author;
        this.message = message;
        this.rank = rank;
    }

    public String getMessage() {
        return message;
    }

    public Rights getRank() {
        return rank;
    }

    public String getAuthor() {
        return author;
    }
}