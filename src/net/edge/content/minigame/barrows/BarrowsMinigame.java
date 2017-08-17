package net.edge.content.minigame.barrows;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.dialogue.impl.StatementDialogue;
import net.edge.content.item.FoodConsumable;
import net.edge.content.item.PotionConsumable;
import net.edge.content.minigame.Minigame;
import net.edge.net.packet.out.SendMinimapState;
import net.edge.util.rand.RandomUtils;
import net.edge.world.World;
import net.edge.world.entity.EntityState;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.mob.drop.Drop;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.GroundItem;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.ItemCache;
import net.edge.world.locale.Position;
import net.edge.world.object.GameObject;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static net.edge.content.achievements.Achievement.BARROWS;

/**
 * Holds functionality for the barrows minigame.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class BarrowsMinigame extends Minigame {
	
	/**
	 * The location for the chest.
	 */
	private static final Position CHEST_LOCATION = new Position(3551, 9694, 0);
	
	/**
	 * Constructs a new {@link BarrowsMinigame} minigame.
	 */
	public BarrowsMinigame() {
		super("BARROWS", MinigameSafety.DEFAULT, MinigameType.NORMAL);
	}
	
	/**
	 * Attempts to dig at the specified spot and start the barrows minigame.
	 * @param player the player whom is digging.
	 * @return <true> if the dig was successful, <false> otherwise.
	 */
	public static boolean dig(Player player) {
		Optional<BarrowsData> data = BarrowsData.getDefinitionForLocation(player.getPosition());
		
		if(!data.isPresent())
			return false;
		
		player.move(data.get().getSpawn());
		new BarrowsMinigame().onEnter(player);
		return true;
	}
	
	/**
	 * Handles functionality for when a player interacts with a sarcophagus (grave)
	 * @param player the player to execute the functionality for.
	 */
	private void sarcophagusInteraction(Player player, GameObject object) {
		BarrowsData data = BarrowsData.VALUES.stream().filter(b -> b.getSarcophagusId() == object.getId()).findAny().orElse(null);
		
		if(data == null)
			return;
		
		BarrowBrother current = new BarrowBrother(data, player, data.getSpawn());
		player.getMinigameContainer().getBarrowsContainer().setCurrent(current);
		World.get().getMobs().add(current);
		current.forceChat("How dare you disturb our grave!");
		current.getNewCombat().attack(player);
	}
	
	/**
	 * The dialogue for when a player breaks into a crypt.
	 * @param player the player to handle this dialogue for.
	 */
	private void advanceDialogue(Player player) {
		player.getDialogueBuilder().append(new StatementDialogue("You've broken into a crypt! You've found a hidden tunnel!", "Do you want to enter it?"), new OptionDialogue(t -> {
			if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
				player.move(CHEST_LOCATION);
			}
			player.closeWidget();
		}, "Yeah, I'm fearless!", "No way, that looks scary."));
	}
	
	/**
	 * Gets a random brother to assign as the pointer.
	 * @param player the player to get a random brother for.
	 * @return the barrow brother.
	 */
	private Optional<BarrowBrother> getRandom(Player player) {
		BarrowsData data = BarrowsData.VALUES.stream().filter(def -> !player.getMinigameContainer().getBarrowsContainer().getKilledBrothers().contains(def)).findAny().orElse(null);
		if(data == null)
			return Optional.empty();
		else
			return Optional.of(new BarrowBrother(data, player, new Position(3552, 9694, 0)));
	}
	
	@Override
	public boolean canTeleport(Player player, Position position) {
		player.out(new SendMinimapState(0));
		Optional<BarrowBrother> current = player.getMinigameContainer().getBarrowsContainer().getCurrent();
		if(current.isPresent() && current.get().getState() != EntityState.ACTIVE)
			return true;
		current.ifPresent(World.get().getMobs()::remove);
		return true;
	}
	
	@Override
	public boolean canPot(Player player, PotionConsumable potion) {
		return true;
	}
	
	@Override
	public boolean canEat(Player player, FoodConsumable potion) {
		return true;
	}
	
	@Override
	public boolean canPickup(Player player, GroundItem node) {
		return true;
	}
	
	@Override
	public boolean canDrop(Player player, Item item, int slot) {
		return true;
	}
	
	@Override
	public boolean onFirstClickObject(Player player, GameObject object) {
		Optional<BarrowBrother> current = player.getMinigameContainer().getBarrowsContainer().getCurrent();
		
		//Exiting.
		BarrowsData stair = BarrowsData.VALUES.stream().filter(d -> d.getStairId() == object.getId()).findAny().orElse(null);
		if(stair != null) {
			current.ifPresent(c -> {
				if(c.getState() == EntityState.ACTIVE)
					World.get().getMobs().remove(c);
			});
			player.out(new SendMinimapState(0));
			player.move(new Position(stair.getLocation().getX(), stair.getLocation().getY(), stair.getLocation().getZ()));
			return true;
		}
		
		//Summoning a brother
		BarrowsData sarcophagus = BarrowsData.VALUES.stream().filter(d -> d.getSarcophagusId() == object.getId()).findAny().orElse(null);
		if(sarcophagus != null) {
			if(current.isPresent() && current.get().getState() == EntityState.ACTIVE) {
				player.message("You have already summoned this barrows brother.");
				return false;
			}
			//Last one.
			if(player.getMinigameContainer().getBarrowsContainer().getKilledBrothers().size() >= 5) {
				advanceDialogue(player);
				return true;
			}
			if(player.getMinigameContainer().getBarrowsContainer().getKilledBrothers().contains(sarcophagus)) {
				player.message("You have already killed this barrows brother.");
				return false;
			}
			sarcophagusInteraction(player, object);
			return true;
		}
		
		BarrowsContainer container = player.getMinigameContainer().getBarrowsContainer();
		if(object.getId() == 10284 || object.getId() == 6775) {
			if(!container.getCurrent().isPresent()) {
				container.setCurrent(getRandom(player));
			}
			if(container.getCurrent().isPresent() && container.getCurrent().get().getState() != EntityState.ACTIVE) {
				if(player.getMinigameContainer().getBarrowsContainer().getKilledBrothers().size() != 5) {
					return false;
				}
				player.getMinigameContainer().getBarrowsContainer().setCurrent(container.getCurrent().get());
				World.get().getMobs().add(container.getCurrent().get());
				container.getCurrent().get().forceChat("How dare you steal from us!");
				container.getCurrent().get().getNewCombat().attack(player);
				return true;
			}
			if(!container.getCurrent().isPresent()) {
				BARROWS.inc(player);
				ObjectList<Item> loot = new ObjectArrayList<>();
				int item = RandomUtils.inclusive(3, 7);
				while(item != 0) {
					Drop[] drops = ItemCache.COMMON.get(ItemCache.BARROWS);
					int d = RandomUtils.inclusive(RandomUtils.nextBoolean() ? drops.length - 1 : 6);
					Drop drop = drops[d];
					if(drop.roll(ThreadLocalRandom.current())) {
						loot.add(new Item(drop.getId(), RandomUtils.inclusive(drop.getMinimum(), drop.getMaximum())));
						item--;
					}
				}
				
				Position position = new Position(BarrowsData.AHRIM.getLocation().getX(), BarrowsData.AHRIM.getLocation().getY(), BarrowsData.AHRIM.getLocation().getZ());
				player.out(new SendMinimapState(0));
				if(loot.isEmpty())
					player.message("You open the chest and it's... empty?");
				else {
					player.message("You grabbed one thing from the chest and a magical force teleported you.");
//					teleport.attach(() -> player.getInventory().addOrDrop(loot)); FIXME: teleport attachments
				}
//				DefaultTeleportSpell.teleport(player, teleport); TODO: add telepors
				player.getMinigameContainer().getBarrowsContainer().getKilledBrothers().clear();
				
				return true;
			}
			player.message("You haven't killed the last barrows brother yet.");
			return false;
		}
		return true;
	}
	
	@Override
	public void onDeath(Player player) {
		player.out(new SendMinimapState(0));
		Optional<BarrowBrother> current = player.getMinigameContainer().getBarrowsContainer().getCurrent();
		if(current.isPresent() && current.get().getState() != EntityState.ACTIVE)
			return;
		current.ifPresent(World.get().getMobs()::remove);
	}
	
	@Override
	public void onKill(Player player, Actor victim) {
		Mob mob = victim.toMob();
		
		Optional<BarrowBrother> current = player.getMinigameContainer().getBarrowsContainer().getCurrent();
		if(current.isPresent() && mob.getId() != current.get().getId()) {
			return;
		}
		player.getMinigameContainer().getBarrowsContainer().setCurrent(Optional.empty());
		player.getMinigameContainer().getBarrowsContainer().getKilledBrothers().add(current.get().getData());
	}
	
	@Override
	public void onLogin(Player player) {
		player.out(new SendMinimapState(0));
		Optional<BarrowsData> data = BarrowsData.getDefinitionForCave(player.getPosition());
		
		if(!data.isPresent()) {
			return;
		}
		
		player.move(new Position(data.get().getLocation().getX(), data.get().getLocation().getY()));
	}
	
	@Override
	public void onLogout(Player player) {
		onLogin(player);
		
		Optional<BarrowBrother> current = player.getMinigameContainer().getBarrowsContainer().getCurrent();
		
		if(current.isPresent() && current.get().getState() != EntityState.ACTIVE) {
			return;
		}
		current.ifPresent(World.get().getMobs()::remove);
	}
	
	@Override
	public boolean canLogout(Player player) {
		return true;
	}
	
	@Override
	public boolean contains(Player player) {
		return player.getPosition().within(3524, 9728, 3583, 9671, 3) || player.getPosition().within(3543, 9703, 3560, 9686, 0);
	}
	
	@Override
	public void onEnter(Player player) {
		player.out(new SendMinimapState(2));
	}
	
}