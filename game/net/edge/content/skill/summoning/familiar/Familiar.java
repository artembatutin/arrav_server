package net.edge.content.skill.summoning.familiar;

import net.edge.net.message.OutputMessages;
import net.edge.task.Task;
import net.edge.util.TextUtils;
import net.edge.util.rand.RandomUtils;
import net.edge.world.World;
import net.edge.content.TabInterface;
import net.edge.content.dialogue.Expression;
import net.edge.content.skill.Skills;
import net.edge.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.content.skill.summoning.familiar.passive.impl.PeriodicalAbility;
import net.edge.content.skill.summoning.specials.SummoningData;
import net.edge.locale.Position;
import net.edge.world.Graphic;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.npc.NpcDeath;
import net.edge.world.node.entity.npc.impl.Follower;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.List;
import java.util.Optional;

/**
 * The familiar represents a npc which will follow the players and has various
 * abilities the player can use in order to ease up the game for him.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class Familiar extends Follower {
	
	/**
	 * The data of this summoned familiar.
	 */
	private final SummoningData data;
	
	/**
	 * Life of the current familiar.
	 */
	private int duration;
	
	/**
	 * Constructs a new {@link Familiar}.
	 * @param data the summoning data.
	 */
	public Familiar(SummoningData data) {
		super(data.getNpcId(), new Position(0, 0));
		this.data = data;
		duration = data.getLife();
	}
	
	/**
	 * The spawn task chained to this familiar.
	 */
	private Optional<FamiliarSpawnTask> task;
	
	@Override
	public boolean isFamiliar() {
		return true;
	}
	
	/**
	 * Checks if this player has the requirements to summon this npc.
	 * @param player the player to check for.
	 * @return <true> if the player can summon this familiar, <false> otherwise.
	 */
	public boolean canSummon(Player player) {
		if(!player.getSkills()[Skills.SUMMONING].reqLevel(data.getLevelRequired())) {
			player.message("You need a summoning level of " + data.getLevelRequired() + " to summon this familiar.");
			return false;
		}
		if(player.getSkills()[Skills.SUMMONING].getLevel() < data.getSummonCost()) {
			player.message("You don't have enough summoning points to summon this familiar.");
			return false;
		}
		return true;
	}
	
	/**
	 * Activates the passive abilities which are non combatant linked to this familiar.
	 * @param player the player this ability is for.
	 */
	private void activatePassiveAbilities(Player player) {
		Optional<PassiveAbility> ability = this.getPassiveAbility();
		
		if(!ability.isPresent()) {
			return;
		}
		
		if(ability.get().getPassiveAbilityType().equals(PassiveAbility.PassiveAbilityType.PERIODICAL)) {
			PeriodicalAbility periodical_ability = (PeriodicalAbility) ability.get();
			periodical_ability.onExecute(player);
		}
	}
	
	/**
	 * Attempts to summon this familiar dependant on the {@code login} flag.
	 * @param player the player to summon this familiar for.
	 * @param login  whether we're summoning dependant on login.
	 */
	public void summon(Player player, boolean login) {
		/* Checks if the familiar is a large npc. */
		boolean isLarge = size() > 1;
		List<Position> pos = World.getTraversalMap().getSurroundedTraversableTiles(player.getPosition(), player.size(), size());
		if(!pos.isEmpty()) {
			Position p = RandomUtils.random(pos);
			this.setPosition(p);
		} else {
			player.message("You cannot summon this familiar on this position.");
			return;
		}
		
		/* Caches the familiar instance to this player. */
		player.setFamiliar(Optional.of(this));
		/* Add the npc to the world */
		World.get().getNpcs().add(this);
		/* Make the familiar follow the player */
		this.getMovementQueue().follow(player);
		/* Play the graphic when the familiar is spawned */
		this.graphic(isLarge ? new Graphic(1315) : new Graphic(1314));
		/* activate the familiars passive abilities */
		activatePassiveAbilities(player);
		/* we set the interface */
		setInterface(player);
		if(!login) {
			player.message("You summon " + TextUtils.appendIndefiniteArticle(this.getDefinition().getName()) + ".");
		}
		/* Adding experience for summoning. */
		player.getSkills()[Skills.SUMMONING].increaseExperience(data.getSummonExperience());
		/* Get the specific ability type for this familiar. */
		FamiliarAbility ability = this.getAbilityType();
		/* Activate the familiars ability */
		ability.initialise(player);
		/* We start the familiar duration task */
		task = Optional.of(new FamiliarSpawnTask(player, this));
		World.get().submit(task.get());
	}
	
	/**
	 * Sets the interface and the variables that have to be set on the interface.
	 * @param player the player we're setting this for.
	 */
	public void setInterface(Player player) {
		OutputMessages encoder = player.getMessages();
		/* We send the summoning points left out of the total summoning points this player has */
		encoder.sendString(Integer.toString(player.getSkills()[Skills.SUMMONING].getLevel()) + "/" + Integer.toString(player.getSkills()[Skills.SUMMONING].getRealLevel()), 18045);
		/* We send the special attack points left out of the total amount */
		encoder.sendString("60/60", 18024);
		/* We send the familiars name */
		encoder.sendString(this.getDefinition().getName(), 18028);
		/* Time of our familiar. */
		player.getMessages().sendString(getDuration() + " minutes", 18043);
		/* We set the familiars face */
		encoder.sendNpcModelOnInterface(18021, this.getId());
		encoder.sendInterfaceAnimation(18021, Expression.CALM.getExpression());
		/* We don't know all the face animation for familiars so it's disabled for now TODO */
		//encoder.sendInterfaceAnimation(18021, Expression.DEFAULT.getExpression());
		/* We can't force the tab to be viewed because we have multiple gameframes TODO*/
		//encoder.sendForceTab(id);
		/* Set the side bar */
		TabInterface.SUMMONING.sendInterface(player, 18017);
	}
	
	/**
	 * Attempts to dismiss this familiar dependant on the {@code logout} flag.
	 * @param player the player to dismiss this familiar for.
	 * @param logout whether we're dismissing dependant on log out.
	 */
	public void dismiss(Player player, boolean logout) {
		if(logout) {
			/* Set this current position to a place where nothing exists */
			this.setPosition(new Position(0, 0));
			/* Make this familiar stop facing the character */
			this.faceEntity(null);
			/* Stop making this familiar follow */
			this.setFollowing(false);
			/* Set the follow task to null */
			this.setFollowEntity(null);
			/* Remove this npc from the world */
			World.get().getNpcs().remove(this);
			/* Disable the familiars task. */
			task = Optional.empty();
			return;
		}
		/* Make this familiar stop facing the character */
		this.faceEntity(null);
		/* Stop making this familiar follow */
		this.setFollowing(false);
		/* Set the follow task to null */
		this.setFollowEntity(null);
		/* Remove this npc from the world */
		World.get().submit(new NpcDeath(this));
		/* Check if the familiar can hold items */
		if(this.getAbilityType().isHoldableContainer()) {
			/* Cast to ItemHoldableAbility, this is safe since we checked */
			FamiliarContainer ability = (FamiliarContainer) this.getAbilityType();
			/* We check if this container contains any items */
			if(ability.getContainer().size() > 0) {
				/* We drop all the item this familiar holds. */
				ability.dropAll(this.getPosition());
				/* Tell the player the familiar dropped all it's items */
				player.message("<col=ff0000>Your familiar has dropped all the items it was holding.");
			}
		}
		/* Set the players familiar instance to empty */
		player.setFamiliar(Optional.empty());
		/* Disable the familiars task. */
		task = Optional.empty();
		/* Set the side bar */
		TabInterface.SUMMONING.sendInterface(player, -1);
	}
	
	/**
	 * The method which should be overriden if this familiar has an
	 * ability where an item can be used on it.
	 * @param player the player whom is using an item on the npc.
	 * @param npc    the npc whom's being interacted by a player.
	 * @param item   the item being used on the npc.
	 * @return <true> if theres an action, <false> otherwise.
	 */
	public boolean itemOnNpc(Player player, Npc npc, Item item) {
		return false;
	}
	
	/**
	 * The ability type chained to this familiar.
	 * @return the familiar ability type.
	 */
	public abstract FamiliarAbility getAbilityType();
	
	/**
	 * The passive ability chained to this familiar.
	 * @return the passive ability instance wrapped in an
	 * optional if theres a value present, {@link Optional#empty()} otherwise.
	 */
	public abstract Optional<PassiveAbility> getPassiveAbility();
	
	/**
	 * Checks if this familiar is combatic or not.
	 * @return <true> if this familiar is combatic, <false> otherwise.
	 */
	public abstract boolean isCombatic();
	
	/**
	 * The non passive ability which can be executed when a player
	 * executes an action with his familiar.
	 */
	public void getNonPassiveAbility() {
		//on default nothing is done.
	}
	
	/**
	 * Attempts to interact with this familiar
	 * @param player the player whom is interacting with the familiar.
	 * @param npc    the npc this player is interacting with.
	 * @param id     the action id being interacted with.
	 */
	public abstract void interact(Player player, Npc npc, int id);
	
	/**
	 * @return {@link #data}.
	 */
	public SummoningData getData() {
		return data;
	}
	
	/**
	 * Gets the duration of this familiar in minutes.
	 * @return duration of this familiar.
	 */
	public int getDuration() {
		return duration;
	}
	
	/**
	 * Sets the new {@link #duration}.
	 * @param duration new value to set.
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	/**
	 * Holds functionality for the amount of time this familiar has left
	 * to live.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private static final class FamiliarSpawnTask extends Task {
		
		/**
		 * The player this familiar belongs too.
		 */
		private final Player player;
		
		/**
		 * The familiar this task is for.
		 */
		private final Familiar familiar;
		
		/**
		 * An interval state to remove points each 30 seconds.
		 */
		private boolean interval;
		
		/**
		 * Constructs a new {@link FamiliarSpawnTask}.
		 * @param player   the player this familiar belongs too.
		 * @param familiar the familiar this task is active for.
		 */
		FamiliarSpawnTask(Player player, Familiar familiar) {
			super(50, false);
			this.player = player;
			this.familiar = familiar;
		}
		
		@Override
		public void execute() {
			if(!player.getFamiliar().isPresent()) {
				this.cancel();
				return;
			}
			if(player.getFamiliar().get().isDead()) {
				this.cancel();
				return;
			}
			//TODO: PROPER DECREASING. Why orb lvl isn't same as skill tab's?
			player.getSkills()[Skills.SUMMONING].decreaseLevel(1);
			player.getMessages().sendString(Integer.toString(player.getSkills()[Skills.SUMMONING].getLevel()) + "/" + Integer.toString(player.getSkills()[Skills.SUMMONING].getRealLevel()), 18045);
			Skills.refresh(player, Skills.SUMMONING);
			
			interval = !interval;
			if(interval)
				return;
			familiar.setDuration(familiar.getDuration() - 1);
			player.getMessages().sendString(familiar.getDuration() + " minutes", 18043);
			
			if(familiar.getDuration() < 1 || player.getSkills()[Skills.SUMMONING].getLevel() == 0) {
				familiar.dismiss(player, false);
				player.message("@red@Your familiar has vanished.");
				this.cancel();
			} else if(familiar.getDuration() < 4) {
				player.message("@red@You have " + familiar.getDuration() + " minute before your familiar vanishes.");
			}
		}
		
	}
}
