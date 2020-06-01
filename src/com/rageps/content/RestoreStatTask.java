package com.rageps.content;

import com.rageps.content.skill.Skill;
import com.rageps.content.skill.Skills;
import com.rageps.content.skill.prayer.Prayer;
import com.rageps.world.World;
import com.rageps.task.Task;
import com.rageps.world.entity.EntityState;
import com.rageps.world.entity.actor.player.Player;

/**
 * The class that handles the restoration of weakened skills.
 * @author lare96 <http://github.com/lare96>
 */
public final class RestoreStatTask extends Task {
	
	/**
	 * The amount running amount to exclude the other restores.
	 */
	private int run = 0;
	
	/**
	 * Creates a new {@link RestoreStatTask}.
	 */
	public RestoreStatTask() {
		super(10, false);
	}
	
	@Override
	public void execute() {
		if(World.get().getPlayers().isEmpty())
			return;
		for(Player player : World.get().getPlayers()) {
			if(player == null) {
				continue;
			}
			if(player.getState() != EntityState.ACTIVE) {
				continue;
			}
			if(player.isDead()) {
				continue;
			}
			
			Skill hp = player.getSkills()[Skills.HITPOINTS];
			int maxCons = player.getMaximumHealth();
			int currentCons = hp.getLevel();
			if(currentCons > maxCons) {
				hp.decreaseLevel(1);
				Skills.refresh(player, Skills.HITPOINTS);
			} else if(currentCons < maxCons && player.getAttr().get("accept_aid").getBoolean()) {
				int amount = player.getAttr().get("lunar_dream").getBoolean() ? 5 : 1;
				if(Prayer.isActivated(player, Prayer.RAPID_HEAL)) {
					amount += 1;
				}
				player.getSkills()[Skills.HITPOINTS].increaseLevel(amount, maxCons);
				Skills.refresh(player, Skills.HITPOINTS);
			}
			if(run != 6)//Excluding the other skills to restore.
				continue;
			for(int i = 0; i < player.getSkills().length; i++) {
				if(i == Skills.HITPOINTS)
					continue;
				int realLevel = player.getSkills()[i].getRealLevel();
				if(player.getSkills()[i].getLevel() < realLevel && i != Skills.PRAYER && i != Skills.SUMMONING) {
					player.getSkills()[i].increaseLevel(1);
					if(Prayer.isActivated(player, Prayer.RAPID_RESTORE)) {
						if(player.getSkills()[i].getLevel() < realLevel) {
							player.getSkills()[i].increaseLevel(1);
						}
					}
					Skills.refresh(player, i);
				} else if(player.getSkills()[i].getLevel() > realLevel && i != Skills.PRAYER && i != Skills.SUMMONING) {
					player.getSkills()[i].decreaseLevel(1);
					Skills.refresh(player, i);
				}
			}
		}
		run += 1;
		if(run == 7)
			run = 0;
	}
	
	@Override
	public void onCancel() {
		World.get().submit(new RestoreStatTask());
	}
}
