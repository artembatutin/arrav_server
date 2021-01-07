package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.util.ActionListener;
import com.rageps.world.entity.actor.player.Player;

import java.util.function.Function;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class EnterNamePacket extends Packet {

    private final String title;
    private final Function<String, ActionListener> action;
    private final Player player;

    public EnterNamePacket(String title, Player player, Function<String, ActionListener> action) {
        this.title = title;
        this.action = action;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public String getTitle() {
        return title;
    }

    public Function<String, ActionListener> getAction() {
        return action;
    }
}