package net.edge.net.packet;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.content.TabInterface;
import net.edge.content.skill.construction.Palette;
import net.edge.content.skill.construction.Palette.PaletteTile;
import net.edge.content.skill.construction.furniture.Furniture;
import net.edge.content.skill.construction.furniture.HotSpots;
import net.edge.content.wilderness.WildernessActivity;
import net.edge.net.codec.*;
import net.edge.util.ActionListener;
import net.edge.content.clanchat.ClanMember;
import net.edge.content.market.MarketItem;
import net.edge.locale.Position;
import net.edge.world.World;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.npc.NpcDefinition;
import net.edge.world.node.entity.npc.drop.ItemCache;
import net.edge.world.node.entity.npc.drop.NpcDrop;
import net.edge.world.node.entity.npc.drop.NpcDropTable;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.ItemNode;
import net.edge.world.object.DynamicObject;
import net.edge.world.object.ObjectDirection;
import net.edge.world.object.ObjectNode;
import net.edge.world.object.ObjectType;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static net.edge.world.node.NodeState.INACTIVE;

/**
 * The utility class used to queue {@link IncomingMsg}s to be encoded and
 * written to the Client.
 * @author lare96 <http://github.com/lare96>
 */
public final class PacketWriter {
	
	/**
	 * The player that will queue these messages.
	 */
	private final Player player;
	
	/**
	 * Creates a new {@link PacketWriter}.
	 * @param player the player that will queue these messages.
	 */
	public PacketWriter(Player player) {
		this.player = player;
	}
	
	/**
	 * The message that forces the player to view {@code id} tab.
	 * @param tab the tab to force on the player.
	 */
	public void sendForceTab(TabInterface tab) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(106);
		msg.put(tab.getOld());
		msg.put(tab.getNew());
	}
	
	/**
	 * The message that either shows or hides a layer on an interface.
	 * @param id   the interface to show or hide a layer on.
	 * @param hide if the layer should be hidden or shown.
	 */
	public void sendInterfaceLayer(int id, boolean hide) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(171);
		msg.put(hide ? 1 : 0);
		msg.putShort(id);
	}
	
	/**
	 * The message that updates a special bar with {@code amount} of special
	 * energy.
	 * @param id     the special bar to update with energy.
	 * @param amount the amount of energy to update a special bar with.
	 */
	public void sendUpdateSpecial(int id, int amount) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(70);
		msg.putShort(amount);
		msg.putShort(0, ByteOrder.LITTLE);
		msg.putShort(id, ByteOrder.LITTLE);
	}
	
	/**
	 * The messages that display {@code str} on an empty chatbox.
	 * @param str the string to display on the chatbox.
	 */
	public void sendChatboxString(String str) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		sendString(str, 357);
		sendString("Click here to continue", 358);
		sendChatInterface(356);
	}
	
	/**
	 * The message that shows player activity on pvp panel.
	 * @param pkers the players active in wilderness
	 */
	public void sendWildernessActivity(ObjectList<Player> pkers) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(150, MessageType.VARIABLE_SHORT);
		int fools = WildernessActivity.getFooledCount(player);
		msg.put(pkers.size() + fools);
		for(Player p : pkers) {
			msg.put(WildernessActivity.getX(p.getPosition()));
			msg.put(WildernessActivity.getY(p.getPosition()));
		}
		if(fools > 0) {//fooled map activity.
			while(fools != 0) {
				msg.put(WildernessActivity.fooledX());
				msg.put(WildernessActivity.fooledY());
				fools--;
			}
		}
		msg.endVarSize();
	}
	
	/**
	 * The messages that play an animation for an object that only the
	 * underlying player can see.
	 * @param position  the position the object is on.
	 * @param animation the animation to play for this object.
	 * @param type      the object type of the object.
	 * @param direction the direction this object is facing.
	 */
	public void sendObjectAnimation(Position position, int animation, ObjectType type, ObjectDirection direction) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		sendCoordinates(position);
		GameBuffer msg = player.getSession().getStream();
		msg.message(160);
		msg.put(((0 & 7) << 4) + (0 & 7), ByteTransform.S);
		msg.put((type.getId() << 2) + (direction.getId() & 3), ByteTransform.S);
		msg.putShort(animation, ByteTransform.A);
	}
	
	/**
	 * The messages that play an animation for an object that all local players
	 * can see.
	 * @param position  the position the object is on.
	 * @param animation the animation to play for this object.
	 * @param type      the object type of the object.
	 * @param direction the direction this object is facing.
	 */
	public void sendLocalObjectAnimation(Position position, int animation, ObjectType type, ObjectDirection direction) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		player.getMessages().sendObjectAnimation(position, animation, type, direction);
		player.getLocalPlayers().stream().filter(Objects::nonNull).forEach(p -> p.getMessages().sendObjectAnimation(position, animation, type, direction));
		
	}
	
	/**
	 * The message that creates a graphic that only the underlying player can
	 * see.
	 * @param id       the id of the graphic that will be created.
	 * @param position the position of the graphic that will be created.
	 * @param level    the height of the graphic that will be created.
	 */
	public void sendGraphic(int id, Position position, int level) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		sendCoordinates(position);
		GameBuffer msg = player.getSession().getStream();
		msg.message(4);
		msg.put(0);
		msg.putShort(id);
		msg.put(level);
		msg.putShort(0);
	}
	
	/**
	 * The message that creates a graphic that all local players can see.
	 * @param id       the id of the graphic that will be created.
	 * @param position the position of the graphic that will be created.
	 * @param level    the height of the graphic that will be created.
	 */
	public void sendLocalGraphic(int id, Position position, int level) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		player.getMessages().sendGraphic(id, position, level);
		player.getLocalPlayers().stream().filter(Objects::nonNull).forEach(p -> p.getMessages().sendGraphic(id, position, level));
		
	}
	
	/**
	 * The message that creates a graphic that all players can see.
	 * @param id       the id of the graphic that will be created.
	 * @param position the position of the graphic that will be created.
	 * @param level    the height of the graphic that will be created.
	 */
	public static void sendAllGraphic(int id, Position position, int level) {
		World.getRegions().getSurroundingRegions(position).forEach(r -> r.getPlayers().forEach(p -> p.getMessages().sendGraphic(id, position, level)));
	}
	
	/**
	 * The message that allows for an interface to be animated.
	 * @param id        the interface to animate on.
	 * @param animation the animation to animate the interface with.
	 */
	public void sendInterfaceAnimation(int id, int animation) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(200);
		msg.putShort(id);
		msg.putShort(animation);
	}
	
	/**
	 * The message that updates the state of the multi-combat icon.
	 * @param hide determines if the icon should be turned on or off.
	 */
	public void sendMultiIcon(boolean hide) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(61);
		msg.put(hide ? 0 : 1);
	}
	
	/**
	 * The message that sends {@code item} on a specific interface slot.
	 * @param id   the interface to display the item on.
	 * @param item the item to display on the interface.
	 * @param slot the slot on the interface to display the item on.
	 */
	public void sendItemOnInterfaceSlot(int id, Item item, int slot) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(34, MessageType.VARIABLE_SHORT);
		msg.putShort(id);
		msg.put(slot);
		msg.putShort(item == null ? 0 : item.getId() + 1);
		int am = item == null ? 0 : item.getAmount();
		if(am > 254) {
			msg.put(255);
			msg.putInt(am);
		} else {
			msg.put(am);
		}
		msg.endVarSize();
	}
	
	/**
	 * The message that sends an item model on an interface.
	 * @param id    the interface id to send the model on.
	 * @param zoom  the zoom of the model that will be sent.
	 * @param model the item model that will be sent on the interface, or in other
	 *              words the item identification.
	 */
	public void sendItemModelOnInterface(int id, int zoom, int model) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(246);
		msg.putShort(id, ByteOrder.LITTLE);
		msg.putShort(zoom).putShort(model);
		
	}
	
	/**
	 * The message that sends an array of items on an interface.
	 * @param id     the interface that the items will be sent on.
	 * @param items  the items that will be sent on the interface.
	 * @param length the amount of items that will be sent on the interface.
	 */
	public void sendItemsOnInterface(int id, Item[] items, int length) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(53, MessageType.VARIABLE_SHORT);
		msg.putShort(id);
		if(items == null) {
			msg.putShort(0);
			msg.put(0);
			msg.putShort(0, ByteTransform.A, ByteOrder.LITTLE);
		} else {
			msg.putShort(length);
			for(Item item : items) {
				if(item != null) {
					if(item.getAmount() > 254) {
						msg.put(255);
						msg.putInt(item.getAmount(), ByteOrder.INVERSE_MIDDLE);
					} else {
						msg.put(item.getAmount());
					}
					boolean noted = (id >= 270 && id <= 279) || (id == 3900);
					msg.putShort(item.getId() + (noted ? 0 : 1), ByteTransform.A, ByteOrder.LITTLE);
					if(id == 3900) {
						if(item.getValue().getPrice() > 254) {
							msg.put(255);
							msg.putInt(item.getValue().getPrice(), ByteOrder.INVERSE_MIDDLE);
						} else {
							msg.put(item.getValue().getPrice());
						}
					}
				} else {
					msg.put(0);
					msg.putShort(0, ByteTransform.A, ByteOrder.LITTLE);
					if(id == 3900) {
						msg.put(0);
					}
				}
			}
		}
		msg.endVarSize();
	}
	
	/**
	 * The message that sends an array of shop items on the panel.
	 * @param id    the interface that the items will be sent on.
	 * @param items the items that will be sent on the panel.
	 */
	public void sendShopItemsOnInterface(int id, IntArrayList items) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(53, MessageType.VARIABLE_SHORT);
		msg.putShort(id);
		if(items == null) {
			msg.putShort(0);
			msg.put(0);
			msg.putShort(0, ByteTransform.A, ByteOrder.LITTLE);
		} else {
			msg.putShort(items.size());
			for(int i : items) {
				MarketItem item = MarketItem.get(i);
				if(item != null) {
					msg.put(item.isUnlimitedStock() ? 1 : 0);
					if(!item.isUnlimitedStock()) {
						if(item.getStock() > 254) {
							msg.put(255);
							msg.putInt(item.getStock(), ByteOrder.INVERSE_MIDDLE);
						} else {
							msg.put(item.getStock());
						}
					}
					boolean noted = (id >= 270 && id <= 279) || (id == 3900);
					msg.putShort(item.getId() + (noted ? 0 : 1), ByteTransform.A, ByteOrder.LITTLE);
					if(item.getPrice() > 254) {
						msg.put(255);
						msg.putInt(item.getPrice(), ByteOrder.INVERSE_MIDDLE);
					} else {
						msg.put(item.getPrice());
					}
				} else {
					msg.put(0);
					msg.putShort(0, ByteTransform.A, ByteOrder.LITTLE);
					msg.put(0);
					msg.put(0);
				}
			}
		}
		msg.endVarSize();
	}
	
	/**
	 * The message that sends an shop price update.
	 * @param item the item that will be updated.
	 */
	public void sendShopItemPrice(MarketItem item) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(54, MessageType.VARIABLE_SHORT);
		if(item.getPrice() > 254) {
			msg.put(255);
			msg.putInt(item.getPrice(), ByteOrder.INVERSE_MIDDLE);
		} else {
			msg.put(item.getPrice());
		}
		msg.putShort(item.getId() + 1, ByteTransform.A, ByteOrder.LITTLE);
		msg.endVarSize();
	}
	
	/**
	 * The message that sends an shop price update.
	 * @param item the item that will be updated.
	 */
	public void sendShopItemStock(MarketItem item) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(55, MessageType.VARIABLE_SHORT);
		if(item.getStock() > 254) {
			msg.put(255);
			msg.putInt(item.getStock(), ByteOrder.INVERSE_MIDDLE);
		} else {
			msg.put(item.getStock());
		}
		msg.putShort(item.getId() + 1, ByteTransform.A, ByteOrder.LITTLE);
		msg.endVarSize();
	}
	
	/**
	 * The message that sends an array of items on an interface, with the length
	 * being the capacity of {@code items}.
	 * @param id    the interface that the items will be sent on.
	 * @param items the items that will be sent on the interface.
	 */
	public void sendItemsOnInterface(int id, Item[] items) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		if(id == -1)
			return;
		int length = (items == null) ? 0 : items.length;
		sendItemsOnInterface(id, items, length);
	}
	
	/**
	 * The message that sends the head model of an NPC to an interface.
	 * @param id    the interface to send the model on.
	 * @param model the NPC model that will be sent on the interface, or in other
	 *              words the NPC identification.
	 */
	public void sendNpcModelOnInterface(int id, int model) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(75);
		msg.putShort(model, ByteTransform.A, ByteOrder.LITTLE);
		msg.putShort(id, ByteTransform.A, ByteOrder.LITTLE);
		
	}
	
	/**
	 * The message that sends the head model of a player to an interface.
	 * @param id the interface to send the model on.
	 */
	public void sendPlayerModelOnInterface(int id) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(185);
		msg.putShort(id, ByteTransform.A, ByteOrder.LITTLE);
	}
	
	/**
	 * The message that causes a sidebar icon to start flashing.
	 * @param code the identification of the sidebar to flash. The code for each
	 *             of the sidebar icons are as follows:
	 *             <p>
	 *             <p>
	 *             Attack type: 0
	 *             <p>
	 *             Stats: -1
	 *             <p>
	 *             Quests: -2
	 *             <p>
	 *             Inventory: -3
	 *             <p>
	 *             Wearing: -4
	 *             <p>
	 *             Prayer: -5
	 *             <p>
	 *             Magic: -6
	 *             <p>
	 *             Empty: -7
	 *             <p>
	 *             Friends list: -8
	 *             <p>
	 *             Ignore list: -9
	 *             <p>
	 *             Log out: -10
	 *             <p>
	 *             Settings: -11
	 *             <p>
	 *             Emote: -12
	 *             <p>
	 *             Music: -13
	 *             <p>
	 *             <p>
	 */
	public void sendFlashSidebar(int code) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(24);
		msg.put(code, ByteTransform.A);
	}
	
	/**
	 * The message that changes the state of the minimap.
	 * @param code the new state of the minimap. The code for each of the minimap
	 *             states are as follows:
	 *             <p>
	 *             <p>
	 *             Normal: 0
	 *             <p>
	 *             Normal, but unclickable: 1
	 *             <p>
	 *             Blacked out: 2
	 *             <p>
	 *             <p>
	 */
	public void sendMinimapState(int code) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer str = player.getSession().getStream();
		str.message(99);
		str.put(code);
		
	}
	
	/**
	 * The packet that sends the camera angle based on the player position.
	 * @param position      the position of the camera.
	 * @param height        the height of the camera from the ground.
	 * @param movementSpeed how fast the camera will turn to the angle.
	 * @param rotationSpeed the angle the camera will turn to.
	 */
	public void sendCameraAngle(Position position, int height, int movementSpeed, int rotationSpeed) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer str = player.getSession().getStream();
		str.message(177);
		str.put(position.getLocalX(player.getPosition()));
		str.put(position.getLocalY(player.getPosition()));
		str.putShort(height);
		str.put(movementSpeed);
		str.put(rotationSpeed);
		
	}
	
	/**
	 * The packet that moves the actual camera based on the player's position.
	 * @param position      the {@code Position} to go to.
	 * @param height        the height of the camera.
	 * @param movementSpeed how fast the camera will move per cycle.
	 * @param rotationSpeed the angle the camera will turn to while moving. max 99, 100 is instant.
	 */
	public void sendCameraMovement(Position position, int height, int movementSpeed, int rotationSpeed) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(166);
		msg.put(position.getLocalX(player.getPosition()));
		msg.put(position.getLocalY(player.getPosition()));
		msg.putShort(height);
		msg.put(movementSpeed);
		msg.put(rotationSpeed);
		
	}
	
	/**
	 * The message that causes the screen and camera to shake.
	 * @param parameter the position parameter to oscillate. The position parameters
	 *                  are as follows:
	 *                  <p>
	 *                  <p>
	 *                  Camera location along world X axis (a horizontal axis, aligned
	 *                  with tool.mapviewer grid X): 0
	 *                  <p>
	 *                  Camera location along world Z axis (vertical axis): 1
	 *                  <p>
	 *                  Camera location along world Y axis (a horizontal axis, aligned
	 *                  with tool.mapviewer grid Y): 2
	 *                  <p>
	 *                  Camera orientation in world X plane w.r.t. world Z axis, i.e.
	 *                  yaw: 3
	 *                  <p>
	 *                  Camera orientation in world Z plane w.r.t. world X axis, i.e.
	 *                  pitch: 4
	 *                  <p>
	 *                  <p>
	 * @param jitter    the amount of randomization in the screen shake.
	 * @param amplitude the maximum extent of the shake.
	 * @param frequency how often the screen will shake (scaled by 100).
	 */
	public void sendCameraShake(int parameter, int jitter, int amplitude, int frequency) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		Preconditions.checkArgument(parameter <= 4);
		GameBuffer msg = player.getSession().getStream();
		msg.message(35);
		msg.put(parameter);
		msg.put(jitter);
		msg.put(amplitude);
		msg.put(frequency);
		
	}
	
	/**
	 * The message that resets the position of the camera.
	 */
	public void sendResetCameraPosition() {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(107);
		
	}
	
	/**
	 * The message that sends the system update timer. A timer showing how many
	 * seconds until a 'System Update' will appear in the lower left hand corner
	 * of the game screen. After the timer reaches 0 all players are
	 * disconnected and are unable to log in again until server is restarted.
	 * Players connecting will receive a message stating,
	 * "The server is being updated. Please wait 1 minute and try again."
	 * (unless stated otherwise).
	 * @param amount the amount of time until an update.
	 */
	public void sendSystemUpdate(int amount) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(114);
		msg.putShort(amount, ByteOrder.LITTLE);
	}
	
	/**
	 * The message that sends the underlying player's run energy percentage to
	 * the correct place.
	 */
	public void sendRunEnergy() {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(110);
		msg.put((int) player.getRunEnergy());
		
	}
	
	/**
	 * The packets that switch on or off the running orb/switch.
	 * @param running_state the state of the running orb.
	 * @an instance of this encoder
	 */
	//	public void sendRunningState(boolean running_state) {//TODO BROKEN.
	//		ByteMessage msg = ByteMessage.register();
	//		111);
	//		msg.put(running_state ? 1 : 0);
	//		player.queue(msg);
	//
	//	}
	
	/**
	 * The message that changes the color of an interface that is text.
	 * @param id    the interface identification to send the color on.
	 * @param color the new color that will be added to the interface. The color
	 *              hex codes are as follows:
	 *              <p>
	 *              <p>
	 *              Red: 0x6000
	 *              <p>
	 *              Yellow: 0x33FF66
	 *              <p>
	 *              Green: 0x3366
	 *              <p>
	 *              <p>
	 */
	public void sendInterfaceColor(int id, int color) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(122);
		msg.putShort(id, ByteTransform.A, ByteOrder.LITTLE);
		msg.putShort(color, ByteTransform.A, ByteOrder.LITTLE);
		
	}
	
	/**
	 * The message that launches a projectile that only the underlying player
	 * can see.
	 * @param position    the position of the projectile.
	 * @param offset      the offset position of the projectile.
	 * @param speed       the speed of the projectile.
	 * @param gfxMoving   the rate that projectile gfx moves in.
	 * @param startHeight the starting height of the projectile.
	 * @param endHeight   the ending height of the projectile.
	 * @param lockon      the lockon value of this projectile.
	 * @param time        the time it takes for this projectile to hit its desired
	 *                    position.
	 */
	public void sendProjectile(Position position, Position offset, int speed, int gfxMoving, int startHeight, int endHeight, int lockon, int time) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		sendCoordinates(position);
		GameBuffer msg = player.getSession().getStream();
		msg.message(117);
		msg.put(0);
		msg.put(offset.getX());
		msg.put(offset.getY());
		msg.putShort(lockon);
		msg.putShort(gfxMoving);
		msg.put(startHeight);
		msg.put(endHeight);
		msg.putShort(time);
		msg.putShort(speed);
		msg.put(16);
		msg.put(64);
		
	}
	
	/**
	 * The message that launches a projectile that all of the local players can
	 * see.
	 * @param position    the position of the projectile.
	 * @param offset      the offset position of the projectile.
	 * @param speed       the speed of the projectile.
	 * @param gfxMoving   the rate that projectile gfx moves in.
	 * @param startHeight the starting height of the projectile.
	 * @param endHeight   the ending height of the projectile.
	 * @param lockon      the lockon value of this projectile.
	 * @param time        the time it takes for this projectile to hit its desired
	 *                    position.
	 */
	public void sendAllProjectile(Position position, Position offset, int speed, int gfxMoving, int startHeight, int endHeight, int lockon, int time) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;

		// Sino: what did i say about this; filter creates a new arraylist each iteration.

		for(Player p : player.getLocalPlayers()) {
			if(p == null) {
				continue;
			}
			p.getMessages().sendProjectile(position, offset, speed, gfxMoving, startHeight, endHeight, lockon, time);
		}
//		player.getLocalPlayers().stream().filter(Objects::nonNull).forEach(p -> );
	}
	
	/**
	 * The message that changes the configuration value for a certain client
	 * setting in the form of a byte.
	 * @param id    the setting identification number.
	 * @param state the new value for the setting.
	 */
	public void sendConfig(int id, int state) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();

		if(state < Byte.MIN_VALUE || state > Byte.MAX_VALUE) {
			msg.message(87);
			msg.putShort(id, ByteOrder.LITTLE);
			msg.putInt(state, ByteOrder.MIDDLE);
			return; // Sino: y u no } else {, ik vind block statements mooier idk why
		}

		msg.message(36);
		msg.putShort(id, ByteOrder.LITTLE);
		msg.put(state);
		
	}
	
	/**
	 * The packet that sends the player's desired skill goal.
	 * @param id   the identification number of the skill.
	 * @param goal the desired level to reach.
	 */
	public void sendSkillGoal(int id, int goal) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(135);
		msg.put(id);
		msg.put(goal);
	}
	
	/**
	 * The message that sends the enter input box.
	 * @param title the title of this enter input box.
	 */
	public void sendEnterName(String title, Function<String, ActionListener> action) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(187, MessageType.VARIABLE);
		msg.putCString(title);
		msg.endVarSize();
		player.setEnterInputListener(Optional.of(action));
	}
	
	/**
	 * The message that sends an browser pop-up link.
	 * @param link the link extending the edgeville domain page to be sent.
	 */
	public void sendLink(String link) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(100, MessageType.VARIABLE);
		msg.putCString(link);
		msg.endVarSize();
	}
	
	/**
	 * The message that sends the enter amount input box.
	 * @param title the title of this enter input box.
	 */
	public void sendEnterAmount(String title, Function<String, ActionListener> listener) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return; // ik denk nu ook dat je cycle times wat beter zijn, iiedere keer je write() oproept of writeAndFlush() gaat t wat langer duren

		GameBuffer msg = player.getSession().getStream();
		msg.message(27, MessageType.VARIABLE);
		msg.putCString(title);
		msg.endVarSize();
		player.setEnterInputListener(Optional.of(listener));
	}
	
	/**
	 * Updates a single entry on the top lists.
	 * @param index the index of the top list.
	 */
	public void sendScoreInput(int index, String title, int kills, int deaths, int killstreak) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(30, MessageType.VARIABLE);
		msg.putShort(index);
		msg.putShort(kills);
		msg.putShort(deaths);
		msg.putShort(killstreak);
		msg.putCString(title);
		msg.endVarSize();
	}
	
	/**
	 * The message that spawns an object only the underlying player can see.
	 * @param object the object to spawn for the player.
	 */
	public void sendObject(ObjectNode object) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		sendCoordinates(object.getGlobalPos());
		GameBuffer msg = player.getSession().getStream();
		msg.message(151);
		msg.put(0, ByteTransform.S);
		msg.putInt(object.getId());
		msg.put((object.getObjectType().getId() << 2) + (object.getDirection().getId() & 3), ByteTransform.S);
	}
	
	/**
	 * The message that removes an object only the underlying player can see.
	 * @param object the object to remove for the player.
	 */
	public void sendRemoveObject(ObjectNode object) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		sendCoordinates(object.getGlobalPos());
		GameBuffer msg = player.getSession().getStream();
		msg.message(101);
		msg.put((object.getObjectType().getId() << 2) + (object.getDirection().getId() & 3), ByteTransform.C);
		msg.put(0);
	}
	
	/**
	 * Removes all spawned objects.
	 */
	public void removeAllObjects() {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(131);
	}
	
	/**
	 * Sending a construction panel for the player.
	 * @param spot the hotspot object clicked.
	 */
	public void sendConstruction(HotSpots spot) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		Furniture[] panel = spot.getFurnitures();
		if(panel == null || panel.length == 0)
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(130, MessageType.VARIABLE);
		msg.put(panel.length);
		for(Furniture furniture : panel) {
			msg.putShort(furniture.getItemId());
			msg.put(furniture.getLevel());
			msg.put(furniture.getRequiredItems().length);
			for(Item req : furniture.getRequiredItems()) {
				msg.putShort(req.getId());
				msg.putShort(req.getAmount());
			}
		}
		msg.endVarSize();

		player.getHouse().get().getPlan().setPanel(panel);
	}
	
	/**
	 * The messages that replace an existing object with a new one.
	 * @param object the object being replaced.
	 * @param id     the id of the new object to be set.
	 */
	public void sendReplaceObject(ObjectNode object, int id) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		sendRemoveObject(object);
		object.setId(id);
		sendObject(object);
		
	}
	
	/**
	 * The message that sends the underlying player's skill to the proper
	 * interfaces.
	 * @param id    the identification number of the skill.
	 * @param level the level reached in this skill.
	 * @param exp   the amount of experience obtained in this skill.
	 */
	public void sendSkill(int id, int level, int exp) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(134);
		msg.put(id);
		msg.putInt(exp, ByteOrder.MIDDLE);
		msg.putInt(level);
	}
	
	/**
	 * The message that closes any interfaces the underlying player has openShop.
	 */
	public void sendCloseWindows() {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;

		GameBuffer msg = player.getSession().getStream();
		msg.message(219);
		player.getDialogueBuilder().interrupt();
	}
	
	/**
	 * Sends an object construction object.
	 */
	public void sendObject_cons(int objectX, int objectY, int objectId, int face, int objectType, int height) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		Optional<ObjectDirection> dir = ObjectDirection.valueOf(face);
		Optional<ObjectType> type = ObjectType.valueOf(objectType);
		if(!dir.isPresent()) {
			if(player.getRights() == Rights.ADMINISTRATOR)
				player.message("Couldn't find direction, " + face);
			return;
		}
		if(!type.isPresent()) {
			if(player.getRights() == Rights.ADMINISTRATOR)
				player.message("Couldn't find type, " + objectType);
			return;
		}
		sendObject(new DynamicObject(objectId, new Position(objectX, objectY, height), ObjectDirection.valueOf(face).get(), ObjectType.valueOf(objectType).get(), false, 0, player.getInstance()));
	}
	
	/**
	 * Constructs a palette map. Used for construction.
	 * @param palette palette to be sent.
	 */
	public void constructMapRegion(Palette palette) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(241, MessageType.VARIABLE_SHORT);
		msg.putShort(player.getPosition().getRegionX() + 6, ByteTransform.A);
		msg.putShort(player.getPosition().getRegionY() + 6);
		for (int z = 0; z < 4; z++) {
			for (int x = 0; x < 13; x++) {
				for (int y = 0; y < 13; y++) {
					PaletteTile tile = palette.getTile(x, y, z);
					boolean b = false;
					if (x < 2 || x > 10 || y < 2 || y > 10)
						b = true;
					int toWrite = !b && tile != null ? 5 : 0;
					msg.put(toWrite);
					if(toWrite == 5) {
						int val = tile.getX() << 14 | tile.getY() << 3 | tile.getZ() << 24 | tile.getRotation() << 1;
						msg.putInt(val);
					}
				}
			}
		}
		msg.endVarSize();
	}
	
	/**
	 * The message that sends a fading to the player's screen.
	 * @param start    the start fade in duration.
	 * @param duration the duration of the fading.
	 * @param end      the end fade out duration.
	 */
	public void sendFade(int start, int duration, int end) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(80);
		msg.put(start);
		msg.put(duration);
		msg.put(end);

	}
	
	/**
	 * The message that sends a item on an interface.
	 * @param widget the interface/widget id.
	 * @param itemId the item id to sent.
	 */
	public void sendItemOnInterface(int widget, int itemId) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(82);
		msg.putInt(widget);
		msg.putInt(itemId);

	}
	
	/**
	 * The message that sends the first private messaging list load status.
	 * @param code the status of the friends list. The status for the friends
	 *             lists are as follows:
	 *             <p>
	 *             <p>
	 *             Loading: 0
	 *             <p>
	 *             Connecting: 1
	 *             <p>
	 *             Loaded: 2
	 *             <p>
	 *             <p>
	 */
	public void sendPrivateMessageListStatus(int code) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(221);
		msg.put(code);

	}
	
	/**
	 * The message that sends a clan member to the friend list.
	 * @param members the {@link ClanMember}s list to send.
	 */
	public void sendClanMemberList(ObjectList<ClanMember> members) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(51, MessageType.VARIABLE);
		msg.putShort(members.size());
		for(ClanMember m : members) {
			msg.putCString(m.getPlayer().getUsername());
			msg.put(m.isMuted() ? 1 : 0);
			msg.put(m.getRank().toIcon(player, m.getPlayer()));
		}
		msg.endVarSize();
	}
	
	/**
	 * The message that sends a clan member to the friend list.
	 * @param bans the ban list to send.
	 */
	public void sendClanBanList(ObjectList<String> bans) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(52, MessageType.VARIABLE);
		msg.putShort(bans.size());
		for(String s : bans) {
			msg.putCString(s);
		}
		msg.endVarSize();
	}
	
	/**
	 * The message that sends a player to the friend list.
	 * @param name   the player's name to add to the list.
	 * @param online if the player is online or not.
	 */
	public void sendPrivateMessageFriend(long name, boolean online) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		int value = online ? 1 : 0;
		if(value != 0)
			value += 9;
		GameBuffer msg = player.getSession().getStream();
		msg.message(50);
		msg.putLong(name);
		msg.put(value);
	}
	
	/**
	 * The message that sends a hint arrow on a position.
	 * @param position  the position to send the arrow on.
	 * @param direction the direction on the position to send the arrow on. The
	 *                  possible directions to put the arrow on are as follows:
	 *                  <p>
	 *                  <p>
	 *                  Middle: 2
	 *                  <p>
	 *                  West: 3
	 *                  <p>
	 *                  East: 4
	 *                  <p>
	 *                  South: 5
	 *                  <p>
	 *                  North: 6
	 *                  <p>
	 *                  <p>
	 */
	public void sendPositionHintArrow(Position position, int direction) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(254);
		msg.put(direction);
		msg.putShort(position.getX());
		msg.putShort(position.getY());
		msg.put(position.getZ());
	}
	
	/**
	 * The message that sends a hint arrow on {@code character}.
	 * @param character the character to send a hint arrow on.
	 */
	public void sendCharacterHintArrow(EntityNode character) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(248);
		msg.put(character.isNpc() ? 1 : 10);
		msg.putShort(character.getSlot());
		msg.put(0);
	}
	
	/**
	 * The message that sends a private message to another player.
	 * @param name    the name of the player you are sending the message to.
	 * @param rights  the rights the player sending the message has.
	 * @param message the actual message compressed into bytes.
	 * @param size    the size of the message being sent.
	 */
	public void sendPrivateMessage(long name, int rights, byte[] message, int size) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(196, MessageType.VARIABLE);
		msg.putLong(name);
		msg.putInt(player.getPrivateMessage().getLastMessage().getAndIncrement());
		msg.put(rights);
		msg.putBytes(message, size);
		msg.endVarSize();
	}
	
	/**
	 * The message that sends the players current coordinates to the client.
	 * @param position the coordinates to send to the client.
	 */
	private void sendCoordinates(Position position) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		if(position == null)
			return;
		if(player.getLastRegion() == null)
			player.setLastRegion(player.getPosition().copy());
		GameBuffer msg = player.getSession().getStream();
		msg.message(85);
		msg.put(position.getY() - (player.getLastRegion().getRegionY() * 8), ByteTransform.C);
		msg.put(position.getX() - (player.getLastRegion().getRegionX() * 8), ByteTransform.C);
	}
	
	/**
	 * The message that opens a walkable interface for the underlying player.
	 * @param id the identification of the interface to openShop.
	 */
	public void sendWalkable(int id) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(208);
		msg.putInt(id);
	}
	
	/**
	 * The message that spawns a ground item.
	 * @param item the ground item to spawn.
	 */
	public void sendGroundItem(ItemNode item) {
		if(item == null)
			return;
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		sendCoordinates(item.getPosition());
		GameBuffer msg = player.getSession().getStream();
		msg.message(44);
		msg.putShort(item.getItem().getId(), ByteTransform.A, ByteOrder.LITTLE);
		msg.putShort(item.getItem().getAmount());
		msg.put(0);
	}
	
	/**
	 * The message that removes a ground item.
	 * @param item the ground item to remove.
	 */
	public void sendRemoveGroundItem(ItemNode item) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		sendCoordinates(item.getPosition());
		GameBuffer msg = player.getSession().getStream();
		msg.message(156);
		msg.put(0, ByteTransform.S);
		msg.putShort(item.getItem().getId());
	}
	
	/**
	 * The message that sends the player context menus.
	 * @param slot   the slot for the option to be placed in.
	 * @param top    the condition if the menu should on top.
	 * @param option the string literal option to display.
	 */
	public void sendContextMenu(int slot, boolean top, String option) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(104, MessageType.VARIABLE);
		msg.put(slot, ByteTransform.C);
		msg.put(top ? 1 : 0, ByteTransform.A);
		msg.putCString(option);
		msg.endVarSize();
	}
	
	/**
	 * The message that attaches text to an interface.
	 * @param text the text to attach to the interface.
	 * @param id   the identification for the interface.
	 */
	public void sendString(String text, int id) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(126, MessageType.VARIABLE_SHORT);
		msg.putCString(text);
		msg.putShort(id, ByteTransform.A);
		msg.endVarSize();
		msg.endVarSize();
	}
	
	/**
	 * The message that opens an interface and displays another interface over
	 * the inventory area.
	 * @param open    the interface to openShop.
	 * @param overlay the interface to send on the inventory area.
	 */
	public void sendInventoryInterface(int open, int overlay) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(248);
		msg.putShort(open, ByteTransform.A);
		msg.putShort(overlay);
	}
	
	/**
	 * The message that opens an interface for underlying player.
	 * @param id the identification number of the interface to openShop.
	 */
	public void sendInterface(int id) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(97);
		msg.putShort(id);
	}
	
	/**
	 * The message that sends the underlying player a message to the chatbox.
	 * @param message the message to send.
	 */
	public void sendMessage(String message) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(253, MessageType.VARIABLE);
		msg.putCString(message);
		msg.endVarSize();
	}
	
	/**
	 * Sends the npc information opening the panel in a case we don't have the npc required.
	 * @param id   the id of the npc to be sent.
	 * @param drop the drop of this npc, may be null.
	 */
	public void sendNpcInformation(int id, NpcDropTable drop) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(121, MessageType.VARIABLE_SHORT);
		msg.putInt(id);
		if(id != 0) {
			if(id > NpcDefinition.DEFINITIONS.length)
				return;
			NpcDefinition def = NpcDefinition.DEFINITIONS[id];
			if(def == null)
				return;
			msg.putShort(drop.getCommon() == null ? 0 : drop.getCommon().size());
			if(drop.getCommon() != null) {
				for(ItemCache c : drop.getCommon()) {
					msg.putShort(c.ordinal());
				}
			}
			msg.putShort(drop.getUnique() == null ? 0 : drop.getUnique().size());
			if(drop.getUnique() != null) {
				for(NpcDrop d : drop.getUnique()) {
					msg.putShort(d.getId());
					msg.putShort(d.getMinimum());
					msg.putShort(d.getMaximum());
					msg.put(d.getChance().ordinal());
				}
			}
		}
		msg.endVarSize();
	}
	
	/**
	 * The message that sends an interface to a certain sidebar.
	 * @param id interface sending.
	 * @param tab The tab being sent.
	 */
	public void sendSidebarInterface(int id, TabInterface tab) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(71);
		msg.putShort(id);
		msg.put(tab.getOld(), ByteTransform.A);
		msg.put(tab.getNew(), ByteTransform.A);
	}
	
	/**
	 * The message that sends the current map region.
	 */
	public void sendMapRegion() {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		player.setLastRegion(player.getPosition().copy());
		player.setUpdates(true, false);
		player.setUpdateRegion(true);
		GameBuffer msg = player.getSession().getStream();
		msg.message(73);
		msg.putShort(player.getPosition().getRegionX() + 6, ByteTransform.A);
		msg.putShort(player.getPosition().getRegionY() + 6);
	}
	
	/**
	 * The message that disconnects the underlying player.
	 */
	public void sendLogout() {
		if(!player.isHuman())
			return;
		World.get().queueLogout(player);
		if(player.getSession().getChannel().isActive()) {
			GameBuffer msg = player.getSession().getStream();
			msg.message(109, MessageType.VARIABLE_SHORT);
			msg.endVarSize();
		}
	}
	
	/**
	 * The message that sends the slot and membership status to the client.
	 */
	public void sendDetails() {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(249);
		msg.put(1, ByteTransform.A);
		msg.putShort(player.getSlot(), ByteTransform.A, ByteOrder.LITTLE);
	}
	
	/**
	 * The message that sends a clan chat message.
	 */
	public void sendClanMessage(String author, String message, String clanName, Rights rank) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(217, MessageType.VARIABLE);
		msg.putCString(author);
		msg.putCString(message);
		msg.putCString(clanName);
		msg.putShort(rank.getProtocolValue());
		msg.endVarSize();
	}
	
	/**
	 * The message that sends a yell message.
	 */
	public void sendYell(String author, String message, Rights rank) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(210, MessageType.VARIABLE);
		msg.putCString(author);
		msg.putCString(message);
		msg.putShort(rank.getProtocolValue());
		msg.endVarSize();
	}
	
	/**
	 * The message that shows an interface in the chat box.
	 * @param id the identification of interface to show.
	 */
	public void sendChatInterface(int id) {
		if(player.getState() == INACTIVE || !player.isHuman())
			return;
		GameBuffer msg = player.getSession().getStream();
		msg.message(164);
		msg.putShort(id, ByteOrder.LITTLE);
	}
	
}
