package com.rageps.net.refactor.packet.in.handler.mob_actions;

import com.rageps.action.impl.MobAction;
import com.rageps.content.skill.summoning.Summoning;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.net.refactor.packet.in.model.mob_actions.ThirdMobActionPacket;
import com.rageps.world.World;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
import com.rageps.world.locale.Boundary;
import com.rageps.world.locale.Position;

import static com.rageps.action.ActionContainers.THIRD;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ThirdMobActionPacketHandler implements PacketHandler<ThirdMobActionPacket> {

    @Override
    public void handle(Player player, ThirdMobActionPacket packet) {


        if(player.getActivityManager().contains(ActivityManager.ActivityType.NPC_ACTION))
            return;

        int index = packet.getIndex();
        Mob mob = World.get().getMobRepository().get(index - 1);
        if(mob == null)
            return;
        Position position = mob.getPosition().copy();
        player.getMovementListener().append(() -> {
            if(new Boundary(position, mob.size()).within(player.getPosition(), player.size(), 1)) {
                player.facePosition(mob.getPosition());
                mob.facePosition(player.getPosition());
                if(Summoning.interact(player, mob, 3)) {
                    return;
                }
                MobAction e = THIRD.get(mob.getId());
                if(e != null) {
                    e.click(player, mob, 3);
                }
            }
        });
        if(player.getRights().greater(Rights.ADMINISTRATOR))
            player.message("[NPC3]:" + mob.toString());
        player.getActivityManager().execute(ActivityManager.ActivityType.NPC_ACTION);

    }
}
