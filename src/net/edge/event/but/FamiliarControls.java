package net.edge.event.but;

import net.edge.content.skill.summoning.Summoning;
import net.edge.content.skill.summoning.familiar.Familiar;
import net.edge.content.skill.summoning.familiar.FamiliarContainer;
import net.edge.event.EventInitializer;
import net.edge.event.impl.ButtonEvent;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.Optional;

public class FamiliarControls extends EventInitializer {
	
	@Override
	public void init() {
		ButtonEvent e = new ButtonEvent() {
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
		
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				Summoning.callFamiliar(player);
				return true;
			}
		};
		e.register(70115);
		
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				Summoning.dismiss(player, false);
				return true;
			}
		};
		e.register(70118);

	}
	
}
