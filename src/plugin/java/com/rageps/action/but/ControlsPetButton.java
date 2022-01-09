package com.rageps.action.but;

import com.rageps.action.impl.ButtonAction;
import com.rageps.content.item.pets.Pet;
import com.rageps.action.ActionInitializer;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.region.TraversalMap;
import com.rageps.world.locale.Position;

import java.util.Optional;

public class ControlsPetButton extends ActionInitializer {
	
	@Override
	public void init() {
		ButtonAction e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				Optional<Pet> pet = player.getPetManager().getPet();
				if(pet.isPresent()) {
					Pet p = pet.get();
					Position position = TraversalMap.getRandomNearby(player.getPosition(), player.getPosition(), p.size());
					if(position != null) {
						p.move(position);
					} else {
						p.move(player.getPosition());
					}
					p.forceChat(p.getProgress().getData().getType().getShout());
				}
				return true;
			}
		};
		e.register(74078);
		
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				Pet.onLogout(player);
				player.message("Your pet is now gone");
				return true;
			}
		};
		e.register(74081);
		
	}
	
}
