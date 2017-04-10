package net.edge.world.content.minigame.warriorsguild;

import net.edge.world.content.dialogue.impl.NpcDialogue;
import net.edge.world.content.item.PotionConsumable;
import net.edge.world.content.minigame.Minigame;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.entity.player.assets.PrayerBook;
import net.edge.world.content.dialogue.Expression;
import net.edge.world.content.item.FoodConsumable;
import net.edge.world.content.skill.prayer.Prayer;

/**
 * The abstract class which defines the basics each guild room accesses.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class GuildRoom extends Minigame {

	/**
	 * Constructs a new {@link GuildRoom}.
	 * @param type {@link #type}.
	 */
	public GuildRoom(String name, GuildRoomType type) {
		super(name, MinigameSafety.DEFAULT, MinigameType.NORMAL);
	}

	@Override
	public boolean canPray(Player player, Prayer prayer) {
		if(prayer.getType().equals(PrayerBook.CURSES)) {
			player.getDialogueBuilder().append(new NpcDialogue(4286, Expression.ANGRY, "What? You're not allowed to use those kind of", "black magicks in here!"));
			return false;
		}
		return true;
	}

	@Override
	public final boolean canEat(Player player, FoodConsumable food) {
		return true;
	}

	@Override
	public final boolean canPot(Player player, PotionConsumable potion) {
		return true;
	}

	@Override
	public final boolean canLogout(Player player) {
		return true;
	}

	/**
	 * The enumerated type whose elements represent a set of constants
	 * which define the type of this {@link GuildRoom}.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public enum GuildRoomType {
		CYCLOPS_ROOM,
		ANIMATION_ROOM,
		DUMMY_ROOM,
		CATAPULT_ROOM,
		SHOT_PUT_ROOM,
		JIMMYS_CHALLENGE;
	}
}
