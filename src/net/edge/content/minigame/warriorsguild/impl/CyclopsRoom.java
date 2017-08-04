package net.edge.content.minigame.warriorsguild.impl;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.net.packet.out.SendInterfaceItem;
import net.edge.net.packet.out.SendWalkable;
import net.edge.task.Task;
import net.edge.util.TextUtils;
import net.edge.util.rand.RandomUtils;
import net.edge.content.combat.CombatType;
import net.edge.content.dialogue.Conversation;
import net.edge.content.dialogue.Expression;
import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.dialogue.impl.PlayerDialogue;
import net.edge.content.dialogue.test.DialogueAppender;
import net.edge.content.minigame.Minigame;
import net.edge.content.minigame.warriorsguild.GuildRoom;
import net.edge.content.minigame.warriorsguild.WarriorsGuild;
import net.edge.world.locale.Position;
import net.edge.world.locale.loc.SquareLocation;
import net.edge.world.World;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.GroundItem;
import net.edge.world.entity.item.Item;
import net.edge.world.object.GameObject;

import java.util.*;

/**
 * The class which represents functionality for the cyclops room.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class CyclopsRoom extends GuildRoom {

	/**
	 * Represents the location of the cyclops room.
	 */
	public static final SquareLocation LOCATION = new SquareLocation(2837, 3533, 2877, 3557, 2);

	/**
	 * The flag which determines if the player has entered the room.
	 */
	private Optional<CyclopsRoomTask> entered = Optional.empty();

	/**
	 * Constructs a new {@link CyclopsRoom}.
	 */
	public CyclopsRoom() {
		super("CYCLOPS_ROOM", GuildRoomType.CYCLOPS_ROOM);
	}

	public static boolean enter(Player player, GameObject object) {
		player.move(new Position(2840, 3539, 2));
		return true;
	}

	private void updateInterface(Player player) {
		Defender defender = Defender.getNext(player);
		player.out(new SendInterfaceItem(34002, defender.item.getId()));
		player.text(34001, "@or1@" + defender.toString());
		player.out(new SendWalkable(34000));
	}

	@Override
	public boolean canHit(Player player, Actor other, CombatType type) {
		return entered.isPresent() && other.isMob();
	}

	@Override
	public boolean onFirstClickObject(Player player, GameObject object) {
		switch(object.getId()) {
			case 57647:
			case 57644:
				if(entered.isPresent()) {
					player.move(new Position(2846, player.getPosition().getY(), 2));//FIXME use the proper walk-through door function.
					entered = Optional.empty();
					player.out(new SendWalkable(-1));
					return false;
				}
				if(!player.getInventory().contains(new Item(WarriorsGuild.WARRIOR_GUILD_TOKEN.getId(), 200))) {
					player.getDialogueBuilder().append(new NpcDialogue(4289, Expression.SERIOUS, "You must have a minimum of 200 warriors guild", "tokens to enter the room."));
					return false;
				}
				onEnter(player);
				return false;
			case 57641:
				player.move(new Position(2841, 3538, 0));
				player.setMinigame(Optional.empty());
				return false;
			default:
				return false;
		}
	}

	@Override
	public boolean onFirstClickNpc(Player player, Mob mob) {
		player.getDialogueBuilder().send(new KamfreenaConversation());
		return false;
	}

	@Override
	public void onLogin(Player player) {

	}

	@Override
	public void onLogout(Player player) {
		if(entered.isPresent()) {
			entered = Optional.empty();
			player.setMinigame(Optional.empty());
			player.move(new Position(2844, 3540));
		}
	}
	
	@Override
	public void onTeleportBefore(Player player, Position position) {
		if(entered.isPresent()) {
			entered = Optional.empty();
			player.setMinigame(Optional.empty());
		}
	}

	@Override
	public boolean canPickup(Player player, GroundItem node) {
		return true;
	}

	@Override
	public void onPickup(Player player, Item item) {
		updateInterface(player);
	}

	@Override
	public void onEnter(Player player) {
		if(!entered.isPresent()) {
			player.getInventory().remove(new Item(WarriorsGuild.WARRIOR_GUILD_TOKEN.getId(), 200));
			entered = Optional.of(new CyclopsRoomTask(this, player));
			World.get().submit(entered.get());
			player.move(new Position(2847, player.getPosition().getY(), 2));//FIXME use the proper walk-through door function.
			updateInterface(player);
		}
	}

	@Override
	public void onKill(Player player, Actor other) {
		Defender defender = Defender.getNext(player);
		boolean rollRare = true; // 10% chance.
		if(rollRare) {
			GroundItem node = new GroundItem(defender.item, other.getPosition(), player);
			node.getRegion().register(node);
		}
	}

	@Override
	public boolean contains(Player player) {
		return LOCATION.inLocation(player.getPosition());
	}

	@Override
	public Optional<Minigame> copy() {
		return Optional.of(new CyclopsRoom());
	}

	/**
	 * The conversation a player can have with kamfreena.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class KamfreenaConversation implements Conversation {

		@Override
		public void send(Player player, int index) {

		}

		private static final NpcDialogue[] RANDOM_MESSAGES = new NpcDialogue[]{new NpcDialogue(4289, Expression.HEAD_SWAYS_TALKS_FAST, "When you aim for perfection, you discover", "it's a moving target."), new NpcDialogue(4289, Expression.HEAD_SWAYS_TALKS_FAST, "Patience and persistence can bring down the", "tallest tree."), new NpcDialogue(4289, Expression.HEAD_SWAYS_TALKS_FAST, "Be master of mind rather than mastered by mind."), new NpcDialogue(4289, Expression.HEAD_SWAYS_TALKS_FAST, "A reflection on a pool of water does not", "reveal its depth."), new NpcDialogue(4289, Expression.HEAD_SWAYS_TALKS_FAST, "Life isn't fair, that doesn't mean ", "you can't win.")};

		@Override
		public DialogueAppender dialogues(Player player) {
			DialogueAppender ap = new DialogueAppender(player);
			NpcDialogue random = RandomUtils.random(RANDOM_MESSAGES);
			ap.chain(new NpcDialogue(4289, Expression.QUESTIONING, "Knight! Stop talking to me, you know how this works."));
			ap.chain(new OptionDialogue(t -> {
				if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
					player.getDialogueBuilder().advance();
				} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
					player.getDialogueBuilder().go(6);
				} else {
					player.closeWidget();
				}
			}, "Well, how does it work?", "What defender will be dropped?", "Nevermind."));
			ap.chain(new PlayerDialogue(Expression.QUESTIONING, "Well how does it work?"));
			ap.chain(new NpcDialogue(4289, Expression.SAYS_NOTHING, "Ummm..."));
			ap.chain(new NpcDialogue(4289, Expression.HAPPY, "Ah yeah I remember knight!"));
			ap.chain(random);
			ap.chain(new PlayerDialogue(Expression.PRIDEFUL, "I don't think I know that.").attachAfter(() -> player.closeWidget()));
			ap.chain(new PlayerDialogue(Expression.HAPPY, "What defender will be dropped?"));
			ap.chain(new NpcDialogue(4289, Expression.SERIOUS, "The defender that will be dropped is: ", TextUtils.capitalize(Defender.getNext(player).name().replaceAll("_", " ").toLowerCase()) + ".").attachAfter(() -> player.closeWidget()));
			return ap;
		}
	}

	/**
	 * The task that runs for players whom are in the cyclops room.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private static final class CyclopsRoomTask extends Task {

		/**
		 * The player this task is running for.
		 */
		private final Player player;

		/**
		 * The instance of the room this task is dependent of.
		 */
		private final CyclopsRoom room;

		/**
		 * Constructs a new {@link CyclopsRoomTask}.
		 * @param room   {@link #room}.
		 * @param player {@link #player}.
		 */
		public CyclopsRoomTask(CyclopsRoom room, Player player) {
			super(100);
			this.room = room;
			this.player = player;
		}

		@Override
		protected void onSequence() {
			if(!room.entered.isPresent()) {
				player.move(new Position(2846, RandomUtils.nextBoolean() ? 3541 : 3540, 2));
				this.cancel();
				return;
			}
		}

		@Override
		protected void execute() {
			if(!player.getInventory().contains(new Item(WarriorsGuild.WARRIOR_GUILD_TOKEN.getId(), 25))) {
				player.move(new Position(2846, RandomUtils.nextBoolean() ? 3541 : 3540, 2));
				player.getDialogueBuilder().append(new NpcDialogue(4289, Expression.CALM, "Your time is up..."));
				
				if(player.getCombat().isAttacking()) {
					player.getCombat().reset();
				}
				
				if(player.getCombat().getVictim() != null) {
					player.getCombat().getVictim().getCombat().reset();
				}
				
				this.cancel();
				return;
			}
			player.message("25 of your tokens crumble away...");
			player.getInventory().remove(new Item(WarriorsGuild.WARRIOR_GUILD_TOKEN.getId(), 25));
		}

		@Override
		protected void onCancel() {
			if(room.entered.isPresent()) {
				room.entered = Optional.empty();
			}
		}

	}

	/**
	 * The enumerated type whose elements represent a set of constants used to
	 * calculate the defenders that will be dropped next.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public enum Defender {
		BRONZE_DEFENDER(0, 8844),
		IRON_DEFENDER(1, 8845),
		STEEL_DEFENDER(2, 8846),
		BLACK_DEFENDER(3, 8847),
		MITHRIL_DEFENDER(4, 8848),
		ADAMANT_DEFENDER(5, 8849),
		RUNE_DEFENDER(6, 8850),
		DRAGON_DEFENDER(7, 20072);

		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<Defender> VALUES = Sets.immutableEnumSet(EnumSet.allOf(Defender.class));

		/**
		 * The item for this defender.
		 */
		private final Item item;

		/**
		 * Constructs a new {@link Defender}.
		 * @param id    {@link #item}.
		 */
		Defender(int index, int id) {
			this.item = new Item(id);
		}

		@Override
		public String toString() {
			return TextUtils.capitalize(this.name().replaceAll("_", " ").replaceAll("DEFENDER", "def.").toLowerCase());
		}

		/**
		 * Gets the next defender that will be dropped for the specified {@code player}.
		 * @param player the player to calculate the defender for.
		 * @return the next {@link Defender} that should be obtained by the player.
		 */
		public static Defender getNext(Player player) {
			for(Defender defender : VALUES) {
				if(!player.getEquipment().contains(defender.item) && !player.getInventory().contains(defender.item)) {
					return defender;
				}
			}
			return BRONZE_DEFENDER;
		}
		
	}
}
