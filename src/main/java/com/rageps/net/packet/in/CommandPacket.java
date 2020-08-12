package com.rageps.net.packet.in;

import com.rageps.command.CommandDispatcher;
import com.rageps.content.skill.prayer.PrayerBook;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.IncomingPacket;
import com.rageps.world.World;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.combat.strategy.player.special.CombatSpecial;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;

/**
 * The message that is sent from the client when the player chats anything
 * beginning with '::'.
 * @author lare96 <http://github.com/lare96>
 */
public final class CommandPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, GamePacket buf) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.COMMAND_MESSAGE)) {
			return;
		}
		
		String command = buf.getCString();
		String[] parts = command.toLowerCase().split(" ");
		
		if(player.getRights() == Rights.ADMINISTRATOR) {
			if(parts[0].equalsIgnoreCase("spec")) {
				CombatSpecial.restore(player, 100);
				player.setPrayerBook(PrayerBook.CURSES);
				return;
			} else if(parts[0].equalsIgnoreCase("man")) {
				Mob npc = Mob.getNpc(1, player.getPosition().copy().move(1, 0));
				npc.getMovementQueue().setLockMovement(true);
				npc.setOwner(player);
				npc.setRespawn(false);
				npc.setCurrentHealth(100_000);
				World.get().getMobRepository().add(npc);
				return;
			} else if(parts[0].equalsIgnoreCase("hit")) {
				player.damage(new Hit(250));
				return;
			} else if(parts[0].equalsIgnoreCase("men")) {
				for(int y = 0; y < 3; y++) {
					for(int x = 0; x < 3; x++) {
						Mob npc = Mob.getNpc(1, player.getPosition().copy().move(x - 1, y + 1));
						npc.getMovementQueue().setLockMovement(true);
						npc.setOwner(player);
						npc.setRespawn(false);
						npc.setCurrentHealth(100_000);
						World.get().getMobRepository().add(npc);
					}
				}
				return;
			}
		}
		
		CommandDispatcher.execute(player, parts, command);
		player.getActivityManager().execute(ActivityManager.ActivityType.COMMAND_MESSAGE);
	}
}
