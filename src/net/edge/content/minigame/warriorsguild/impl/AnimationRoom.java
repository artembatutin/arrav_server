package net.edge.content.minigame.warriorsguild.impl;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.content.dialogue.impl.StatementDialogue;
import net.edge.content.minigame.Minigame;
import net.edge.content.minigame.warriorsguild.GuildRoom;
import net.edge.task.LinkedTaskSequence;
import net.edge.world.Animation;
import net.edge.world.World;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.CombatType;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.move.ForcedMovement;
import net.edge.world.entity.actor.move.ForcedMovementDirection;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.activity.ActivityManager;
import net.edge.world.entity.item.GroundItem;
import net.edge.world.entity.item.Item;
import net.edge.world.locale.Position;
import net.edge.world.locale.loc.SquareLocation;
import net.edge.world.object.GameObject;

import java.util.EnumSet;
import java.util.Optional;

import static net.edge.content.achievements.Achievement.ANIMATOR;
import static net.edge.world.entity.item.ItemIdentifiers.WARRIOR_GUILD_TOKEN;

/**
 * The class which represents functionality for the animation room.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class AnimationRoom extends GuildRoom {
	
	/**
	 * Represents the location of the animation room.
	 */
	public static final SquareLocation LOCATION = new SquareLocation(2849, 3534, 2861, 3545, 0);
	
	/**
	 * The animated armour that should be spawned.
	 */
	private Optional<AnimatedArmour> armour = Optional.empty();
	
	/**
	 * Constructs a new {@link AnimationRoom}.
	 */
	public AnimationRoom() {
		super("ANIMATION_ROOM", GuildRoomType.ANIMATION_ROOM);
	}
	
	/**
	 * Attempts to enter the animation room.
	 * @param player the player this task is dependant of.
	 * @param object the object that represents the door.
	 * @return {@code true} if the player entered, {@code false} otherwise.
	 */
	public static boolean enter(Player player, GameObject object) {
		player.move(new Position(player.getPosition().getX(), 3545));
		return true;
	}
	
	@Override
	public boolean onItemOnObject(Player player, GameObject object, Item item) {
		Optional<AnimatedArmour.ArmourData> data = AnimatedArmour.ArmourData.getArmour(item.getId());
		if(!data.isPresent()) {
			return false;
		}
		if(!player.getInventory().containsAll(data.get().set)) {
			player.message("You are missing some items in order to animate this armour set.");
			return false;
		}
		armour = Optional.of(new AnimatedArmour(data.get(), object.getGlobalPos()));
		onEnter(player);
		return true;
	}
	
	@Override
	public boolean canDrop(Player player, Item item, int slot) {
		return true;
	}
	
	@Override
	public boolean canHit(Player player, Actor other, CombatType type) {
		return armour.isPresent() && other.isMob();
	}
	
	@Override
	public boolean onFirstClickObject(Player player, GameObject node) {
		switch(node.getId()) {
			case 57647:
			case 57644:
				onLogout(player);
				player.setMinigame(Optional.empty());
				player.move(new Position(player.getPosition().getX(), 3546));
				return false;
			default:
				return false;
		}
	}
	
	@Override
	public void onTeleportBefore(Player player, Position position) {
		onLogout(player);
		player.setMinigame(Optional.empty());
	}
	
	@Override
	public void onLogin(Player player) {

	}
	
	@Override
	public void onLogout(Player player) {
		drop(player, false);
	}
	
	@Override
	public boolean canPickup(Player player, GroundItem node) {
		return true;
	}
	
	@Override
	public void onEnter(Player player) {
		if(!armour.isPresent()) {
			return;
		}
		AnimatedArmour arm = armour.get();
		player.getActivityManager().setAllExcept(ActivityManager.ActivityType.LOG_OUT, ActivityManager.ActivityType.DIALOGUE_INTERACTION);
		player.animation(new Animation(827));
		player.getDialogueBuilder().append(new StatementDialogue("You place your armour on the platform where it", "disappears..."));
		player.getInventory().removeAll(arm.data.set);
		LinkedTaskSequence seq = new LinkedTaskSequence();
		seq.connect(5, () -> {
			player.getDialogueBuilder().append(new StatementDialogue("The animator hums, something appears to be working.", "You stand back..."));
			ForcedMovement.create(player, player.getPosition().move(0, 3), null).setSecondSpeed(42).setDirection(ForcedMovementDirection.SOUTH).submit();
		});
		seq.connect(3, () -> {
			World.get().getMobs().add(arm);
			arm.setOwner(player);
		});
		seq.connect(2, () -> {
			arm.forceChat("I'M ALIVE!!!!");
		});
		seq.connect(1, () -> arm.getCombat().attack(player));
		seq.connect(1, () -> player.getActivityManager().enable());
		seq.start();
	}
	
	@Override
	public void onDestruct(Player player) {
		armour.ifPresent(World.get().getMobs()::remove);
	}
	
	@Override
	public boolean contains(Player player) {
		return LOCATION.inLocation(player.getPosition());
	}
	
	@Override
	public void onKill(Player player, Actor other) {
		if(other.isMob() && armour.isPresent() && other.toMob().getId() == armour.get().getId()) {
			ANIMATOR.inc(player);
			drop(player, true);
		}
	}
	
	@Override
	public Optional<Minigame> copy() {
		return Optional.of(new AnimationRoom());
	}
	
	/**
	 * Handles the drops for when an armour is killed.
	 */
	private void drop(Player player, boolean tokens) {
		if(!armour.isPresent()) {
			return;
		}
		player.getInventory().addOrDrop(new Item(WARRIOR_GUILD_TOKEN, armour.get().data.tokens));
		player.getInventory().addOrDrop(armour.get().data.set);
		armour.ifPresent(World.get().getMobs()::remove);
		armour = Optional.empty();
	}
	
	/**
	 * The class which represents a single animated armour npc.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private static final class AnimatedArmour extends Mob {
		
		/**
		 * The data type of this armour.
		 */
		private final ArmourData data;
		
		/**
		 * Constructs a new {@link AnimatedArmour}.
		 * @param data     {@link #data}.
		 * @param position the position to spawn this npc at.
		 */
		AnimatedArmour(ArmourData data, Position position) {
			super(data.npcId, position);
			this.data = data;
		}
		
		@Override
		public Mob create() {
			return new AnimatedArmour(data, this.getPosition());
		}
		
		/**
		 * The enumerated type whose elements represent a set of constants
		 * whom contain information about the armour.
		 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
		 */
		enum ArmourData {
			BRONZE(4278, 5, 1155, 1117, 1075),
			IRON(4279, 10, 1153, 1115, 1067),
			STEEL(4280, 15, 1157, 1119, 1069),
			BLACK(4281, 20, 1165, 1125, 1077),
			MITHRIL(4282, 25, 1159, 1121, 1071),
			ADAMANT(4283, 30, 1161, 1123, 1073),
			RUNE(4284, 40, 1163, 1127, 1079);
			
			/**
			 * Caches our enum values.
			 */
			private static final ImmutableSet<ArmourData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(ArmourData.class));
			
			/**
			 * The npc id of this animated armour.
			 */
			private final int npcId;
			
			/**
			 * The amount of tokens received on killing this animated armour.
			 */
			private final int tokens;
			
			/**
			 * The set of items required to animate the set of armour.
			 */
			private final Item[] set;
			
			/**
			 * Constructs a new {@link ArmourData}.
			 * @param npcId  {@link #npcId}.
			 * @param tokens {@link #tokens}.
			 * @param set    {@link #set}.
			 */
			ArmourData(int npcId, int tokens, int... set) {
				this.npcId = npcId;
				this.tokens = tokens;
				this.set = Item.convert(set);
			}
			
			/**
			 * Gets an armour data enumerated if the id matches any of the items the set
			 * contains of.
			 * @param id the id to return an enumerator from.
			 * @return an armour data wrapped in an optional, {@link Optional#empty()} otherwise.
			 */
			private static Optional<ArmourData> getArmour(int id) {
				for(ArmourData data : VALUES) {
					for(Item item : data.set) {
						if(item.getId() == id) {
							return Optional.of(data);
						}
					}
				}
				return Optional.empty();
			}
		}
	}
}
