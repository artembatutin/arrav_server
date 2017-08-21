package net.edge.net.packet.in;

import net.edge.content.combat.attack.listener.CombatListenerDispatcher;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.hit.HitIcon;
import net.edge.content.combat.hit.Hitsplat;
import net.edge.content.combat.strategy.player.special.CombatSpecial;
import net.edge.content.commands.CommandDispatcher;
import net.edge.content.combat.content.MagicSpell;
import net.edge.content.combat.strategy.npc.NpcMeleeStrategy;
import net.edge.content.combat.strategy.player.PlayerMagicStrategy;
import net.edge.content.combat.strategy.player.PlayerMeleeStrategy;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.packet.IncomingPacket;
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
//				int levelreq = 50;
//				RangedAmmunition[] ammo = new RangedAmmunition[] { RangedAmmunition.RUNE_KNIFE };
//				RangedWeaponDefinition.AttackType type = RangedWeaponDefinition.AttackType.THROWN;
//				RangedWeaponDefinition def = new RangedWeaponDefinition(levelreq, type, ammo);
//				player.getCombat().setStrategy(new PlayerRangedStrategy(def));
				CombatSpecial.restore(player, 100);
			} else if (parts[0].equalsIgnoreCase("test")) {
//				CombatListenerDispatcher.load();
				player.damage(new Hit(240, Hitsplat.NORMAL, HitIcon.NONE));
			} else if (parts[0].equalsIgnoreCase("blitz")) {
//				CombatProjectileDefinition.createLoader().load();
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.ICE_BLITZ));
			} else if (parts[0].equalsIgnoreCase("man")) {
				Mob npc = Mob.getNpc(1, player.getPosition().copy().move(1, 0));
				npc.setOwner(player);
				npc.setRespawn(false);
				npc.setCurrentHealth(100_000);
				World.get().getMobs().add(npc);
			}
		}

		CommandDispatcher.execute(player, parts, command);
		player.getActivityManager().execute(ActivityManager.ActivityType.COMMAND_MESSAGE);
	}
}
