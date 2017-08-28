package net.edge.net.packet.in;

import net.edge.content.TabInterface;
import net.edge.content.commands.CommandDispatcher;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.packet.IncomingPacket;
import net.edge.task.Task;
import net.edge.util.json.impl.CombatProjectileLoader;
import net.edge.world.World;
import net.edge.world.entity.EntityState;
import net.edge.world.entity.actor.combat.hit.CombatHit;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.combat.strategy.player.special.CombatSpecial;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;
import net.edge.world.entity.actor.player.assets.Spellbook;
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

		if(player.getRights() == Rights.ADMINISTRATOR) {
			if(parts[0].equalsIgnoreCase("spec")) {
				CombatSpecial.restore(player, 100);
				player.setSpellbook(Spellbook.ANCIENT);
				TabInterface.MAGIC.sendInterface(player, Spellbook.ANCIENT.getId());
				return;
			} else if(parts[0].equalsIgnoreCase("test2")) {
				new CombatProjectileLoader().load();
				return;
			} else if(parts[0].equalsIgnoreCase("test")) {
				new Task(1, false) {
					int ticks = 0;

					@Override
					protected void execute() {
						if(ticks == 0) {
							CombatHit[] hits = new CombatHit[1];
							for(int index = 0; index < hits.length; index++) {
								hits[index] = new CombatHit(new Hit(index * 10 + 10), 2, 0);
							}
							player.getCombat().submitHits(player, hits);
						}
						if(ticks == 1) {
							CombatHit[] hits = new CombatHit[2];
							for(int index = 0; index < hits.length; index++) {
								hits[index] = new CombatHit(new Hit(index * 10 + 20), 1, 0);
							}
							player.getCombat().submitHits(player, hits);
							cancel();
						}
						ticks++;
					}
				}.submit();
				return;
			} else if(parts[0].equalsIgnoreCase("man")) {
				Mob npc = Mob.getNpc(1, player.getPosition().copy().move(1, 0));
				npc.setOwner(player);
				npc.setRespawn(false);
				npc.setCurrentHealth(100_000);
				World.get().getMobs().add(npc);
				return;
			} else if(parts[0].equalsIgnoreCase("men")) {
				for(int i = 0; i < 3; i++) {
					Mob npc = Mob.getNpc(1, player.getPosition().copy().move(i - 1, 0));
					npc.getMovementQueue().setLockMovement(true); //nice
					npc.setOwner(player);
					npc.setRespawn(false);
					npc.setCurrentHealth(100_000);
					World.get().getMobs().add(npc);
				}
				return;
			} else if(parts[0].equalsIgnoreCase("kbd")) {
				Mob npc = Mob.getNpc(50, player.getPosition().copy().move(3, 0));
				npc.setOwner(player);
				npc.setRespawn(false);
				World.get().getMobs().add(npc);
				npc.setState(EntityState.ACTIVE);
				return;
			}
		}

		CommandDispatcher.execute(player, parts, command);
		player.getActivityManager().execute(ActivityManager.ActivityType.COMMAND_MESSAGE);
	}
}
