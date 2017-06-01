package net.edge.net.message.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.net.codec.ByteTransform;
import net.edge.net.message.InputMessageListener;
import net.edge.util.Stopwatch;
import net.edge.content.combat.magic.lunars.LunarSpells;
import net.edge.content.skill.magic.Enchanting;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;
import net.edge.world.node.entity.player.assets.activity.ActivityManager.ActivityType;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.ItemDefinition;

/**
 * The message sent from the client when a player uses magic on an inventory item.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class MagicOnItemMessage implements InputMessageListener {
	
	private final Stopwatch delay = new Stopwatch().reset();
	
	@Override
	public void handleMessage(Player player, int opcode, int size, ByteMessage payload) {
		if(player.getActivityManager().contains(ActivityType.MAGIC_ON_ITEM)) {
			return;
		}
		
		if(!delay.elapsed(1200)) {
			return;
		}
		
		int slot = payload.getShort(true, ByteTransform.NORMAL);
		int id = payload.getShort(true, ByteTransform.A);
		int interfaceId = payload.getShort(true, ByteTransform.C);
		int spellId = payload.getShort(true, ByteTransform.A);
		
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
