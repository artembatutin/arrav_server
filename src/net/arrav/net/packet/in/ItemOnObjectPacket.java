package net.arrav.net.packet.in;

import net.arrav.Arrav;
import net.arrav.action.ActionContainer;
import net.arrav.action.impl.ItemOnObjectAction;
import net.arrav.content.minigame.MinigameHandler;
import net.arrav.content.object.pit.FirepitManager;
import net.arrav.content.skill.smithing.Smithing;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.IncomingPacket;
import net.arrav.world.World;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;
import net.arrav.world.entity.actor.player.assets.activity.ActivityManager;
import net.arrav.world.entity.item.Item;
import net.arrav.world.entity.object.GameObject;
import net.arrav.world.entity.region.Region;
import net.arrav.world.locale.Boundary;
import net.arrav.world.locale.Position;

import java.util.Optional;

/**
 * The message sent from the client when a player uses an item on an object.
 * @author Artem Batutin
 */
public final class ItemOnObjectPacket implements IncomingPacket {
	
	public static final ActionContainer<ItemOnObjectAction> OBJECTS = new ActionContainer<>();
	public static final ActionContainer<ItemOnObjectAction> ITEMS = new ActionContainer<>();
	
	@Override
	public void handle(Player player, int opcode, int size, GamePacket buf) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.ITEM_ON_OBJECT)) {
			return;
		}
		
		int container = buf.getShort(false);
		int objectId = buf.getMedium();
		int objectY = buf.getShort(true, ByteTransform.A, ByteOrder.LITTLE);
		int slot = buf.getShort(true, ByteOrder.LITTLE);
		int objectX = buf.getShort(true, ByteTransform.A, ByteOrder.LITTLE);
		int itemId = buf.getShort(false);
		Item item = player.getInventory().get(slot);
		Position position = new Position(objectX, objectY, player.getPosition().getZ());
		if(item == null || container != 3214 || objectId < 0 || objectY < 0 || slot < 0 || objectX < 0 || itemId < 0) {
			return;
		}
		if(item.getId() != itemId) {
			return;
		}
		Region reg = World.getRegions().getRegion(position);
		if(reg == null)
			return;
		Optional<GameObject> o = reg.getObject(objectId, position.toLocalPacked());
		if(!o.isPresent())
			return;
		
		final GameObject object = o.get();
		if(player.getRights().greater(Rights.ADMINISTRATOR) && Arrav.DEBUG)
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
