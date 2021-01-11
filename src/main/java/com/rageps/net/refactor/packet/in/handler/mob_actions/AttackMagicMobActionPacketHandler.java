package com.rageps.net.refactor.packet.in.handler.mob_actions;

import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.net.refactor.packet.in.model.mob_actions.AttackMagicMobPacket;
import com.rageps.world.World;
import com.rageps.world.entity.actor.combat.CombatUtil;
import com.rageps.world.entity.actor.combat.magic.CombatSpell;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class AttackMagicMobActionPacketHandler implements PacketHandler<AttackMagicMobPacket> {

    @Override
    public void handle(Player player, AttackMagicMobPacket packet) {

        if(player.getActivityManager().contains(ActivityManager.ActivityType.NPC_ACTION))
            return;

        int index = packet.getIndex();
        int spellId = packet.getSpellId();

        Mob mob = World.get().getMobRepository().get(index - 1);
        CombatSpell spell = CombatSpell.get(spellId);
        if(mob == null || spell == null || !CombatUtil.checkAttack(player, mob)) {
            return;
        }
        player.getTolerance().reset();
        player.setSingleCast(spell);
        player.getCombat().attack(mob);
        player.getActivityManager().execute(ActivityManager.ActivityType.NPC_ACTION);

    }
}
