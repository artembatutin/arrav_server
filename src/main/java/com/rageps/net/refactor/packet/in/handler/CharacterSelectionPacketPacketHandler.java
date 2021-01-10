package com.rageps.net.refactor.packet.in.handler;

import com.rageps.net.refactor.packet.in.model.CharacterSelectionPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerAttributes;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
import com.rageps.world.entity.actor.update.UpdateFlag;

import static com.rageps.content.achievements.Achievement.CHANGE_APPEARANCE;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class CharacterSelectionPacketPacketHandler implements PacketHandler<CharacterSelectionPacketPacket> {

    @Override
    public void handle(Player player, CharacterSelectionPacketPacket packet) {
        if(player.getActivityManager().contains(ActivityManager.ActivityType.CHARACTER_SELECTION))
            return;

        player.getAppearance().setValues(packet.getValues());
        player.getFlags().flag(UpdateFlag.APPEARANCE);
        player.closeWidget();
        if(player.getAttributeMap().getInt(PlayerAttributes.INTRODUCTION_STAGE) == 0) {
            player.getAttributeMap().set(PlayerAttributes.INTRODUCTION_STAGE, 1);
        }
        player.getActivityManager().execute(ActivityManager.ActivityType.CHARACTER_SELECTION);
        CHANGE_APPEARANCE.inc(player);
    }
}
