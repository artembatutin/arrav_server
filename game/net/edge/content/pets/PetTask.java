package net.edge.content.pets;

import net.edge.task.Task;
import net.edge.util.Utility;
import net.edge.content.TabInterface;
import net.edge.world.World;
import net.edge.world.node.entity.player.Player;

public final class PetTask extends Task {
	
	private final Player player;
	
	private final Pet pet;
	
	public PetTask(Player player, Pet pet) {
		super(1);
		this.player = player;
		this.pet = pet;
	}
	
	private boolean notificated_starving = false;
	
	@Override
	protected void execute() {
		pet.getProgress().updateHunger();
		
		player.getMessages().sendString(Double.toString(Utility.round(pet.getProgress().getHunger(), 2)) + "%", 19032);
		
		if(pet.getProgress().getHunger() == 100.0) {
			this.cancel();
			World.get().getNpcs().remove(pet);
			player.getPetManager().reset();
			TabInterface.SUMMONING.sendInterface(player, -1);
			player.message("Your pet has ran away to find some food!");
			return;
		}
		
		if(pet.getProgress().getHunger() >= 90.0 && !notificated_starving) {
			player.message("@red@Your pet is starving, feed it before it runs off.");
			notificated_starving = true;
		}
		
		pet.getProgress().updateGrowth();
		
		player.getMessages().sendString(Double.toString(Utility.round(pet.getProgress().getGrowth(), 2)) + "%", 19030);
		
		if(pet.getProgress().getGrowth() == 100) {
			this.cancel();
			
			//TODO artem you know what to do here.
		}
	}
}
