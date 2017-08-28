package net.edge.world.entity.actor.player.assets.activity;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.content.skill.action.SkillActionTask;
import net.edge.task.LinkedTaskSequence;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.container.session.ExchangeSessionManager;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

/**
 * Holds functionality for the disabling certain activities for the player.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class ActivityManager {

	/**
	 * The player this manager is for.
	 */
	private final Player player;

	/**
	 * Constructs a new {@link ActivityManager}.
	 *
	 * @param player {@link #player}.
	 */
	public ActivityManager(Player player) {
		this.player = player;
	}

	/**
	 * The collection of disabled activities.
	 */
	private final EnumSet<ActivityType> disabled = EnumSet.noneOf(ActivityType.class);

	/**
	 * The collection holding all the possible activity types whom can be disabled / enabled.
	 */
	private static final ImmutableSet<ActivityType> VALUES = ActivityType.VALUES;

	/**
	 * Adds the {@code type} to the collection to disable this activity type.
	 *
	 * @param type the type to disable.
	 */
	public void set(ActivityType... type) {
		Arrays.stream(type).forEach(disabled::add);
	}

	/**
	 * Disables every kind of activity.
	 */
	public void disable() {
		VALUES.stream().forEach(disabled::add);
	}

	/**
	 * Adds all the possible ability types to the collection <b>except</b> the ones
	 * specified.
	 *
	 * @param types the types to <b>not</b> set.
	 */
	public void setAllExcept(ActivityType... types) {
		List<ActivityType> collection = Arrays.asList(types);
		VALUES.stream().filter(type -> !collection.contains(type)).forEach(this::set);
	}

	/**
	 * Removes the {@code type} from the collection to re-enable this activity type.
	 *
	 * @param type the type to enable
	 */
	public void remove(ActivityType... types) {
		Arrays.stream(types).forEach(disabled::remove);
	}

	/**
	 * Clears the list and enables every activity.
	 */
	public void enable() {
		disabled.clear();
	}

	/**
	 * Removes all the possible ability types from the collection <b>except</b> the ones
	 * specified.
	 *
	 * @param types the types to <b>not</b> remove.
	 */
	public void removeAllExcept(ActivityType... types) {
		List<ActivityType> collection = Arrays.asList(types);
		VALUES.stream().filter(type -> !collection.contains(type)).forEach(disabled::remove);
	}

	/**
	 * Checks if the given {@code type} is present.
	 *
	 * @param type the type to check for.
	 * @return <true> if it is, <false> otherwise.
	 */
	public boolean contains(ActivityType type) {
		executeBeforeHook(type);
		return disabled.contains(type);
	}

	/**
	 * Checks if any of the specified {@code types} is in the set of
	 * disabled activity types.
	 *
	 * @param types the types to check for.
	 * @return <true> if any of the type is in the set, <false> otherwise.
	 */
	public boolean containsAny(ActivityType... types) {
		List<ActivityType> collection = Arrays.asList(types);
		return collection.stream().anyMatch($it -> disabled.contains($it));
	}

	/**
	 * Checks if all the specified {@code types} are in the collection.
	 *
	 * @param types the types to check for.
	 * @return <true> if they are, <false> otherwise.
	 */
	public boolean containsAll(ActivityType... types) {
		return disabled.containsAll(Arrays.asList(types));
	}

	/**
	 * Attempts to execute the certain hook for the specified {@code type}.
	 *
	 * @param type the type to execute the hook for.
	 * @param hook the function to execute.
	 */
	public void onHook(ActivityType type, ActivityHook hook) {
		if(!disabled.contains(type)) {
			return;
		}
		hook.attempt();
	}

	/**
	 * Attempts to execute the certain hook if any of the specified {@code types} are present
	 * in the collection of disabled activities.
	 *
	 * @param hook  the function to execute.
	 * @param types the types to execute the hook for.
	 */
	public void onSpecifiedHook(ActivityHook hook, ActivityType... types) {
		if(!containsAny(types)) {
			return;
		}
		hook.attempt();
	}

	/**
	 * Executes the hook unless the specified {@code types} are executed.
	 *
	 * @param hook  the hook to execute.
	 * @param type  the type that was currently executed.
	 * @param types the types to <b>not</b> execute for.
	 */
	public void onHookExcept(ActivityHook hook, ActivityType type, ActivityType... types) {
		List<ActivityType> collection = Arrays.asList(types);

		if(collection.stream().anyMatch($it -> $it.equals(type))) {
			return;
		}

		hook.attempt();
	}

	/**
	 * Executes functionality on any hook.
	 *
	 * @param hook the function to execute.
	 */
	public void onAnyHook(ActivityHook hook) {
		hook.attempt();
	}

	private void skillHook(Player player, ActivityType current) {
		if(player.getSkillActionTask().isPresent()) {
			SkillActionTask task = player.getSkillActionTask().get();

			if(task.getAction().onDisable().isPresent()) {

				if(Arrays.stream(task.getAction().onDisable().get()).anyMatch(t -> t == current)) {
					task.getAction().stop();
				}
			}
		}
	}

	private void dreamHook(ActivityType type) {
		if(!player.getAttr().get("lunar_dream").getBoolean() || type.equals(ActivityType.CLICK_BUTTON)) {
			return;
		}

		player.getAttr().get("lunar_dream").set(false);
		LinkedTaskSequence seq = new LinkedTaskSequence();
		seq.connect(2, () -> player.getActivityManager().enable());
		seq.start();
	}

	/**
	 * Executes any hook <b>BEFORE</b> the hook is checked for.
	 *
	 * @param player the player to execute for.
	 * @param type   the type to execute.
	 */
	public void executeBeforeHook(ActivityType type) {
		ActivityManager activity = player.getActivityManager();
		activity.onHookExcept(() -> dreamHook(type), type, ActivityType.CHAT_MESSAGE);
	}

	/**
	 * Executes the hooks when any activity is handled.
	 *
	 * @param player the player to execute for.
	 * @param type   the current activity type executed.
	 */
	public void execute(ActivityType type) {
		ActivityManager activity = player.getActivityManager();
		activity.onHook(ActivityType.WALKING, () -> ExchangeSessionManager.get().reset(player));
		activity.onHook(ActivityType.INTERFACE_CLICK, () -> player.getAttr().get("banking").set(false));
		activity.onHook(ActivityType.INTERFACE_CLICK, () -> player.getAttr().get("bob").set(false));
		skillHook(player, type);
	}

	/**
	 * The enumerated type whose elements represent activity types which can be disabled.
	 *
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public enum ActivityType {
		/**
		 * Stops the player from facing a position
		 */
		FACE_POSITION, /**
		 * Stops the player from using magic on items
		 */
		MAGIC_ON_ITEM, /**
		 * Stops the player from walking.
		 */
		WALKING, /**
		 * Stops the player from attacking others
		 */
		ATTACK_PLAYER, /**
		 * Blocks the player from accepting his looks
		 */
		CHARACTER_SELECTION, /**
		 * Blocks sending messages on the global chat
		 */
		CHAT_MESSAGE, /**
		 * Blocks interface button interactions
		 */
		CLICK_BUTTON, /**
		 * Blocks logging out
		 */
		LOG_OUT, /**
		 * Blocks command executing
		 */
		COMMAND_MESSAGE, /**
		 * Blocks private messaging
		 */
		PRIVATE_MESSAGE, /**
		 * Blocks item dropping
		 */
		DROP_ITEM, /**
		 * Blocks the enter input packet
		 */
		ENTER_INPUT, /**
		 * Blocks player following
		 */
		FOLLOW_PLAYER, /**
		 * Blocks player from closing interfaces
		 */
		INTERFACE_CLICK, /**
		 * Blocks player from interface actions.
		 */
		INTERFACE_ACTION, /**
		 * Blocks player from interacting with items
		 */
		ITEM_ACTION, /**
		 * Blocks player from equiping items and other kind of item interface clicks
		 */
		ITEM_INTERFACE, /**
		 * Blocks item on item interactions
		 */
		ITEM_ON_ITEM, /**
		 * Blocks item on npc interactions
		 */
		ITEM_ON_NPC, /**
		 * Blocks item on object interactions
		 */
		ITEM_ON_OBJECT, /**
		 * Blocks item on player interactions
		 */
		ITEM_ON_PLAYER, /**
		 * Blocks npc interactions
		 */
		NPC_ACTION, /**
		 * Blocks object interactions
		 */
		OBJECT_ACTION, /**
		 * Blocks pickup item from ground
		 */
		PICKUP_ITEM, /**
		 * Blocks dialogue interactions
		 */
		DIALOGUE_INTERACTION, /**
		 * Blocks request messages (Trade / duel)
		 */
		REQUEST_MESSAGE, /**
		 * Blocks teleporting
		 */
		TELEPORT;

		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<ActivityType> VALUES = Sets.immutableEnumSet(EnumSet.allOf(ActivityType.class));
	}

}
