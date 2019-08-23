package net.arrav.content.skill.agility.obstacle;

import net.arrav.world.Animation;

import java.util.Optional;

/**
 * The enum whose elements represent the defined Obstacle definition types.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public enum ObstacleType {
	GRAPPLE_HOOK(7081, "You shoot your grapple around the pillar...", "... and make it safely to the other side."),
	VINES(-1, "You attempt to cut the vines...", null),
	LOG_BALANCE(762, "You carefully walk across the slippery log...", "...and make it safely to the other side."),
	BALANCE_BEAM(16079, "You carefully walk on the balance beam...", null),
	TIGHT_ROPE(762, "You carefully cross the tightrope...", "...You make it safely to the other side."),
	ROCKS(839, "You climb over the rocks...", "...You make it safely to the other side."),
	STEPPING_STONE(769, "You jump across the stepping stones...", "...You make it safely to the other side."),
	LEDGE(756, "You walk carefully across the ledge...", "...You make it safely to the other side."),
	ROPE_SWING(751, "You swing across the rope...", "...You make it safely to the other side."),
	CRUMBLING_WALL(839, "You climb over the crumbling wall.", null),
	NETTING(828, "You climb the netting.", null),
	TREE_BRANCH_UP(828, "You climb the tree...", "... to the platform above."),
	TREE_BRANCH_UP_ADVANCED(828, "You keep climbing the tree...", "...to an even higher platform."),
	TREE_BRANCH_DOWN(828, "You climb down the tree and land on the ground.", null),
	PIPE(844, "You squeeze into the pipe...", null),
	STRANGE_FLOOR(3067, "You carefully jump over the strange floor...", null),
	LADDER(828, "You climb down the ladder.", null),
	DITCH(6132, "You jump across the ditch...", null),
	RUN_ACROSS_SIGNPOST(2922, "You skillfully run across the sign-board.", null),
	POLE_SWING(11784, "You take the jump...", "...and successfully land."),
	JUMP_OVER_BARRIER(2923, "You jump through the barrier...", "...and found a way out."),
	JUMP_GAP(2586, "You jump across the gap...", null),
	SLIDE_ROOF(11792, "You slide down the roof...", "... and successfully land."),
	RUN_UP_WALL(10493, "You look if anybody is watching you...", null),
	CLIMB_UP_WALL(10023, "You climb-up the wall...", null),
	SPRING_DEVICE(4189, "You walk-up to the tip of the spring device...", "... and successfully land."),
	WILDERNESS_GATE(762, "You go through the gate and try to arrav over the ridge...", "...You skillfully balance across the ridge.");
	
	/**
	 * The animation played upon passing this obstacle.
	 */
	private final Animation animation;
	
	/**
	 * The message sent prior passing this obstacle.
	 */
	private final String prior;
	
	/**
	 * The message sent following after passing this obstacle.
	 */
	private final Optional<String> following;
	
	/**
	 * Constructs a new {@link ObstacleType}.
	 * @param animation {@link #animation}.
	 * @param prior {@link #prior}.
	 * @param following {@link #following}.
	 */
	ObstacleType(int animation, String prior, String following) {
		this.animation = new Animation(animation);
		this.prior = prior;
		this.following = Optional.ofNullable(following);
	}
	
	/**
	 * @return {@link #animation}.
	 */
	public Animation getAnimation() {
		return animation;
	}
	
	/**
	 * @return {@link #animation}.
	 */
	public int getAnimationId() {
		return animation.getId();
	}
	
	/**
	 * @return {@link #prior}.
	 */
	public String getMessage() {
		return prior;
	}
	
	/**
	 * @return {@link #following}.
	 */
	public Optional<String> getCrossedMessage() {
		return following;
	}
}
