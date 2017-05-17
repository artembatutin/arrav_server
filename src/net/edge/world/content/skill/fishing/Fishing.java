package net.edge.world.content.skill.fishing;

import net.edge.task.Task;
import net.edge.world.content.skill.SkillData;
import net.edge.world.content.skill.Skills;
import net.edge.world.content.skill.action.impl.HarvestingSkillAction;
import net.edge.world.model.locale.Position;
import net.edge.world.model.node.entity.model.Animation;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;

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
			for(Item i : items) {
				Catchable c = Catchable.getCatchable(i.getId()).orElse(null);
				Skills.experience(getPlayer(), c.getExperience(), skill().getId());
			}
		}
		if(!(Boolean) player.getAttr().get("fishing").get()) {
			player.getAttr().get("fishing").set(true);
		}
	}
	
	@Override
	public void onStop() {
		getPlayer().animation(null);
		if((Boolean) player.getAttr().get("fishing").get()) {
			player.getAttr().get("fishing").set(false);
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
