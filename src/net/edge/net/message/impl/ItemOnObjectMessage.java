package net.edge.net.message.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.message.InputMessageListener;
import net.edge.world.World;
import net.edge.world.content.minigame.MinigameHandler;
import net.edge.world.content.skill.cooking.CookingData;
import net.edge.world.content.skill.crafting.JewelleryMoulding;
import net.edge.world.content.skill.crafting.PotClaying;
import net.edge.world.content.skill.crafting.Spinning;
import net.edge.world.content.skill.firemaking.Bonfire;
import net.edge.world.content.skill.prayer.PrayerBoneAltar;
import net.edge.world.content.skill.smithing.Smithing;
import net.edge.world.locale.Boundary;
import net.edge.world.locale.Position;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;
import net.edge.world.node.entity.player.assets.activity.ActivityManager;
import net.edge.world.node.item.Item;
import net.edge.world.object.ObjectNode;

import java.util.Optional;

/**
 * The message sent from the client when a player uses an item on an object.
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemOnObjectMessage implements InputMessageListener {
	
	// TODO: When cache reading is done, check position of objects.
	
	@Override
	public void handleMessage(Player player, int opcode, int size, ByteMessage payload) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.ITEM_ON_OBJECT)) {
			return;
		}
		
		int container = payload.getShort(false);
		int objectId = payload.getMedium();
		int objectY = payload.getShort(true, ByteTransform.A, ByteOrder.LITTLE);
		int slot = payload.getShort(true, ByteOrder.LITTLE);
		int objectX = payload.getShort(true, ByteTransform.A, ByteOrder.LITTLE);
		int itemId = payload.getShort(false);
		Item item = player.getInventory().get(slot);
		Position position = new Position(objectX, objectY, player.getPosition().getZ());
		
		if(item == null || container != 3214 || objectId < 0 || objectY < 0 || slot < 0 || objectX < 0 || itemId < 0) {
			return;
		}
		if(item.getId() != itemId) {
			return;
		}
		
		Optional<ObjectNode> o = World.getRegions().getRegion(position).getObject(objectId, position.toLocalPacked());
		if(!o.isPresent())
			return;
		final ObjectNode object = o.get();
		
		if(player.getRights().greater(Rights.ADMINISTRATOR))
			player.message("[ItemOnObject message] objectId = " + object.toString() + ", itemId = " + item.getId());
		
		player.facePosition(position);
		player.getMovementListener().append(() -> {
			if(new Boundary(position, object.getDefinition().getSize()).within(player.getPosition(), player.size(), 1)) {
				if(!MinigameHandler.execute(player, m -> m.onItemOnObject(player, object, item))) {
					return;
				}
				if(JewelleryMoulding.openInterface(player, item, object)) {
					return;
				}
				if(Bonfire.addLogs(player, item, object, false)) {
					return;
				}
				if(World.getFirepitEvent().fire(player, object, item)) {
					return;
				}
				if(PrayerBoneAltar.produce(player, itemId, object)) {
					return;
				}
				if(Smithing.openInterface(player, item, object)) {
					return;
				}
				if(Spinning.openInterface(player, item, object)) {
					return;
				}
				if(PotClaying.openInterface(player, item, object)) {
					return;
				}
				
				switch(objectId) {
					case 114:
					case 2728:
					case 25730:
						CookingData c = CookingData.forItem(item);
						if(c == null)
							return;
						player.getAttr().get("cooking_usingStove").set(true);
						player.getAttr().get("cooking_data").set(c);
						player.getAttr().get("cooking_object").set(object);
						c.openInterface(player);
						break;
					case 2732:
						CookingData d = CookingData.forItem(item);
						if(d == null)
							return;
						player.getAttr().get("cooking_usingStove").set(false);
						player.getAttr().get("cooking_data").set(d);
						player.getAttr().get("cooking_object").set(object);
						d.openInterface(player);
						break;
				}
				
			}
		});
		player.getActivityManager().execute(ActivityManager.ActivityType.ITEM_ON_OBJECT);
	}
}
