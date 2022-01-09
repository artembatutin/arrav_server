package com.rageps.content.item;

import com.rageps.content.skill.Skills;
import com.rageps.task.LinkedTaskSequence;
import com.rageps.task.Task;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.combat.hit.HitIcon;
import com.rageps.world.entity.actor.combat.hit.Hitsplat;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.model.Animation;

import java.util.stream.IntStream;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 9-6-2017.
 */
public final class OverloadEffectTask extends Task {
	
	/**
	 * The player this effect is for.
	 */
	private final Player player;
	
	/**
	 * Creates a new {@link Task}.
	 */
	public OverloadEffectTask(Player player) {
		super(25, false);
		this.player = player;
	}
	
	/**
	 * The task cycles 40 times which makes up for 6 minutes.
	 * <p>The task is ran every 25 ticks (15 seconds)</p>
	 */
	private int cycle = 10;
	
	private int damageCycle;
	
	@Override
	protected void onSubmit() {
		LinkedTaskSequence seq = new LinkedTaskSequence(true);
		seq.connect(1, () -> {
			if(damageCycle == 5) {
				seq.cancel();
				return;
			}
			player.damage(new Hit(100, Hitsplat.NORMAL, HitIcon.NONE));
			player.animation(new Animation(3170, Animation.AnimationPriority.HIGH));
			damageCycle++;
		});
		seq.start();
		updateSkills();
	}
	
	/**
	 * A function executed when the {@code counter} reaches the {@code delay}.
	 */
	@Override
	protected void execute() {
		if(player.inWilderness() || cycle < 1) {
			player.message("The effects of overload have ran out.");
			this.cancel();
			return;
		}
		updateSkills();
		cycle--;
	}
	
	@Override
	public void onCancel() {
		restoreSkills();
		player.resetOverloadEffect(false);
	}
	
	/**
	 * Applies the overload effect for the player.
	 */
	private void updateSkills() {
		PotionConsumable.onOverloadEffect(player);
	}
	
	/**
	 * Removes the overload effect and restores the skills back to their real state.
	 */
	private void restoreSkills() {
		IntStream.range(0, 6).filter(i -> i != Skills.PRAYER).forEach(i -> Skills.restore(player, i));
	}
}
