package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.CommandPacketPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class CommandPacketPacketDecoder implements PacketDecoder<CommandPacketPacket> {

    @Override
    public CommandPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
        String command = reader.getCString();
        return new CommandPacketPacket(command);
    }
}
