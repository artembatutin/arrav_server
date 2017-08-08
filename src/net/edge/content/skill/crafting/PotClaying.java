package net.edge.content.skill.crafting;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.action.impl.ItemOnObjectAction;
import net.edge.action.impl.ObjectAction;
import net.edge.net.packet.out.SendEnterAmount;
import net.edge.net.packet.out.SendItemModelInterface;
import net.edge.task.Task;
import net.edge.util.TextUtils;
import net.edge.content.skill.SkillData;
import net.edge.content.skill.action.impl.ProducingSkillAction;
import net.edge.world.Animation;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.object.GameObject;

import java.util.EnumSet;
import java.util.Optional;

import static net.edge.content.achievements.Achievement.POTTERY;

/**
 * Holds functionality for transforming clay into different kind of pots.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class PotClaying extends ProducingSkillAction {
	
	/**
	 * The pot data for this pot claying skill action.
	 */
	private final PotClayingData pot;
	
	/**
	 * Determines if we're firing the pots.
	 */
	private final boolean fired;
	
	/**
	 * The amount of times this task has to run.
	 */
	private int counter;
	
	/**
	 * Constructs a new {@link PotClaying}.
	 * @param player {@link #getPlayer()}.
	 * @param pot    {@link #pot}.
	 * @param amount {@link #counter}.
	 * @param fired  {@link #fired}.
	 */
	public PotClaying(Player player, PotClayingData pot, int amount, boolean fired) {
		super(player, Optional.empty());
		this.pot = pot;
		this.counter = amount;
		this.fired = fired;
	}
	
	/**
	 * Represents the clay item.
	 */
	private static final Item CLAY = new Item(1761);
	
	/**
	 * Attempts to register a certain amount of pots.
	 * @param player   the player to register this for.
	 * @param buttonId the button the player clicked.
	 * @return {@code true} if the skill action was started, {@code false} otherwise.
	 */
	public static boolean create(Player player, int buttonId) {
		PotClayingData data = PotClayingData.getDefinition(buttonId).orElse(null);
		
		if(data == null || !player.getAttr().get("crafting_pots").getBoolean()) {
			return false;
		}
		
		if(data.amount == -1) {
			player.out(new SendEnterAmount("How many you would like to register?", s -> () -> PotClaying.create(player, data, Integer.parseInt(s), player.getAttr().get("crafting_potfired").getBoolean())));
			return true;
		}
		create(player, data, data.amount, player.getAttr().get("crafting_potfired").getBoolean());
		return true;
	}
	
	/**
	 * Creates the item the player was pot claying for.
	 * @param player the player to register this for.
	 * @param pot    the pot to register.
	 * @param amount the amount to register.
	 * @param fired  determines if we're firing the pots.
	 * @return <true> if the skill action was started, <false> otherwise.
	 */
	public static void create(Player player, PotClayingData pot, int amount, boolean fired) {
		PotClaying crafting = new PotClaying(player, pot, amount, fired);
		crafting.start();
	}
	
	public static void action() {
		ObjectAction click = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.text(8879, "What would you like to make?");
				player.out(new SendItemModelInterface(8941, 105, 1931));
				player.out(new SendItemModelInterface(8942, 120, 2313));
				player.out(new SendItemModelInterface(8943, 100, 1923));
				player.out(new SendItemModelInterface(8944, 100, 5350));
				player.out(new SendItemModelInterface(8945, 150, 4440));
				player.text(8949, "\\n\\n\\n\\nPot");
				player.text(8953, "\\n\\n\\n\\nPie Dish");
				player.text(8957, "\\n\\n\\n\\nBowl");
				player.text(8961, "\\n\\n\\n\\nPlant pot");
				player.text(8965, "\\n\\n\\n\\nPot lid");
				player.getAttr().get("crafting_potfired").set(true);
				player.getAttr().get("crafting_pots").set(true);
				player.chatWidget(8938);
				return true;
			}
		};
		click.registerFirst(2643);
		ItemOnObjectAction a = new ItemOnObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, Item item, int container, int slot) {
				player.text(8879, "What would you like to make?");
				player.out(new SendItemModelInterface(8941, 105, 1787));
				player.out(new SendItemModelInterface(8942, 120, 1789));
				player.out(new SendItemModelInterface(8943, 100, 1791));
				player.out(new SendItemModelInterface(8944, 100, 5352));
				player.out(new SendItemModelInterface(8945, 150, 4438));
				player.text(8949, "\\n\\n\\n\\nPot");
				player.text(8953, "\\n\\n\\n\\nPie Dish");
				player.text(8957, "\\n\\n\\n\\nBowl");
				player.text(8961, "\\n\\n\\n\\nPlant pot");
				player.text(8965, "\\n\\n\\n\\nPot lid");
				player.getAttr().get("crafting_potfired").set(false);
				player.getAttr().get("crafting_pots").set(true);
				player.chatWidget(8938);
				return true;
			}
		};
		a.registerObj(2642);
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{fired ? pot.unfired.item : CLAY});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{fired ? pot.fired.item : pot.unfired.item});
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			counter--;
			if(counter <= 0)
				t.cancel();
			POTTERY.inc(player);
		}
	}
	
	@Override
	public Optional<Animation> animation() {
		return Optional.of(new Animation(fired ? 899 : 896));
	}
	
	@Override
	public int delay() {
		return fired ? 5 : 4;
	}
	
	@Override
	public boolean instant() {
		return true;
	}
	
	@Override
	public boolean init() {
		player.closeWidget();
		return checkCrafting();
	}
	
	@Override
	public boolean canExecute() {
		return checkCrafting();
	}
	
	@Override
	public double experience() {
		return fired ? pot.fired.experience : pot.unfired.experience;
	}
	
	@Override
	public SkillData skill() {
		return SkillData.CRAFTING;
	}
	
	@Override
	public void onStop() {
		player.getAttr().get("crafting_pots").set(false);
		player.getAttr().get("crafting_potfired").set(false);
	}
	
	/**
	 * Checks if the skill action can be started.
	 * @return <true> if it can, <false> otherwise.
	 */
	private boolean checkCrafting() {
		if(!player.getSkills()[skill().getId()].reqLevel(pot.requirement)) {
			String name = fired ? pot.fired.item.getDefinition().getName() : pot.unfired.item.getDefinition().getName();
			player.message("You need a crafting level of " + pot.requirement + " to register " + TextUtils.appendIndefiniteArticle(name));
			return false;
		}
		return true;
	}
	
	/**
	 * The enumerated type whose elements represent a set of constants used to define
	 * the data of the various pots that can be created.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public enum PotClayingData {
		POT(34245, new PotPolicy(1787, 6.3), new PotPolicy(1931, 6.3), 1, 1),
		POT5(34244, POT, 5),
		POT10(34243, POT, 10),
		POTX(34242, POT, -1),
		
		PIE_DISH(34249, new PotPolicy(1789, 11.5), new PotPolicy(2313, 8.5), 7, 1),
		PIE_DISH5(34248, PIE_DISH, 5),
		PIE_DISH10(34247, PIE_DISH, 10),
		PIE_DISHX(34246, PIE_DISH, -1),
		
		BOWL(34253, new PotPolicy(1791, 18), new PotPolicy(1923, 15), 8, 1),
		BOWL5(34252, BOWL, 5),
		BOWL10(34251, BOWL, 10),
		BOWLX(34250, BOWL, -1),
		
		PLANT_POT(35001, new PotPolicy(5352, 20), new PotPolicy(5350, 17.5), 19, 1),
		PLANT_POT5(35000, PLANT_POT, 5),
		PLANT_POT10(34255, PLANT_POT, 10),
		PLANT_POTX(34254, PLANT_POT, -1),
		
		POT_LID(35005, new PotPolicy(4438, 20), new PotPolicy(4440, 20), 25, 1),
		POT_LID5(35004, POT_LID, 5),
		POT_LID10(35003, POT_LID, 10),
		POT_LIDX(35002, POT_LID, -1);
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<PotClayingData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(PotClayingData.class));
		
		/**
		 * The button identification.
		 */
		private final int buttonId;
		
		/**
		 * The item for the unfired pot.
		 */
		private final PotPolicy unfired;
		
		/**
		 * The item for the fired pot.
		 */
		private final PotPolicy fired;
		
		/**
		 * The requirement to register this pot.
		 */
		private final int requirement;
		
		/**
		 * The amount of pots to register.
		 */
		private final int amount;
		
		/**
		 * Constructs a new {@link PotClayingData}.
		 * @param buttonId          {@link #buttonId}.
		 * @param unfired           {@link #unfired}.
		 * @param fired             {@link #fired}.
		 * @param requirement       {@link #requirement}.
		 * @param experienceUnfired {@link #experienceUnfired}.
		 * @param experienceFired   {@link #experienceFired}.
		 * @param amount            {@link #amount}.
		 */
		PotClayingData(int buttonId, PotPolicy unfired, PotPolicy fired, int requirement, int amount) {
			this.buttonId = buttonId;
			this.unfired = unfired;
			this.fired = fired;
			this.requirement = requirement;
			this.amount = amount;
		}
		
		/**
		 * Constructs a new {@link PotClayingData}.
		 * @param buttonId {@link #buttonId}.
		 * @param data     the data to register the {@link PotClayingData} from.
		 * @param amount   {@link #amount}.
		 */
		PotClayingData(int buttonId, PotClayingData data, int amount) {
			this.buttonId = buttonId;
			this.unfired = data.unfired;
			this.fired = data.fired;
			this.requirement = data.requirement;
			this.amount = amount;
		}
		
		public static Optional<PotClayingData> getDefinition(int identifier) {
			return VALUES.stream().filter(s -> s.buttonId == identifier).findAny();
		}
	}
	
	/**
	 * A simple policy which defines a contract each pot has to follow.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private static final class PotPolicy {
		
		/**
		 * The item which represents this pot.
		 */
		private final Item item;
		
		/**
		 * The experience gained for this pot.
		 */
		private final double experience;
		
		/**
		 * Constructs a new {@link PotPolicy}.
		 * @param item       {@link #item}.
		 * @param experience {@link #experience}.
		 */
		public PotPolicy(int item, double experience) {
			this.item = new Item(item);
			this.experience = experience;
		}
	}
}
