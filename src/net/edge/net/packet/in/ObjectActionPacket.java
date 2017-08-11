package net.edge.net.packet.in;

import net.edge.Application;
import net.edge.content.minigame.MinigameHandler;
import net.edge.content.shootingstar.ShootingStarManager;
import net.edge.content.skill.firemaking.Bonfire;
import net.edge.content.skill.hunter.Hunter;
import net.edge.action.ActionContainer;
import net.edge.action.impl.ObjectAction;
import net.edge.world.entity.region.Region;
import net.edge.world.locale.Boundary;
import net.edge.world.locale.Position;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.packet.IncomingPacket;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;
import net.edge.world.entity.actor.player.assets.activity.ActivityManager;
import net.edge.world.entity.item.Item;
import net.edge.world.object.*;

import java.util.Optional;


/**
 * The message sent from the client when a player clicks an object.
 * @author Artem Batutin <artembatutin@gmail.com
 */
public final class ObjectActionPacket implements IncomingPacket {
	
	
	/*
	 * All of the object events.
	 */
	public static final ActionContainer<ObjectAction> FIRST = new ActionContainer<>();
	public static final ActionContainer<ObjectAction> SECOND = new ActionContainer<>();
	public static final ActionContainer<ObjectAction> THIRD = new ActionContainer<>();
	public static final ActionContainer<ObjectAction> FOURTH = new ActionContainer<>();
	public static final ActionContainer<ObjectAction> FIFTH = new ActionContainer<>();
	public static final ActionContainer<ObjectAction> CONSTRUCTION = new ActionContainer<>();
	
	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
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
	private void click(int action, Player player, IncomingMsg payload) {
		//Getting data.
		int objectId = payload.getMedium();
		int objectX = payload.getShort(false);
		int objectY = payload.getShort(false);
		
		//Validating data.
		Position position = new Position(objectX, objectY, player.getPosition().getZ());
		if(objectId < 0 || objectX < 0 || objectY < 0)
			return;
		player.facePosition(position);
		//construction clicks.
		if(player.getHouse().isOwnerHome()) {
			ObjectAction e = CONSTRUCTION.get(objectId);
			player.message(objectId+"");
			if(e != null) {
				player.getHouse().get().getPlan().setObjectX(objectX);
				player.getHouse().get().getPlan().setObjectY(objectY);
				Boundary boundary = new Boundary(position, ObjectDefinition.DEFINITIONS[objectId].getSize());
				player.getMovementListener().append(() -> {
					if(boundary.within(player.getPosition(), player.size(), 1)) {
						e.click(player, new DynamicObject(objectId, new Position(objectX, objectY, player.getPosition().getZ()), ObjectDirection.SOUTH, ObjectType.GENERAL_PROP, false, 0, player.getInstance()), action);
					}
				});
			}
		}
		Region reg = World.getRegions().getRegion(position).orElse(null);
		if(reg == null)
			return;
		Optional<GameObject> o = reg.getObject(objectId, position.toLocalPacked());
		if(!o.isPresent())
			return;
		final GameObject object = o.get();
		if(player.getRights().equals(Rights.ADMINISTRATOR) && Application.DEBUG)
			player.message("[OBJ"+action+"]:" + object.getId() + " - " + object.getGlobalPos().toString());
		boolean distanceIgnore = (action == 1 && (objectId == 85584 || objectId == 85532 || objectId == 85534));
		Boundary boundary = new Boundary(position, object.getDefinition().getSize());
		player.getMovementListener().append(() -> {
			if(distanceIgnore || boundary.within(player.getPosition(), player.size(), 1)) {
				switch(action) {
					case 1:
						if(!MinigameHandler.execute(player, m -> m.onFirstClickObject(player, object))) {
							return;
						}
						if(event(player, object, FIRST, 1))
							return;
						if(ShootingStarManager.get().mine(player, objectId))
							return;
						break;
					case 2:
						if(!MinigameHandler.execute(player, m -> m.onSecondClickObject(player, object)))
							return;
						if(event(player, object, SECOND, 2))
							return;
						if(ShootingStarManager.get().getShootingStar().prospect(player, objectId))
							return;
						break;
					case 3:
						if(!MinigameHandler.execute(player, m -> m.onThirdClickObject(player, object)))
							return;
						if(event(player, object, THIRD, 3))
							return;
						break;
					case 4:
						if(!MinigameHandler.execute(player, m -> m.onFourthClickObject(player, object))) {
							return;
						}
						if(event(player, object, FOURTH, 4))
							return;
						break;
					case 5:
						if(!MinigameHandler.execute(player, m -> m.onFourthClickObject(player, object))) {
							return;
						}
						if(event(player, object, FIFTH, 5))
							return;
						break;
				}
			}
		});
	}
	
	/**
	 * Tries to handle the {@link ObjectAction} action.
	 */
	private boolean event(Player player, GameObject object, ActionContainer<ObjectAction> con, int click) {
		ObjectAction e = con.get(object.getId());
		if(e != null) {
			if(e.click(player, object, click))
				return true;
		}
		return false;
	}
	
	/**
	 * Handles the spell on object for the {@code player}.
	 * @param player  the player to handle this for.
	 * @param payload the payload for reading the sent data.
	 */
	private void spellObject(Player player, IncomingMsg payload) {
		//Getting data.
		int objectId = payload.getMedium();
		int objectX = payload.getShort(false);
		int objectY = payload.getShort(false);
		int spell = payload.getShort(false);
		//Validating data.
		Position position = new Position(objectX, objectY, player.getPosition().getZ());
		if(spell < 0 || objectId < 0 || objectX < 0 || objectY < 0)
			return;
		Region reg = World.getRegions().getRegion(position).orElse(null);
		if(reg == null)
			return;
		Optional<GameObject> o = reg.getObject(objectId, position.toLocalPacked());
		if(!o.isPresent())
			return;
		//Controlling data.
		player.facePosition(position);
		final GameObject object = o.get();
		player.getMovementListener().append(() -> {
			if(new Boundary(position, object.getDefinition().getSize()).within(player.getPosition(), player.size(), 1)) {
				switch(objectId) {
					//TODO: Create event if will be used.
				}
			}
		});
	}
	
}
