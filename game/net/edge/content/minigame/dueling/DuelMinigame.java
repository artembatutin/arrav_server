package net.edge.content.minigame.dueling;

import net.edge.task.LinkedTaskSequence;
import net.edge.util.log.Log;
import net.edge.util.log.impl.DuelLog;
import net.edge.util.rand.RandomUtils;
import net.edge.world.World;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.special.CombatSpecial;
import net.edge.content.container.session.impl.DuelSession;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.item.FoodConsumable;
import net.edge.content.item.PotionConsumable;
import net.edge.content.minigame.Minigame;
import net.edge.content.skill.prayer.Prayer;
import net.edge.locale.Position;
import net.edge.locale.loc.SquareLocation;
import net.edge.world.node.NodeType;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;
import net.edge.world.object.ObjectNode;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
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
	
	/**
	 * The rules for this duel minigame.
	 */
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
	public static final SquareLocation OBSTACLES_ARENA_CHECK = new SquareLocation(3364, 3244, 3389, 3258, 0);
	
	/**
	 * The square location for the default dueling arena.
	 */
	public static final SquareLocation DEFAULT_ARENA_CHECK = new SquareLocation(3332, 3244, 3357, 3258, 0);
	
	/**
	 * The square location for the obstacles dueling arena.
	 */
	public static final SquareLocation OBSTACLES_ARENA = new SquareLocation(3366, 3246, 3386, 3256, 0);
	
	/**
	 * The square location for the default dueling arena.
	 */
	public static final SquareLocation DEFAULT_ARENA = new SquareLocation(3335, 3246, 3355, 3256, 0);
	
	/**
	 * Applies the staked items to the winner and clears the minigame session.
	 * @param loser  the loser who lost the duel.
	 * @param winner the winner who won the duel.
	 * @param logout whether the duel was won through the opponent logging out.
	 */
	public void applyWin(Player loser, Player winner, boolean logout) {
		loser.getMessages().sendContextMenu(2, false, "Challenge");
		loser.move(deathPosition(loser));
		loser.setMinigame(Optional.empty());
		this.restore(loser);
		
		if(!logout) {
			loser.message("You have been defeated by " + winner.getFormatUsername() + ".");
		}
		
		winner.getMessages().sendContextMenu(2, false, "Challenge");
		winner.move(deathPosition(winner));
		winner.message("You have successfully defeated " + loser.getFormatUsername() + " to win the duel.");
		
		this.restore(winner);
		
		if(session.getExchangeSession().get(loser).isEmpty()) {
			winner.setMinigame(Optional.empty());
			return;
		}

		World.getLoggingManager().write(Log.create(new DuelLog(loser, winner, false, session.getExchangeSession().get(loser), session.getExchangeSession().get(winner))));
		World.getLoggingManager().write(Log.create(new DuelLog(winner, loser, true, session.getExchangeSession().get(loser), session.getExchangeSession().get(winner))));
		
		winner.getMessages().sendString(loser.getFormatUsername(), 6840);
		winner.getMessages().sendString(Integer.toString(loser.determineCombatLevel()), 6839);
		
		winner.getMessages().sendItemsOnInterface(6822, session.getExchangeSession().get(loser).toArray());
		
		winner.getMessages().sendInterface(6733);
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
		
		if(rules.contains(DuelingRules.OBSTACLES)) {
			player.move(OBSTACLES_ARENA.random());
			other.move(OBSTACLES_ARENA.random());
			startCountdown();
			return;
		}
		
		if(rules.contains(DuelingRules.NO_MOVEMENT)) {
			player.move(DEFAULT_ARENA_CHECK.random());
			List<Position> pos = World.getTraversalMap().getSurroundedTraversableTiles(player.getPosition(), player.size(), other.size());
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
			player.getMessages().sendContextMenu(2, true, "Attack");
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
	public void onKill(Player player, EntityNode victim) {
		applyWin(victim.toPlayer(), player, false);
	}
	
	@Override
	public void onInterfaceClick(Player player) {
		if(!claim) {
			return;
		}
		claim = false;
		
		List<Item> items = new ArrayList<>();
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
		return claim || OBSTACLES_ARENA_CHECK.inLocation(player.getPosition()) || DEFAULT_ARENA_CHECK.inLocation(player.getPosition());
	}
	
	@Override
	public Position deathPosition(Player player) {
		return new Position(3361 + RandomUtils.inclusive(11), 3264 + RandomUtils.inclusive(3), 0);
	}
	
	@Override
	public boolean canEat(Player player, FoodConsumable food) {
		if(rules.contains(DuelingRules.NO_FOOD)) {
			player.message("Eating food has been disabled during this duel.");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canPot(Player player, PotionConsumable potion) {
		if(rules.contains(DuelingRules.NO_DRINKS)) {
			player.message("Drinking potions have been disabled during this duel.");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canPray(Player player, Prayer prayer) {
		if(rules.contains(DuelingRules.NO_PRAYER)) {
			player.message("Activating prayers have been disabled during this duel.");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean onFirstClickObject(Player player, ObjectNode object) {
		if(object.getId() == 3203) {
			if(rules.contains(DuelingRules.NO_FORFEIT)) {
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
	public boolean canHit(Player player, EntityNode victim, CombatType type) {
		if(victim.getType().equals(NodeType.NPC)) {
			return true;
		}
		if(!session.getOther(player).equals(victim)) {
			player.message("You can't attack this person.");
			return false;
		}
		if(!started) {
			player.message("You can't attack yet...");
			return false;
		}
		if(rules.contains(DuelingRules.WHIP_DDS_ONLY)) {
			if(!type.equals(CombatType.MELEE)) {
				player.message("You can only hit the opponent with a whip or dds.");
				player.getCombatBuilder().reset();
				return false;
			}
			if(!player.getEquipment().containsAny(4151, 1215, 1231, 5680, 5698)) {
				player.message("You can only hit the opponent with a whip or dds.");
				player.getCombatBuilder().reset();
				return false;
			}
		}
		if(rules.contains(DuelingRules.NO_MAGIC)) {
			if(type.equals(CombatType.MAGIC)) {
				player.message("Magical attacks have been disabled during this duel.");
				player.getCombatBuilder().reset();
				return false;
			}
		}
		if(rules.contains(DuelingRules.NO_RANGED)) {
			if(type.equals(CombatType.RANGED)) {
				player.message("Ranged attacks have been disabled during this duel.");
				player.getCombatBuilder().reset();
				return false;
			}
		}
		if(rules.contains(DuelingRules.NO_MELEE)) {
			if(type.equals(CombatType.MELEE)) {
				player.message("Melee attacks have been disabled during this duel.");
				player.getCombatBuilder().reset();
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean canUseSpecialAttacks(Player player, CombatSpecial special) {
		if(rules.contains(DuelingRules.NO_SPECIAL_ATTACKS)) {
			player.message("Special attacks have been disabled during this duel.");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canWalk(Player player) {
		if(rules.contains(DuelingRules.NO_MOVEMENT)) {
			player.message("Walking has been disabled during this duel.");
			return false;
		}
		if(claim) {
			onInterfaceClick(player);
		}
		return true;
	}
}
