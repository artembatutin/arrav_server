package net.edge.net.packet;

import net.edge.world.node.entity.player.Player;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 6-7-2017.
 */
public interface OutgoingPacket {
    
    default void onSent(Player player) {
    
    };
    
    void write(Player player);
}
