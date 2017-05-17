package net.edge.world.content;

import net.edge.task.Task;
import net.edge.utils.rand.RandomUtils;
import net.edge.world.World;
import net.edge.world.model.locale.Position;
import net.edge.world.model.node.entity.model.Animation;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.object.ObjectDirection;
import net.edge.world.model.node.object.ObjectNode;

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
	 * The slashed object which will replace the current {@code object}.
	 */
	private final ObjectNode slashed;
	
	/**
	 * Constructs a new {@link WebSlashing}.
	 * @param object {@link #object}.
	 */
	private WebSlashing(ObjectNode object) {
		super(RandomUtils.inclusive(10, 20));
		this.object = object;
		this.slashed = new ObjectNode(SLASHED_WEB, object.getPosition(), object.getDirection());
	}
	
	/**
	 * Attempts to slash the webs around the world.
	 * @param player the player slashing the webs.
	 * @param object the object representing the webs.
	 * @return {@code true} if anything was slashed, {@code false} otherwise.
	 */
	public static boolean slash(Player player, ObjectNode object) {
		if(Arrays.stream(OBJECTS).noneMatch(d -> d.getId() == object.getId() && d.getPosition().same(object.getPosition()) && d.getDirection().equals(object.getDirection()))) {
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
		World.submit(slashing);
		return true;
	}
	
	@Override
	protected void onSubmit() {
		World.getRegions().getRegion(object.getPosition()).unregister(object);
		World.getRegions().getRegion(slashed.getPosition()).register(slashed);
	}
	
	@Override
	protected void execute() {
		World.getRegions().getRegion(slashed.getPosition()).unregister(slashed);
		World.getRegions().getRegion(object.getPosition()).register(object);
	}
	
	/**
	 * The identification for a slashed web.
	 */
	private static final int SLASHED_WEB = 734;
	
	/**
	 * An array containing all the objects which represent webs that can be slashed.
	 */
	private static final ObjectNode[] OBJECTS = new ObjectNode[]{new ObjectNode(42736, new Position(3106, 3958), ObjectDirection.SOUTH)};
}
