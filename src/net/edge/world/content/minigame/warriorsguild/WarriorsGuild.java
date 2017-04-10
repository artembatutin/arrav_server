package net.edge.world.content.minigame.warriorsguild;

import net.edge.world.content.minigame.warriorsguild.impl.AnimationRoom;
import net.edge.world.content.minigame.warriorsguild.impl.CyclopsRoom;
import net.edge.world.model.locale.Position;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;
import net.edge.world.model.node.object.ObjectNode;

/**
 * The class which is responsible for executing minigame rooms of
 * the warriors guild.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class WarriorsGuild {

	/**
	 * Represents the item for a warrior guild token.
	 */
	public static final Item WARRIOR_GUILD_TOKEN = new Item(8851);

	/**
	 * Attempts to add the player to the {@link AnimationRoom}.
	 * @param player the player to handle the this functionality for.
	 * @param object the object that is being interacted with.
	 * @return {@code true} if the player entered, {@code false} otherwise.
	 */
	public static boolean enterAnimationRoom(Player player, ObjectNode object) {
		if(object.getId() != 15641 && object.getId() != 15644) {
			return false;
		}
		if(!object.getPosition().same(new Position(2855, 3546)) && !object.getPosition().same(new Position(2854, 3546))) {
			return false;
		}

		return AnimationRoom.enter(player, object);
	}

	/**
	 * Attempts to add the player to the {@link CyclopsRoom}.
	 * @param player the player to handle this functionality for.
	 * @param object the object being interacted with.
	 * @return {@code true} if the player was added, {@code false} otherwise.
	 */
	public static boolean enterCyclopsRoom(Player player, ObjectNode object) {
		if(object.getId() != 43741) {
			return false;
		}
		if(!object.getPosition().same(new Position(2839, 3537))) {
			return false;
		}
		return CyclopsRoom.enter(player, object);
	}

}
