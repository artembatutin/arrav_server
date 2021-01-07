package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.content.wilderness.WildernessActivity;
import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.meta.PacketType;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.WildernessActivityPacket;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class WildernessActivityPacketEncoder implements PacketEncoder<WildernessActivityPacket> {

    @Override
    public GamePacket encode(WildernessActivityPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(150, PacketType.VARIABLE_SHORT);
        int fools = message.getFools();
        builder.put(message.getPkers().size() + fools);
        for(Player p : message.getPkers()) {
            builder.put(WildernessActivity.getX(p.getPosition()));
            builder.put(WildernessActivity.getY(p.getPosition()));
        }
        if(fools > 0) {//fooled map activity.
            while(fools != 0) {
                builder.put(WildernessActivity.fooledX());
                builder.put(WildernessActivity.fooledY());
                fools--;
            }
        }
        return builder.toGamePacket();
    }
}
