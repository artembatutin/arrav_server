package net.arrav.content.skill.farming.patch;

import net.arrav.content.skill.farming.FarmingConstants;
import net.arrav.content.skill.farming.FarmingManager;
import net.arrav.content.skill.farming.attributes.GrowthState;
import net.arrav.content.skill.farming.attributes.PatchAttribute;
import net.arrav.content.skill.farming.seed.SeedClass;
import net.arrav.content.skill.farming.seed.SeedType;
import net.arrav.content.skill.farming.seed.impl.AllotmentSeed;
import net.arrav.content.skill.farming.seed.impl.FlowerSeed;
import net.arrav.task.Task;
import net.arrav.util.rand.RandomUtils;
import net.arrav.world.entity.EntityState;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;
import net.arrav.world.entity.item.Item;

public final class Patch {
	
	public Patch(PatchType patchType, SeedType seedType, GrowthState growth, int stage) {
		this.patchType = patchType;
		this.seedType = seedType;
		this.seedTypeName = seedType == null ? null : seedType.toString();
		this.growthState = growth;
		this.stage = stage;
		this.seedClass = seedType == null ? null : seedType.getSeedClass();
	}
	
	private final PatchType patchType;
	
	private transient SeedType seedType;
	
	private final String seedTypeName;
	
	private SeedClass seedClass;
	
	private GrowthState growthState;
	
	private int stage;
	
	private int weedStage;
	
	private int attributes;
	
	private int diseaseImmunity = FarmingConstants.DISEASE_IMMUNITY;
	
	private Item harvestedItem;
	
	private Item product;
	
	private transient boolean growthTaskSubmitted;
	
	private long timePlanted;
	
	private long wateredTimer;
	
	public void submitGrowthTask(final Player player) {
		if(growthTaskSubmitted || seedType == null)
			return;
		growthTaskSubmitted = true;
		
		long waitTime = 60_000;
		if(player.getRights().equal(Rights.EXTREME_DONATOR)) {
			waitTime = 30_000;
		} else if(player.getRights().equal(Rights.SUPER_DONATOR)) {
			waitTime = 45_000;
		} else if(player.getRights().equal(Rights.DONATOR)) {
			waitTime = 50_000;
		}
		final long waitTimer = waitTime;
		final int totalMinutes = seedType.getGrowthTime()[0] * seedType.getGrowthTime()[1];
		final int minutesForGrowth = totalMinutes / seedType.getValues().length;
		
		while(System.currentTimeMillis() - timePlanted >= (minutesForGrowth * (stage + 1) * waitTimer)) {
			if(!addStage(player) || isFullyGrown()) {
				break;
			}
		}
		
		(new Task(16, false) {
			@Override
			protected void execute() {
				if(player.getState() != EntityState.ACTIVE || getWeedStage() <= 0) {
					cancel();
					return;
				}
				if(growthState == GrowthState.WATERED && System.currentTimeMillis() - wateredTimer >= (seedType.getGrowthTime()[1] * waitTimer)) {
					growthState = GrowthState.GROWING;
					wateredTimer = System.currentTimeMillis();
				}
				
				if(System.currentTimeMillis() - timePlanted >= (minutesForGrowth * (stage + 1) * waitTimer)) {
					if(growthState == GrowthState.DISEASED && (stage >= seedType.getValues().length - 2 || RandomUtils.inclusive(10) == 0)) {
						growthState = GrowthState.DEAD;
						FarmingManager.updatePatch(player, patchType);
						cancel();
						return;
					}
					decreaseDiseaseImmunity();
					if(addStage(player)) {
						FarmingManager.updatePatch(player, patchType);
					}
					if(isFullyGrown()) {
						if(getSeedType().getSeedClass() == SeedClass.BUSHES) {
							if(getHarvestedItem().getAmount() > 0 && growthState == GrowthState.GROWING) {
								getHarvestedItem().setAmount(getHarvestedItem().getAmount() - 1);
								FarmingManager.updatePatch(player, patchType);
							}
						} else if(getSeedType().getSeedClass() == SeedClass.TREES) {
							if(getHarvestedItem().getAmount() > 0 && growthState == GrowthState.GROWING) {
								boolean firstGrowth = getHarvestedItem().getAmount() >= getProduct().getAmount();
								final int amount = getHarvestedItem().getAmount() - (1 + RandomUtils.inclusive(10));
								getHarvestedItem().setAmount(amount < 0 ? 0 : amount);
								if(firstGrowth) {
									FarmingManager.updatePatch(player, patchType);
								}
							}
						} else {
							player.message("One of your crops is now fully grown!");
							cancel();
						}
					}
				}
			}
		}).submit();
	}
	
	public void decreaseDiseaseImmunity() {
		diseaseImmunity -= RandomUtils.inclusive(15);
		if(diseaseImmunity < FarmingConstants.DISEASE_IMMUNITY)
			diseaseImmunity = FarmingConstants.DISEASE_IMMUNITY;
	}
	
	public void setPlantConfigurations(Player player) {
		timePlanted = System.currentTimeMillis();
		wateredTimer = System.currentTimeMillis();
		
		if(getSeedType() != null && getSeedType().getSeedClass() == SeedClass.ALLOTMENT) {
			PatchType otherPatchType = null;
			switch(patchType) {
				case FALADOR_NORTH_WEST_ALLOTMENT:
				case FALADOR_SOUTH_EAST_ALLOTMENT:
					otherPatchType = PatchType.FALADOR_FLOWER_PATCH;
					break;
				case CATHERBY_NORTH_ALLOTMENT:
				case CATHERBY_SOUTH_ALLOTMENT:
					otherPatchType = PatchType.CATHERBY_FLOWER_PATCH;
					break;
				case ARDOUGNE_NORTH_ALLOTMENT:
				case ARDOUGNE_SOUTH_ALLOTMENT:
					otherPatchType = PatchType.ARDOUGNE_FLOWER_PATCH;
					break;
				case CANIFIS_NORTH_WEST_ALLOTMENT:
				case CANIFIS_SOUTH_EAST_ALLOTMENT:
					otherPatchType = PatchType.CANIFIS_FLOWER_PATCH;
					break;
				default:
					return;
			}
			final Patch otherPatch = player.patches.get(otherPatchType);
			if(otherPatch != null && otherPatch.getSeedType() != null) {
				final int seedId = otherPatch.getSeedType().getSeed().getId();
				boolean protectedPatch = false;
				if(seedId == FlowerSeed.WHITE_LILY.getSeed().getId()) {
					protectedPatch = true;
				} else if(seedId == FlowerSeed.MARIGOLD.getSeed().getId()) {
					protectedPatch = getSeedType().getSeed().getId() == AllotmentSeed.ONION.getSeed().getId() || getSeedType().getSeed().getId() == AllotmentSeed.TOMATO.getSeed().getId() || getSeedType().getSeed().getId() == AllotmentSeed.POTATO.getSeed().getId();
					
				} else if(seedId == FlowerSeed.NASTURTIUM.getSeed().getId()) {
					protectedPatch = getSeedType().getSeed().getId() == AllotmentSeed.WATERMELON.getSeed().getId();
				}
				if(protectedPatch && !hasAttribute(PatchAttribute.PROTECTED)) {
					addAttribute(PatchAttribute.PROTECTED);
					player.message("Your patch feels protected...");
				}
			}
		}
	}
	
	public boolean isFullyGrown() {
		return getSeedType() != null && getStage() >= getSeedType().getValues().length - 1;
	}
	
	public boolean isProtected(Player player) {
		if(getSeedType() == null)
			return false;
		if(getSeedType() == FlowerSeed.WHITE_LILY || hasAttribute(PatchAttribute.PROTECTED))
			return true;
		if(player == null)
			return false;
		PatchType otherPatchType = null;
		switch(patchType) {
			case FALADOR_NORTH_WEST_ALLOTMENT:
			case FALADOR_SOUTH_EAST_ALLOTMENT:
				otherPatchType = PatchType.FALADOR_FLOWER_PATCH;
				break;
			case CATHERBY_NORTH_ALLOTMENT:
			case CATHERBY_SOUTH_ALLOTMENT:
				otherPatchType = PatchType.CATHERBY_FLOWER_PATCH;
				break;
			case ARDOUGNE_NORTH_ALLOTMENT:
			case ARDOUGNE_SOUTH_ALLOTMENT:
				otherPatchType = PatchType.ARDOUGNE_FLOWER_PATCH;
				break;
			case CANIFIS_NORTH_WEST_ALLOTMENT:
			case CANIFIS_SOUTH_EAST_ALLOTMENT:
				otherPatchType = PatchType.CANIFIS_FLOWER_PATCH;
				break;
			default:
				return false;
		}
		final Patch otherPatch = player.patches.get(otherPatchType);
		if(otherPatch != null && otherPatch.getSeedType() != null) {
			final int seedId = otherPatch.getSeedType().getSeed().getId();
			if(seedId == FlowerSeed.WHITE_LILY.getSeed().getId()) {
				return true;
				
			} else if(seedId == FlowerSeed.MARIGOLD.getSeed().getId()) {
				return getSeedType().getSeed().getId() == AllotmentSeed.ONION.getSeed().getId() || getSeedType().getSeed().getId() == AllotmentSeed.TOMATO.getSeed().getId() || getSeedType().getSeed().getId() == AllotmentSeed.POTATO.getSeed().getId();
				
			} else if(seedId == FlowerSeed.NASTURTIUM.getSeed().getId()) {
				return getSeedType().getSeed().getId() == AllotmentSeed.WATERMELON.getSeed().getId();
			}
		}
		return false;
	}
	
	public PatchType getPatchType() {
		return patchType;
	}
	
	public SeedType getSeedType() {
		return seedType;
	}
	
	public void setSeedType(SeedType seedType) {
		this.seedType = seedType;
	}
	
	public SeedClass getSeedClass() {
		return seedClass;
	}
	
	public void setSeedClass(SeedClass seedClass) {
		this.seedClass = seedClass;
	}
	
	public String getSeedTypeName() {
		return seedTypeName;
	}
	
	public GrowthState getGrowthState() {
		return growthState;
	}
	
	public void setGrowthState(GrowthState growthState) {
		this.growthState = growthState;
	}
	
	public int getStage() {
		return stage;
	}
	
	public void setStage(int stage) {
		this.stage = stage;
	}
	
	public boolean addStage(Player player) {
		if(weedStage < FarmingConstants.WEED_CONFIG.length - 1)
			return false;
		final int length = seedType.getValues().length;
		stage++;
		if(stage >= length) {
			stage = length - 1;
			return false;
		}
		
		if(growthState != GrowthState.DISEASED && growthState != GrowthState.DEAD && stage < length - 1
				//&& MathUtil.random(getDiseaseImmunity()) == 0 //TODO
				&& !isProtected(player)) {
			if(growthState == GrowthState.WATERED && !isFullyGrown()) {
				growthState = GrowthState.GROWING;
			} else {
				growthState = GrowthState.DISEASED;
			}
		}
		
		return true;
	}
	
	public int getWeedStage() {
		return weedStage;
	}
	
	public void setWeedStage(int weedStage) {
		this.weedStage = weedStage;
	}
	
	public void addAttribute(PatchAttribute patchAttribute) {
		attributes |= patchAttribute.getShift();
	}
	
	public void removeAttribute(PatchAttribute patchAttribute) {
		attributes &= ~patchAttribute.getShift();
	}
	
	public boolean hasAttribute(PatchAttribute patchAttribute) {
		return (attributes & patchAttribute.getShift()) != 0;
	}
	
	public int getDiseaseImmunity() {
		return diseaseImmunity;
	}
	
	public void setDiseaseImmunity(int diseaseImmunity) {
		this.diseaseImmunity = diseaseImmunity;
	}
	
	public Item getHarvestedItem() {
		return harvestedItem;
	}
	
	public void setHarvestedItem(Item harvestedItem) {
		this.harvestedItem = harvestedItem;
	}
	
	public Item getProduct() {
		return product;
	}
	
	public void setProduct(Item product) {
		this.product = product;
	}
}
