package com.rageps.content.skill.cooking;

import com.rageps.content.achievements.Achievement;
import com.rageps.action.impl.ItemOnObjectAction;
import com.rageps.content.skill.SkillData;
import com.rageps.content.skill.action.impl.ProducingSkillAction;
import com.rageps.task.Task;
import com.rageps.world.model.Animation;
import com.rageps.world.model.Graphic;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerAttributes;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.ItemDefinition;
import com.rageps.world.entity.object.GameObject;
import com.rageps.world.entity.region.Region;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public final class Cooking extends ProducingSkillAction {
	
	static final Item BURNT_PIE = new Item(2329);
	private static final double COOKING_BURN_RATE = 50.0;
	private final CookingData data;
	private final GameObject object;
	private final boolean cookStove;
	private int counter;
	private boolean burned;
	private final boolean spell;
	private final ThreadLocalRandom random = ThreadLocalRandom.current();
	
	public Cooking(Player player, GameObject object, CookingData data, boolean cookStove, int counter, boolean spell) {
		super(player, spell ? Optional.empty() : Optional.of(object.getPosition()));
		this.data = data;
		this.object = object;
		this.cookStove = cookStove;
		this.counter = counter;
		this.spell = spell;
	}
	
	public Cooking(Player player, GameObject object, CookingData data, boolean cookStove, int counter) {
		this(player, object, data, cookStove, counter, false);
	}
	
	@Override
	public void onStop() {
		player.getAttributeMap().set(PlayerAttributes.COOKING_DATA, null);

		if(!spell) {
			player.getAttributeMap().set(PlayerAttributes.COOKING_OBJECT, null);
			player.getAttributeMap().set(PlayerAttributes.COOKING_USINGSTOVE, false);
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
			if(!burned)
				Achievement.BON_APETITE.inc(player);
		}
	}
	
	@Override
	public boolean canExecute() {
		if(!checkCooking()) {
			player.closeWidget();
			return false;
		}
		burned = !determineBurn();
		return true;
	}
	
	@Override
	public boolean init() {
		player.closeWidget();
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
		if(player.getSkills()[skill().getId()].getCurrentLevel() >= data.getMasterLevel() || spell) {
			return true;
		}
		double burn_chance = (COOKING_BURN_RATE - (cookStove ? 5.0 : 1.0));
		double cook_level = getPlayer().getSkills()[skill().getId()].getCurrentLevel();
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
		Region reg = object.getRegion();
		if(reg == null)
			return false;
		if(!spell && object.getDefinition().getName().contains("fire") && !reg.getObject(object.getId(), object.getLocalPos()).isPresent()) {
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
	
	public static void action() {
		ItemOnObjectAction a = new ItemOnObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, Item item, int container, int slot) {
				CookingData c = CookingData.forItem(item);
				if(c == null)
					return false;
				player.getAttributeMap().set(PlayerAttributes.COOKING_USINGSTOVE, true);
				player.getAttributeMap().set(PlayerAttributes.COOKING_DATA, c);
				player.getAttributeMap().set(PlayerAttributes.COOKING_OBJECT, object);
				c.openInterface(player);
				return true;
			}
		};
		a.registerObj(114);
		a.registerObj(2728);
		a.registerObj(25730);
		a.registerObj(24283);
		a.registerObj(2732);
	}
}
