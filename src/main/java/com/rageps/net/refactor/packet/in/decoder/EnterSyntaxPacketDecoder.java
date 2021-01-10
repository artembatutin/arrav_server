package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.EnterAmountPacketPacket;
import com.rageps.net.refactor.packet.in.model.EnterSyntaxPacketPacket;
import com.rageps.util.TextUtils;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class EnterSyntaxPacketDecoder implements PacketDecoder<EnterSyntaxPacketPacket> {

    @Override
    public EnterSyntaxPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
         String text = TextUtils.hashToName(reader.getLong());
        return new EnterSyntaxPacketPacket(text);
    }
}
