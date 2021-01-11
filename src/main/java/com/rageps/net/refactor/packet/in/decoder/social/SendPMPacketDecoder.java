package com.rageps.net.refactor.packet.in.decoder.social;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.social.RemoveIgnorePacket;
import com.rageps.net.refactor.packet.in.model.social.SendPMPacket;
import com.rageps.util.ChatCodec;

/**
 *@author Tamatea <tamateea@gmail.com>
 */
public class SendPMPacketDecoder implements PacketDecoder<SendPMPacket> {

    @Override
    public SendPMPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
        long to = reader.getLong();


        final byte[] input = reader.getBytes(packet.getLength() - Long.BYTES);

        final String decoded = ChatCodec.decode(input);
        final byte[] compressed = ChatCodec.encode(decoded);
        return new SendPMPacket(to, decoded, compressed);
    }
}
