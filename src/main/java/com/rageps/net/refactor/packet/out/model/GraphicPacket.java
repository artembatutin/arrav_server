package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.entity.EntityState;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.locale.Position;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class GraphicPacket extends Packet {

    private final int id, level;
    private final Position position;
    private final Player player;

    public static void local(Player player, int id, Position position, int level) {
        if(player.getState() == EntityState.INACTIVE)
            return;
        player.send(new GraphicPacket(player, id, position, level));
        for(Player p : player.getLocalPlayers()) {
            if(p == null)
                continue;
            p.send(new GraphicPacket(player, id, position, level));
        }
    }

    public GraphicPacket(Player player, int id, Position position, int level) {
        this.id = id;
        this.position = position;
        this.level = level;
        this.player = player;
    }

    public Position getPosition() {
        return position;
    }

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public Player getPlayer() {
        return player;
    }
}