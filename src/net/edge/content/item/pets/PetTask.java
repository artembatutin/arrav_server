package net.edge.content.item.pets;

import net.edge.task.Task;
import net.edge.content.TabInterface;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;

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
		player.text(19032, ((int) pet.getProgress().getHunger()) + "%");
		if(pet.getProgress().getHunger() >= 100.0) {
			this.cancel();
			World.get().getMobs().remove(pet);
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
			player.text(19030, ((int) pet.getProgress().getGrowth()) + "%");
			if(pet.getProgress().getGrowth() >= 100) {
				this.cancel();
				player.getPetManager().progressed();
			}
		}
	}
}
