package net.edge.content.minigame.warriorsguild.impl;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
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
import net.edge.locale.Position;
import net.edge.locale.loc.SquareLocation;
import net.edge.world.World;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.ItemNode;
import net.edge.world.object.ObjectNode;

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

	public static boolean enter(Player player, ObjectNode object) {
		player.move(new Position(2840, 3539, 2));
		return true;
	}

	private void updateInterface(Player player) {
		Defender defender = Defender.getNext(player);
		player.getMessages().sendItemOnInterface(34002, defender.item.getId());
		player.getMessages().sendString("@or1@" + defender.toString(), 34001);
		player.getMessages().sendWalkable(34000);
	}

	@Override
	public boolean canHit(Player player, EntityNode other, CombatType type) {
		return entered.isPresent() && other.isNpc();
	}

	@Override
	public boolean onFirstClickObject(Player player, ObjectNode object) {
		switch(object.getId()) {
			case 57647:
			case 57644:
				if(entered.isPresent()) {
					player.move(new Position(2846, player.getPosition().getY(), 2));//FIXME use the proper walk-through door function.
					entered = Optional.empty();
					player.getMessages().sendWalkable(-1);
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
	public boolean onFirstClickNpc(Player player, Npc npc) {
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
	public boolean canPickup(Player player, ItemNode node) {
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
	public void onKill(Player player, EntityNode other) {
		Defender defender = Defender.getNext(player);
		boolean rollRare = true; // 10% chance.
		if(rollRare) {
			ItemNode node = new ItemNode(defender.item, other.getPosition(), player);
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
					player.getMessages().sendCloseWindows();
				}
			}, "Well, how does it work?", "What defender will be dropped?", "Nevermind."));
			ap.chain(new PlayerDialogue(Expression.QUESTIONING, "Well how does it work?"));
			ap.chain(new NpcDialogue(4289, Expression.SAYS_NOTHING, "Ummm..."));
			ap.chain(new NpcDialogue(4289, Expression.HAPPY, "Ah yeah I remember knight!"));
			ap.chain(random);
			ap.chain(new PlayerDialogue(Expression.PRIDEFUL, "I don't think I know that.").attachAfter(() -> player.getMessages().sendCloseWindows()));
			ap.chain(new PlayerDialogue(Expression.HAPPY, "What defender will be dropped?"));
			ap.chain(new NpcDialogue(4289, Expression.SERIOUS, "The defender that will be dropped is: ", TextUtils.capitalize(Defender.getNext(player).name().replaceAll("_", " ").toLowerCase()) + ".").attachAfter(() -> player.getMessages().sendCloseWindows()));
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
				
				if(player.getCombatBuilder().isAttacking()) {
					player.getCombatBuilder().reset();
				}
				
				if(player.getCombatBuilder().getVictim() != null) {
					player.getCombatBuilder().getVictim().getCombatBuilder().reset();
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
		 * The index this enumerator is on.
		 */
		private final int index;

		/**
		 * The item for this defender.
		 */
		private final Item item;

		/**
		 * The comperator which will sort the defenders based on item ids.
		 */
		private static final Comparator<Defender> COMPERATOR = (m1, m2) -> Integer.compare(m1.index, m2.index);

		/**
		 * Constructs a new {@link Defender}.
		 * @param index {@link #index}.
		 * @param id    {@link #item}.
		 */
		Defender(int index, int id) {
			this.index = index;
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
			List<Defender> col = new ArrayList<>();

			player.getEquipment().stream().filter(Objects::nonNull).filter(item -> getDefender(item.getId()).isPresent()).forEach(item -> col.add(getDefender(item.getId()).get()));
			player.getInventory().stream().filter(Objects::nonNull).filter(item -> getDefender(item.getId()).isPresent()).forEach(item -> col.add(getDefender(item.getId()).get()));

			if(col.isEmpty()) {
				return BRONZE_DEFENDER;
			}

			return valueOf(col.stream().max(COMPERATOR).get().index + 1).orElse(DRAGON_DEFENDER);
		}

		public static Optional<Defender> valueOf(int index) {
			return VALUES.stream().filter(defender -> defender.index == index).findAny();
		}

		public static Optional<Defender> getDefender(int id) {
			return VALUES.stream().filter(defender -> defender.item.getId() == id).findAny();
		}
	}
}
