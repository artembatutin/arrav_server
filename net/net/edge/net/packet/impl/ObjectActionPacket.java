package net.edge.net.packet.impl;

import net.edge.content.minigame.MinigameHandler;
import net.edge.content.skill.firemaking.Bonfire;
import net.edge.content.skill.hunter.Hunter;
import net.edge.event.EventContainer;
import net.edge.event.impl.ObjectEvent;
import net.edge.locale.Boundary;
import net.edge.locale.Position;
import net.edge.net.codec.ByteMessage;
import net.edge.net.packet.PacketReader;
import net.edge.world.World;
import net.edge.world.node.entity.npc.impl.gwd.GodwarsFaction;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.activity.ActivityManager;
import net.edge.world.node.item.Item;
import net.edge.world.object.ObjectNode;

import java.util.Optional;


/**
 * The message sent from the client when a player clicks an object.
 * @author lare96 <http://github.com/lare96>
 */
public final class ObjectActionPacket implements PacketReader {
	
	public static final EventContainer<ObjectEvent> FIRST = new EventContainer<>();
	
	public static final EventContainer<ObjectEvent> SECOND = new EventContainer<>();
	
	public static final EventContainer<ObjectEvent> THIRD = new EventContainer<>();
	
	public static final EventContainer<ObjectEvent> FOURTH = new EventContainer<>();
	
	public static final EventContainer<ObjectEvent> FIFTH = new EventContainer<>();
	
	@Override
	public void handle(Player player, int opcode, int size, ByteMessage payload) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.OBJECT_ACTION))
			return;
		switch(opcode) {
			case 132:
				click(1, player, payload);
				break;
			case 252:
				click(2, player, payload);
				break;
			case 70:
				click(3, player, payload);
				break;
			case 234:
				click(4, player, payload);
				break;
			case 228:
				click(5, player, payload);
				break;
			case 35:
				spellObject(player, payload);
				break;
		}
		player.getActivityManager().execute(ActivityManager.ActivityType.OBJECT_ACTION);
	}
	
	/**
	 * Handles object click for the {@code player}.
	 * @param action  the action number.
	 * @param player  the player to handle this for.
	 * @param payload the payload for reading the sent data.
	 */
	private void click(int action, Player player, ByteMessage payload) {
		//Getting data.
		int objectId = payload.getMedium();
		int objectX = payload.getShort(false);
		int objectY = payload.getShort(false);
		
		//Validating data.
		Position position = new Position(objectX, objectY, player.getPosition().getZ());
		if(objectId < 0 || objectX < 0 || objectY < 0)
			return;
		Optional<ObjectNode> o = World.getRegions().getRegion(position).getObject(objectId, position.toLocalPacked());
		if(!o.isPresent())
			return;
		player.facePosition(position);
		final ObjectNode object = o.get();
		
		boolean distanceIgnore = (action == 1 && (objectId == 85584 || objectId == 85532 || objectId == 85534));
		player.getMovementListener().append(() -> {
			if(distanceIgnore || new Boundary(position, object.getDefinition().getSize()).within(player.getPosition(), player.size(), 1)) {
				switch(action) {
					case 1:
						if(!MinigameHandler.execute(player, m -> m.onFirstClickObject(player, object))) {
							return;
						}
						if(event(player, object, FIRST))
							return;
						if(Bonfire.addLogs(player, new Item(-100), object, true))
							return;
						if(World.getShootingStarEvent().mine(player, objectId))
							return;
						if(Hunter.claim(player, object))
							return;
						if(Hunter.pickup(player, object))
							return;
						if(GodwarsFaction.enterChamber(player, objectId))
							return;
						break;
					case 2:
						if(!MinigameHandler.execute(player, m -> m.onSecondClickObject(player, object)))
							return;
						if(event(player, object, SECOND))
							return;
						if(World.getShootingStarEvent().getShootingStar().prospect(player, objectId))
							return;
						break;
					case 3:
						if(!MinigameHandler.execute(player, m -> m.onThirdClickObject(player, object)))
							return;
						if(event(player, object, THIRD))
							return;
						break;
					case 4:
						if(!MinigameHandler.execute(player, m -> m.onFourthClickObject(player, object))) {
							return;
						}
						if(event(player, object, FOURTH))
							return;
						break;
					case 5:
						if(!MinigameHandler.execute(player, m -> m.onFourthClickObject(player, object))) {
							return;
						}
						if(event(player, object, FIFTH))
							return;
						break;
				}
			}
		});
	}
	
	/**
	 * Tries to handle the {@link ObjectEvent} action.
	 */
	private boolean event(Player player, ObjectNode object, EventContainer<ObjectEvent> con) {
		ObjectEvent e = con.get(object.getId());
		if(e != null) {
			if(e.click(player, object, 1))
				return true;
		}
		return false;
	}
	
	/**
	 * Handles the spell on object for the {@code player}.
	 * @param player  the player to handle this for.
	 * @param payload the payload for reading the sent data.
	 */
	private void spellObject(Player player, ByteMessage payload) {
		//Getting data.
		int objectId = payload.getMedium();
		int objectX = payload.getShort(false);
		int objectY = payload.getShort(false);
		int spell = payload.getShort(false);
		//Validating data.
		Position position = new Position(objectX, objectY, player.getPosition().getZ());
		if(spell < 0 || objectId < 0 || objectX < 0 || objectY < 0)
			return;
		Optional<ObjectNode> o = World.getRegions().getRegion(position).getObject(objectId, position.toLocalPacked());
		if(!o.isPresent())
			return;
		//Controlling data.
		player.facePosition(position);
		final ObjectNode object = o.get();
		player.getMovementListener().append(() -> {
			if(new Boundary(position, object.getDefinition().getSize()).within(player.getPosition(), player.size(), 1)) {
				switch(objectId) {
					//TODO: Create event if will be used.
				}
			}
		});
	}
	
}
