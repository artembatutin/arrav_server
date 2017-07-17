package net.edge.content.skill.firemaking;

import net.edge.task.Task;
import net.edge.content.skill.SkillData;
import net.edge.content.skill.Skills;
import net.edge.content.skill.action.impl.DestructionSkillAction;
import net.edge.world.locale.loc.Location;
import net.edge.world.locale.Position;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Animation;
import net.edge.world.Direction;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.Optional;

/**
 * Represents the process for creating fires.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Firemaking extends DestructionSkillAction {
	
	/**
	 * The definition of the {@link LogType} we're handling.
	 */
	private final LogType log;
	
	/**
	 * The definition of the {@link FireLighter} we're handling.
	 */
	private final FireLighter lighter;
	
	/**
	 * Constructs a new {@link Firemaking} skill action.
	 * @param player     the player we're starting this action for.
	 * @param firstItem  the first item this player used.
	 * @param secondItem the second item the first item was used on.
	 */
	Firemaking(Player player, Item firstItem, Item secondItem) {
		super(player, Optional.empty());
		lighter = FireLighter.getDefinition(firstItem.getId(), secondItem.getId()).orElse(null);
		log = LogType.getDefinition(firstItem.getId(), secondItem.getId()).orElse(null);
	}
	
	public static boolean execute(Player player, Item firstItem, Item secondItem) {
		Firemaking firemaking = new Firemaking(player, firstItem, secondItem);
		
		if(firemaking.log == null || firemaking.lighter == null) {
			return false;
		}
		if(firstItem.getId() == firemaking.lighter.getItem() && secondItem.equals(firemaking.log.getLog()) || firstItem.equals(firemaking.log.getLog()) && secondItem.getId() == firemaking.lighter.getItem()) {
			firemaking.start();
			return true;
		}
		return false;
	}
	
	@Override
	public void onDestruct(Task t, boolean success) {
		if(success) {
			getPlayer().animation(null);
			World.get().submit(new FiremakingTask(this));
			if(lighter != null && lighter != FireLighter.TINDERBOX) {
				player.getInventory().remove(new Item(lighter.getItem(), 1));
			}
			Position p = getPlayer().getPosition();
			if(World.getTraversalMap().isTraversable(p, Direction.WEST, getPlayer().size())) {
				getPlayer().getMovementQueue().walk(Direction.WEST.getX(), Direction.WEST.getY());
			} else if(World.getTraversalMap().isTraversable(p, Direction.EAST, getPlayer().size())) {
				getPlayer().getMovementQueue().walk(Direction.EAST.getX(), Direction.EAST.getY());
			}
			getPlayer().facePosition(p);
		}
		t.cancel();
	}
	
	@Override
	public boolean init() {
		Position p = getPlayer().getPosition();
		if(getPlayer().getRegion().getObjects(p).hasInteractive() || !World.getTraversalMap().isTraversable(p, Direction.WEST, getPlayer().size()) && !World.getTraversalMap().isTraversable(p, Direction.EAST, getPlayer().size()) || Location.isAtHome(getPlayer())) {
			getPlayer().message("You can't start a fire here.");
			return false;
		}
		return true;
	}
	
	@Override
	public Item destructItem() {
		return log.getLog();
	}
	
	@Override
	public int delay() {
		return RandomUtils.inclusive(2, 6);
	}
	
	@Override
	public boolean instant() {
		return false;
	}
	
	@Override
	public boolean canExecute() {
		return checkFiremaking();
	}
	
	@Override
	public double experience() {
		return log.getExperience();
	}
	
	@Override
	public SkillData skill() {
		return SkillData.FIREMAKING;
	}
	
	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(new Animation(733));
	}
	
	private boolean checkFiremaking() {
		if(Location.isAtHome(getPlayer())) {
			getPlayer().message("You cannot light fire here.");
			return false;
		}
		if(!getPlayer().getSkills()[Skills.FIREMAKING].reqLevel(log.getRequirement())) {
			getPlayer().message("You need a firemaking level of " + log.getRequirement() + " to light this log.");
			return false;
		}
		if(!getPlayer().getInventory().contains(lighter.getItem())) {
			getPlayer().message("You must have a lighter in order to light this log!");
			return false;
		}
		return true;
	}
	
	/**
	 * @return {@link #log}
	 */
	LogType getLogType() {
		return log;
	}
	
	/**
	 * @return {@link #lighter}.
	 */
	FireLighter getFireLighter() {
		return lighter;
	}
}
