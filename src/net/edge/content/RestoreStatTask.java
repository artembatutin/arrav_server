package net.edge.content;

import net.edge.content.combat.strategy.player.special.CombatSpecial;
import net.edge.content.skill.Skill;
import net.edge.content.skill.Skills;
import net.edge.content.skill.prayer.Prayer;
import net.edge.task.Task;
import net.edge.world.World;
import net.edge.world.entity.EntityState;
import net.edge.world.entity.actor.player.Player;

/**
 * The class that handles the restoration of weakened skills.
 *
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
			} else if(currentCons < maxCons) {
				int amount = player.getAttr().get("lunar_dream").getBoolean() ? 5 : 1;
				hp.increaseLevel(amount, maxCons);
				if(Prayer.isActivated(player, Prayer.RAPID_HEAL)) {
					if(player.getSkills()[Skills.HITPOINTS].getLevel() < maxCons) {
						player.getSkills()[Skills.HITPOINTS].increaseLevel(1, maxCons);
					}
				}
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
			if(player.getSpecialPercentage().get() < 100) {
//				if(player.getRights().equal(Rights.ADMINISTRATOR)) {
//					CombatSpecial.restore(player, 100);
//					return;
//				}
				CombatSpecial.restore(player, 5);
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
