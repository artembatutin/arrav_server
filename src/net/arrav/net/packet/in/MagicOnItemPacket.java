package net.arrav.net.packet.in;

import net.arrav.content.skill.magic.Enchanting;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.IncomingPacket;
import net.arrav.util.Stopwatch;
import net.arrav.world.entity.actor.combat.magic.lunars.LunarSpells;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;
import net.arrav.world.entity.actor.player.assets.activity.ActivityManager.ActivityType;
import net.arrav.world.entity.item.Item;
import net.arrav.world.entity.item.ItemDefinition;

/**
 * The message sent from the client when a player uses magic on an inventory item.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class MagicOnItemPacket implements IncomingPacket {
	
	private final Stopwatch delay = new Stopwatch().reset();
	
	@Override
	public void handle(Player player, int opcode, int size, GamePacket buf) {
		if(player.getActivityManager().contains(ActivityType.MAGIC_ON_ITEM)) {
			return;
		}
		
		if(!delay.elapsed(1200)) {
			return;
		}
		
		int slot = buf.getShort(true, ByteTransform.NORMAL);
		int id = buf.getShort(true, ByteTransform.A);
		int interfaceId = buf.getShort(true, ByteTransform.C);
		int spellId = buf.getShort(true, ByteTransform.A);
		
		if(slot < 0 || interfaceId < 0 || spellId < 0 || id < 0 || id > ItemDefinition.DEFINITIONS.length)
			return;
		
		Item item = player.getInventory().get(slot);
		
		if(!Item.valid(item)) {
			return;
		}
		
		if(player.getRights().greater(Rights.ADMINISTRATOR)) {
			player.message("interface = " + interfaceId + ", spell = " + spellId + "");
		}
		
		delay.reset();
		if(Enchanting.cast(player, item, interfaceId, spellId, slot)) {
			return;
		}
		if(LunarSpells.castItemSpells(player, item, spellId, interfaceId)) {
			return;
		}
		player.getActivityManager().execute(ActivityType.MAGIC_ON_ITEM);
	}
	
}
