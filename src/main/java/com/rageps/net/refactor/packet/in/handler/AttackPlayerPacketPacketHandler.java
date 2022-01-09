package com.rageps.net.refactor.packet.in.handler;

import com.rageps.content.minigame.Minigame;
import com.rageps.content.minigame.MinigameHandler;
import com.rageps.net.refactor.packet.in.model.AttackPlayerPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.entity.actor.combat.CombatUtil;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
import com.rageps.world.entity.item.container.session.ExchangeSession;
import com.rageps.world.entity.item.container.session.ExchangeSessionManager;
import com.rageps.world.entity.item.container.session.impl.DuelSession;

import java.util.Optional;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class AttackPlayerPacketPacketHandler implements PacketHandler<AttackPlayerPacketPacket> {

    @Override
    public void handle(Player player, AttackPlayerPacketPacket packet) {
        if(player.getActivityManager().contains(ActivityManager.ActivityType.ATTACK_PLAYER))
            return;

        Player victim = packet.getVictim();

        if(!CombatUtil.checkAttack(player, victim))
            return;

        player.getCombat().attack(victim);
        player.getActivityManager().execute(ActivityManager.ActivityType.ATTACK_PLAYER);
    }

}
