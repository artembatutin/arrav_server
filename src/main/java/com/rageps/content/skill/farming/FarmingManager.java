package com.rageps.content.skill.farming;

import com.rageps.content.skill.farming.attributes.GrowthState;
import com.rageps.content.skill.farming.attributes.PatchAttribute;
import com.rageps.content.skill.farming.patch.Patch;
import com.rageps.content.skill.farming.patch.PatchType;
import com.rageps.content.skill.farming.seed.SeedClass;
import com.rageps.content.skill.farming.seed.impl.MushroomSeed;
import com.rageps.net.refactor.packet.out.model.ConfigPacket;
import com.rageps.world.entity.actor.player.Player;

public final class FarmingManager {
	
	public static void login(Player player) {
		for(Patch patch : player.patches.values()) {
			if(patch.getSeedType() == null && patch.getSeedTypeName() != null) {
				patch.setSeedType(patch.getSeedClass().getInstance(patch.getSeedTypeName()));
			}
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
			case HOME_EAST_TREE_PATCH:
			case HOME_WEST_TREE_PATCH:
				final Patch varrockTree = player.patches.get(PatchType.VARROCK_CASTLE_TREE_PATCH);
				final Patch faladorTree = player.patches.get(PatchType.HOME_EAST_TREE_PATCH);
				final Patch taverleyTree = player.patches.get(PatchType.HOME_WEST_TREE_PATCH);
				value = ((0 << 24)) + (getPatchValue(varrockTree) << 16) + (getPatchValue(faladorTree) << 8) + (getPatchValue(taverleyTree));
				player.send(new ConfigPacket(patchType.getConfigId(), value));
				break;
			case CATHERBY_NORTH_ALLOTMENT:
			case CATHERBY_SOUTH_ALLOTMENT:
			case FALADOR_NORTH_WEST_ALLOTMENT:
			case FALADOR_SOUTH_EAST_ALLOTMENT:
				final Patch catherbyN = player.patches.get(PatchType.CATHERBY_NORTH_ALLOTMENT);
				final Patch catherbyS = player.patches.get(PatchType.CATHERBY_SOUTH_ALLOTMENT);
				final Patch faladorNW = player.patches.get(PatchType.FALADOR_NORTH_WEST_ALLOTMENT);
				final Patch faladorSE = player.patches.get(PatchType.FALADOR_SOUTH_EAST_ALLOTMENT);
				value = ((getPatchValue(catherbyS) << 24)) + (getPatchValue(catherbyN) << 16) + (getPatchValue(faladorSE) << 8) + (getPatchValue(faladorNW));
				player.send(new ConfigPacket(patchType.getConfigId(), value));
				break;
			case ARDOUGNE_NORTH_ALLOTMENT:
			case ARDOUGNE_SOUTH_ALLOTMENT:
			case CANIFIS_NORTH_WEST_ALLOTMENT:
			case CANIFIS_SOUTH_EAST_ALLOTMENT:
				final Patch ardougneN = player.patches.get(PatchType.ARDOUGNE_NORTH_ALLOTMENT);
				final Patch ardougneS = player.patches.get(PatchType.ARDOUGNE_SOUTH_ALLOTMENT);
				final Patch canafisNW = player.patches.get(PatchType.CANIFIS_NORTH_WEST_ALLOTMENT);
				final Patch canafisSE = player.patches.get(PatchType.CANIFIS_SOUTH_EAST_ALLOTMENT);
				value = ((getPatchValue(canafisSE) << 24)) + (getPatchValue(canafisNW) << 16) + (getPatchValue(ardougneS) << 8) + (getPatchValue(ardougneN));
				player.send(new ConfigPacket(patchType.getConfigId(), value));
				break;
			case FALADOR_FLOWER_PATCH:
			case CATHERBY_FLOWER_PATCH:
			case ARDOUGNE_FLOWER_PATCH:
			case CANIFIS_FLOWER_PATCH:
				final Patch faladorFlower = player.patches.get(PatchType.FALADOR_FLOWER_PATCH);
				final Patch catherbyFlower = player.patches.get(PatchType.CATHERBY_FLOWER_PATCH);
				final Patch ardougneFlower = player.patches.get(PatchType.ARDOUGNE_FLOWER_PATCH);
				final Patch canifisFlower = player.patches.get(PatchType.CANIFIS_FLOWER_PATCH);
				value = ((getPatchValue(canifisFlower) << 24)) + (getPatchValue(ardougneFlower) << 16) + (getPatchValue(catherbyFlower) << 8) + (getPatchValue(faladorFlower));
				player.send(new ConfigPacket(patchType.getConfigId(), value));
				break;
			case FALADOR_HERB_PATCH: //3057, 3310
			case CATHERBY_HERB_PATCH: //2812, 3463
			case ARDOUGNE_HERB_PATCH: //2669, 3375
			case CANIFIS_HERB_PATCH: //3600, 3525
				final Patch faladorHerb = player.patches.get(PatchType.FALADOR_HERB_PATCH);
				final Patch catherbyHerb = player.patches.get(PatchType.CATHERBY_HERB_PATCH);
				final Patch ardougneHerb = player.patches.get(PatchType.ARDOUGNE_HERB_PATCH);
				final Patch canifisHerb = player.patches.get(PatchType.CANIFIS_HERB_PATCH);
				value = ((getPatchValue(canifisHerb) << 24)) + (getPatchValue(ardougneHerb) << 16) + (getPatchValue(catherbyHerb) << 8) + (getPatchValue(faladorHerb));
				player.send(new ConfigPacket(patchType.getConfigId(), value));
				break;
			case VARROCK_BUSH_PATCH:
			case RIMMINGTON_BUSH_PATCH:
			case ARDOUGNE_BUSH_PATCH:
			case ETCETERIA_BUSH_PATCH:
				final Patch varrockBush = player.patches.get(PatchType.VARROCK_BUSH_PATCH);
				final Patch rimmingtonBush = player.patches.get(PatchType.RIMMINGTON_BUSH_PATCH);
				final Patch ardougneBush = player.patches.get(PatchType.ARDOUGNE_BUSH_PATCH);
				final Patch etceteriaBush = player.patches.get(PatchType.ETCETERIA_BUSH_PATCH);
				value = ((getPatchValue(ardougneBush) << 24)) + (getPatchValue(etceteriaBush) << 16) + (getPatchValue(rimmingtonBush) << 8) + (getPatchValue(varrockBush));
				player.send(new ConfigPacket(patchType.getConfigId(), value));
				break;
			case CANAFIS_MUSHROOM_PATCH:
				final Patch canafisMushroom = player.patches.get(PatchType.CANAFIS_MUSHROOM_PATCH);
				value = (getPatchValue(canafisMushroom) << 8);
				player.send(new ConfigPacket(patchType.getConfigId(), value));
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