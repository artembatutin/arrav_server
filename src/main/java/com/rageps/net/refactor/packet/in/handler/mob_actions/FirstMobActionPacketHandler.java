package com.rageps.net.refactor.packet.in.handler.mob_actions;

import com.rageps.action.impl.MobAction;
import com.rageps.content.item.pets.Pet;
import com.rageps.content.minigame.MinigameHandler;
import com.rageps.content.skill.summoning.Summoning;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.net.refactor.packet.in.model.AdvanceDialoguePacketPacket;
import com.rageps.net.refactor.packet.in.model.mob_actions.FirstMobActionPacket;
import com.rageps.world.World;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
import com.rageps.world.locale.Boundary;
import com.rageps.world.locale.Position;

import static com.rageps.action.ActionContainers.FIRST;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class FirstMobActionPacketHandler implements PacketHandler<FirstMobActionPacket> {

    @Override
    public void handle(Player player, FirstMobActionPacket packet) {

        if(player.getActivityManager().contains(ActivityManager.ActivityType.NPC_ACTION))
            return;

        int index = packet.getIndex();

        Mob mob = World.get().getMobRepository().get(index - 1);
        if(mob == null)
            return;
        Position position = mob.getPosition().copy();
        if(mob.getId() == 4650) {
            player.getMovementQueue().smartWalk(new Position(3079, 3508));
        }
        player.getMovementListener().append(() -> {
            if(new Boundary(position, mob.size()).within(player.getPosition(), player.size(), mob.getId() == 4650 ? 3 : 1)) {
                player.facePosition(mob.getPosition());
                mob.facePosition(player.getPosition());
                if(!MinigameHandler.execute(player, m -> m.onFirstClickNpc(player, mob))) {
                    return;
                }
                if(Summoning.interact(player, mob, 1)) {
                    return;
                }
                if(Pet.pickup(player, mob)) {
                    return;
                }
                MobAction e = FIRST.get(mob.getId());
                if(e != null) {
                    e.click(player, mob, 1);
                }
            }
        });
        if(player.getRights().greater(Rights.ADMINISTRATOR) && World.get().getEnvironment().isDebug())
            player.message("[NPC1]:" + mob.toString());
        player.getActivityManager().execute(ActivityManager.ActivityType.NPC_ACTION);

    }
}
