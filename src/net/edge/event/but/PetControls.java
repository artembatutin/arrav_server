package net.edge.event.but;

import net.edge.content.pets.Pet;
import net.edge.event.EventInitializer;
import net.edge.event.impl.ButtonEvent;
import net.edge.locale.Position;
import net.edge.world.World;
import net.edge.world.node.entity.player.Player;

import java.util.Optional;

public class PetControls extends EventInitializer {
	
	@Override
	public void init() {
		ButtonEvent e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				Optional<Pet> pet = player.getPetManager().getPet();
				if(pet.isPresent()) {
					Pet p = pet.get();
					Position position = World.getTraversalMap().getRandomNearby(player.getPosition(), player.getPosition(), p.size());
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
		
		e = new ButtonEvent() {
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
