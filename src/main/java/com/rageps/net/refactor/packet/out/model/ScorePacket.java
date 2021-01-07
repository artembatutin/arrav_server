package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ScorePacket extends Packet {

    private final String title;
    private final int index, kills, deaths, killstreak;

    public ScorePacket(int index, String title, int kills, int deaths, int killstreak) {
        this.index = index;
        this.title = title;
        this.kills = kills;
        this.deaths = deaths;
        this.killstreak = killstreak;
    }

    public String getTitle() {
        return title;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getIndex() {
        return index;
    }

    public int getKills() {
        return kills;
    }

    public int getKillstreak() {
        return killstreak;
    }
}