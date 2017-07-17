package net.edge.content.pets;

import net.edge.world.node.actor.player.Player;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public final class PetManager {
	
	/**
	 * The player instance for this pet manager.
	 */
	private final Player player;
	
	/**
	 * The current pet spawned.
	 */
	private Pet pet;
	
	/**
	 * The set of all pets progressions.
	 */
	private Set<PetProgress> pets = new HashSet<>();
	
	/**
	 * Constructs a new {@link PetManager}.
	 * @param player the player this pet manager is for.
	 */
	public PetManager(Player player) {
		this.player = player;
	}
	
	/**
	 * Sets the new spawned pet.
	 * @param pet the pet instance spawned.
	 */
	public Pet put(Pet pet) {
		if(pet == null)//loading but no pet found.
			return null;
		Pet finalPet = pet;
		Optional<PetProgress> saved = pets.stream().filter(p -> p.getData() == finalPet.getProgress().getData()).findAny();
		if(saved.isPresent()) {
			pet = new Pet(saved.get(), player.getPosition());
		} else {
			pets.add(pet.getProgress());
		}
		this.pet = pet;
		return pet;
	}
	
	/**
	 * The player's pet has reaching the growing stage.
	 */
	public void progressed() {
		if(pet == null)
			return;
		Optional<PetData> next = pet.getProgress().getData().getPolicy().getNext();
		if(next.isPresent()) {
			PetData data = next.get();
			Pet pet = new Pet(data, this.pet.getPosition());
			Pet.onLogout(player);
			this.pet = pet;
			Pet.onLogin(player);
			player.message("Your pet has gone bigger!");
		}
	}
	
	/**
	 * Resets the current pet the player has spawned.
	 */
	public void reset() {
		this.pet = null;
	}
	
	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * @return the pets
	 */
	public Set<PetProgress> getProgress() {
		return pets;
	}
	
	/**
	 * Gets the pet the player currently has summoned.
	 * @return the pet the player currently has summoned or {@link Optional#empty()}.
	 */
	public Optional<Pet> getPet() {
		if(pet == null) {
			return Optional.empty();
		} else {
			return Optional.of(pet);
		}
	}
}
