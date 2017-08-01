package net.edge.content.skill.farming;

import net.edge.content.skill.farming.attributes.GrowthState;
import net.edge.content.skill.farming.attributes.PatchAttribute;
import net.edge.content.skill.farming.patch.Patch;
import net.edge.content.skill.farming.patch.PatchType;
import net.edge.content.skill.farming.seed.SeedClass;
import net.edge.content.skill.farming.seed.impl.MushroomSeed;
import net.edge.net.packet.out.SendConfig;
import net.edge.world.entity.actor.player.Player;

public final class FarmingManager {
	
	public static void login(Player player) {
		/*int value;
		int configId;
		
		configId = PatchType.VARROCK_CASTLE_TREE_PATCH.getConfigId();
		final Patch varrockTree = player.getPlayerFields().getSkillAttributes().getPatches().get(PatchType.VARROCK_CASTLE_TREE_PATCH);
		final Patch faladorTree = player.getPlayerFields().getSkillAttributes().getPatches().get(PatchType.FALADOR_TREE_PATCH);
		final Patch taverleyTree = player.getPlayerFields().getSkillAttributes().getPatches().get(PatchType.TAVERLEY_TREE_PATCH);
		value = ((0 << 24))
				+ (getPatchValue(varrockTree) << 16)
				+ (getPatchValue(faladorTree) << 8)
				+ (getPatchValue(taverleyTree));
		player.out(new SendConfig((configId, value);
		
		
		configId = PatchType.CATHERBY_NORTH_ALLOTMENT.getConfigId();
		final Patch catherbyN = player.getPlayerFields().getSkillAttributes().getPatches().get(PatchType.CATHERBY_NORTH_ALLOTMENT);
		final Patch catherbyS = player.getPlayerFields().getSkillAttributes().getPatches().get(PatchType.CATHERBY_SOUTH_ALLOTMENT);
		final Patch faladorNW = player.getPlayerFields().getSkillAttributes().getPatches().get(PatchType.FALADOR_NORTH_WEST_ALLOTMENT);
		final Patch faladorSE = player.getPlayerFields().getSkillAttributes().getPatches().get(PatchType.FALADOR_SOUTH_EAST_ALLOTMENT);
		value = ((getPatchValue(catherbyS) << 24))
				+ (getPatchValue(catherbyN) << 16)
				+ (getPatchValue(faladorSE) << 8)
				+ (getPatchValue(faladorNW));
		player.out(new SendConfig((configId, value);
		
		
		configId = PatchType.ARDOUGNE_NORTH_ALLOTMENT.getConfigId();
		final Patch ardougneN = player.getPlayerFields().getSkillAttributes().getPatches().get(PatchType.ARDOUGNE_NORTH_ALLOTMENT);
		final Patch ardougneS = player.getPlayerFields().getSkillAttributes().getPatches().get(PatchType.ARDOUGNE_SOUTH_ALLOTMENT);
		final Patch canafisNW = player.getPlayerFields().getSkillAttributes().getPatches().get(PatchType.CANIFIS_NORTH_WEST_ALLOTMENT);
		final Patch canafisSE = player.getPlayerFields().getSkillAttributes().getPatches().get(PatchType.CANIFIS_SOUTH_EAST_ALLOTMENT);
		value = ((getPatchValue(canafisSE) << 24))
				+ (getPatchValue(canafisNW) << 16)
				+ (getPatchValue(ardougneS) << 8)
				+ (getPatchValue(ardougneN));
		player.out(new SendConfig((configId, value);
		
		
		configId = PatchType.FALADOR_FLOWER_PATCH.getConfigId();
		final Patch faladorFlower = player.getPlayerFields().getSkillAttributes().getPatches().get(PatchType.FALADOR_FLOWER_PATCH);
		final Patch catherbyFlower = player.getPlayerFields().getSkillAttributes().getPatches().get(PatchType.CATHERBY_FLOWER_PATCH);
		final Patch ardougneFlower = player.getPlayerFields().getSkillAttributes().getPatches().get(PatchType.ARDOUGNE_FLOWER_PATCH);
		final Patch canifisFlower = player.getPlayerFields().getSkillAttributes().getPatches().get(PatchType.CANIFIS_FLOWER_PATCH);
		value = ((getPatchValue(canifisFlower) << 24))
				+ (getPatchValue(ardougneFlower) << 16)
				+ (getPatchValue(catherbyFlower) << 8)
				+ (getPatchValue(faladorFlower));
		player.out(new SendConfig((configId, value);
		
		
		configId = PatchType.FALADOR_HERB_PATCH.getConfigId();
		final Patch faladorHerb = player.getPlayerFields().getSkillAttributes().getPatches().get(PatchType.FALADOR_HERB_PATCH);
		final Patch catherbyHerb = player.getPlayerFields().getSkillAttributes().getPatches().get(PatchType.CATHERBY_HERB_PATCH);
		final Patch ardougneHerb = player.getPlayerFields().getSkillAttributes().getPatches().get(PatchType.ARDOUGNE_HERB_PATCH);
		final Patch canifisHerb = player.getPlayerFields().getSkillAttributes().getPatches().get(PatchType.CANIFIS_HERB_PATCH);
		value = ((getPatchValue(canifisHerb) << 24))
				+ (getPatchValue(ardougneHerb) << 16)
				+ (getPatchValue(catherbyHerb) << 8)
				+ (getPatchValue(faladorHerb));
		player.out(new SendConfig((configId, value);
		
		
		configId = PatchType.VARROCK_BUSH_PATCH.getConfigId();
		final Patch varrockBush = player.getPlayerFields().getSkillAttributes().getPatches().get(PatchType.VARROCK_BUSH_PATCH);
		final Patch rimmingtonBush = player.getPlayerFields().getSkillAttributes().getPatches().get(PatchType.RIMMINGTON_BUSH_PATCH);
		final Patch ardougneBush = player.getPlayerFields().getSkillAttributes().getPatches().get(PatchType.ARDOUGNE_BUSH_PATCH);
		final Patch etceteriaBush = player.getPlayerFields().getSkillAttributes().getPatches().get(PatchType.ETCETERIA_BUSH_PATCH);
		value = ((getPatchValue(ardougneBush) << 24))
				+ (getPatchValue(etceteriaBush) << 16)
				+ (getPatchValue(rimmingtonBush) << 8)
				+ (getPatchValue(varrockBush));
		player.out(new SendConfig((configId, value);
		
		
		configId = PatchType.CANAFIS_MUSHROOM_PATCH.getConfigId();
		final Patch canafisMushroom = player.getPlayerFields().getSkillAttributes().getPatches().get(PatchType.CANAFIS_MUSHROOM_PATCH);
		value = (getPatchValue(canafisMushroom) << 8)
				+ (0);
		player.out(new SendConfig((configId, value);*/
		
		for(Patch patch : player.getPatches().values()) {
			patch.submitGrowthTask(player);
			if(patch.getPatchType() != null) {
				updatePatch(player, patch.getPatchType());
			}
		}
	}
	
	public static void updatePatch(Player player, PatchType patchType) {
		int value;
		switch(patchType) {
			case VARROCK_CASTLE_TREE_PATCH:
			case FALADOR_TREE_PATCH:
			case TAVERLEY_TREE_PATCH:
				final Patch varrockTree = player.getPatches().get(PatchType.VARROCK_CASTLE_TREE_PATCH);
				final Patch faladorTree = player.getPatches().get(PatchType.FALADOR_TREE_PATCH);
				final Patch taverleyTree = player.getPatches().get(PatchType.TAVERLEY_TREE_PATCH);
				value = ((0 << 24)) + (getPatchValue(varrockTree) << 16) + (getPatchValue(faladorTree) << 8) + (getPatchValue(taverleyTree));
				player.out(new SendConfig(patchType.getConfigId(), value));
				break;
			case CATHERBY_NORTH_ALLOTMENT:
			case CATHERBY_SOUTH_ALLOTMENT:
			case FALADOR_NORTH_WEST_ALLOTMENT:
			case FALADOR_SOUTH_EAST_ALLOTMENT:
				final Patch catherbyN = player.getPatches().get(PatchType.CATHERBY_NORTH_ALLOTMENT);
				final Patch catherbyS = player.getPatches().get(PatchType.CATHERBY_SOUTH_ALLOTMENT);
				final Patch faladorNW = player.getPatches().get(PatchType.FALADOR_NORTH_WEST_ALLOTMENT);
				final Patch faladorSE = player.getPatches().get(PatchType.FALADOR_SOUTH_EAST_ALLOTMENT);
				value = ((getPatchValue(catherbyS) << 24)) + (getPatchValue(catherbyN) << 16) + (getPatchValue(faladorSE) << 8) + (getPatchValue(faladorNW));
				player.out(new SendConfig(patchType.getConfigId(), value));
				break;
			case ARDOUGNE_NORTH_ALLOTMENT:
			case ARDOUGNE_SOUTH_ALLOTMENT:
			case CANIFIS_NORTH_WEST_ALLOTMENT:
			case CANIFIS_SOUTH_EAST_ALLOTMENT:
				final Patch ardougneN = player.getPatches().get(PatchType.ARDOUGNE_NORTH_ALLOTMENT);
				final Patch ardougneS = player.getPatches().get(PatchType.ARDOUGNE_SOUTH_ALLOTMENT);
				final Patch canafisNW = player.getPatches().get(PatchType.CANIFIS_NORTH_WEST_ALLOTMENT);
				final Patch canafisSE = player.getPatches().get(PatchType.CANIFIS_SOUTH_EAST_ALLOTMENT);
				value = ((getPatchValue(canafisSE) << 24)) + (getPatchValue(canafisNW) << 16) + (getPatchValue(ardougneS) << 8) + (getPatchValue(ardougneN));
				player.out(new SendConfig(patchType.getConfigId(), value));
				break;
			case FALADOR_FLOWER_PATCH:
			case CATHERBY_FLOWER_PATCH:
			case ARDOUGNE_FLOWER_PATCH:
			case CANIFIS_FLOWER_PATCH:
				final Patch faladorFlower = player.getPatches().get(PatchType.FALADOR_FLOWER_PATCH);
				final Patch catherbyFlower = player.getPatches().get(PatchType.CATHERBY_FLOWER_PATCH);
				final Patch ardougneFlower = player.getPatches().get(PatchType.ARDOUGNE_FLOWER_PATCH);
				final Patch canifisFlower = player.getPatches().get(PatchType.CANIFIS_FLOWER_PATCH);
				value = ((getPatchValue(canifisFlower) << 24)) + (getPatchValue(ardougneFlower) << 16) + (getPatchValue(catherbyFlower) << 8) + (getPatchValue(faladorFlower));
				player.out(new SendConfig(patchType.getConfigId(), value));
				break;
			case FALADOR_HERB_PATCH: //3057, 3310
			case CATHERBY_HERB_PATCH: //2812, 3463
			case ARDOUGNE_HERB_PATCH: //2669, 3375
			case CANIFIS_HERB_PATCH: //3600, 3525
				final Patch faladorHerb = player.getPatches().get(PatchType.FALADOR_HERB_PATCH);
				final Patch catherbyHerb = player.getPatches().get(PatchType.CATHERBY_HERB_PATCH);
				final Patch ardougneHerb = player.getPatches().get(PatchType.ARDOUGNE_HERB_PATCH);
				final Patch canifisHerb = player.getPatches().get(PatchType.CANIFIS_HERB_PATCH);
				value = ((getPatchValue(canifisHerb) << 24)) + (getPatchValue(ardougneHerb) << 16) + (getPatchValue(catherbyHerb) << 8) + (getPatchValue(faladorHerb));
				player.out(new SendConfig(patchType.getConfigId(), value));
				break;
			case VARROCK_BUSH_PATCH:
			case RIMMINGTON_BUSH_PATCH:
			case ARDOUGNE_BUSH_PATCH:
			case ETCETERIA_BUSH_PATCH:
				final Patch varrockBush = player.getPatches().get(PatchType.VARROCK_BUSH_PATCH);
				final Patch rimmingtonBush = player.getPatches().get(PatchType.RIMMINGTON_BUSH_PATCH);
				final Patch ardougneBush = player.getPatches().get(PatchType.ARDOUGNE_BUSH_PATCH);
				final Patch etceteriaBush = player.getPatches().get(PatchType.ETCETERIA_BUSH_PATCH);
				value = ((getPatchValue(ardougneBush) << 24)) + (getPatchValue(etceteriaBush) << 16) + (getPatchValue(rimmingtonBush) << 8) + (getPatchValue(varrockBush));
				player.out(new SendConfig(patchType.getConfigId(), value));
				break;
			case CANAFIS_MUSHROOM_PATCH:
				final Patch canafisMushroom = player.getPatches().get(PatchType.CANAFIS_MUSHROOM_PATCH);
				value = (getPatchValue(canafisMushroom) << 8);
				player.out(new SendConfig(patchType.getConfigId(), value));
				break;
		}
	}
	
	public static int getPatchValue(Patch patch) {
		if(patch == null)
			return 0;
		if(patch.getSeedType() != null) {
			int growthShift = patch.getGrowthState().getShift();
			if(growthShift != GrowthState.GROWING.getShift() && patch.getSeedType().getSeedClass() == SeedClass.BUSHES) {
				growthShift -= 0x01;
			} else if(growthShift != GrowthState.GROWING.getShift() && patch.getSeedType().getSeedClass() == SeedClass.TREES) {
				growthShift = GrowthState.GROWING.getShift();
				patch.setGrowthState(GrowthState.GROWING);
			}
			int growth = patch.getGrowthState() != null ? (growthShift << 6) : 0;
			int value = patch.getSeedType().getValues()[patch.getStage()];
			if(patch.getSeedType().getSeedClass() == SeedClass.BUSHES) {
				value -= patch.getHarvestedItem().getAmount();
			} else if(patch.getSeedType().getSeedClass() == SeedClass.TREES) {
				if(patch.getHarvestedItem().getAmount() >= patch.getProduct().getAmount()) {
					value += 2;
				} else if(patch.hasAttribute(PatchAttribute.CHECKED_HEALTH)) {
					value += 1;
				}
			} else if(patch.getSeedType() == MushroomSeed.BITTERCAP && patch.getHarvestedItem().getAmount() > 0) {
				value += patch.getHarvestedItem().getAmount();
			}
			return growth | value;
		} else {
			return patch.getWeedStage() < FarmingConstants.WEED_CONFIG.length ? FarmingConstants.WEED_CONFIG[patch.getWeedStage()] : FarmingConstants.WEED_CONFIG[3];
		}
	}
}