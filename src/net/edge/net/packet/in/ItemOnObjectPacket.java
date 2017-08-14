package net.edge.net.packet.in;

import net.edge.Application;
import net.edge.action.ActionContainer;
import net.edge.action.impl.ItemOnObjectAction;
import net.edge.content.minigame.MinigameHandler;
import net.edge.content.object.pit.FirepitManager;
import net.edge.content.skill.smithing.Smithing;
import net.edge.world.entity.region.Region;
import net.edge.world.locale.Boundary;
import net.edge.world.locale.Position;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.packet.IncomingPacket;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;
import net.edge.world.entity.actor.player.assets.activity.ActivityManager;
import net.edge.world.entity.item.Item;
import net.edge.world.object.GameObject;

import java.util.Optional;

/**
 * The message sent from the client when a player uses an item on an object.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class ItemOnObjectPacket implements IncomingPacket {
	
	public static final ActionContainer<ItemOnObjectAction> OBJECTS = new ActionContainer<>();
	public static final ActionContainer<ItemOnObjectAction> ITEMS = new ActionContainer<>();
	
	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
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
		Region reg = World.getRegions().getRegion(position).orElse(null);
		if(reg == null)
			return;
		Optional<GameObject> o = reg.getObject(objectId, position.toLocalPacked());
		if(!o.isPresent())
			return;
		
		final GameObject object = o.get();
		if(player.getRights().greater(Rights.ADMINISTRATOR) && Application.DEBUG)
			player.message("[ItemOnObject message] objectId = " + object.toString() + ", itemId = " + item.getId());
		
		player.facePosition(position);
		player.getMovementListener().append(() -> {
			if(new Boundary(position, object.getDefinition().getSize()).within(player.getPosition(), player.size(), 1)) {
				if(!MinigameHandler.execute(player, m -> m.onItemOnObject(player, object, item))) {
					return;
				}
				ItemOnObjectAction a = OBJECTS.get(objectId);
				if(a != null) {
					if(a.click(player, object, item, container, slot))
						return;
				}
				a = ITEMS.get(item.getId());
				if(a != null) {
					if(a.click(player, object, item, container, slot))
						return;
				}
				if(FirepitManager.get().fire(player, object, item)) {
					return;
				}
				if(Smithing.openInterface(player, item, object)) {
					return;
				}
			}
		});
		player.getActivityManager().execute(ActivityManager.ActivityType.ITEM_ON_OBJECT);
	}
}
