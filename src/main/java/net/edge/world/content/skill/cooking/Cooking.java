package net.edge.world.content.skill.cooking;

import net.edge.task.Task;
import net.edge.world.content.skill.SkillData;
import net.edge.world.content.skill.action.impl.ProducingSkillAction;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.ItemDefinition;
import net.edge.world.object.ObjectNode;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public final class Cooking extends ProducingSkillAction {
	
	static final Item BURNT_PIE = new Item(2329);
	private static final double COOKING_BURN_RATE = 50.0;
	private final CookingData data;
	private final ObjectNode object;
	private final boolean cookStove;
	private int counter;
	private boolean burned;
	private final boolean spell;
	private final ThreadLocalRandom random = ThreadLocalRandom.current();
	
	public Cooking(Player player, ObjectNode object, CookingData data, boolean cookStove, int counter, boolean spell) {
		super(player, spell ? Optional.empty() : Optional.of(object.getGlobalPos()));
		this.data = data;
		this.object = object;
		this.cookStove = cookStove;
		this.counter = counter;
		this.spell = spell;
	}
	
	public Cooking(Player player, ObjectNode object, CookingData data, boolean cookStove, int counter) {
		this(player, object, data, cookStove, counter, false);
	}
	
	@Override
	public void onStop() {
		player.getAttr().get("cooking_data").set(null);
		
		if(!spell) {
			player.getAttr().get("cooking_object").set(null);
			player.getAttr().get("cooking_usingStove").set(false);
		}
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			player.animation(spell ? new Animation(4413) : !cookStove ? new Animation(897) : new Animation(896));
			if(spell) {
				player.graphic(new Graphic(746, 100));
			}
			if(!spell) {
				player.message((burned ? "Oops! You accidently burn the " : "You cook the ").concat(ItemDefinition.DEFINITIONS[data.getRawId()].getName()));
			}
			counter--;
			if(counter == 0)
				t.cancel();
		}
	}
	
	@Override
	public boolean canExecute() {
		if(!checkCooking()) {
			player.getMessages().sendCloseWindows();
			return false;
		}
		burned = !determineBurn();
		return true;
	}
	
	@Override
	public boolean init() {
		player.getMessages().sendCloseWindows();
		return checkCooking();
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{new Item(data.getRawId())});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{burned ? new Item(data.getBurntId()) : new Item(data.getCookedId())});
	}
	
	@Override
	public double experience() {
		return burned ? 0 : data.getExperience();
	}
	
	@Override
	public int delay() {
		return 4;
	}
	
	@Override
	public boolean instant() {
		return true;
	}
	
	@Override
	public SkillData skill() {
		return SkillData.COOKING;
	}
	
	private boolean determineBurn() {
		if(player.getSkills()[skill().getId()].getLevel() >= data.getMasterLevel() || spell) {
			return false;
		}
		double burn_chance = (COOKING_BURN_RATE - (cookStove ? 5.0 : 1.0));
		double cook_level = getPlayer().getSkills()[skill().getId()].getLevel();
		double lev_needed = data.getLevel();
		double burn_stop = data.getMasterLevel();
		double multi_a = (burn_stop - lev_needed);
		double burn_dec = (burn_chance / multi_a);
		double multi_b = (cook_level - lev_needed);
		burn_chance -= (multi_b * burn_dec);
		double randNum = random.nextDouble() * 100.0;
		return burn_chance <= randNum;
	}
	
	private boolean checkCooking() {
		if(counter == 0)
			return false;
		if(!spell && object.getDefinition().getName().contains("fire") && !object.getRegion().getObject(object.getId(), object.getLocalPos()).isPresent()) {
			return false;//Fire doesn't exist.
		}
		if(!player.getInventory().contains(data.getRawId())) {
			player.message("You don't have any " + data.toString() + " to cook.");
			return false;
		}
		if(!player.getSkills()[skill().getId()].reqLevel(data.getLevel())) {
			player.message("You need a cooking level of " + data.getLevel() + " to cook " + data.toString() + ".");
			return false;
		}
		return true;
	}
}
