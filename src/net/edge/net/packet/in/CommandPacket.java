package net.edge.net.packet.in;

import net.edge.content.commands.CommandDispatcher;
import net.edge.content.newcombat.strategy.npc.NpcMeleeStrategy;
import net.edge.content.newcombat.strategy.player.melee.LongswordOrScimitarWeapon;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.packet.IncomingPacket;
import net.edge.world.World;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.activity.ActivityManager;

/**
 * The message that is sent from the client when the player chats anything
 * beginning with '::'.
 * @author lare96 <http://github.com/lare96>
 */
public final class CommandPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.COMMAND_MESSAGE))
			return;
		String command = payload.getCString();
		String[] parts = command.toLowerCase().split(" ");

		if (parts[0].startsWith("shit")) {
			Mob npc = Mob.getNpc(1677, player.getPosition().copy().move(1, 0));
			npc.setOwner(player);
			npc.setRespawn(false);
			World.get().getMobs().add(npc);

			player.getNewCombat().setStrategy(new LongswordOrScimitarWeapon());
			npc.getNewCombat().setStrategy(new NpcMeleeStrategy());

			player.getNewCombat().attack(npc);
		}

		CommandDispatcher.execute(player, parts, command);
		player.getActivityManager().execute(ActivityManager.ActivityType.COMMAND_MESSAGE);
	}
}
