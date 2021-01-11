package com.rageps.net.refactor.packet.in.handler;

import com.rageps.content.skill.magic.Enchanting;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.refactor.packet.in.model.MagicOnItemPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.entity.actor.combat.magic.lunars.LunarSpells;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.ItemDefinition;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class MagicOnItemPacketPacketHandler implements PacketHandler<MagicOnItemPacketPacket> {

    @Override
    public void handle(Player player, MagicOnItemPacketPacket packet) {

        if(player.getActivityManager().contains(ActivityManager.ActivityType.MAGIC_ON_ITEM)) {
            return;
        }

        if(!player.magicOnItemDelay.elapsed(1200)) {
            return;
        }

        int slot = packet.getSlot();
        int id = packet.getId();
        int interfaceId = packet.getInterfaceId();
        int spellId = packet.getSpellId();

        if(slot < 0 || interfaceId < 0 || spellId < 0 || id < 0 || id > ItemDefinition.DEFINITIONS.length)
            return;

        Item item = player.getInventory().get(slot);

        if(!Item.valid(item)) {
            return;
        }

        if(player.getRights().greater(Rights.ADMINISTRATOR)) {
            player.message("interface = " + interfaceId + ", spell = " + spellId + "");
        }

        player.magicOnItemDelay.reset();
        if(Enchanting.cast(player, item, interfaceId, spellId, slot)) {
            return;
        }
        if(LunarSpells.castItemSpells(player, item, spellId, interfaceId)) {
            return;
        }
        player.getActivityManager().execute(ActivityManager.ActivityType.MAGIC_ON_ITEM);


    }
}
