package net.edge.content.minigame.dueling;
//package org.edge.world.content.minigame.minigame.duelarena;
//
//import java.util.EnumSet;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.IntStream;
//
//import com.google.common.collect.ImmutableSet;
//import com.google.common.collect.Sets;
//
//import net.ev.game.world.EntityType;
//import net.ev.game.world.entity.player.Player;
//import net.ev.game.world.World;
//import net.ev.game.world.entity.Actor;
//import CombatType;
//import Prayer;
//import net.ev.game.character.player.Privileges.Rights;
//import net.ev.game.character.player.content.FoodConsumable;
//import net.ev.game.character.player.content.PotionConsumable;
//import org.edge.world.content.dialogue.impl.OptionDialogue;
//import org.edge.world.content.dialogue.impl.OptionDialogue.OptionType;
//import Minigame;
//import Barrows;
//import net.ev.game.world.item.Item;
//import org.edge.world.content.container.ItemContainer;
//import org.edge.world.content.container.ItemContainerPolicy;
//import net.ev.game.location.Location;
//import net.ev.game.location.Position;
//import net.ev.game.location.SquareLocation;
//import net.ev.game.world.object.GameObject;
//import org.edge.task.LinkedTaskSequence;
//import net.ev.utility.BitMask;
//import net.ev.utility.RandomGen;
//
///**
// * Holds functionality for the duel arena minigame.
// * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
// */
//public final class DuelArena extends Minigame {
//
//	/**
//	 * The player whom is the controller of this session.
//	 */
//	private final Player player;
//
//	/**
//	 * The rival whom is being dueled.
//	 */
//	private Player rival;
//
//	/**
//	 * The items the player has staked.
//	 */
//	private final ItemContainer container = new ItemContainer(28, ItemContainerPolicy.NORMAL);
//
//	/**
//	 * Holds the values for the rules selected.
//	 */
//	private final BitMask rules = new BitMask();
//
//	/**
//	 * Constructs a new {@link Barrows} minigame.
//	 */
//	public DuelArena(Player player) {
//		super(MinigameSafety.SAFE, MinigameType.NORMAL);
//		this.player = player;
//	}
//
//	/**
//	 * The square location for the obstacles dueling arena.
//	 */
//	public static final SquareLocation OBSTACLES_ARENA = new SquareLocation(3366, 3246, 3386, 3256, 0);
//
//	/**
//	 * The square location for the default dueling arena.
//	 */
//	public static final SquareLocation DEFAULT_ARENA = new SquareLocation(3335, 3246, 3355, 3256, 0);
//
//	/**
//	 * The stage this session is at.
//	 */
//	private DuelStage stage;
//
//	@Override
//	public void onLogin(Player player) {
//		//nothing happens on login.
//	}
//
//	@Override
//	public boolean canWalk(Player player) {
//		if(player.getDuelSession().stage == DuelStage.COUNTDOWN || (player.getDuelSession().stage == DuelStage.FIGHT
//				&& player.getDuelSession().rules.has(DuelingRules.NO_MOVEMENT.value))) {
//			player.message("You can't move during this dueling session!");
//			return false;
//		}
//		if(player.getDuelSession().inOfferSession()) {
//			player.getDuelSession().decline(false);
//		}
//		return true;	
//	}
//
//	@Override
//	public boolean canHit(Player player, Actor victim) {
//		if(player.getDuelSession().stage != DuelStage.FIGHT) {
//			if(victim.isNpc()) {
//				return true;
//			} else {
//				player.getDuelSession().request((Player) victim);
//			}
//			return false;
//		}
//		if((Player) victim != player.getDuelSession().rival) {
//			player.message("You can't attack this person.");
//			return false;
//		}
//		if(player.getCombat().getStrategy() == null) {
//			return true;
//		} else {
//			CombatType type = player.getCombat().getStrategy().getCombatType();
//			if(player.getDuelSession().rules.has(DuelingRules.WHIP_DDS_ONLY.value)) {
//				if(!type.same(CombatType.MELEE)) {
//					player.message("You can only hit the opponent with a whip or dds.");
//					return false;
//				}
//				if(!player.getEquipment().containsAny(4151, 1215, 1231, 5680, 5698)) {
//					player.message("You can only hit the opponent with a whip or dds.");
//					return false;
//				}
//			}
//			if(player.getDuelSession().rules.has(DuelingRules.NO_MAGIC.value)) {
//				if(type.same(CombatType.MAGIC)) {
//					player.message("Magical attacks have been disabled during this duel.");
//					return false;
//				}
//			}
//			if(player.getDuelSession().rules.has(DuelingRules.NO_RANGED.value)) {
//				if(type.same(CombatType.RANGED)) {
//					player.message("Ranged attacks have been disabled during this duel.");
//					return false;
//				}
//			}
//			if(player.getDuelSession().rules.has(DuelingRules.NO_MELEE.value)) {
//				if(type.same(CombatType.MELEE)) {
//					player.message("Melee attacks have been disabled during this duel.");
//					return false;
//				}
//			}
//		}
//		return true;
//	}
//
//	@Override
//	public boolean canEat(Player player, FoodConsumable food) {
//		if(player.getDuelSession().stage != DuelStage.FIGHT) {
//			return true;
//		}
//		if(player.getDuelSession().rules.has(DuelingRules.NO_FOOD.value)) {
//			player.message("Eating food has been disabled during this duel.");
//			return false;
//		}
//		return true;
//	}
//
//	@Override
//	public boolean canPot(Player player, PotionConsumable potion) {
//		if(player.getDuelSession().stage != DuelStage.FIGHT) {
//			return true;
//		}
//		if(player.getDuelSession().rules.has(DuelingRules.NO_DRINKS.value)) {
//			player.message("Drinking potions have been disabled during this duel.");
//			return false;
//		}
//		return true;
//	}
//
//	@Override
//	public boolean canPray(Player player, Prayer prayer) {
//		if(player.getDuelSession().stage != DuelStage.FIGHT) {
//			return true;
//		}
//		if(player.getDuelSession().rules.has(DuelingRules.NO_PRAYER.value)) {
//			player.message("Activating prayers have been disabled during this duel.");
//			return false;
//		}
//		return true;
//	}
//
//	@Override
//	public boolean onFirstClickObject(Player player, GameObject object) {
//		if(object.getId() == 3203) {
//			if(player.getDuelSession().rules.has(DuelingRules.NO_FORFEIT.value)) {
//				player.message("Forfeiting has been disabled during this duel.");
//				return false;
//			}
//			player.getDialogueChain().append(new OptionDialogue(t -> {
//				if(t.same(OptionType.FIRST_OPTION)) {
//					this.applyWin(player, rival, false);
//				}
//			}, "Yes, I want to forfeit.", "Nevermind."));
//			return true;
//		}
//		return false;
//	}
//
//	@Override
//	public Position deathPosition(Player player) {
//		RandomUtils gen = new RandomUtils();
//		return new Position(3361 + gen.inclusive(11), 3264 + gen.inclusive(3), 0);
//	}
//
//	@Override
//	public void onKill(Player player, Actor victim) {
//		applyWin((Player) victim, player, false);
//	}
//
//	@Override
//	public void onLogout(Player player) {
//		if(stage == DuelStage.FIGHT || stage == DuelStage.COUNTDOWN) {
//			if(player == this.player) {
//				applyWin(this.player, this.rival, true);
//			} else if(player == this.rival) {
//				applyWin(this.rival, this.player, true);
//			}
//			return;
//		}
//		decline(true);
//	}
//
//	@Override
//	public void onEnter(Player player) {
//		// no functionality needed.
//	}
//
//	@Override
//	public boolean contains(Player player) {
//		return Location.inDuelArena(player) || inDuelSession();
//	}
//
//	/**
//	 * Executes functionality for both {@code loser} as {@code winner}.
//	 * @param loser		the loser whom lost the duel.
//	 * @param winner	the winner whom won the duel.
//	 * @param logout	the flag whether the player logged out or not.
//	 */
//	public void applyWin(Player loser, Player winner, boolean logout) {
//		loser.getMessages().sendContextMenu(3, "Challenge");
//		loser.move(deathPosition(loser));
//		if(!logout) {
//			loser.message("You have been defeated by " + winner.getFormatUsername() + ".");
//		}
//
//		winner.getMessages().sendContextMenu(3, "Challenge");
//		winner.move(deathPosition(winner));
//		winner.message("You have successfully defeated " + loser.getFormatUsername() + " to win the duel.");
//		winner.getInventory().addAll(winner.getDuelSession().container.container());
//		winner.getInventory().addAll(loser.getDuelSession().container.container());
//		winner.getDuelSession().reset();
//		loser.getDuelSession().reset();
//	}
//
//	@Override
//	public boolean canClickMenu(Player player, Player other) {
//		if(inOfferSession()) {
//			return false;
//		}
//		if(player.getDuelSession().stage == DuelStage.FIGHT) {
//			return true;
//		}
//		player.getDuelSession().request(other);
//		return false;
//	}
//
//	/**
//	 * Attempts to request a dueling session with the {@code other} player.
//	 * @return <true> if the request was successful, <false> otherwise.
//	 */
//	public void request(Player other) {
//		if(inDuelSession()) {
//			player.message("You are already in a dueling session!");
//			return;
//		}
//		if(other.getDuelSession().inDuelSession()) {
//			player.message("They are already in a dueling session!");
//			return;
//		}
//		if(player.same(other)) {
//			player.message("You cannot initiate a duel session with yourself!");
//			return;
//		}
//		if(player.same(other.getDuelSession().rival)) {
//			this.rival = other;
//			other.getDuelSession().rival = player;
//			player.getDuelSession().stage = DuelStage.OFFER;
//			other.getDuelSession().stage = DuelStage.OFFER;
//			execute(DuelStage.OFFER);
//			other.getDuelSession().execute(DuelStage.OFFER);
//			player.text("", 6684);
//			other.text("", 6684);
//			player.text("", 6571);
//			other.text("", 6571);
//			player.text("Whip & dds only", 669);
//			other.text("Whip & dds only", 669);
//			player.getMessages().sendIntState(286, rules.get());
//			other.getMessages().sendIntState(286, rules.get());
//			container.refresh(player, 6669);
//			container.refresh(rival, 6669);
//			container.refresh(other, 6670);
//			container.refresh(player, 6670);
//
//			player.faceEntity(other);
//			other.faceEntity(player);
//			return;
//		}
//		this.rival = other;
//		player.message("Sending duel request...");
//		other.message(player.getFormatUsername() + ":duelreq:");
//		player.faceEntity(other);
//		return;
//	}
//
//	/**
//	 * This method is called directly in the packet.
//	 * @param button	the button clicked.
//	 * @return <true> if the button was clicked, <false> otherwise.
//	 */
//	public boolean clickButton(int button) {
//		if(toggleRules(button)) {
//			return true;
//		}
//		switch(button) {
//		case 25120:
//			if(player.getDuelSession().inDuelSession()) {
//				if(!rival.getDuelSession().inDuelSession()) {
//					System.out.println("Reached");
//					break;
//				}
//				player.getDuelSession().stage = DuelStage.CONFIRMATION_SCREEN;
//				rival.getDuelSession().stage = DuelStage.CONFIRMATION_SCREEN;
//				player.text("Waiting for other player...", 6571);
//				rival.text("Other player has accepted...", 6571);
//				if(player.getDuelSession().stage == DuelStage.CONFIRMATION_SCREEN && rival.getDuelSession().stage == DuelStage.CONFIRMATION_SCREEN) {
//					player.getDuelSession().stage = DuelStage.COUNTDOWN;
//					rival.getDuelSession().stage = DuelStage.COUNTDOWN;
//					rival.getDuelSession().execute(DuelStage.COUNTDOWN);
//					player.getDuelSession().execute(DuelStage.COUNTDOWN);
//					if(this.rules.has(DuelingRules.OBSTACLES.value)) {
//						player.move(OBSTACLES_ARENA.random());
//						rival.move(OBSTACLES_ARENA.random());
//					} else if(this.rules.has(DuelingRules.NO_MOVEMENT.value)) {
//						player.move(DEFAULT_ARENA.random());
//						List<Position> pos = TraversalMap.getSurroundedTraversableTiles(player.getPosition(), player.size(), rival.size());
//						if(pos.size() > 0) {
//							Position p = new RandomGen().random(pos);
//							rival.move(p);
//						}
//					} else {
//						player.move(DEFAULT_ARENA.random());
//						rival.move(DEFAULT_ARENA.random());
//					}
//					this.restore(player);
//					this.restore(rival);
//				}
//			}
//			break;
//		case 26018:
//			if(player.getDuelSession().inDuelSession()) {
//				if(!rival.getDuelSession().inDuelSession())
//					break;
//				if(rival.getInventory().remaining() < player.getDuelSession().container.size()) {
//					player.message(rival.getFormatUsername() + " does not have enough free slots for the staked items.");
//					break;
//				}
//				player.getDuelSession().stage = DuelStage.FIRST_SCREEN;
//				player.text("Waiting for other player...", 6684);
//				rival.text("Other player has accepted", 6684);
//				if(player.getDuelSession().stage == DuelStage.FIRST_SCREEN && rival.getDuelSession().stage == DuelStage.FIRST_SCREEN) {
//					rival.getDuelSession().execute(DuelStage.FIRST_SCREEN);
//					player.getDuelSession().execute(DuelStage.FIRST_SCREEN);
//				}
//			}
//			return true;
//		}
//		return false;
//	}
//
//	/**
//	 * Attempts to toggle the rules at duel arena.
//	 * @param button	the button clicked.
//	 * @return <true> if the rule was toggled, <false> otherwise.
//	 */
//	private boolean toggleRules(int button) {
//		Optional<DuelingRules> has_rule = DuelingRules.getRules(button);
//
//		if(!has_rule.isPresent()) {
//			return false;
//		}
//
//		if(!inDuelSession() || !rival.getDuelSession().inDuelSession()) {
//			return false;
//		}
//
//		DuelingRules rule = has_rule.get();
//
//		if(this.rules.has(rule.value)) {
//			this.rules.unset(rule.value);
//			rival.getDuelSession().rules.unset(rule.value);
//			player.getDuelSession().stage = DuelStage.OFFER;
//			rival.getDuelSession().stage = DuelStage.OFFER;
//			player.text("", 6571);
//			rival.text("", 6571);
//			player.getMessages().sendIntState(286, rules.get());
//			rival.getMessages().sendIntState(286, rules.get());
//			return true;
//		}
//
//		if(!rule.meets(player, this) || !rule.meets(rival, this)) {
//			return false;
//		}
//
//		this.rules.set(rule.value);
//		rival.getDuelSession().rules.set(rule.value);
//		player.getDuelSession().stage = DuelStage.OFFER;
//		rival.getDuelSession().stage = DuelStage.OFFER;
//		player.text("", 6571);
//		rival.text("", 6571);
//		player.getMessages().sendIntState(286, rules.get());
//		rival.getMessages().sendIntState(286, rules.get());
//		return true;
//	}
//
//	/**
//	 * Executes a certain {@code stage} for this dueling session.
//	 * @param stage	the stage to execute.
//	 */
//	public void execute(DuelStage stage) {
//		if(!inDuelSession()) {
//			return;
//		}
//		switch(stage) {
//		case OFFER:
//			int remaining = rival.getInventory().remaining();
//			player.text("Dueling with: " + name(rival) + " (level-" + rival.determineCombatLevel() + ")" + " who has @gre@" + remaining + " free slots", 6671);
//
//			IntStream.range(0, 14).forEach(order -> {
//				if(player.getEquipment().get(order) != null) {
//					player.getMessages().sendItemOnInterfaceSlot(13824, player.getEquipment().get(order), order);
//				}
//			});
//
//			player.getMessages().sendItemsOnInterface(3322, player.getInventory().container());
//			player.getMessages().sendInventoryInterface(6575, 3321);
//			break;
//		case FIRST_SCREEN:
//			List<DuelingRules> collection = DuelingRules.VALUES.asList();
//			player.text("", 8240);
//			player.text("", 8241);
//
//			int interfaceIndex = 8242;
//
//			for(int i = DuelingRules.NO_FORFEIT.ordinal(); i < DuelingRules.NO_SPECIAL_ATTACKS.ordinal(); i++) {
//				if(rules.has(collection.get(i).value)) {
//					player.text(getRuleText(i, false), interfaceIndex);
//					rival.text(getRuleText(i, false), interfaceIndex);
//					interfaceIndex++;
//				}
//			}
//
//			boolean worn = false;
//			for(int i = DuelingRules.HELM.ordinal(); i < DuelingRules.VALUES.size(); i++) {
//				if(rules.has(collection.get(i).value)) {
//					player.text(getRuleText(i - DuelingRules.HELM.ordinal(), true), interfaceIndex);
//					rival.text(getRuleText(i - DuelingRules.HELM.ordinal(), true), interfaceIndex);
//					interfaceIndex++;
//					worn = true;
//				}
//			}
//
//			if(worn) {
//				player.text("Some worn items will be taken off.", 8238);
//				player.text("Existing prayers will be stopped.", 8250);
//				player.text("", 8239);
//			} else {
//				player.text("Existing prayers will be stopped.", 8250);
//				player.text("", 8238);
//				player.text("", 8239);
//			}
//
//			if(interfaceIndex < 8254) {
//				for(int i = interfaceIndex; i < 8254; i++) {
//					if(i != 8250) {
//						player.text("", i);
//					}
//				}
//			}
//
//			StringBuilder playerBuilder = new StringBuilder();
//			StringBuilder rivalBuilder = new StringBuilder();
//			container.forEach(item -> {
//				if(item == null) {
//					return;
//				}
//				playerBuilder.append(item.getDefinition().getName()).append("  (x").append(item.getAmount()).append(")\\n");
//
//			});
//			rival.getDuelSession().container.forEach(item -> {
//				if(item == null) {
//					return;
//				}
//				rivalBuilder.append(item.getDefinition().getName()).append("  (x").append(item.getAmount()).append(")\\n");
//			});
//
//			if(playerBuilder.length() == 0) {
//				playerBuilder.append("Absolutely nothing!");
//			}
//
//			if(rivalBuilder.length() == 0) {
//				rivalBuilder.append("Absolutely nothing!");
//			}
//
//			player.text(rivalBuilder.toString(), 6516);
//			player.text(playerBuilder.toString(), 6517);
//
//
//			player.getMessages().sendItemsOnInterface(3322, player.getInventory().container());
//			player.getMessages().sendInventoryInterface(6412, 3321);
//			break;
//		case CONFIRMATION_SCREEN:
//			//just to make sure both players have confirmed.
//			break;
//		case COUNTDOWN:
//			player.getCombat().reset();
//			player.closeInterface();
//			player.getMessages().sendContextMenu(3, "Attack");
//			LinkedTaskSequence seq = new LinkedTaskSequence();
//			seq.connect(2, () -> player.forceChat("3"));
//			seq.connect(2, () -> player.forceChat("2"));
//			seq.connect(2, () -> player.forceChat("1"));
//			seq.connect(2, () -> {
//				player.forceChat("FIGHT!");
//				player.getDuelSession().stage = DuelStage.FIGHT;
//			});
//			seq.start();
//			break;
//		case FIGHT:
//			//doesn't need functionality.
//			break;
//		}
//	}
//
//	/**
//	 * Gets the text to draw for the line identification.
//	 * @param id			the id to get the text for.
//	 * @param equipment		whether the id is for the equipment text.
//	 * @return the string containing the text to draw.
//	 */
//	private String getRuleText(int id, boolean equipment) {
//		if(!equipment) {
//			switch(id) {
//			case 0: return "You can not forfeit the duel.";
//			case 1: return "You can not move during the duel.";
//			case 2: return "You can not use ranged attacks.";
//			case 3: return "You can not use melee attacks.";
//			case 4: return "You can not use magic attacks.";
//			case 5: return "You can not use drinks.";
//			case 6: return "You can not use food.";
//			case 7: return "You can not use prayer.";
//			case 8: return "There will be obstacles in the arena.";
//			case 9: return "You can only attack with `fun` weapons.";
//			case 10: return "You can not use special attacks.";
//			}
//		} else {
//			switch(id) {
//			case 0: return "You can not wear items on your head.";
//			case 1: return "You can not wear items on your back.";
//			case 2: return "You can not wear items on your neck.";
//			case 3: return "You can not wear weapons.";
//			case 4: return "You can not wear items on your chest.";
//			case 5: return "You can not wear items on your offhand(Includes 2h weapons)";
//			case 6: return "You can not wear items on your legs.";
//			case 7: return "You can not wear items on your hands.";
//			case 8: return "You can not wear items on your feet.";
//			case 9: return "You can not wear items on your fingers.";
//			case 10: return "You can not wear items in your quiver.";
//			}
//		}
//		return "";
//	}
//
//	/**
//	 * Attempts to remove an item from the container.
//	 * @param item	the item to remove.
//	 */
//	public void remove(Item item) {
//		if(!inDuelSession()) {
//			return;
//		}
//		if(!container.contains(item)) {
//			return;
//		}
//		if(item.getAmount() > container.amount(item.getId())) {
//			item.setAmount(container.amount(item.getId()));
//		}
//		if(container.remove(item)) {
//			player.getInventory().add(item);
//			container.shift();
//			String stake = name(player);
//			int remaining = player.getInventory().remaining();
//			rival.text("Dueling with: " + stake + " (level-" + player.determineCombatLevel() + ")" + " who has @gre@" + remaining + " free slots", 6671);
//			player.getMessages().sendItemsOnInterface(3322, player.getInventory().container());
//			int length = container.size();
//			player.getMessages().sendItemsOnInterface(6669, container.container(), length);
//			rival.getMessages().sendItemsOnInterface(6670, container.container(), length);
//			player.getDuelSession().stage = DuelStage.OFFER;
//			rival.getDuelSession().stage = DuelStage.OFFER;
//			player.text("", 6684);
//			rival.text("", 6684);
//		}
//	}
//
//	/**
//	 * Attempts to add an item to the container.
//	 * @param item	the item being added to the container. 
//	 * @param slot	the slot being added from.
//	 */
//	public void add(Item item, int slot) {
//		if(!inDuelSession()) {
//			return;
//		}
//		if(!Item.valid(item) || !player.getInventory().contains(item.getId())) {
//			return;
//		}
//		if(!item.getDefinition().isTradeable()) {
//			player.message("You cannot stake this item!");
//			return;
//		}
//		if(item.getAmount() > player.getInventory().amount(item.getId()) && !item.getDefinition().isStackable()) {
//			item.setAmount(player.getInventory().amount(item.getId()));
//		} else if(item.getAmount() > player.getInventory().get(slot).getAmount() && item.getDefinition().isStackable()) {
//			item.setAmount(player.getInventory().get(slot).getAmount());
//		}
//		if(container.add(item)) {
//			player.getInventory().remove(item, slot);
//			String stake = name(player);
//			int remaining = player.getInventory().remaining();
//			rival.text("Dueling with: " + stake + " (level-" + player.determineCombatLevel() + ")" + " who has @gre@" + remaining + " free slots", 6671);
//			player.getMessages().sendItemsOnInterface(3322, player.getInventory().container());
//			int length = container.size();
//			player.getMessages().sendItemsOnInterface(6669, container.container(), length);
//			rival.getMessages().sendItemsOnInterface(6670, container.container(), length);
//			player.getDuelSession().stage = DuelStage.OFFER;
//			rival.getDuelSession().stage = DuelStage.OFFER;
//			player.text("", 6684);
//			rival.text("", 6684);
//		}
//	}
//
//	/**
//	 * Attempts to decline the duel session for both players.
//	 * @param logout	whether the declination was due to a logout.
//	 */
//	public void decline(boolean logout) {
//		player.getInventory().addAll(container.container());
//		rival.getInventory().addAll(rival.getDuelSession().container.container());
//		if(!logout) {
//			rival.message("The other player has declined the duel!");
//			player.message("You have declined the duel.");
//		}
//
//		rival.getDuelSession().reset();
//		reset();
//	}
//
//	/**
//	 * Resets the dueling session for the controller.
//	 */
//	public void reset() {
//		if(!inDuelSession()) {
//			return;
//		}
//
//		player.closeInterface();
//		player.getAttr().get("dueling").set(false);
//		container.clear();
//		player.faceEntity(null);
//		this.rules.clear();
//		player.getDuelSession().stage = null;
//		rival.getDuelSession().stage = null;
//	}
//
//	/**
//	 * Checks if the player is in a duel session.
//	 * @return <true> if the player is, <false> otherwise.
//	 */
//	public boolean inDuelFight() {
//		DuelStage stage = player.getDuelSession().stage;
//		DuelStage rivalStage = rival.getDuelSession().stage;
//		return stage == DuelStage.FIGHT && rivalStage == DuelStage.FIGHT;
//	}
//
//	/**
//	 * Checks if the player is in a duel session.
//	 * @return <true> if the player is, <false> otherwise.
//	 */
//	public boolean inDuelSession() {
//		DuelStage stage = player.getDuelSession().stage;
//		return stage == DuelStage.OFFER || stage == DuelStage.FIRST_SCREEN || stage == DuelStage.CONFIRMATION_SCREEN || stage == DuelStage.COUNTDOWN || stage == DuelStage.FIGHT;
//	}
//
//	/**
//	 * Checks if the player is in an offer session.
//	 * @return <true> if the player is, <false> otherwise.
//	 */
//	public boolean inOfferSession() {
//		return stage == DuelStage.OFFER || stage == DuelStage.FIRST_SCREEN || stage == DuelStage.CONFIRMATION_SCREEN;
//	}
//
//	/**
//	 * Determines and returns the duel display name for {@code player}.
//	 * @param player the player to determine this display name for.
//	 * @return the duel display name.
//	 */
//	private String name(Player player) {
//		return player.getFormatUsername().concat(player.getPrivileges().getPriorityRight().equal(Rights.MODERATOR) ? "@cr1@" : player.getPrivileges().getPriorityRight().greater(Rights.MODERATOR) ? "@cr2@" : "");
//	}
//
//	/**
//	 * @return the container
//	 */
//	public ItemContainer getContainer() {
//		return container;
//	}
//
//	/**
//	 * The possible stages for a dueling session to be in.
//	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
//	 */
//	private enum DuelStage {
//		OFFER,
//		FIRST_SCREEN,
//		CONFIRMATION_SCREEN,
//		COUNTDOWN,
//		FIGHT;
//	}
//
//	/**
//	 * The enumerated type whose elements represent the dueling rules for a 
//	 * duel session.
//	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
//	 */
//	private enum DuelingRules {
//		NO_FORFEIT(1, 26065) {
//			@Override
//			public boolean meets(Player player, DuelArena duel) {
//				if(duel.rules.has(NO_MOVEMENT.value)) {
//					player.message("You can't toggle no-forfeiting while having no-movement enabled.");
//					return false;
//				}
//				if(duel.rules.has(OBSTACLES.value)) {
//					player.message("You can't toggle no-forfeiting while having obstacles enabled.");
//					return false;
//				}
//				return true;
//			}
//		},
//		NO_MOVEMENT(2, 26066) {
//			@Override
//			public boolean meets(Player player, DuelArena duel) {
//				if(duel.rules.has(NO_FORFEIT.value)) {
//					player.message("You can't toggle no-movement while having no-forfeiting enabled.");
//					return false;
//				}
//				if(duel.rules.has(OBSTACLES.value)) {
//					player.message("You can't toggle no-movement while having obstacles enabled.");
//					return false;
//				}
//				return true;
//			}
//		},
//		NO_RANGED(16, 26069) {
//			@Override
//			public boolean meets(Player player, DuelArena duel) {
//				if(duel.rules.has(NO_MELEE.value) && duel.rules.has(NO_MAGIC.value)) {
//					player.message("You must have atleast one combat type enabled.");
//					return false;
//				}
//				if(duel.rules.has(ARROWS.value)) {
//					player.message("You can't toggle no-ranged while having arrows disabled.");
//					return false;
//				}
//				return true;
//			}
//		},
//		NO_MELEE(32, 26070) {
//			@Override
//			public boolean meets(Player player, DuelArena duel) {
//				if(duel.rules.has(NO_RANGED.value) && duel.rules.has(NO_MAGIC.value)) {
//					player.message("You must have atleast one combat type enabled.");
//					return false;
//				}
//				if(duel.rules.has(WHIP_DDS_ONLY.value)) {
//					player.message("You can't toggle no-melee while having whip & dds only enabled.");
//					return false;
//				}
//				if(duel.rules.has(WEAPON.value)) {
//					player.message("You can't toggle no-melee while having weapons disabled.");
//					return false;
//				}
//				return true;
//			}
//		}, 
//		NO_MAGIC(64, 26071) {
//			@Override
//			public boolean meets(Player player, DuelArena duel) {
//				if(duel.rules.has(NO_MELEE.value) && duel.rules.has(NO_RANGED.value)) {
//					player.message("You must have atleast one combat type enabled.");
//					return false;
//				}
//				return true;
//			}
//		},
//		NO_DRINKS(128, 26072),
//		NO_FOOD(256, 26073),
//		NO_PRAYER(512, 26074),
//		OBSTACLES(1024, 26076) {
//			@Override
//			public boolean meets(Player player, DuelArena duel) {
//				if(duel.rules.has(NO_FORFEIT.value)) {
//					player.message("You can't toggle obstacles while having no-forfeiting enabled.");
//					return false;
//				}
//				if(duel.rules.has(NO_MOVEMENT.value)) {
//					player.message("You can't toggle obstacles while having no-movement enabled.");
//					return false;
//				}
//				return true;
//			}
//		},
//		WHIP_DDS_ONLY(4096, 2158) {
//			@Override
//			public boolean meets(Player player, DuelArena duel) {
//				if(duel.rules.has(WEAPON.value)) {
//					player.message("You can't toggle whip & dds only while having weapons disabled.");
//					return false;
//				}
//				if(duel.rules.has(NO_MELEE.value)) {
//					player.message("You can't toggle whip & dds only while having no melee disabled.");
//					return false;
//				}
//				return true;
//			}
//		},
//		NO_SPECIAL_ATTACKS(8192, 30136),
//
//		HELM(16384, 53245),
//		CAPE(32768, 53246),
//		AMULET(65536, 53247),
//		WEAPON(131072, 53249) {
//			@Override
//			public boolean meets(Player player, DuelArena duel) {
//				if(duel.rules.has(NO_MELEE.value)) {
//					player.message("You can't disable weapons while having no-melee enabled.");
//					return false;
//				}
//				if(duel.rules.has(WHIP_DDS_ONLY.value)) {
//					player.message("You can't disable weapons while having whip & dds only enabled.");
//					return false;
//				}
//				return true;
//			}
//		},
//		TORSO(262144, 53250),
//		SHIELD(524288, 53251),
//		LEGS(2097152, 53252),
//		GLOVES(8388608, 53255),
//		BOOTS(16777216, 53254),
//		RING(67108864, 53253),
//		ARROWS(134217728, 53248) {
//			@Override
//			public boolean meets(Player player, DuelArena duel) {
//				if(duel.rules.has(NO_RANGED.value)) {
//					player.message("You can't disable arrows while having no-ranged enabled.");
//					return false;
//				}
//				return true;
//			}
//		};
//
//		/**
//		 * Caches our enum values.
//		 */
//		private static final ImmutableSet<DuelingRules> VALUES = Sets.immutableEnumSet(EnumSet.allOf(DuelingRules.class));
//
//		/**
//		 * The value to send to the client to enable/disable the frame.
//		 */
//		private final int value;
//
//		/**
//		 * The button identification of this rule.
//		 */
//		private final int buttonId;
//
//		/**
//		 * The rule condition chained to this rule.
//		 */
//		public boolean meets(Player player, DuelArena duel) {
//			return true;
//		}
//
//		/**
//		 * Constructs a new {@link DuelingRules}.
//		 * @param value		{@link #value}.
//		 * @param buttonId	{@link #buttonId}.
//		 */
//		DuelingRules(int value, int buttonId) {
//			this.value = value;
//			this.buttonId = buttonId;
//		}
//
//		/**
//		 * Searches for a match.
//		 * @param buttonId	the button id to check a match for.
//		 * @return a {@link DuelingRules} enumerator wrapped in an optional, {@link Optional#empty()} otherwise.
//		 */
//		protected static Optional<DuelingRules> getRules(int buttonId) {
//			return VALUES.stream().filter(def -> def.buttonId == buttonId).findFirst();
//		}
//	}
//}
