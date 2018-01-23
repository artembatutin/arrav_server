package net.arrav.content.object.pit;

import net.arrav.GameConstants;
import net.arrav.content.skill.SkillData;
import net.arrav.content.skill.action.SkillAction;
import net.arrav.task.Task;
import net.arrav.util.rand.RandomUtils;
import net.arrav.world.Animation;
import net.arrav.world.World;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.activity.ActivityManager;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * Represents the skill action of firing the fire pit.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class PitFiring extends SkillAction {

	/**
	 * The fire pit object this skill action is dependent of.
	 */
	private final FirepitObject pit;

	/**
	 * The burning task for the pit.
	 */
	public static Task burning;

	/**
	 * Constructs a new {@link PitFiring}.
	 * @param player {@link #getPlayer()}.
	 * @param pit    {@link #pit}.
	 */
	PitFiring(Player player, FirepitObject pit) {
		super(player, Optional.of(pit.getPosition()));
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
		pit.publish();
		burning = new FirepitTask(pit);
		World.get().submit(burning);
		player.animation(null);
		player.message("You successfully fired the pit!");
		GameConstants.EXPERIENCE_MULTIPLIER = 2;
		World.get().message("@red@ " + player.getFormatUsername() + " @blu@ has ignited the fire pit!", true);
		World.get().message("@red@ Enjoy the double experience for five hours.", true);
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
