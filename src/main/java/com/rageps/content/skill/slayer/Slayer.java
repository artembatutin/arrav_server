package com.rageps.content.skill.slayer;

import com.jsoniter.annotation.JsonProperty;
import com.rageps.content.achievements.Achievement;
import com.rageps.content.dialogue.Dialogue;
import com.rageps.content.dialogue.impl.*;
import com.rageps.content.dialogue.test.DialogueAppender;
import com.rageps.content.market.currency.Currency;
import com.rageps.world.entity.actor.player.PlayerAttributes;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import com.rageps.action.impl.ItemAction;
import com.rageps.action.impl.MobAction;
import com.rageps.content.PlayerPanel;
import com.rageps.content.skill.Skill;
import com.rageps.content.skill.Skills;
import com.rageps.util.TextUtils;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.container.impl.Inventory;

import java.util.Optional;

/**
 * Holds functionality for the Slayer skill.
 * @author <a href="http://www.rune-server.org/members/Stand+Up/">Stan</a>
 */
public final class Slayer {
	
	/**
	 * A map containing all the slayer masters with the possible tasks they can give.
	 */
	public static final ObjectList<SlayerBoss> SLAYER_BOSSES = new ObjectArrayList<>();
	
	/**
	 * A map containing all the slayer masters with the possible tasks they can give.
	 */
	public static final Object2ObjectArrayMap<SlayerMaster, SlayerKeyPolicy[]> SLAYER_KEYS = new Object2ObjectArrayMap<>();
	
	/**
	 * A map which contains each slayer key by the position of the npcs.
	 */
	public static final Object2ObjectArrayMap<String, SlayerLocationPolicy> SLAYER_LOCATIONS = new Object2ObjectArrayMap<>();
	
	/**
	 * A map which contains each slayer key by the level required slayer.
	 */
	public static final Object2ObjectArrayMap<String, Integer> SLAYER_LEVELS = new Object2ObjectArrayMap<>();
	
	/**
	 * The slayer master this player is on.
	 */
	@JsonProperty(value = "master")
	private final SlayerMaster master;
	
	/**
	 * The slayer key policy this player has.
	 */
	@JsonProperty(value = "key")
	private final String key;
	
	/**
	 * The slayer key policy this player has.
	 */
	@JsonProperty(value = "dif")
	private final SlayerDifficulty difficulty;
	
	/**
	 * Rewards for this slayer task.
	 */
	//private final Item[] rewards;
	
	/**
	 * The amount of points received at the end of the task.
	 */
	@JsonProperty(value = "points")
	public final int points;
	
	/**
	 * The amount of times this player has to kill the task.
	 */
	@JsonProperty(value = "kill")
	private int amount;
	
	/**
	 * Constructs a new {@link Slayer}.
	 */
	public Slayer(SlayerMaster master, SlayerKeyPolicy policy) {
		this.master = master;
		this.key = policy.getKey();
		this.amount = RandomUtils.random(policy.getAmount());
		this.difficulty = policy.getDifficulty();
		//this.rewards = policy.getRewards();
		this.points = (int) (amount * 0.4 * ((difficulty.getValue() + 1) * 2.4));
	}
	
	/**
	 * Opens the slayer panel for a particular player.
	 * @param player the player interacting with the panel.
	 */
	public static void openPanel(Player player) {
		player.widget(-10);
		player.text(252, player.getSlayerPoints() + "");
		player.text(253, player.getSlayer().isPresent() ? (TextUtils.capitalize(player.getSlayer().get().getKey().toLowerCase() + " x " + player.getSlayer().get().getAmount())) : "none");
		updateBlocked(player);
	}
	
	public static void actionItem() {
		ItemAction activate = new ItemAction() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				if(container != Inventory.INVENTORY_DISPLAY_ID)
					return true;
				SlayerMaster master = player.getSlayer().isPresent() ? player.getSlayer().get().getMaster() : SlayerMaster.SPRIA;
				player.getDialogueBuilder().append(new NpcDialogue(master.getNpcId(), "Ughh, what do you want?"), new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
						player.getDialogueBuilder().go(3);
					} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
						player.getDialogueBuilder().advance();
					} else {
						player.getDialogueBuilder().last();
					}
				}, "Can I get a new task?", "How many kills are left?", "Nevermind"), new PlayerDialogue("How many kills are left?"), new NpcDialogue(master.getNpcId(), player.getSlayer().isPresent() ? new String[]{"You must kill another " + player.getSlayer().get().amount + " " + player.getSlayer().get().toString() + "."} : new String[]{"You don't have a slayer task, come speak to ", "me or another slayer master in order to get assigned ", "to a task."}).attach(() -> player.closeWidget()), new PlayerDialogue("Can I get a new task?"), new NpcDialogue(master.getNpcId(), player.getSlayer().isPresent() ? new String[]{"You already are assigned to a slayer task..."} : new String[]{"Come speak to me or another slayer master ", "in order to get assigned to a task."}).attach(() -> player.closeWidget()), new PlayerDialogue("Nevermind").attach(() -> player.closeWidget()));
				return true;
			}
		};
		activate.register(4155);
		ItemAction killsLeft = new ItemAction() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				SlayerMaster master = player.getSlayer().isPresent() ? player.getSlayer().get().getMaster() : SlayerMaster.SPRIA;
				if(player.getSlayer().isPresent()) {
					player.getDialogueBuilder().append(new NpcDialogue(master.getNpcId(), "You must kill another " + player.getSlayer().get().amount + " " + player.getSlayer().get().toString() + "."));
				} else {
					player.getDialogueBuilder().append(new NpcDialogue(master.getNpcId(), "You don't have a slayer task, come speak to ", "me or another slayer master in order to get assigned ", "to a task."));
				}
				return true;
			}
		};
		killsLeft.registerEquip(4155);
	}
	
	public static void actionMob() {
		for(SlayerMaster master : SlayerMaster.values()) {
			MobAction e = new MobAction() {
				@Override
				public boolean click(Player player, Mob npc, int click) {
					if(!player.getSkills()[Skills.SLAYER].reqLevel(master.getRequirement())) {
						player.message("You need a slayer level of " + master.getRequirement() + " to access " + TextUtils.capitalize(master.toString().toLowerCase()) + ".");
						return false;
					}
					
					player.getDialogueBuilder().append(new NpcDialogue(master.getNpcId(), "'Ello, and what are you after, then?"), new OptionDialogue(t -> {
						if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
							Optional<Slayer> task = getTask(player, master);
							Dialogue[] dialogues = player.getSlayer().isPresent() ? new Dialogue[]{new PlayerDialogue("I need another assignment."), new NpcDialogue(master.getNpcId(), "You already have a slayer task.", "Speak to me once you have completed it.")} : !task.isPresent() ? new Dialogue[]{new PlayerDialogue("I need another assignment."), new NpcDialogue(master.getNpcId(), "There was no task found, please try again shortly.")} : new Dialogue[]{new PlayerDialogue("I need another assignment."), new NpcDialogue(master.getNpcId(), "Excellent, you're doing great, your new task", "is to kill " + task.get().amount + " " + TextUtils.capitalize(task.get().getKey().toLowerCase() + ".")).attach(() -> player.setSlayer(Optional.of(task.get())))};
							player.getDialogueBuilder().append(dialogues);
						} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
							teleport(player, master);
						} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
							player.getDialogueBuilder().advance();
						} else {
							player.getDialogueBuilder().last();
						}
					}, "I need another assignment.", "I want to teleport to my assignment.", "Can I buy a slayer gem?", "Give me a boss task."), new PlayerDialogue("Can I buy a slayer gem?"), new NpcDialogue(master.getNpcId(), "Yes, it costs 10,000 gp."), new OptionDialogue(t -> {
						if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
							player.getDialogueBuilder().advance();
						} else {
							player.getDialogueBuilder().skip();
						}
					}, "Buy the slayer gem", "Nevermind"), new RequestItemDialogue(new Item(995, 100000), new Item(4155), "You hand over 100,000 coins to buy \\na slayer gem.", Optional.empty()).attach(() -> player.closeWidget()), new PlayerDialogue("Nevermind").attach(() -> player.closeWidget()));
					return true;
				}
			};
			e.registerFirst(master.getNpcId());
		}
	}
	
	/**
	 * Checks if the specified {@code player} can attack the {@code mob}.
	 * @param player the player to check for.
	 * @param mob the mob being attacked.
	 * @return <true> if the player can, <false> otherwise.
	 */
	public static boolean canAttack(Player player, Mob mob) {
		if(!player.getSkills()[Skills.SLAYER].reqLevel(mob.getDefinition().getSlayerRequirement())) {
			player.message("You need a slayer level of " + mob.getDefinition().getSlayerRequirement() + " to slay this creature.");
			return false;
		}
		return true;
	}
	
	/**
	 * Decrements the remaining slayer task by 1 if the specified {@code mob}
	 * was assigned as a task.
	 * @param player the player to decrement this for.
	 * @param mob the mob to check for.
	 * @return <true> if the remaining slayer task was decremented, <false> otherwise.
	 */
	public static boolean decrement(Player player, Mob mob) {
		if(!player.getSlayer().isPresent()) {
			return false;
		}
		
		Slayer slayer = player.getSlayer().get();
		if(mob.getDefinition().getSlayerKey() != null && !slayer.getKey().equals(mob.getDefinition().getSlayerKey())) {
			return false;
		}
		
		slayer.amount--;
		
		if(slayer.amount < 1) {
			player.message("You have completed your slayer task.");
			player.message("To get another slayer task go talk to a slayer master.");
			Skills.experience(player, slayer.getDifficulty().getValue() * (100 + RandomUtils.inclusive(5, 5 + (15 * (slayer.getAmount() / 2)))), Skills.SLAYER);
			Rights right = player.getRights();
			int donatorBonus = right.equals(Rights.EXTREME_DONATOR) ? RandomUtils.inclusive(1, 10) : right.equals(Rights.SUPER_DONATOR) ? RandomUtils.inclusive(1, 5) : right.equals(Rights.DONATOR) ? RandomUtils.inclusive(1, 3) : 0;
			Currency.SLAYER_POINTS.getCurrency().recieveCurrency(player, slayer.points + donatorBonus);
			player.setSlayer(Optional.empty());
			player.getAttributeMap().plus(PlayerAttributes.SLAYER_TASKS, 1);
			PlayerPanel.SLAYER_COUNT.refresh(player, "@or2@ - Completed tasks: @yel@" + player.getAttributeMap().getInt(PlayerAttributes.SLAYER_TASKS));
			Achievement.SLAYER_MASTER.inc(player);
			return true;
		} else {
			PlayerPanel.SLAYER_TASK.refresh(player, "@or2@ - Slayer task: @yel@" + (player.getSlayer().isPresent() ? (player.getSlayer().get().getAmount() + " " + player.getSlayer().get().toString()) : "none"));
		}
		
		String npc_indefinite_article = TextUtils.appendIndefiniteArticle(mob.getDefinition().getName().toLowerCase());
		player.message("You have defeated " + npc_indefinite_article + ", only " + slayer.amount + " more to go.");
		Skills.experience(player, slayer.getDifficulty().getValue() * (50 + RandomUtils.inclusive(1, 25)), Skills.SLAYER);
		return true;
	}
	
	public static boolean clickButton(Player player, int button) {
		SlayerMaster master = player.getSlayer().isPresent() ? player.getSlayer().get().getMaster() : SlayerMaster.SPRIA;
		if(button >= 113 && button <= 117) {
			player.getBlockedTasks()[button - 113] = null;
			updateBlocked(player);
			return true;
		}
		switch(button) {
			case 110://teleport
				teleport(player, master);
				return true;
			case 111:
				skip(player, master);
				return true;
			case 112:
				block(player, master);
				return true;
			default:
				return false;
		}
	}
	
	/**
	 * Blocks a slayer task.
	 * @param player the player doing slayer.
	 * @param master the master associated to the task.
	 */
	public static void block(Player player, SlayerMaster master) {
		if(!player.getSlayer().isPresent()) {
			player.getDialogueBuilder().append(new NpcDialogue(master.getNpcId(), "You don't have a slayer assignment."));
			return;
		}
		int open = getOpen(player);
		if(open != -1 && Currency.SLAYER_POINTS.getCurrency().currencyAmount(player) >= 100) {
			Currency.SLAYER_POINTS.getCurrency().takeCurrency(player, 100);
			player.getBlockedTasks()[open] = player.getSlayer().get().getKey();
			player.setSlayer(Optional.empty());
			player.getDialogueBuilder().append(new NpcDialogue(master.getNpcId(), "I have successfully blocked and cleared this", "assignment for you."));
			updateBlocked(player);
		} else if(open == -1) {
			player.message("You cannot block this slayer task as you have no more space to block it.");
		} else {
			player.message("You do not have enough slayer points to do this.");
		}
	}
	
	/**
	 * Skips a slayer task.
	 * @param player the player doing slayer.
	 * @param master the master associated to the task.
	 */
	private static void skip(Player player, SlayerMaster master) {
		if(!player.getSlayer().isPresent()) {
			player.getDialogueBuilder().append(new NpcDialogue(master.getNpcId(), "You don't have a slayer assignment."));
			return;
		}
		
		if(Currency.SLAYER_POINTS.getCurrency().currencyAmount(player) >= 30) {
			Currency.SLAYER_POINTS.getCurrency().takeCurrency(player, 30);
			player.setSlayer(Optional.empty());
			player.getDialogueBuilder().append(new NpcDialogue(master.getNpcId(), "I have successfully reset your slayer assignment"));
		} else {
			player.message("You do not have enough slayer points to do this.");
		}
	}
	
	public static int getOpen(Player player) {
		for(int i = 0; i < 5; i++) {
			if(player.getBlockedTasks()[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	private static void updateBlocked(Player player) {
		//Blocked tasks.
		for(int i = 0; i < 5; i++) {
			String blocked = player.getBlockedTasks()[i];
			if(blocked == null)
				player.text(254 + i, "empty");
			else
				player.text(254 + i, TextUtils.capitalize(blocked.toLowerCase()));
		}
	}
	
	/**
	 * The teleport to task function.
	 * @param player the player doing slayer.
	 * @param master the master associated to the task.
	 */
	public static void teleport(Player player, SlayerMaster master) {
		DialogueAppender app = new DialogueAppender(player);
		
		app.chain(new PlayerDialogue("I want to teleport to my assignment."));
		
		if(!player.getSlayer().isPresent()) {
			app.chain(new NpcDialogue(master.getNpcId(), "You currently don't have a slayer task."));
			app.start();
			return;
		}
		
		SlayerLocationPolicy location = SLAYER_LOCATIONS.get(player.getSlayer().get().getKey());
		
		if(location == null) {
			app.chain(new NpcDialogue(master.getNpcId(), "This location currently doesn't exist, please report", "it on the forums."));
			app.start();
			return;
		}
		
		Rights right = player.getRights();
		int basePrice = location.getPrice();
		int price = right.equals(Rights.EXTREME_DONATOR) ? ((int) (basePrice * 0.50)) : right.equals(Rights.SUPER_DONATOR) ? ((int) (basePrice * 0.80)) : right.equals(Rights.DONATOR) ? ((int) (basePrice * 0.90)) : basePrice;
		if(location.getPrice() == 0) {
			app.chain(new NpcDialogue(master.getNpcId(), "Alright, teleporting to this task will be free."));
		} else {
			app.chain(new NpcDialogue(master.getNpcId(), "Alright, that will be " + price + " coins."));
		}
		
		app.chain(new OptionDialogue(t -> {
			if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
				app.getBuilder().go(2);
			} else {
				app.getBuilder().advance();
			}
		}, "Yes, teleport me", "No, i'll stay here"));
		
		app.chain(new PlayerDialogue("Nah, i'll stay here").attachAfter(() -> player.closeWidget()));
		
		Dialogue dialogue = location.getPrice() == 0 ? new StatementDialogue("You teleport to your task for free.").attach(() -> player.move(RandomUtils.random(location.getPositions()))) : new RequestItemDialogue(new Item(995, price), "You handed " + price + " coins over to be \\nteleported to your assignment.", Optional.of(() -> player.move(RandomUtils.random(location.getPositions())))).attachAfter(() -> player.closeWidget());
		
		app.chain(dialogue);
		
		app.start();
	}
	
	/**
	 * Decrements the players remaining slayer task by the specified {@code amount}.
	 * @param player the player to decrement this for.
	 * @param amount the amount to decrement.
	 * @return <true> if the remaining slayer task was decremented, <false> otherwise.
	 */
	public static boolean decrement(Player player, int amount) {
		if(!player.getSlayer().isPresent()) {
			return false;
		}
		
		Slayer slayer = player.getSlayer().get();
		
		slayer.amount = slayer.amount - amount < 1 ? 0 : slayer.amount - amount;
		return true;
	}
	
	/**
	 * Gets and assigns a task for the specified {@code player} from the specified
	 * {@code master} with the specified {@code difficulty}.
	 * @param player the player this task is for.
	 * @param master the master this task is from.
	 */
	public static Optional<Slayer> getTask(Player player, SlayerMaster master) {
		Skill skill = player.getSkills()[Skills.SLAYER];
		int combat = player.determineCombatLevel();
		ObjectArrayList<String> blocked = ObjectArrayList.wrap(player.getBlockedTasks());
		ObjectArrayList<SlayerKeyPolicy> tasks = new ObjectArrayList<>();
		
		int count = 0;
		for(SlayerKeyPolicy task : SLAYER_KEYS.get(master)) {
			if(skill.getRealLevel() < SLAYER_LEVELS.getOrDefault(task.getKey(), 99))
				continue;//Player wont be able to do slayer on a higher boss slayer requirement.
			if(blocked.contains(task.getKey()))
				continue;//The player blocked this task.
			if(combat >= task.getCombatRequirement()) {
				tasks.add(count, task);
				count++;
			}
		}
		if(tasks.isEmpty()) {
			return Optional.empty();
		}
		
		SlayerKeyPolicy policy = RandomUtils.random(tasks);
		return Optional.of(new Slayer(master, policy));
	}
	
	/**
	 * @return the master
	 */
	public SlayerMaster getMaster() {
		return master;
	}
	
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * @return the difficulty
	 */
	public SlayerDifficulty getDifficulty() {
		return difficulty;
	}
	
	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}
	
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	@Override
	public String toString() {
		return TextUtils.capitalize(key.toLowerCase());
	}
	
}