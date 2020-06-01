package com.rageps.action.mob;

import com.rageps.action.impl.MobAction;
import com.rageps.content.dialogue.impl.NpcDialogue;
import com.rageps.content.dialogue.test.DialogueAppender;
import com.rageps.content.pvp.AncientArtefacts;
import com.rageps.action.ActionInitializer;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;

public class Mandrith extends ActionInitializer {
	@Override
	public void init() {
		MobAction e = new MobAction() {
			@Override
			public boolean click(Player player, Mob npc, int click) {
				DialogueAppender a = new DialogueAppender(player);
				for(AncientArtefacts artefacts : AncientArtefacts.values()) {
					if(player.getInventory().contains(artefacts.getId())) {
						a.chain(new NpcDialogue(6537, "Take my rewards for those ancient artefacts!", "I've been looking for ages...").attachAfter(() -> player.closeWidget()));
						final int amount = player.getInventory().computeAmountForId(artefacts.getId());
						player.message("Mandrith bought your " + amount + " " + artefacts.name().toLowerCase().replace("_", " ") + " for a total of " + artefacts.getPrice() * amount + " coins.");
						player.getInventory().remove(new Item(artefacts.getId(), amount));
						player.getInventory().addOrDrop(new Item(995, artefacts.getPrice() * amount));
					} else {
						a.chain(new NpcDialogue(6537, "Oh well, you have no ancient artefacts? ", "Come back when you have something that interests me...").attachAfter(() -> player.closeWidget()));
					}
				}
				a.start();
				return true;
			}
		};
		e.registerFirst(6537);
	}
}
