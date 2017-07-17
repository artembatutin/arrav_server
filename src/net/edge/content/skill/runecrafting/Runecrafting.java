package net.edge.content.skill.runecrafting;

import net.edge.event.impl.ObjectEvent;
import net.edge.task.Task;
import net.edge.world.node.item.container.impl.Inventory;
import net.edge.content.skill.SkillData;
import net.edge.content.skill.action.impl.ProducingSkillAction;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.item.Item;
import net.edge.world.object.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Holds functionality for the runecrafting skill.
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
	 * Constructs a new {@link Runecrafting}
	 * @param player {@link #player}.
	 * @param object the object the {@code player} is interacting with.
	 */
	private Runecrafting(Player player, ObjectNode object, Altar altar) {
		super(player, Optional.of(object.getGlobalPos()));
		this.altar = altar;
	}
	
	public static void event() {
		for(Altar a : Altar.values()) {
			ObjectEvent rc = new ObjectEvent() {
				@Override
				public boolean click(Player player, ObjectNode object, int click) {
					Runecrafting runeCrafting = new Runecrafting(player, object, a);
					runeCrafting.start();
					return true;
				}
			};
			rc.registerFirst(a.getObjectId());
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
		int amount = altar.getRune().getBestMultiplier(player).getMultiply();
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
		return altar.getExperience();
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

}
