//package com.rageps.net.packet.in;
//
//import com.rageps.action.ActionContainer;
//import com.rageps.action.impl.ItemOnObjectAction;
//import com.rageps.content.minigame.MinigameHandler;
//import com.rageps.content.object.pit.FirepitManager;
//import com.rageps.content.skill.smithing.Smithing;
//import com.rageps.net.codec.ByteOrder;
//import com.rageps.net.codec.ByteTransform;
//import com.rageps.net.codec.game.GamePacket;
//import com.rageps.net.packet.IncomingPacket;
//import com.rageps.world.World;
//import com.rageps.world.entity.actor.player.Player;
//import com.rageps.world.entity.actor.player.assets.Rights;
//import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
//import com.rageps.world.entity.item.Item;
//import com.rageps.world.entity.object.GameObject;
//import com.rageps.world.entity.region.Region;
//import com.rageps.world.locale.Boundary;
//import com.rageps.world.locale.Position;
//
//import java.util.Optional;
//
///**
// * The message sent from the client when a player uses an item on an object.
// * @author Artem Batutin
// */
//public final class ItemOnObjectPacket implements IncomingPacket {
//
//
//
//	@Override
//	public void handle(Player player, int opcode, int size, GamePacket buf) {
//		if(player.getActivityManager().contains(ActivityManager.ActivityType.ITEM_ON_OBJECT)) {
//			return;
//		}
//
//		int container = buf.getShort(false);
//		int objectId = buf.getMedium();
//		int objectY = buf.getShort(true, ByteTransform.A, ByteOrder.LITTLE);
//		int slot = buf.getShort(true, ByteOrder.LITTLE);
//		int objectX = buf.getShort(true, ByteTransform.A, ByteOrder.LITTLE);
//		int itemId = buf.getShort(false);
//		Item item = player.getInventory().get(slot);
//		Position position = new Position(objectX, objectY, player.getPosition().getZ());
//		if(item == null || container != 3214 || objectId < 0 || objectY < 0 || slot < 0 || objectX < 0 || itemId < 0) {
//			return;
//		}
//		if(item.getId() != itemId) {
//			return;
//		}
//		Region reg = World.getRegions().getRegion(position);
//		if(reg == null)
//			return;
//		Optional<GameObject> o = reg.getObject(objectId, position.toLocalPacked());
//		if(!o.isPresent())
//			return;
//
//		final GameObject object = o.get();
//		if(player.getRights().greater(Rights.ADMINISTRATOR) && World.get().getEnvironment().isDebug())
//			player.message("[ItemOnObject message] objectId = " + object.toString() + ", itemId = " + item.getId());
//
//		player.facePosition(position);
//		player.getMovementListener().append(() -> {
//			if(new Boundary(position, object.getDefinition().getSize()).within(player.getPosition(), player.size(), 1)) {
//				if(!MinigameHandler.execute(player, m -> m.onItemOnObject(player, object, item))) {
//					return;
//				}
//				ItemOnObjectAction a = OBJECTS.get(objectId);
//				if(a != null) {
//					if(a.click(player, object, item, container, slot))
//						return;
//				}
//				a = ITEMS.get(item.getId());
//				if(a != null) {
//					if(a.click(player, object, item, container, slot))
//						return;
//				}
//				if(FirepitManager.get().fire(player, object, item)) {
//					return;
//				}
//				if(Smithing.openInterface(player, item, object)) {
//					return;
//				}
//			}
//		});
//		player.getActivityManager().execute(ActivityManager.ActivityType.ITEM_ON_OBJECT);
//	}
//}
