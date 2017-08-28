package net.edge.content.item.pets;

import net.edge.content.TabInterface;
import net.edge.content.dialogue.Expression;
import net.edge.content.item.FoodConsumable;
import net.edge.content.skill.summoning.familiar.Familiar;
import net.edge.net.packet.out.SendForceTab;
import net.edge.net.packet.out.SendInterfaceAnimation;
import net.edge.net.packet.out.SendInterfaceNpcModel;
import net.edge.util.TextUtils;
import net.edge.world.Animation;
import net.edge.world.World;
import net.edge.world.entity.actor.mob.Follower;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.locale.Position;

import java.util.Optional;

/**
 * The class which represents a single pet.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Pet extends Follower {
	
	/**
	 * The progress that this pet has made.
	 */
	private final PetProgress progress;
	
	/**
	 * The task running for this pet.
	 */
	private PetTask task;
	
	/**
	 * Constructs a new {@link Pet}.
	 * @param data     the data to construct this pet from.
	 * @param position the position this pet should be spawned at.
	 */
	public Pet(PetData data, Position position) {
		super(data.getPolicy().getNpcId(), position);
		this.progress = new PetProgress(data);
	}
	
	/**
	 * Constructs a new {@link Pet}.
	 * @param progress {@link #progress}.
	 * @param position the position this pet should be spawned at.
	 */
	public Pet(PetProgress progress, Position position) {
		super(progress.getData().getPolicy().getNpcId(), position);
		this.progress = progress;
	}
	
	@Override
	public boolean isPet() {
		return true;
	}
	
	/**
	 * Attempts to feed the pet the player has spawned.
	 * @param player the player feeding the pet.
	 * @param mob    the mob representing the pet.
	 * @param food   the item representing the food.
	 * @return {@code true} if the pet was fed, {@code false} if not.
	 */
	public static boolean feed(Player player, Mob mob, Item food) {
		PetData data = PetData.getNpc(mob.getId()).orElse(null);
		
		if(data == null) {
			return false;
		}
		
		Pet pet = player.getPetManager().getPet().orElse(null);
		
		if(pet == null) {
			player.message("This is not your pet.");
			return false;
		}
		
		if(pet.getProgress().getHunger() < 15) {
			player.message("Your pet is not hungry at the moment...");
			return false;
		}
		
		FoodConsumable consumable = FoodConsumable.forId(food.getId()).orElse(null);
		boolean possible = data.getType().eat(consumable);
		
		if(!possible) {
			player.message("You can't feed " + TextUtils.appendIndefiniteArticle(food.getDefinition().getName()) + " to your " + pet.progress.getData().getType().toString() + ".");
			return false;
		}
		
		player.facePosition(pet.getPosition());
		player.animation(new Animation(827));
		player.getInventory().remove(food);
		pet.getProgress().setHunger(pet.getProgress().getHunger() - 15D);
		pet.forceChat("yum!");
		player.message("Your pet happily eats the " + food.getDefinition().getName() + ".");
		player.text(19032, ((int) pet.getProgress().getHunger()) + "%");
		return true;
	}
	
	/**
	 * Attempts to spawn the pet.
	 * @param player the player spawning the pet.
	 * @param item   the item that represents the pet.
	 * @return {@code true} if the pet was spawned, {@code false} otherwise.
	 */
	public static boolean canDrop(Player player, Item item) {
		PetData data = PetData.getItem(item.getId()).orElse(null);
		
		if(data == null) {
			return false;
		}
		
		Optional<Pet> hasPet = player.getPetManager().getPet();
		if(hasPet.isPresent()) {
			player.message("You already have a pet summoned.");
			return true;
		}
		
		Optional<Familiar> hasFamiliar = player.getFamiliar();
		if(hasFamiliar.isPresent()) {
			player.message("You already have a familiar summoned.");
			return true;
		}
		
		
		
		Pet pet = new Pet(data, player.getPosition());
		pet = player.getPetManager().put(pet);
		World.get().getMobs().add(pet);
		pet.getMovementQueue().follow(player);
		setInterface(player, pet);
		pet.forceChat(pet.getProgress().getData().getType().getShout());
		pet.task = new PetTask(player, pet);
		World.get().submit(pet.task);
		player.getInventory().remove(item);
		return true;
	}
	
	/**
	 * Functionality that should occur when a player logs in.
	 * @param player the player logging in.
	 */
	public static void onLogin(Player player) {
		Pet pet = player.getPetManager().getPet().orElse(null);
		
		if(pet == null) {
			return;
		}
		
		pet.setPosition(player.getPosition());
		World.get().getMobs().add(pet);
		pet.getMovementQueue().follow(player);
		setInterface(player, pet);
		pet.forceChat(pet.getProgress().getData().getType().getShout());
		pet.task = new PetTask(player, pet);
		World.get().submit(pet.task);
	}
	
	/**
	 * The functionality that should occur when a player logs out.
	 * @param player the player logging out.
	 */
	public static void onLogout(Player player) {
		Pet pet = player.getPetManager().getPet().orElse(null);
		if(pet == null) {
			return;
		}
		World.get().getMobs().remove(pet);
		pet.task.cancel();
		TabInterface.SUMMONING.sendInterface(player, -1);
		player.out(new SendForceTab(TabInterface.INVENTORY));
		pet.postUpdate();
	}
	
	/**
	 * Attempts to pickup the pet.
	 * @param player the player picking up the pet.
	 * @param mob    the mob that was picked up.
	 * @return {@code true} if the pet was picked up, {@code false} otherwise.
	 */
	public static boolean pickup(Player player, Mob mob) {
		PetData data = PetData.getNpc(mob.getId()).orElse(null);
		if(data == null) {
			return false;
		}
		
		Pet pet = player.getPetManager().getPet().orElse(null);
		
		if(pet == null) {
			player.message("This is not your pet.");
			return false;
		}
		if(pet.getSlot() != mob.getSlot()) {
			player.message("This is not your pet.");
			return false;
		}
		if(!player.getInventory().canAdd(data.getPolicy().getItem())) {
			player.message("You don't have enough space in your inventory!");
			return false;
		}
		
		World.get().getMobs().remove(pet);
		player.getInventory().add(data.getPolicy().getItem());
		player.getPetManager().reset();
		TabInterface.SUMMONING.sendInterface(player, -1);
		player.message("You picked up your pet.");
		pet.task.cancel();
		return true;
	}
	
	/**
	 * Attempts to set the interface for the specified player.
	 * @param player the player to set the interface for.
	 * @param pet    the pet that was spawned.
	 */
	private static void setInterface(Player player, Pet pet) {
		player.text(19021, pet.getDefinition().getName());
		player.text(19030, (int) pet.progress.getGrowth() + "%");
		if(pet.getProgress().getData().getPolicy().isLast())
			player.text(19032, "-");
		else
			player.text(19032, (int) pet.progress.getHunger() + "%");
		player.out(new SendInterfaceNpcModel(19019, pet.getId()));
		player.out(new SendInterfaceAnimation(19019, Expression.CALM.getExpression()));
		TabInterface.SUMMONING.sendInterface(player, 19017);
	}
	
	/**
	 * @return the progress.
	 */
	public PetProgress getProgress() {
		return progress;
	}
	
	/**
	 * @return the task
	 */
	public PetTask getTask() {
		return task;
	}
	
	/**
	 * @param task the task to set
	 */
	public void setTask(PetTask task) {
		this.task = task;
	}
}
