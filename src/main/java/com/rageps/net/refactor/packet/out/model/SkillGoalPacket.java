package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class SkillGoalPacket extends Packet {

    private final int id, goal;

    public SkillGoalPacket(int id, int goal) {
        this.id = id;
        this.goal = goal;
    }

    public int getId() {
        return id;
    }

    public int getGoal() {
        return goal;
    }
}