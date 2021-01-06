package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.CloseInterfacePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class CloseInterfacePacketEncoder implements PacketEncoder<CloseInterfacePacket> {

    @Override
    public GamePacket encode(CloseInterfacePacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(219);
        return builder.toGamePacket();
    }

    @Override
    public boolean onSent(CloseInterfacePacket message) {
        message.getDialogueBuilder().interrupt();
        return true;
    }
}
