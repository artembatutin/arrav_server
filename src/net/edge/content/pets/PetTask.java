package net.edge.content.pets;

import net.edge.task.Task;
import net.edge.util.Utility;
import net.edge.content.TabInterface;
import net.edge.world.World;
import net.edge.world.node.entity.player.Player;

import java.util.Optional;

public final class PetTask extends Task {
	
	private final Player player;
	
	private final Pet pet;
	
	public PetTask(Player player, Pet pet) {
		super(100);
		this.player = player;
		this.pet = pet;
	}
	
	@Override
	protected void execute() {
		//hunger update.
		pet.getProgress().updateHunger();
		player.getMessages().sendString(((int) pet.getProgress().getHunger()) + "%", 19032);
		if(pet.getProgress().getHunger() >= 100.0) {
			this.cancel();
			World.get().getNpcs().remove(pet);
			player.getPetManager().reset();
			TabInterface.SUMMONING.sendInterface(player, -1);
			player.message("Your pet has ran away to find some food!");
			return;
		}
		if(pet.getProgress().getHunger() >= 90.0) {
			player.message("@red@Your pet is starving, feed it before it runs off.");
		}
		
		//growth update if next pet is possible
		if(pet.getProgress().getData().getPolicy().getNext().isPresent()) {
			pet.getProgress().updateGrowth();
			player.getMessages().sendString(((int) pet.getProgress().getGrowth()) + "%", 19030);
			if(pet.getProgress().getGrowth() >= 100) {
				this.cancel();
				player.getPetManager().progressed();
			}
		}
	}
}
