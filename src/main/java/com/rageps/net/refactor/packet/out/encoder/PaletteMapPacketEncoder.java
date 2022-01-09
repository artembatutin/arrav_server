package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.content.skill.construction.Palette;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.meta.PacketType;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.PaletteMapPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class PaletteMapPacketEncoder implements PacketEncoder<PaletteMapPacket> {

    @Override
    public GamePacket encode(PaletteMapPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(241, PacketType.VARIABLE_SHORT);
        builder.putShort(message.getPlayer().getPosition().getRegionX() + 6, DataTransformation.ADD);
        builder.putShort(message.getPlayer().getPosition().getRegionY() + 6);
        for(int z = 0; z < 4; z++) {
            for(int x = 0; x < 13; x++) {
                for(int y = 0; y < 13; y++) {
                    Palette.PaletteTile tile = message.getPalette().getTile(x, y, z);
                    boolean b = false;
                    if(x < 2 || x > 10 || y < 2 || y > 10)
                        b = true;
                    int toWrite = !b && tile != null ? 5 : 0;
                    builder.put(toWrite);
                    if(toWrite == 5) {
                        int val = tile.getX() << 14 | tile.getY() << 3 | tile.getZ() << 24 | tile.getRotation() << 1;
                        builder.putInt(val);
                    }
                }
            }
        }
        return builder.toGamePacket();
    }
}
