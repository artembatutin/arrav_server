package net.edge.net.packet.in;

import net.edge.content.combat.attack.FormulaFactory;
import net.edge.content.combat.content.MagicSpell;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.strategy.player.PlayerMagicStrategy;
import net.edge.content.combat.strategy.player.special.CombatSpecial;
import net.edge.content.commands.CommandDispatcher;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.packet.IncomingPacket;
import net.edge.task.Task;
import net.edge.world.World;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;
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

		if (player.getRights() == Rights.ADMINISTRATOR) {
			if (parts[0].equalsIgnoreCase("spec")) {
				CombatSpecial.restore(player, 100);
			} else if (parts[0].equalsIgnoreCase("test2")) {
				player.heal();
				CombatHit[] hits = new CombatHit[8];
				for (int index = 0; index < hits.length; index++) {
					hits[index] = new CombatHit(new Hit(index * 10 + 10), 1, 0);
				}
				player.getCombat().submitHits(player, hits);
			} else if (parts[0].equalsIgnoreCase("test")) {


				player.heal();
				new Task(0, true) {
					int ticks = 0;
					@Override
					protected void execute() {
//						if (ticks == 3) {
//							player.getCombat().submitHits(player, new CombatHit(new Hit(60), 1, 0));
//							cancel();
//						}

						if (ticks == 1) {
							player.getCombat().submitHits(player, new CombatHit(new Hit(10), 1, 0));
							cancel();
						}

						if (ticks == 0) {
							CombatHit[] hits = new CombatHit[2];
							for (int index = 0; index < hits.length; index++) {
								hits[index] = new CombatHit(new Hit(index * 10 + 20), 1, 0);
							}
							player.getCombat().submitHits(player, hits);
						}

						ticks++;
					}
				}.submit();


			} else if (parts[0].equalsIgnoreCase("blitz")) {
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.ICE_BLITZ));
			} else if (parts[0].equalsIgnoreCase("man")) {
				Mob npc = Mob.getNpc(1, player.getPosition().copy().move(1, 0));
				npc.getMovementQueue().setLockMovement(true);
				npc.setOwner(player);
				npc.setRespawn(false);
				npc.setCurrentHealth(100_000);
				World.get().getMobs().add(npc);
				return;
			}
		}

		CommandDispatcher.execute(player, parts, command);
		player.getActivityManager().execute(ActivityManager.ActivityType.COMMAND_MESSAGE);
	}
}
