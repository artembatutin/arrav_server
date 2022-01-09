package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.CharacterSelectionPacketPacket;
import com.rageps.world.entity.actor.player.PlayerAppearance;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class CharacterSelectionPacketPacketDecoder implements PacketDecoder<CharacterSelectionPacketPacket> {

    /**
     * The valid colors for the character selection message.
     */
    private static final int[][] VALID_COLORS =
            {{0, 11}, // hair color
            {0, 15}, // torso color
            {0, 15}, // legs color
            {0, 5}, // feet color
            {0, 7} // skin color
    };

    @Override
    public CharacterSelectionPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);


        int cursor = 0;
        int[] values = new int[13];
        int gender = reader.get();
        if(gender != PlayerAppearance.GENDER_FEMALE && gender != PlayerAppearance.GENDER_MALE)
            return null;
        values[cursor++] = gender;

        for(int i = 0; i < 7; i++) {
            int value = reader.getShort();
            values[cursor++] = value;
        }
        for(int[] VALID_COLOR : VALID_COLORS) {
            int value = reader.getShort();
            if((value < VALID_COLOR[0]) || (value > VALID_COLOR[1])) {
                return null ;
            }
            values[cursor++] = value;
        }

        return new CharacterSelectionPacketPacket(values);
    }
}
