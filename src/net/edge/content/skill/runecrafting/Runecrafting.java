package net.edge.content.skill.runecrafting;

import net.edge.action.impl.ItemAction;
import net.edge.action.impl.ObjectAction;
import net.edge.content.skill.SkillData;
import net.edge.content.skill.action.impl.ProducingSkillAction;
import net.edge.content.skill.runecrafting.pouch.Pouch;
import net.edge.content.skill.runecrafting.pouch.PouchType;
import net.edge.task.Task;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.container.impl.Inventory;
import net.edge.world.object.GameObject;

import java.util.*;
import java.util.stream.IntStream;

import static net.edge.content.achievements.Achievement.RUNE_CRAFTER;

/**
 * Holds functionality for the runecrafting skill.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Runecrafting extends ProducingSkillAction {

	/**
	 * The altar this player is producing his runes from.
	 */
	private final Altar altar;

	/**
	 * The amount of essences this player sacrificed.
	 */
	private int count;

	/**
	 * Represents the pure essence item identification.
	 */
	private static final Item PURE_ESSENCE = new Item(7936);

	/**
	 * Represents the rune essence item identification.
	 */
	private static final Item RUNE_ESSENCE = new Item(1436);

	/**
	 * Represents the crafting graphic identification.
	 */
	private static final Graphic RUNECRAFTING_GRAPHIC = new Graphic(186);

	/**
	 * Represents the crafting animation identification.
	 */
	private static final Animation RUNECRAFTING_ANIMATION = new Animation(791);

	/**
	 * Represents the a mapping for the Pouches
	 */
	private static Map<PouchType, Pouch> pouches = new HashMap<>(3);

	/**
	 * Constructs a new {@link Runecrafting}
	 *
	 * @param player {@link #player}.
	 * @param object the object the {@code player} is interacting with.
	 */
	private Runecrafting(Player player, GameObject object, Altar altar) {
		super(player, Optional.of(object.getGlobalPos()));
		this.altar = altar;
	}

	public static void action() {
		for(Altar a : Altar.values()) {
			ObjectAction rc = new ObjectAction() {
				@Override
				public boolean click(Player player, GameObject object, int click) {
					Runecrafting runeCrafting = new Runecrafting(player, object, a);
					runeCrafting.start();
					return true;
				}
			};
			rc.registerFirst(a.getObjectId());
			ItemAction action = new ItemAction() {
				@Override
				public boolean click(Player player, Item item, int container, int slot, int click) {
					//					player.teleport(a.getPosition(), TRAINING_PORTAL); // TODO: add teleporting
					return true;
				}
			};
			action.register(a.getTalisman());
		}
	}

	@Override
	public Optional<String> message() {
		String message;
		if(!altar.isDiverse()) {
			message = "You do not have any pure essence left.";
		} else {
			message = "You do not have any essence left.";
		}
		return Optional.of(message);
	}

	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			getPlayer().animation(RUNECRAFTING_ANIMATION);
			getPlayer().graphic(RUNECRAFTING_GRAPHIC);
			t.cancel();
			RUNE_CRAFTER.inc(player, count);
		}
	}

	@Override
	public Optional<Item[]> removeItem() {
		List<Item> remove = new ArrayList<>();
		Inventory inventory = player.getInventory();

		if(altar.isDiverse() && !inventory.containsAny(PURE_ESSENCE.getId(), RUNE_ESSENCE.getId())) {
			return Optional.of(new Item[]{PURE_ESSENCE, RUNE_ESSENCE});
		} else if(!altar.isDiverse() && !inventory.contains(PURE_ESSENCE)) {
			return Optional.of(new Item[]{PURE_ESSENCE});
		}

		if(altar.isDiverse()) {
			int pure = inventory.computeAmountForId(PURE_ESSENCE.getId());
			int rune = inventory.computeAmountForId(RUNE_ESSENCE.getId());
			if(pure > 0) {
				remove.add(new Item(PURE_ESSENCE.getId(), pure));
			}
			if(rune > 0) {
				remove.add(new Item(RUNE_ESSENCE.getId(), rune));
			}
			count = pure + rune;
		} else {
			int rune = inventory.computeAmountForId(PURE_ESSENCE.getId());
			remove.add(new Item(PURE_ESSENCE.getId(), rune));
			count = rune;
		}
		return Optional.of(remove.toArray(new Item[remove.size()]));
	}

	@Override
	public Optional<Item[]> produceItem() {
		Optional<RunecraftingMultiplier> m = altar.getRune().getBestMultiplier(player);
		int amount = m.map(RunecraftingMultiplier::getMultiply).orElse(1);
		return Optional.of(new Item[]{new Item(altar.getRune().getItem().getId(), count * amount)});
	}

	@Override
	public int delay() {
		return 0;
	}

	@Override
	public boolean instant() {
		return true;
	}

	@Override
	public boolean init() {
		return checkRunecrafting();
	}

	@Override
	public boolean canExecute() {
		return checkRunecrafting();
	}

	@Override
	public double experience() {
		return altar.getExperience() * count;
	}

	@Override
	public SkillData skill() {
		return SkillData.RUNECRAFTING;
	}

	private boolean checkRunecrafting() {
		if(altar == null) {
			getPlayer().message("There is no altar.");
			return false;
		}
		if(!getPlayer().getSkills()[skill().getId()].reqLevel(altar.getRequirement())) {
			getPlayer().message("You need a level of " + altar.getRequirement() + " to use this altar!");
			return false;
		}
		return true;
	}

	/**
	 * gets the essence id the player is using, we check for pure essence first
	 *
	 * @param player The player checking for essence
	 * @return The id of the essence
	 */
	private static int getEssenceId(Player player) {
		if(player.getInventory().contains(PURE_ESSENCE)) {
			return PURE_ESSENCE.getId();
		} else if(player.getInventory().contains(RUNE_ESSENCE)) {
			return RUNE_ESSENCE.getId();
		} else {
			return -1;
		}
	}

	public static void fill(Player player, PouchType type) {

		int amount = player.getInventory().computeAmountForId(getEssenceId(player));

		if(amount <= 0) {
			player.message("You have no essence to store in the pouch.");
			return;
		}

		amount = amount < type.getMaxAmount() ? amount : type.getMaxAmount();

		Optional<Pouch> originalPouch = Optional.ofNullable(pouches.get(type));

		if(originalPouch.isPresent()) {

			if(originalPouch.get().getAmount() >= type.getMaxAmount()) {
				player.message("Your pouch is already full.");
				return;
			}

			amount -= originalPouch.get().getAmount();
		}

		Pouch pouch = new Pouch(getEssenceId(player), originalPouch.isPresent() ? originalPouch.get().getAmount() + amount : amount);

		pouches.put(type, pouch);

		IntStream.range(0, amount).forEach(i -> player.getInventory().remove(new Item(pouch.getId())));

		player.message("You fill the " + amount + " essence.");

	}

	public static void examine(Player player, PouchType type) {

		Optional<Pouch> pouch = Optional.ofNullable(pouches.get(type));

		if(!pouch.isPresent()) {
			player.message("This pouch does not contain any essence.");
			return;
		}

		player.message("This pouch contains " + pouch.get().getAmount() + (pouch.get().getId() == PURE_ESSENCE.getId() ? " pure" : " rune") + " essence.");

	}

	public static void empty(Player player, PouchType type) {

		Optional<Pouch> pouch = Optional.ofNullable(pouches.get(type));

		if(!pouch.isPresent()) {
			player.message("This pouch does not contain any essence.");
			return;
		}

		pouches.remove(type);

		IntStream.range(0, pouch.get().getAmount()).forEach(i -> player.getInventory().add(new Item(pouch.get().getId())));
		player.message("You empty the " + pouch.get().getAmount() + " essence left in the pouch.");

	}

}
