package com.rageps.net.refactor.packet.in.handler;

import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.net.refactor.packet.in.model.AdvanceDialoguePacketPacket;
import com.rageps.net.refactor.packet.in.model.MagicOnPlayerPacket;
import com.rageps.world.World;
import com.rageps.world.entity.actor.combat.CombatUtil;
import com.rageps.world.entity.actor.combat.magic.CombatSpell;
import com.rageps.world.entity.actor.combat.magic.lunars.LunarSpells;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class MagicOnPlayerPacketHandler implements PacketHandler<MagicOnPlayerPacket> {

    @Override
    public void handle(Player player, MagicOnPlayerPacket packet) {
        int index = packet.getIndex();
        int spellId = packet.getSpellID();

        Player victim = World.get().getPlayers().get(index - 1);

        if(LunarSpells.castCombatSpells(player, victim, spellId)) {
            return;
        }

        CombatSpell spell = CombatSpell.get(spellId);
        if(spell == null || index < 0 || index > World.get().getPlayers().capacity() || spellId < 0 || !CombatUtil.checkAttack(player, victim)) {
            return;
        }
        player.setSingleCast(spell);
        player.getCombat().attack(victim);
    }
}
