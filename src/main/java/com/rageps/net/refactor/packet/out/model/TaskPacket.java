package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class TaskPacket extends Packet {


    private final String task;

    public TaskPacket(String task) {
        this.task = task;
    }

    public String getTask() {
        return task;
    }
}