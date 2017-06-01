package net.edge.content.skill.firemaking.pits;

import net.edge.task.Task;
import net.edge.util.rand.RandomUtils;
import net.edge.GameConstants;
import net.edge.World;
import net.edge.content.skill.SkillData;
import net.edge.content.skill.action.SkillAction;
import net.edge.world.Animation;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.activity.ActivityManager;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * Represents the skill action of firing the fire pit.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
final class PitFiring extends SkillAction {

	/**
	 * The fire pit object this skill action is dependent of.
	 */
	private final FirepitObject pit;

	/**
	 * Constructs a new {@link PitFiring}.
	 * @param player {@link #getPlayer()}.
	 * @param pit    {@link #pit}.
	 */
	PitFiring(Player player, FirepitObject pit) {
		super(player, Optional.of(pit.getGlobalPos()));
		this.pit = pit;
	}

	@Override
	public Optional<ActivityManager.ActivityType[]> onDisable() {
		return Optional.of(new ActivityManager.ActivityType[]{ActivityManager.ActivityType.WALKING, ActivityManager.ActivityType.TELEPORT});
	}

	@Override
	public void onStop() {
		player.animation(null);
	}

	@Override
	public OptionalInt animationDelay() {
		return OptionalInt.of(12);
	}

	@Override
	public int delay() {
		return 4;
	}

	@Override
	public boolean instant() {
		return true;
	}

	@Override
	public boolean init() {
		if(pit.isActive()) {
			player.message("The pit is already fired...");
			return false;
		}
		if(pit.getElements() < pit.data.count && !pit.data.equals(FirepitData.PHASE_FIVE)) {
			player.message("You can't fire the pit yet...");
			return false;
		}
		player.animation(new Animation(733));
		return true;
	}

	@Override
	public boolean canExecute() {
		if(pit.isActive()) {
			player.message("The pit was fired by someone else...");
			player.animation(null);
			return false;
		}
		return true;
	}

	@Override
	public Optional<Animation> animation() {
		return Optional.of(new Animation(733));
	}

	@Override
	public void execute(Task t) {
		if(RandomUtils.inclusive(0, 200) < 5) {
			player.message("The logs failed to catch fire...");
			return;
		}
		t.cancel();
		pit.setId(FirepitData.PHASE_IGNITED.objectId);
		pit.register();
		World.submit(new FirepitTask(pit));
		player.animation(null);
		player.message("You successfully fired the pit...");
		GameConstants.DOUBLE_BLOOD_MONEY_EVENT = true;
		World.message("@red@ " + player.getFormatUsername() + " @blu@ has ignited the fire pit!", true);
		World.message("@red@ Enjoy the double blood money event for five hours.", true);
	}

	@Override
	public double experience() {
		return 0;
	}

	@Override
	public SkillData skill() {
		return SkillData.FIREMAKING;
	}

	@Override
	public boolean isPrioritized() {
		return false;
	}
}
