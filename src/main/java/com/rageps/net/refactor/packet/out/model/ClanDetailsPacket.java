package com.rageps.net.refactor.packet.out.model;

import com.rageps.content.clanchannel.ClanRank;
import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ClanDetailsPacket extends Packet {

    private final String author, message, clanName;
    private final ClanRank rank;

    public ClanDetailsPacket(String author, String message, String clanName, ClanRank rank) {
        this.author = author;
        this.message = message;
        this.clanName = clanName;
        this.rank = rank;
    }
    public ClanDetailsPacket(String message, String clan, ClanRank rank) {
        this("", message, clan, rank);
    }

    public ClanDetailsPacket(String message, String clan) {
        this("", message, clan, ClanRank.MEMBER);
    }

    public String getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }

    public ClanRank getRank() {
        return rank;
    }

    public String getClanName() {
        return clanName;
    }
}