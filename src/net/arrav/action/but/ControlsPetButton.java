package net.arrav.action.but;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ButtonAction;
import net.arrav.content.item.pets.Pet;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.region.TraversalMap;
import net.arrav.world.locale.Position;

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
