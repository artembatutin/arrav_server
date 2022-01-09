package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;

/**
 *  The message that is sent from the client when the player chats anything
 *  beginning with '::'.
 * @author Tamatea <tamateea@gmail.com>
 */
public class CommandPacketPacket extends Packet {

    private final String command;

    public CommandPacketPacket(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}