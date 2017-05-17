package net.edge.world.content;

import net.edge.task.Task;
import net.edge.world.World;
import net.edge.world.content.combat.special.CombatSpecial;
import net.edge.world.content.skill.Skills;
import net.edge.world.content.skill.prayer.Prayer;
import net.edge.world.model.node.NodeState;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.entity.player.assets.Rights;

/**
 * The class that handles the restoration of weakened skills.
 * @author lare96 <http://github.com/lare96>
 */
public final class RestoreStatTask extends Task {
	
	/**
	 * The amount running amount to exclude the other restores.
	 */
	int run = 0;
	
	/**
	 * Creates a new {@link RestoreStatTask}.
	 */
	public RestoreStatTask() {
		super(5, false);
	}
	
	@Override
	public void execute() {
		for(Player player : World.getPlayers()) {
			if(player == null)
				continue;
			if(player.getState() != NodeState.ACTIVE) {
				continue;
			}
			if(player.isDead()) {
				continue;
			}
			int maxCons = player.getSkills()[Skills.HITPOINTS].getRealLevel() * 10;
			
			//Dream
			if(player.getSkills()[Skills.HITPOINTS].getLevel() < maxCons) {
				int amount = player.getAttr().get("lunar_dream").getBoolean() ? 5 : 1;
				player.getSkills()[Skills.HITPOINTS].increaseLevel(amount, maxCons);
			}
			
			//Aid
			if(player.getSkills()[Skills.HITPOINTS].getLevel() < maxCons && ((Boolean) player.getAttr().get("accept_aid").get())) {
				player.getSkills()[Skills.HITPOINTS].increaseLevel(1, maxCons);
				if(Prayer.isActivated(player, Prayer.RAPID_HEAL)) {
					if(player.getSkills()[Skills.HITPOINTS].getLevel() < maxCons) {
						player.getSkills()[Skills.HITPOINTS].increaseLevel(1, maxCons);
					}
				}
				Skills.refresh(player, Skills.HITPOINTS);
			}
			
			if(run != 6)//Excluding the other skills to restore.
				break;
			
			for(int i = 0; i < player.getSkills().length; i++) {
				if(i == Skills.HITPOINTS)
					continue;
				int realLevel = player.getSkills()[i].getRealLevel();
				if(player.getSkills()[i].getLevel() < realLevel && i != Skills.PRAYER) {
					player.getSkills()[i].increaseLevel(1);
					
					if(Prayer.isActivated(player, Prayer.RAPID_RESTORE)) {
						if(player.getSkills()[i].getLevel() < realLevel) {
							player.getSkills()[i].increaseLevel(1);
						}
					}
					Skills.refresh(player, i);
				} else if(player.getSkills()[i].getLevel() > realLevel && i != Skills.PRAYER) {
					player.getSkills()[i].decreaseLevel(1);
					Skills.refresh(player, i);
				}
			}
			if(player.getSpecialPercentage().get() < 100) {
				if(player.getRights().equal(Rights.DEVELOPER)) {
					CombatSpecial.restore(player, 100);
					return;
				}
				CombatSpecial.restore(player, 5);
			}
		}
		run += 1;
		if(run == 7)
			run = 0;
	}
	
	@Override
	public void onCancel() {
		World.submit(new RestoreStatTask());
	}
}
