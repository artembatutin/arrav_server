package net.edge.net.packet.in;

import net.edge.content.commands.CommandDispatcher;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.packet.IncomingPacket;
import net.edge.world.World;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.combat.strategy.player.special.CombatSpecial;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.content.skill.prayer.PrayerBook;
import net.edge.world.entity.actor.player.assets.Rights;
import net.edge.world.entity.actor.player.assets.activity.ActivityManager;

/**
 * The message that is sent from the client when the player chats anything
 * beginning with '::'.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class CommandPacket implements IncomingPacket {

    @Override
    public void handle(Player player, int opcode, int size, IncomingMsg payload) {
        if (player.getActivityManager().contains(ActivityManager.ActivityType.COMMAND_MESSAGE)) {
            return;
        }

        String command = payload.getCString();
        String[] parts = command.toLowerCase().split(" ");

        if (player.getRights() == Rights.ADMINISTRATOR) {
            if (parts[0].equalsIgnoreCase("spec")) {
                CombatSpecial.restore(player, 100);
                player.setPrayerBook(PrayerBook.CURSES);
                return;
            } else if (parts[0].equalsIgnoreCase("man")) {
                Mob npc = Mob.getNpc(1, player.getPosition().copy().move(1, 0));
                npc.getMovementQueue().setLockMovement(true);
                npc.setOwner(player);
                npc.setRespawn(false);
                npc.setCurrentHealth(100_000);
                World.get().getMobs().add(npc);
                return;
            } else if (parts[0].equalsIgnoreCase("hit")) {
                player.damage(new Hit(250));
                return;
            } else if (parts[0].equalsIgnoreCase("men")) {
                for (int y = 0; y < 3; y++) {
                    for (int x = 0; x < 3; x++) {
                        Mob npc = Mob.getNpc(1, player.getPosition().copy().move(x - 1, y + 1));
                        npc.getMovementQueue().setLockMovement(true);
                        npc.setOwner(player);
                        npc.setRespawn(false);
                        npc.setCurrentHealth(100_000);
                        World.get().getMobs().add(npc);
                    }
                }
                return;
            }
        }

        CommandDispatcher.execute(player, parts, command);
        player.getActivityManager().execute(ActivityManager.ActivityType.COMMAND_MESSAGE);
    }
}
