package net.arrav.content.skill.farming;

import net.arrav.action.impl.ItemOnItemAction;
import net.arrav.action.impl.ItemOnObjectAction;
import net.arrav.action.impl.ObjectAction;
import net.arrav.content.skill.Skills;
import net.arrav.content.skill.farming.attributes.GrowthState;
import net.arrav.content.skill.farming.attributes.PatchAttribute;
import net.arrav.content.skill.farming.patch.Patch;
import net.arrav.content.skill.farming.patch.PatchType;
import net.arrav.content.skill.farming.seed.SeedClass;
import net.arrav.content.skill.farming.seed.SeedType;
import net.arrav.content.skill.farming.seed.impl.FlowerSeed;
import net.arrav.content.skill.farming.seed.impl.TreeSeed;
import net.arrav.content.skill.woodcutting.Tree;
import net.arrav.content.skill.woodcutting.Woodcutting;
import net.arrav.net.packet.in.ObjectActionPacket;
import net.arrav.task.Task;
import net.arrav.world.Animation;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;
import net.arrav.world.entity.item.container.impl.Equipment;
import net.arrav.world.object.GameObject;

import java.util.Optional;

public class FarmingAction {
	
	public static void action() {
		for(PatchType patchType : PatchType.values()) {
			ObjectAction obj = new ObjectAction() {
				@Override
				public boolean click(Player player, GameObject object, int option) {
					final Farming farming = new Farming(player, Optional.of(object.getPosition()));
					farming.patch = player.patches.get(patchType);
					if(farming.patch != null && farming.patch.getSeedType() != null) {
						boolean fullyGrown = farming.patch.isFullyGrown();
						final String stateMessage = farming.patch.getGrowthState() != GrowthState.WATERED ? farming.patch.getSeedType() != null && (farming.patch.getSeedType().getSeedClass() == SeedClass.BUSHES || farming.patch.getSeedType()
								.getSeedClass() == SeedClass.TREES || farming.patch.getSeedType()
								.getSeedClass() == SeedClass.MUSHROOMS || farming.patch.getSeedType() == FlowerSeed.WHITE_LILY) ? "The crops are looking healthy." : "The crops are looking healthy; should probably water them to keep it that way!" : "The crops are looking healthy and watered!";
						if(farming.patch.getSeedType().getSeedClass() == SeedClass.TREES) {
							if(option == 1) {
								if(!farming.patch.hasAttribute(PatchAttribute.CHECKED_HEALTH)) {
									final int experience = farming.patch.getSeedType().getExperience()[1];
									farming.patch.addAttribute(PatchAttribute.CHECKED_HEALTH);
									if(!player.lockedXP)
										Skills.experience(player, experience, Skills.FARMING);
									player.message("You check the tree's health and gain experience.");
									FarmingManager.updatePatch(player, patchType);
									return true;
								} else if(farming.patch.getHarvestedItem().getAmount() >= farming.patch.getProduct().getAmount()) {
									player.message("The tree must first grow at least one log before chopping it down.");
									return true;
								}
								
								int treeId = -1;
								Tree tree = null;
								if(farming.patch.getSeedType() == TreeSeed.WILLOW) {
									treeId = 1308;
									tree = Tree.WILLOW;
								} else if(farming.patch.getSeedType() == TreeSeed.MAPLE) {
									treeId = 1307;
									tree = Tree.MAPLE;
								} else if(farming.patch.getSeedType() == TreeSeed.YEW) {
									treeId = 1309;
									tree = Tree.YEW;
								} else if(farming.patch.getSeedType() == TreeSeed.MAGIC) {
									treeId = 1306;
									tree = Tree.MAGIC;
								}
								if(treeId == -1) {
									player.message("Error attempting to chop tree! Please report to an administrator.");
									return true;
								}
								Woodcutting woodcutting = new Woodcutting(player, tree, object, farming.patch);
								woodcutting.start();
								return true;
							} else if(option == 2) {
								if(farming.patch.getGrowthState() == GrowthState.DISEASED) {
									if(farming.patch.getSeedType().getSeedClass() == SeedClass.TREES || farming.patch.getSeedType().getSeedClass() == SeedClass.BUSHES) {
										int wep = player.getEquipment().getSlot(Equipment.WEAPON_SLOT);
										boolean hasSecateurs = wep == FarmingConstants.MAGIC_SECATEURS || wep == FarmingConstants.SECATEURS_ITEM_ID || player.getInventory()
												.containsAny(FarmingConstants.MAGIC_SECATEURS, FarmingConstants.SECATEURS_ITEM_ID);
										if(hasSecateurs) {
											player.animation(FarmingConstants.SECATEURS_ANIMATION);
											farming.patch.setGrowthState(GrowthState.GROWING);
											FarmingManager.updatePatch(player, patchType);
											return true;
										}
									}
									String cure = farming.patch.getSeedType().getSeedClass() == SeedClass.TREES || farming.patch.getSeedType().getSeedClass() == SeedClass.BUSHES ? "a pair of secateurs" : "a cure plant potion";
									player.message("You need to use " + cure + " on this crop to cure it.");
									return true;
								}
								
								String message = !fullyGrown ? "This tree is still growing..." : farming.patch.getHarvestedItem().getAmount() < farming.patch.getProduct()
										.getAmount() ? "This tree seems sturdy enough to chop down." : "This tree has recently been chopped down.";
								player.message(message);
								return true;
							} else if(option == 4) {
								if(fullyGrown) {
									final int left = (farming.patch.getProduct().getAmount() - farming.patch.getHarvestedItem().getAmount());
									player.message("This tree has " + left + " log" + (left != 1 ? "s" : "") + " left to harvest.");
								} else {
									player.message("You can only do this once the tree is fully grown.");
								}
								return true;
							}
						}
						
						if(option == 4) {
							//guide
							if(fullyGrown) {
								final int left = (farming.patch.getProduct().getAmount() - farming.patch.getHarvestedItem().getAmount());
								player.message("This patch has " + left + " crop" + (left != 1 ? "s" : "") + " left to harvest.");
							} else {
								player.message("You can only do this once the crop is fully grown.");
							}
							return true;
						} else if(option == 2 && farming.patch.getSeedType().getSeedClass() == SeedClass.BUSHES) {
							//inspect
							if(farming.patch.getGrowthState() == GrowthState.DISEASED) {
								if(player.getInventory().contains(6036)) {
									player.animation(new Animation(2288));
									player.getInventory().remove(new Item(6036));
									player.getInventory().add(new Item(229));
									farming.patch.setGrowthState(GrowthState.GROWING);
									FarmingManager.updatePatch(player, patchType);
									return true;
								}
								player.message("The crops are diseased; you need a plant cure potion.");
								return true;
							}
							
							final int experience = farming.patch.getSeedType().getExperience()[1];
							if(experience <= 0) {
								player.message(stateMessage);
								return true;
							} else if(!farming.patch.isFullyGrown()) {
								player.message("You can only do this once the crop is fully grown.");
								return true;
							}
							if(!farming.patch.hasAttribute(PatchAttribute.CHECKED_HEALTH)) {
								farming.patch.addAttribute(PatchAttribute.CHECKED_HEALTH);
								if(!player.lockedXP)
									Skills.experience(player, experience, Skills.FARMING);
								player.message("You check the crop's health and gain experience.");
							} else {
								player.message("You have already checked this crops' health.");
							}
							return true;
						}
						
						if(farming.patch.getGrowthState() == GrowthState.DISEASED) {
							if(player.getInventory().contains(6036)) {
								player.animation(new Animation(2288));
								player.getInventory().remove(new Item(6036));
								player.getInventory().add(new Item(229));
								farming.patch.setGrowthState(GrowthState.GROWING);
								FarmingManager.updatePatch(player, patchType);
								return true;
							}
							player.message("The crops are diseased; you need a plant cure potion.");
							return true;
						} else if(farming.patch.getGrowthState() == GrowthState.DEAD) {
							player.message("This crop has died; you should probably dig it out with a spade.");
							return true;
						} else if(fullyGrown) {
							if(option == 2) {
								player.message("The crops are ready to harvest.");
								return true;
							}
							farming.amountToHarvest = farming.patch.getProduct().getAmount() / 10;
							if(farming.amountToHarvest <= 0)
								farming.amountToHarvest = 1;
						} else if(!fullyGrown) {
							player.message(stateMessage);
							return true;
						}
					} else {
						if(option == 4) {
							player.message("You can only do this once the crop is fully grown.");
							return true;
						}
						if(farming.patch == null)
							farming.patch = new Patch(patchType, null, null, 0);
					}
					
					if(farming.amountToHarvest == 0) {
						farming.amountToHarvest = farming.getHarvestAmount();
					}
					farming.start();
					player.patches.put(patchType, farming.patch);
					return true;
				}
			};
			obj.registerFirst(patchType.getObjectId());
			obj.registerSecond(patchType.getObjectId());
			obj.registerThird(patchType.getObjectId());
			obj.registerFourth(patchType.getObjectId());
			ItemOnObjectAction item = new ItemOnObjectAction() {
				@Override
				public boolean click(Player player, GameObject object, Item item, int container, int slot) {
					final Patch patch = player.patches.get(patchType);
					final boolean weedRemoved = patch != null && patch.getWeedStage() >= FarmingConstants.WEED_CONFIG.length - 1;
					final boolean magicWateringCan = item.getId() == FarmingConstants.MAGIC_WATERING_CAN_ITEM_ID;
					if(item.getId() >= 5333 && item.getId() <= 5340 || magicWateringCan) {
						if(patch == null) {
							player.message("You cannot water this crop!");
							return true;
						}
						if(patch.getSeedType() != null) {
							if(patch.getSeedType() == FlowerSeed.WHITE_LILY) {
								player.message("You cannot water this crop!");
								return true;
							}
							switch(patch.getSeedType().getSeedClass()) {
								case MUSHROOMS:
								case BUSHES:
								case TREES:
									player.message("You cannot water this crop!");
									return true;
							}
						}
						if(patch.getGrowthState() != GrowthState.GROWING) {
							final String message = patch.getSeedType() != null ? "You cannot water these crops anymore!" : "You must first plant seeds on this patch before watering it.";
							player.message(message);
							return true;
						} else if(patch.isFullyGrown()) {
							player.message("You cannot water these crops; they are ready to be harvested.");
							return true;
						}
						player.animation(FarmingConstants.POUR_WATER_ANIMATION);
						
						(new Task(4, false) {
							@Override
							protected void execute() {
								useWateringCan(player, item, magicWateringCan);
								patch.setDiseaseImmunity(patch.getDiseaseImmunity() + 50);
								patch.setGrowthState(GrowthState.WATERED);
								FarmingManager.updatePatch(player, patchType);
								cancel();
							}
						}).submit();
						return true;
					} else if(item.getId() == FarmingConstants.COMPOST_ITEM_ID) {
						if(patch == null) {
							player.message("You cannot compost this crop!");
							return true;
						}
						if(patch.getSeedType() != null) {
							player.message("You can no longer place compost on this patch.");
							return true;
						}
						if(patch.hasAttribute(PatchAttribute.COMPOSTED) || patch.hasAttribute(PatchAttribute.SUPER_COMPOSTED)) {
							player.message("This patch already contains compost.");
							return true;
						}
						patch.addAttribute(PatchAttribute.COMPOSTED);
						player.animation(FarmingConstants.POUR_COMPOST_ANIMATION);
						(new Task(4, false) {
							@Override
							protected void execute() {
								patch.setDiseaseImmunity(patch.getDiseaseImmunity() + 50);
								int nextId = 3727;
								player.getInventory().remove(item);
								player.getInventory().add(new Item(nextId));
								patch.hasAttribute(PatchAttribute.COMPOSTED);
								cancel();
							}
						}).submit();
						return true;
					} else if(item.getId() == FarmingConstants.SUPER_COMPOST_ITEM_ID) {
						if(patch == null) {
							player.message("You cannot compost this crop!");
							return true;
						}
						if(patch.getSeedType() != null) {
							player.message("You can no longer place super compost on this patch.");
							return true;
						}
						if(patch.hasAttribute(PatchAttribute.COMPOSTED) || patch.hasAttribute(PatchAttribute.SUPER_COMPOSTED)) {
							player.message("This patch already contains compost.");
							return true;
						}
						patch.addAttribute(PatchAttribute.SUPER_COMPOSTED);
						player.animation(FarmingConstants.POUR_COMPOST_ANIMATION);
						(new Task(4, false) {
							@Override
							protected void execute() {
								patch.setDiseaseImmunity(patch.getDiseaseImmunity() + 150);
								int nextId = 3727;
								player.getInventory().remove(item);
								player.getInventory().add(new Item(nextId));
								patch.hasAttribute(PatchAttribute.SUPER_COMPOSTED);
								cancel();
							}
						}).submit();
						return true;
					} else if(item.getId() == FarmingConstants.RAKE_ITEM_ID) {
						if(weedRemoved) {
							player.message("This patch has already been raked.");
						} else {
							ObjectActionPacket.FIRST.get(object.getId()).click(player, object, 1);
						}
						return true;
					} else if(item.getId() == FarmingConstants.SPADE_ITEM_ID && patch != null && patch.getSeedType() != null) {
						player.animation(new Animation(830));
						(new Task(3, false) {
							@Override
							protected void execute() {
								player.patches.remove(patch.getPatchType());
								patch.setWeedStage(0);
								FarmingManager.updatePatch(player, patch.getPatchType());
								player.message("You dig the crop from the patch.");
								cancel();
							}
						}).submit();
						return true;
					} else if(item.getId() == 5350) {
						if(patch == null || !weedRemoved) {
							player.message("You cannot collect soil from this patch.");
							return true;
						}
						if(!player.getInventory().contains(FarmingConstants.TROWEL_ITEM_ID)) {
							player.message("You need a gardening trowel to do this.");
							return true;
						}
						player.animation(new Animation(827));
						player.getInventory().remove(item);
						player.getInventory().add(new Item(5356));
						return true;
					} else if(item.getId() == 6036) {
						if(patch == null || patch.getSeedType() == null)
							return true;
						if(patch.getGrowthState() != GrowthState.DISEASED) {
							player.message("This potion can only be used on diseased crops.");
							return true;
						}
						if(patch.getSeedType().getSeedClass() == SeedClass.TREES || patch.getSeedType().getSeedClass() == SeedClass.BUSHES) {
							player.message("This potion cannot be used on this crop. You need a pair of secateurs.");
							return true;
						}
						player.animation(new Animation(2288));
						player.getInventory().remove(item);
						player.getInventory().add(new Item(229));
						patch.setGrowthState(GrowthState.GROWING);
						FarmingManager.updatePatch(player, patchType);
						return true;
					} else if(item.getId() == FarmingConstants.SECATEURS_ITEM_ID || item.getId() == FarmingConstants.MAGIC_SECATEURS) {
						if(patch == null || patch.getSeedType() == null)
							return true;
						if(patch.getSeedType().getSeedClass() != SeedClass.TREES && patch.getSeedType().getSeedClass() != SeedClass.BUSHES)
							return true;
						if(patch.getGrowthState() != GrowthState.DISEASED) {
							String name = patch.getSeedType().getSeedClass() == SeedClass.TREES ? "tree" : "bush";
							player.message("You should only use these when your " + name + " is diseased.");
							return true;
						}
						player.animation(FarmingConstants.SECATEURS_ANIMATION);
						patch.setGrowthState(GrowthState.GROWING);
						FarmingManager.updatePatch(player, patchType);
						return true;
					}
					
					SeedType seedType = null;
					for(SeedType validSeed : patchType.getValidSeeds()) {
						if(validSeed.getSeed().getId() == item.getId()) {
							seedType = validSeed;
							break;
						}
					}
					
					if(seedType != null) {
						if(patch == null) {
							player.message("You must first rake this patch before planting anything!");
							return true;
						} else if(!weedRemoved) {
							player.message("You must finish raking this patch before planting anything.");
							return true;
						} else if(patch.getSeedType() != null) {
							if(item.getId() == patch.getSeedType().getToolId()) {
								ObjectActionPacket.FIRST.get(object.getId()).click(player, object, 1);
							} else {
								player.message("This patch is already growing crops!");
							}
							return true;
						} else if(!player.getInventory().contains(seedType.getSeed())) {
							player.message("You need " + seedType.getSeed().getAmount() + " " + seedType.getSeed().getName().toLowerCase() + (seedType.getSeed().getAmount() != 1 ? "s" : "") + " to plant this crop.");
							return true;
						} else if(seedType.getSeedClass() != SeedClass.TREES && !player.getInventory().contains(FarmingConstants.SEED_DIBBER_ITEM_ID)) {
							player.message("You need a seed dibber to plant these seeds.");
							return true;
						} else if(seedType.getSeedClass() == SeedClass.TREES && !player.getInventory().contains(FarmingConstants.TROWEL_ITEM_ID)) {
							player.message("You need a seed trowel to plant this seedling.");
							return true;
						} else if(seedType.getLevelRequirement() > player.getSkills()[Skills.FARMING].getRealLevel()) {
							player.message("You need a farming level of " + seedType.getLevelRequirement() + " to plant this crop.");
							return false;
						}
						
						player.animation(FarmingConstants.SEED_DIBBER_ANIMATION);
						final SeedType finalSeedType = seedType;
						(new Task(2, false) {
							@Override
							protected void execute() {
								player.animation(null);
								player.getInventory().remove(finalSeedType.getSeed());
								if(finalSeedType.getSeedClass() == SeedClass.TREES) {
									player.getInventory().add(new Item(5350));
								}
								final Patch newPatch = new Patch(patchType, finalSeedType, patch.getGrowthState() == null ? GrowthState.GROWING : patch.getGrowthState(), 0);
								newPatch.setWeedStage(FarmingConstants.WEED_CONFIG.length - 1);
								newPatch.setProduct(finalSeedType.getRewards()[0].copy().setAmount(finalSeedType.getHarvestAmount(patch)));
								newPatch.setHarvestedItem(finalSeedType.getRewards()[0].copy().setAmount(0));
								newPatch.setPlantConfigurations(player);
								player.patches.put(patchType, newPatch);
								FarmingManager.updatePatch(player, patchType);
								newPatch.submitGrowthTask(player);
								if(!player.lockedXP)
									Skills.experience(player, finalSeedType.getExperience()[0], Skills.FARMING);
								cancel();
							}
						}).submit();
					} else {
						player.message("You cannot use this item on the patch.");
					}
					return true;
				}
			};
			item.registerObj(patchType.getObjectId());
		}
		ItemOnItemAction a = new ItemOnItemAction() {
			@Override
			public boolean click(Player player, Item itemOn, Item itemUsed, int slotFirst, int slotSecond) {
				int[] data = null;
				for(int[] itemOnItem : ITEM_ON_ITEM_SAPLING) {
					if(itemOnItem[0] == itemOn.getId()) {
						data = itemOnItem;
						break;
					}
				}
				if(data == null) {
					return false;
				}
				Item wateringCan = null;
				for(Item item : player.getInventory().getItems()) {
					if(item == null) {
						continue;
					}
					if(item.getId() >= 5333 && item.getId() <= 5340 || item.getId() == FarmingConstants.MAGIC_WATERING_CAN_ITEM_ID) {
						wateringCan = item;
						break;
					}
				}
				if(wateringCan == null) {
					player.message("You need a watering can to do this.");
					return true;
				}
				if(!player.getInventory().contains(FarmingConstants.TROWEL_ITEM_ID)) {
					player.message("You need a gardening trowel to do this.");
					return true;
				}
				
				useWateringCan(player, wateringCan, wateringCan.getId() == FarmingConstants.MAGIC_WATERING_CAN_ITEM_ID);
				player.getInventory().remove(itemOn);
				player.getInventory().remove(itemUsed.setAmount(1));
				player.getInventory().add(new Item(data[1]));
				return true;
			}
		};
		a.register(5356);
	}
	
	private static void useWateringCan(Player player, Item item, boolean magicWateringCan) {
		if(!magicWateringCan) {
			int nextId = item.getId() == 5333 ? 5331 : item.getId() - 1;
			player.getInventory().remove(item);
			player.getInventory().add(new Item(nextId));
		}
	}
	
	private static final int[][] ITEM_ON_ITEM_SAPLING = {{5313, 5371}, //willow
			{5314, 5372}, //maple
			{5315, 5373}, //yew
			{5316, 5374}, //magic
	};
}
