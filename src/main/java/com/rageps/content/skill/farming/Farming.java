package com.rageps.content.skill.farming;

import com.rageps.content.achievements.Achievement;
import com.rageps.content.skill.SkillData;
import com.rageps.content.skill.Skills;
import com.rageps.content.skill.action.impl.HarvestingSkillAction;
import com.rageps.content.skill.farming.attributes.PatchAttribute;
import com.rageps.content.skill.farming.patch.Patch;
import com.rageps.content.skill.farming.seed.SeedClass;
import com.rageps.task.Task;
import com.rageps.util.TextUtils;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.ItemDefinition;
import com.rageps.world.entity.item.container.impl.Equipment;
import com.rageps.world.locale.Position;
import com.rageps.world.model.Animation;

import java.util.Optional;

public final class Farming extends HarvestingSkillAction {
	
	public Patch patch;
	
	public int amountToHarvest;
	
	/**
	 * Creates a new {@link HarvestingSkillAction}.
	 * @param player the player this skill action is for.
	 * @param position the position the player should face.
	 */
	public Farming(Player player, Optional<Position> position) {
		super(player, position);
	}
	
	@Override
	public boolean instant() {
		return false;
	}
	
	@Override
	public boolean init() {
		if(patch.getSeedType() != null) {
			if(patch.isFullyGrown()) {
				if(patch.getSeedType().getToolId() != -1 && !getPlayer().getInventory().contains(patch.getSeedType().getToolId())) {
					getPlayer().message("You need " + TextUtils.appendIndefiniteArticle((ItemDefinition.get(patch.getSeedType().getToolId()).getName()) + " to harvest these crops."));
					return false;
				}
			}
			
			if(patch.getSeedType().getLevelRequirement() > getPlayer().getSkills()[Skills.FARMING].getRealLevel()) {
				getPlayer().message("You need a farming level of " + patch.getSeedType().getLevelRequirement() + " to plant this crop.");
				return false;
			}
			
			if(patch.getHarvestedItem().getAmount() >= patch.getProduct().getAmount()) {
				stop();
				getPlayer().animation(null);
				getPlayer().message("This crop does not have anything else to harvest at the time.");
				return false;
			}
			
			int wep = getPlayer().getEquipment().getItems()[Equipment.WEAPON_SLOT] != null ? getPlayer().getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() : 1;
			final boolean hasMagicSecateurs = wep == FarmingConstants.MAGIC_SECATEURS || getPlayer().getInventory().contains(FarmingConstants.MAGIC_SECATEURS);
			if(hasMagicSecateurs && !patch.hasAttribute(PatchAttribute.MAGIC_SECATEURS)) {
				patch.addAttribute(PatchAttribute.MAGIC_SECATEURS);
				patch.getProduct().setAmount((int) (patch.getProduct().getAmount() * 1.1));
				getPlayer().message("Your magic secateurs increase your harvest yield by 10%");
			}
			if(patch.hasAttribute(PatchAttribute.SUPER_COMPOSTED)) {
				patch.addAttribute(PatchAttribute.SUPER_COMPOSTED);
				patch.getProduct().setAmount((int) (patch.getProduct().getAmount() * 1.1));
				getPlayer().message("The super compost increases your harvest yield by 10%");
			} else if(patch.hasAttribute(PatchAttribute.COMPOSTED)) {
				patch.addAttribute(PatchAttribute.COMPOSTED);
				patch.getProduct().setAmount((int) (patch.getProduct().getAmount() * 1.05));
				getPlayer().message("The compost increases your harvest yield by 5%");
			}
		} else {
			if(!getPlayer().getInventory().contains(FarmingConstants.RAKE_ITEM_ID)) {
				getPlayer().message("You need a rake to do this.");
				return false;
			}
			if(patch.getWeedStage() >= FarmingConstants.WEED_CONFIG.length - 1) {
				if(!patch.hasAttribute(PatchAttribute.COMPOSTED) && !patch.hasAttribute(PatchAttribute.SUPER_COMPOSTED)) {
					getPlayer().message("You should probably place compost on this patch.");
				} else {
					getPlayer().message("This patch is ready to grow crops.");
				}
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void onHarvest(Task t, Item[] items, boolean success) {
		if(success) {
			if(patch.getSeedType() != null) {
				Achievement.FARMER.inc(player, patch.getHarvestedItem().getAmount());
				if(patch.getHarvestedItem().getAmount() + amountToHarvest > patch.getProduct().getAmount())
					amountToHarvest = patch.getProduct().getAmount() - patch.getHarvestedItem().getAmount();
				patch.getHarvestedItem().setAmount(patch.getHarvestedItem().getAmount() + amountToHarvest);
				if(patch.getHarvestedItem().getAmount() >= patch.getProduct().getAmount()) {
					stop();
					getPlayer().animation(null);
					if(patch.getSeedType().getSeedClass() == SeedClass.TREES) {
						FarmingManager.updatePatch(getPlayer(), patch.getPatchType());
					} else if(patch.getSeedType().getSeedClass() != SeedClass.BUSHES) {
						getPlayer().patches.remove(patch.getPatchType());
						patch.setWeedStage(0);
						FarmingManager.updatePatch(getPlayer(), patch.getPatchType());
					}
				}
				if(patch.getSeedType().getSeedClass() == SeedClass.BUSHES || patch.getSeedType().getSeedClass() == SeedClass.MUSHROOMS) {
					FarmingManager.updatePatch(getPlayer(), patch.getPatchType());
				}
				
			} else {
				patch.setWeedStage(patch.getWeedStage() + 1);
				if(patch.getWeedStage() >= FarmingConstants.WEED_CONFIG.length - 1) {
					stop();
					getPlayer().animation(null);
				}
				FarmingManager.updatePatch(getPlayer(), patch.getPatchType());
			}
		}
	}
	
	@Override
	public int delay() {
		return 2;
	}
	
	@Override
	public Optional<Animation> animation() {
		return Optional.of(patch.getSeedType() == null ? FarmingConstants.RAKE_ANIMATION : patch.getSeedType().getAnimation());
	}
	
	@Override
	public boolean canExecute() {
		return true;
	}
	
	@Override
	public double experience() {
		return patch.getSeedType() == null ? 3 : patch.getSeedType().getExperience()[2];
	}
	
	@Override
	public SkillData skill() {
		return SkillData.FARMING;
	}
	
	@Override
	public double successFactor() {
		return 1;
	}
	
	@Override
	public Optional<Item[]> removeItems() {
		return Optional.empty();
	}
	
	@Override
	public Item[] harvestItems() {
		if(patch.getSeedType() == null)
			return new Item[]{new Item(FarmingConstants.WEED_ITEM_ID)};
		return new Item[]{new Item(patch.getProduct().getId(), amountToHarvest)};
	}
	
	public int getHarvestAmount() {
		return patch.getSeedType() == null ? (FarmingConstants.WEED_CONFIG.length - 1) - patch.getWeedStage() : patch.getProduct().getAmount() - patch.getHarvestedItem().getAmount();
	}
}
