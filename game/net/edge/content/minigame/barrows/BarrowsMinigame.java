package net.edge.content.minigame.barrows;

import net.edge.util.rand.Chance;
import net.edge.util.rand.RandomUtils;
import net.edge.world.World;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.dialogue.impl.StatementDialogue;
import net.edge.content.item.FoodConsumable;
import net.edge.content.item.PotionConsumable;
import net.edge.content.minigame.Minigame;
import net.edge.content.teleport.impl.DefaultTeleportSpell;
import net.edge.locale.Position;
import net.edge.world.node.NodeState;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.npc.drop.NpcDrop;
import net.edge.world.node.entity.npc.drop.NpcDropManager;
import net.edge.world.node.entity.npc.drop.NpcDropTable;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.ItemNode;
import net.edge.world.object.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static net.edge.util.rand.Chance.VERY_UNCOMMON;

/**
 * Holds functionality for the barrows minigame.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class BarrowsMinigame extends Minigame {
	
	/**
	 * The possible barrows drops.
	 */
	private static final NpcDrop[] DROPS = {
			new NpcDrop(4708, 1, 1, VERY_UNCOMMON),
			new NpcDrop(4710, 1, 1, VERY_UNCOMMON),
			new NpcDrop(4712, 1, 1, VERY_UNCOMMON),
			new NpcDrop(4714, 1, 1, VERY_UNCOMMON),
			new NpcDrop(4716, 1, 1, VERY_UNCOMMON),
			new NpcDrop(4718, 1, 1, VERY_UNCOMMON),
			new NpcDrop(4720, 1, 1, VERY_UNCOMMON),
			new NpcDrop(4722, 1, 1, VERY_UNCOMMON),
			new NpcDrop(4724, 1, 1, VERY_UNCOMMON),
			new NpcDrop(4726, 1, 1, VERY_UNCOMMON),
			new NpcDrop(4728, 1, 1, VERY_UNCOMMON),
			new NpcDrop(4730, 1, 1, VERY_UNCOMMON),
			new NpcDrop(4732, 1, 1, VERY_UNCOMMON),
			new NpcDrop(4734, 1, 1, VERY_UNCOMMON),
			new NpcDrop(4736, 1, 1, VERY_UNCOMMON),
			new NpcDrop(4738, 1, 1, VERY_UNCOMMON),
			new NpcDrop(4740, 1, 120, VERY_UNCOMMON),
			new NpcDrop(4745, 1, 1, VERY_UNCOMMON),
			new NpcDrop(4747, 1, 1, VERY_UNCOMMON),
			new NpcDrop(4749, 1, 1, VERY_UNCOMMON),
			new NpcDrop(4751, 1, 1, VERY_UNCOMMON),
			new NpcDrop(4753, 1, 1, VERY_UNCOMMON),
			new NpcDrop(4755, 1, 1, VERY_UNCOMMON),
			new NpcDrop(4757, 1, 1, VERY_UNCOMMON),
			new NpcDrop(4759, 1, 1, VERY_UNCOMMON)
	};
	
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
	private void sarcophagusInteraction(Player player, ObjectNode object) {
		BarrowsData data = BarrowsData.VALUES.stream().filter(b -> b.getSarcophagusId() == object.getId()).findAny().orElse(null);
		
		if(data == null)
			return;
		
		BarrowBrother current = new BarrowBrother(data, player.getUsername(), data.getSpawn());
		player.getMinigameContainer().getBarrowsContainer().setCurrent(current);
		World.getNpcs().add(current);
		current.forceChat("How dare you disturb our grave!");
		current.getCombatBuilder().attack(player);
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
			player.getMessages().sendCloseWindows();
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
			return Optional.of(new BarrowBrother(data, player.getUsername(), new Position(3552, 9694, 0)));
	}
	
	@Override
	public boolean canTeleport(Player player, Position position) {
		player.getMessages().sendMinimapState(0);
		Optional<BarrowBrother> current = player.getMinigameContainer().getBarrowsContainer().getCurrent();
		if(current.isPresent() && current.get().getState() != NodeState.ACTIVE)
			return true;
		current.ifPresent(World.getNpcs()::remove);
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
	public boolean canPickup(Player player, ItemNode node) {
		return true;
	}
	
	@Override
	public boolean canDrop(Player player, Item item, int slot) {
		return true;
	}
	
	@Override
	public boolean onFirstClickObject(Player player, ObjectNode object) {
		Optional<BarrowBrother> current = player.getMinigameContainer().getBarrowsContainer().getCurrent();
		
		//Exiting.
		BarrowsData stair = BarrowsData.VALUES.stream().filter(d -> d.getStairId() == object.getId()).findAny().orElse(null);
		if(stair != null) {
			current.ifPresent(c -> {
				if(c.getState() == NodeState.ACTIVE)
					World.getNpcs().remove(c);
			});
			player.getMessages().sendMinimapState(0);
			player.move(new Position(stair.getLocation().getX(), stair.getLocation().getY(), stair.getLocation().getZ()));
			return true;
		}
		
		//Summoning a brother
		BarrowsData sarcophagus = BarrowsData.VALUES.stream().filter(d -> d.getSarcophagusId() == object.getId()).findAny().orElse(null);
		if(sarcophagus != null) {
			if(current.isPresent() && current.get().getState() == NodeState.ACTIVE) {
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
			player.message("clicked");
			if(!container.getCurrent().isPresent()) {
				container.setCurrent(getRandom(player));
			}
			if(container.getCurrent().isPresent() && container.getCurrent().get().getState() != NodeState.ACTIVE) {
				if(player.getMinigameContainer().getBarrowsContainer().getKilledBrothers().size() != 5) {
					return false;
				}
				player.getMinigameContainer().getBarrowsContainer().setCurrent(container.getCurrent().get());
				World.getNpcs().add(container.getCurrent().get());
				container.getCurrent().get().forceChat("How dare you steal from us!");
				container.getCurrent().get().getCombatBuilder().attack(player);
				return true;
			}
			if(!container.getCurrent().isPresent()) {
				NpcDropTable table = NpcDropManager.getTables().get(-1);//barrows custom.
				int expected = RandomUtils.inclusive(4, 8);
				List<Item> loot = new ArrayList<>();
				int items = 0;
				player.message("gave item");
				while(items < expected) {
					NpcDrop drop = RandomUtils.random(DROPS);
					if(drop.roll(ThreadLocalRandom.current())) {
						loot.add(new Item(drop.getId(), RandomUtils.inclusive(drop.getMinimum(), drop.getMaximum())));
						items++;
					}
				}
				
				Position position = new Position(BarrowsData.AHRIM.getLocation().getX(), BarrowsData.AHRIM.getLocation().getY(), BarrowsData.AHRIM.getLocation().getZ());
				
				DefaultTeleportSpell teleport = new DefaultTeleportSpell(position, DefaultTeleportSpell.TeleportType.NORMAL);
				
				player.getMessages().sendMinimapState(0);
				
				teleport.attach(() -> player.getInventory().addOrDrop(loot));
				
				DefaultTeleportSpell.teleport(player, teleport);
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
		player.getMessages().sendMinimapState(0);
		Optional<BarrowBrother> current = player.getMinigameContainer().getBarrowsContainer().getCurrent();
		if(current.isPresent() && current.get().getState() != NodeState.ACTIVE)
			return;
		current.ifPresent(World.getNpcs()::remove);
	}
	
	@Override
	public void onKill(Player player, EntityNode victim) {
		Npc npc = victim.toNpc();
		
		Optional<BarrowBrother> current = player.getMinigameContainer().getBarrowsContainer().getCurrent();
		if(current.isPresent() && npc.getId() != current.get().getId()) {
			return;
		}
		player.getMinigameContainer().getBarrowsContainer().setCurrent(Optional.empty());
		player.getMinigameContainer().getBarrowsContainer().getKilledBrothers().add(current.get().getData());
	}
	
	@Override
	public void onLogin(Player player) {
		player.getMessages().sendMinimapState(0);
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
		
		if(current.isPresent() && current.get().getState() != NodeState.ACTIVE) {
			return;
		}
		current.ifPresent(World.getNpcs()::remove);
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
		player.getMessages().sendMinimapState(2);
	}
	
}