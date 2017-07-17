package net.edge.action.but;

import net.edge.content.skill.summoning.Summoning;
import net.edge.content.skill.summoning.familiar.Familiar;
import net.edge.content.skill.summoning.familiar.FamiliarContainer;
import net.edge.action.ActionInitializer;
import net.edge.action.impl.ButtonAction;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.item.Item;

import java.util.Optional;

public class FamiliarControls extends ActionInitializer {
	
	@Override
	public void init() {
		ButtonAction e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				Optional<Familiar> has_familiar = player.getFamiliar();
				
				if(!has_familiar.isPresent()) {
					return true;
				}
				
				Familiar familiar = has_familiar.get();
				
				if(!familiar.getAbilityType().isHoldableContainer()) {
					player.message("This familiar cannot hold items.");
					return true;
				}
				
				FamiliarContainer storage = (FamiliarContainer) familiar.getAbilityType();
				
				if(storage.getContainer().size() < 1) {
					player.message("This familiar is not holding any items.");
					return true;
				}
				
				for(Item item : storage.getContainer().getItems()) {
					if(item == null)
						continue;
					int amount = storage.getContainer().computeAmountForId(item.getId());
					if(item.getAmount() > amount) {
						item.setAmount(amount);
					}
					storage.withdraw(player, item);
				}
				return true;
			}
		};
		e.register(70109);
		
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				Summoning.callFamiliar(player);
				return true;
			}
		};
		e.register(70115);
		
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				Summoning.dismiss(player, false);
				return true;
			}
		};
		e.register(70118);

	}
	
}
