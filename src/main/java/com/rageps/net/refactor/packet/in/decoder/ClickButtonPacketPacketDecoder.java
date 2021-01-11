package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.ClickButtonPacketPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ClickButtonPacketPacketDecoder implements PacketDecoder<ClickButtonPacketPacket> {

    private boolean PROPER_READ = true;

    @Override
    public ClickButtonPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

        int button = PROPER_READ ? reader.getShort() : hexToInt(reader.getBytes(2));

        return new ClickButtonPacketPacket(button);
    }

    private static int hexToInt(byte[] data) {
        int value = 0;
        int n = 1000;
        for(byte aData : data) {
            int num = (aData & 0xFF) * n;
            value += num;
            if(n > 1) {
                n = n / 1000;
            }
        }
        return value;
    }

}
