package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.AdvanceDialoguePacketPacket;
import com.rageps.net.refactor.packet.in.model.InterfaceClickPacketPacket;

/**
 *@author Tamatea <tamateea@gmail.com>
 */
public class InterfaceClickPacketDecoder implements PacketDecoder<InterfaceClickPacketPacket> {

    @Override
    public InterfaceClickPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
        return new InterfaceClickPacketPacket();
    }
}
