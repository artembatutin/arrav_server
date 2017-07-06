package net.edge.net.packet.outgoing;

import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.PacketShit;
import net.edge.world.node.entity.player.Player;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 6-7-2017.
 */
public final class LolcakePacket implements PacketShit {
    private final int open, overlay;

    public LolcakePacket(int open, int overlay) {
        this.open = open;
        this.overlay = overlay;
    }

    @Override
    public void write(Player player) {
        GameBuffer msg = player.getSession().getStream();
        msg.message(248);
        msg.putShort(open, ByteTransform.A);
        msg.putShort(overlay);
    }
}
