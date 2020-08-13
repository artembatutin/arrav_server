package com.rageps.content.skill.fishing;

import com.rageps.content.achievements.Achievement;
import com.rageps.content.skill.SkillData;
import com.rageps.content.skill.Skills;
import com.rageps.content.skill.action.impl.HarvestingSkillAction;
import com.rageps.task.Task;
import com.rageps.world.model.Animation;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerAttributes;
import com.rageps.world.entity.item.Item;
import com.rageps.world.locale.Position;

import java.util.Optional;

public final class Fishing extends HarvestingSkillAction {
	
	private final Tool tool;
	
	public Fishing(Player player, Tool tool, Position position) {
		super(player, Optional.of(position));
		this.tool = tool;
	}
	
	@Override
	public void onHarvest(Task t, Item[] items, boolean success) {
		if(success) {
			int count = 0;
			for(Item i : items) {
				if(i == null)
					continue;
				Catchable c = Catchable.getCatchable(i.getId()).orElse(null);
				Skills.experience(getPlayer(), c.getExperience(), skill().getId());
				count += i.getAmount();
			}
			Achievement.FISHER_MAN.inc(player, count);
		}
		if(!player.getAttributeMap().getBoolean(PlayerAttributes.FISHING)) {
			player.getAttributeMap().set(PlayerAttributes.FISHING, true);
		}
	}
	
	@Override
	public void onStop() {
		getPlayer().animation(null);
		if(player.getAttributeMap().getBoolean(PlayerAttributes.FISHING)) {
			player.getAttributeMap().reset(PlayerAttributes.FISHING);
		}
	}
	
	@Override
	public Optional<Animation> animation() {
		return Optional.of(new Animation(tool.animation));
	}
	
	@Override
	public double successFactor() {
		return tool.success;
	}
	
	@Override
	public Optional<Item[]> removeItems() {
		if(tool.needed <= 0) {
			return Optional.empty();
		}
		return Optional.of(new Item[]{new Item(tool.needed, 1)});
	}
	
	@Override
	public Item[] harvestItems() {
		return tool.onCatch(getPlayer());
	}
	
	@Override
	public boolean instant() {
		return false;
	}
	
	@Override
	public boolean init() {
		if(!checkFishing()) {
			return false;
		}
		getPlayer().message("You begin to fish...");
		getPlayer().animation(new Animation(tool.animation));
		return true;
	}
	
	@Override
	public boolean canExecute() {
		return checkFishing();
	}
	
	@Override
	public double experience() {
		return 0;//experience handled somewhere else.
	}
	
	@Override
	public SkillData skill() {
		return SkillData.FISHING;
	}
	
	private boolean checkFishing() {
		if(!getPlayer().getInventory().contains(tool.id)) {
			getPlayer().message("You need a " + tool + " to fish here!");
			return false;
		}
		if(tool.needed > 0) {
			if(!getPlayer().getInventory().contains(tool.needed)) {
				getPlayer().message("You do not have enough bait.");
				return false;
			}
		}
		if(getPlayer().getInventory().remaining() < 1) {
			getPlayer().message("You do not have any space left in your inventory.");
			return false;
		}
		if(!getPlayer().getSkills()[Skills.FISHING].reqLevel(tool.level)) {
			getPlayer().message("You must have a Fishing level of " + tool.level + " to use this tool.");
			return false;
		}
		return true;
	}
}
