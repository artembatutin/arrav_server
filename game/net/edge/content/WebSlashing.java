package net.edge.content;

import net.edge.task.Task;
import net.edge.util.rand.RandomUtils;
import net.edge.world.World;
import net.edge.world.Animation;
import net.edge.world.node.entity.player.Player;
import net.edge.world.object.ObjectNode;

import java.util.Arrays;

/**
 * Holds support for slashing webs found allover runescape.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class WebSlashing extends Task {
	
	/**
	 * The object we're currently dealing with.
	 */
	private final ObjectNode object;
	
	/**
	 * The saved id of the object being slashed.
	 */
	private final int id;
	
	/**
	 * Constructs a new {@link WebSlashing}.
	 * @param object {@link #object}.
	 */
	private WebSlashing(ObjectNode object) {
		super(RandomUtils.inclusive(10, 20));
		this.object = object;
		this.id = object.getId();
	}
	
	/**
	 * Attempts to slash the webs around the world.
	 * @param player the player slashing the webs.
	 * @param object the object representing the webs.
	 * @return {@code true} if anything was slashed, {@code false} otherwise.
	 */
	public static boolean slash(Player player, ObjectNode object) {
		if(Arrays.stream(OBJECTS).noneMatch(d -> d == object.getId())) {
			return false;
		}
		if(!player.getWebSlashingTimer().elapsed(1800)) {
			return false;
		}
		player.getWebSlashingTimer().reset();
		
		if(player.getWeaponAnimation() != null) {
			player.animation(new Animation(player.getWeaponAnimation().getAttacking()[player.getFightType().getStyle().ordinal()], Animation.AnimationPriority.HIGH));
		} else {
			player.animation(new Animation(player.getFightType().getAnimation(), Animation.AnimationPriority.HIGH));
		}
		if(RandomUtils.inclusive(3) > 1) {
			player.message("You failed to slash through the web.");
			return false;
		}
		WebSlashing slashing = new WebSlashing(object);
		World.get().submit(slashing);
		return true;
	}
	
	@Override
	protected void onSubmit() {
		object.setId(SLASHED_WEB);
		object.publish();
	}
	
	@Override
	protected void execute() {
		object.setId(id);
		object.publish();
	}
	
	/**
	 * The identification for a slashed web.
	 */
	private static final int SLASHED_WEB = 734;
	
	/**
	 * An array containing all the objects which represent webs that can be slashed.
	 */
	private static final int[] OBJECTS = {42736};
}
