package net.edge.content.minigame.warriorsguild;

import net.edge.content.minigame.warriorsguild.impl.AnimationRoom;
import net.edge.content.minigame.warriorsguild.impl.CyclopsRoom;
import net.edge.action.impl.ObjectAction;
import net.edge.world.locale.Position;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.object.GameObject;

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
	
	public static void event() {
		ObjectAction c =new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				return object.getGlobalPos().same(new Position(2839, 3537)) && CyclopsRoom.enter(player, object);
			}
		};
		c.registerFirst(43741);
		ObjectAction a = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				return !(!object.getGlobalPos().same(new Position(2855, 3546)) && !object.getGlobalPos().same(new Position(2854, 3546))) && AnimationRoom.enter(player, object);
			}
		};
		a.registerFirst(15641);
		a.registerFirst(15644);
	}
	
}
