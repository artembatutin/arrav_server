package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class SkillPacket extends Packet {

    private final int id, level, exp;

    private final boolean login;

    public SkillPacket(int id, int level, int exp, boolean login) {
        this.id = id;
        this.level = level;
        this.exp = exp;
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public boolean isLogin() {
        return login;
    }
}