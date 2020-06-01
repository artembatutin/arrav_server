package com.rageps.content.object;

import com.rageps.world.World;
import com.rageps.action.impl.ObjectAction;
import com.rageps.task.Task;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.Animation;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.object.GameObject;

/**
 * Holds support for slashing webs found allover runescape.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class WebSlashing extends Task {
	
	/**
	 * The identification for a slashed web.
	 */
	private static final int SLASHED_WEB = 734;
	
	/**
	 * The object we're currently dealing with.
	 */
	private final GameObject object;
	
	/**
	 * The saved id of the object being slashed.
	 */
	private final int id;
	
	/**
	 * Constructs a new {@link WebSlashing}.
	 * @param object {@link #object}.
	 */
	private WebSlashing(GameObject object) {
		super(RandomUtils.inclusive(10, 20));
		this.object = object;
		this.id = object.getId();
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
	
	public static void action() {
		ObjectAction slash = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(!player.getWebSlashingTimer().elapsed(1800)) {
					return false;
				}
				player.getWebSlashingTimer().reset();
				
				if(player.getWeaponAnimation() != null) {
					player.animation(new Animation(player.getWeaponAnimation().getAttacking()[player.getCombat().getFightType().getStyle().ordinal()], Animation.AnimationPriority.HIGH));
				} else {
					player.animation(new Animation(player.getCombat().getFightType().getAnimation(), Animation.AnimationPriority.HIGH));
				}
				if(RandomUtils.inclusive(3) > 1) {
					player.message("You failed to slash through the web.");
					return false;
				}
				WebSlashing slashing = new WebSlashing(object);
				World.get().submit(slashing);
				return true;
			}
		};
		
		int[] objects = {42736};
		for(int o : objects) {
			slash.registerFirst(o);
		}
	}
	
}
