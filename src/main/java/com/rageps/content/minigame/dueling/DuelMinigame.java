package com.rageps.content.minigame.dueling;

import com.rageps.net.packet.out.SendContextMenu;
import com.rageps.world.World;
import com.rageps.world.locale.loc.SquareArea;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import com.rageps.content.dialogue.impl.OptionDialogue;
import com.rageps.content.item.FoodConsumable;
import com.rageps.content.item.PotionConsumable;
import com.rageps.content.minigame.Minigame;
import com.rageps.content.skill.prayer.Prayer;
import com.rageps.net.packet.out.SendContainer;
import com.rageps.task.LinkedTaskSequence;
import com.rageps.util.TextUtils;
import com.rageps.util.log.Log;
import com.rageps.util.log.impl.DuelLog;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.entity.EntityType;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.CombatType;
import com.rageps.combat.strategy.player.special.CombatSpecial;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.container.impl.EquipmentType;
import com.rageps.world.entity.item.container.session.impl.DuelSession;
import com.rageps.world.entity.object.GameObject;
import com.rageps.world.entity.region.TraversalMap;
import com.rageps.world.locale.Position;

import java.util.EnumSet;
import java.util.Optional;

/**
 * Holds functionality for the fighting session of the dueling minigame.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class DuelMinigame extends Minigame {
	
	/**
	 * The session to manage the fight for.
	 */
	private final DuelSession session;
	
	private final EnumSet<DuelingRules> rules;
	/**
	 * The flag which determines if the fight started.
	 */
	private boolean started = false;
	
	/**
	 * The flag which determines if the player can claim the items yet.
	 */
	private boolean claim = false;
	
	/**
	 * Constructs a new {@link DuelMinigame}.
	 * @param session {@link #session}.
	 */
	public DuelMinigame(DuelSession session) {
		super("DUEL_ARENA", MinigameSafety.SAFE, MinigameType.NORMAL);
		this.session = session;
		this.rules = session.getRules().clone();
	}
	
	/**
	 * The square location for the obstacles dueling arena.
	 */
	private static final SquareArea OBSTACLES_ARENA_CHECK = new SquareArea(3367, 3246, 3384, 3256, 0);
	
	/**
	 * The square location for the default dueling arena.
	 */
	private static final SquareArea DEFAULT_ARENA_CHECK = new SquareArea(3336, 3246, 3354, 3256, 0);
	
	/**
	 * The square location for the obstacles dueling arena.
	 */
	private static final SquareArea OBSTACLES_ARENA = new SquareArea(3367, 3246, 3384, 3256, 0);
	
	/**
	 * The square location for the default dueling arena.
	 */
	private static final SquareArea DEFAULT_ARENA = new SquareArea(3336, 3246, 3354, 3256, 0);
	
	/**
	 * Applies the staked items to the winner and clears the minigame session.
	 * @param loser the loser who lost the duel.
	 * @param winner the winner who won the duel.
	 * @param logout whether the duel was won through the opponent logging out.
	 */
	private void applyWin(Player loser, Player winner, boolean logout) {
		loser.out(new SendContextMenu(2, false, "Challenge"));
		loser.move(deathPosition(loser));
		loser.setMinigame(Optional.empty());
		this.restore(loser);
		
		if(!logout) {
			loser.message("You have been defeated by " + winner.getFormatUsername() + ".");
		}
		
		winner.out(new SendContextMenu(2, false, "Challenge"));
		winner.move(deathPosition(winner));
		winner.message("You have successfully defeated " + loser.getFormatUsername() + " to win the duel.");
		
		this.restore(winner);
		
		if(session.getExchangeSession().get(loser).isEmpty()) {
			winner.setMinigame(Optional.empty());
			return;
		}
		
		World.getLoggingManager().write(Log.create(new DuelLog(loser, winner, false, session.getExchangeSession().get(loser), session.getExchangeSession().get(winner))));
		World.getLoggingManager().write(Log.create(new DuelLog(winner, loser, true, session.getExchangeSession().get(loser), session.getExchangeSession().get(winner))));
		
		winner.text(6840, loser.getFormatUsername());
		winner.text(6839, Integer.toString(loser.determineCombatLevel()));
		
		winner.out(new SendContainer(6822, session.getExchangeSession().get(loser)));
		winner.widget(6733);
		claim = true;
	}
	
	@Override
	public void onLogin(Player player) {
		//nothing occurs on login.
	}
	
	@Override
	public void onLogout(Player player) {
		if(!claim) {
			applyWin(player, session.getOther(player), true);
			return;
		}
		
		onInterfaceClick(player);
	}
	
	@Override
	public void onEnter(Player player) {
		
		session.getPlayers().forEach(this::restore);
		Player other = session.getOther(player);
		
		rules.forEach(e -> {
			if(e.getSlot() != -1 && player.getInventory().computeFreeIndex() != -1) {
				player.getEquipment().unequip(e.getSlot());
			} /*else {
				player.getEquipment().unequip(e.getSlot(), player.getBank().container(0), true, -1);
				player.message("You do not have enough free space to un-equip the disabled items.");
				player.message("So they were moved to your bank instead.");
			} SHOULD NEVER REACH THIS STATE, CHECK BEFORE THE DUEL IF ITS POSSIBLE */
		});
		
		if(getRules().contains(DuelingRules.OBSTACLES)) {
			player.move(OBSTACLES_ARENA.random());
			other.move(OBSTACLES_ARENA.random());
			startCountdown();
			return;
		}
		
		if(getRules().contains(DuelingRules.NO_MOVEMENT)) {
			player.move(DEFAULT_ARENA_CHECK.random());
			ObjectList<Position> pos = TraversalMap.getSurroundedTraversableTiles(player.getPosition(), player.size(), other.size());
			if(pos.size() > 0) {
				Position p = RandomUtils.random(pos);
				other.move(p);
			}
			startCountdown();
			return;
		}
		
		player.move(DEFAULT_ARENA.random());
		other.move(DEFAULT_ARENA.random());
		
		startCountdown();
	}
	
	/**
	 * Starts the countdown for the dueling timer.
	 */
	private void startCountdown() {
		session.getPlayers().forEach(player -> {
			player.out(new SendContextMenu(2, true, "Attack"));
			LinkedTaskSequence seq = new LinkedTaskSequence();
			seq.connect(2, () -> player.forceChat("3"));
			seq.connect(2, () -> player.forceChat("2"));
			seq.connect(2, () -> player.forceChat("1"));
			seq.connect(2, () -> {
				player.forceChat("FIGHT!");
				this.started = true;
			});
			seq.start();
		});
	}
	
	@Override
	public void onKill(Player player, Actor victim) {
		applyWin(victim.toPlayer(), player, false);
	}
	
	@Override
	public void onInterfaceClick(Player player) {
		if(!claim) {
			return;
		}
		claim = false;
		
		ObjectList<Item> items = new ObjectArrayList<>();
		session.getPlayers().forEach(p -> {
			session.getExchangeSession().get(p).forEach(items::add);
			session.getExchangeSession().get(p).clear();
		});
		player.getInventory().addOrDrop(items);
		player.setMinigame(Optional.empty());
	}
	
	@Override
	public boolean canLogout(Player player) {
		return true;
	}
	
	@Override
	public boolean contains(Player player) {
		return claim || OBSTACLES_ARENA_CHECK.inArea(player.getPosition()) || DEFAULT_ARENA_CHECK.inArea(player.getPosition());
	}
	
	@Override
	public Position deathPosition(Player player) {
		return new Position(3361 + RandomUtils.inclusive(11), 3264 + RandomUtils.inclusive(3), 0);
	}
	
	@Override
	public boolean canEquip(Player player, Item item, EquipmentType type) {
		player.message("type: " + type);
		//private final List<DuelingRules> selected_equipment_rules = getRules().stream().filter(DuelingRules.EQUIPMENT_RULES::contains).collect(Collectors.toList());
		
		boolean canEquip = rules.stream().anyMatch(r -> r.getSlot() != -1 && r.getSlot() != type.getSlot());
		
		if(!canEquip) // Je gebruikt nooit brackets bij if statementS?
			player.message("Wearing " + TextUtils.appendIndefiniteArticle(type.name().toLowerCase()) + " has been disabled during this duel.");
		
		return canEquip;
	}
	
	@Override
	public boolean canTrade(Player player, Player other) {
		return false;
	}
	
	@Override
	public boolean canTeleport(Player player, Position position) {
		return false;
	}
	
	@Override
	public boolean canEat(Player player, FoodConsumable food) {
		if(getRules().contains(DuelingRules.NO_FOOD)) {
			player.message("Eating food has been disabled during this duel.");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canPot(Player player, PotionConsumable potion) {
		if(getRules().contains(DuelingRules.NO_DRINKS)) {
			player.message("Drinking potions have been disabled during this duel.");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canPray(Player player, Prayer prayer) {
		if(getRules().contains(DuelingRules.NO_PRAYER)) {
			player.message("Activating prayers have been disabled during this duel.");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean onFirstClickObject(Player player, GameObject object) {
		if(object.getId() == 3203) {
			if(getRules().contains(DuelingRules.NO_FORFEIT)) {
				player.message("Forfeiting has been disabled during this duel.");
				return false;
			}
			player.getDialogueBuilder().append(new OptionDialogue(t -> {
				if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
					this.applyWin(player, session.getOther(player), false);
				}
			}, "Yes, I want to forfeit.", "Nevermind."));
			return true;
		}
		return false;
	}
	
	@Override
	public boolean canHit(Player player, Actor victim, CombatType type) {
		if(victim.getType().equals(EntityType.NPC)) {
			return true;
		}
		if(!session.getOther(player).same(victim)) {
			player.message("You can't attack this person.");
			player.getCombat().reset(false, true);
			return false;
		}
		if(!started) {
			player.message("You can't attack yet...");
			return false;
		}
		if(getRules().contains(DuelingRules.WHIP_DDS_ONLY)) {
			if(!type.equals(CombatType.MELEE)) {
				player.message("You can only hit the opponent with a whip or dds.");
				player.getCombat().reset(false, true);
				return false;
			}
			if(!player.getEquipment().containsAny(4151, 1215, 1231, 5680, 5698)) {
				player.message("You can only hit the opponent with a whip or dds.");
				player.getCombat().reset(false, true);
				return false;
			}
		}
		if(getRules().contains(DuelingRules.NO_MAGIC)) {
			if(type.equals(CombatType.MAGIC)) {
				player.message("Magical attacks have been disabled during this duel.");
				player.getCombat().reset(false, true);
				return false;
			}
		}
		if(getRules().contains(DuelingRules.NO_RANGED)) {
			if(type.equals(CombatType.RANGED)) {
				player.message("Ranged attacks have been disabled during this duel.");
				player.getCombat().reset(false, true);
				return false;
			}
		}
		if(getRules().contains(DuelingRules.NO_MELEE)) {
			if(type.equals(CombatType.MELEE)) {
				player.message("Melee attacks have been disabled during this duel.");
				player.getCombat().reset(false, true);
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean canUseSpecialAttacks(Player player, CombatSpecial special) {
		if(getRules().contains(DuelingRules.NO_SPECIAL_ATTACKS)) {
			player.message("Special attacks have been disabled during this duel.");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canWalk(Player player) {
		if(getRules().contains(DuelingRules.NO_MOVEMENT)) {
			player.message("Walking has been disabled during this duel.");
			return false;
		}
		if(claim) {
			onInterfaceClick(player);
		}
		return true;
	}
	
	/**
	 * The rules for this duel minigame.
	 */
	public EnumSet<DuelingRules> getRules() {
		return rules;
	}
}
