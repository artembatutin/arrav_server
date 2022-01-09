package com.rageps.content.skill.firemaking;

import com.rageps.content.skill.SkillData;
import com.rageps.content.skill.Skills;
import com.rageps.content.skill.action.impl.DestructionSkillAction;
import com.rageps.task.Task;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.region.Region;
import com.rageps.world.entity.region.TraversalMap;
import com.rageps.world.locale.Position;
import com.rageps.world.model.Animation;
import com.rageps.world.model.Direction;

import java.util.Optional;

/**
 * Represents the process for creating fires.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Firemaking extends DestructionSkillAction {
	
	/**
	 * Determines if this firemaking action is being executed because of a familiar.
	 */
	private final boolean familiar;
	
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
	 * @param player the player we're starting this action for.
	 * @param firstItem the first item this player used.
	 * @param secondItem the second item the first item was used on.
	 */
	Firemaking(Player player, Item firstItem, Item secondItem, boolean familiar) {
		super(player, Optional.empty());
		lighter = familiar ? null : FireLighter.getDefinition(firstItem.getId(), secondItem.getId()).orElse(null);
		log = LogType.getDefinition(firstItem.getId(), secondItem.getId()).orElse(null);
		this.familiar = familiar;
	}
	
	public static boolean execute(Player player, Item firstItem, Item secondItem, boolean familiar) {
		Firemaking firemaking = new Firemaking(player, firstItem, secondItem, familiar);
		
		if(familiar) {
			firemaking.start();
			return true;
		}
		
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
			if(!familiar) {
				World.get().submit(new FiremakingTask(this));
			}
			if(!familiar && lighter != null && lighter != FireLighter.TINDERBOX) {
				player.getInventory().remove(new Item(lighter.getItem(), 1));
			}
			if(!familiar) {
				Position p = getPlayer().getPosition();
				if(TraversalMap.isTraversable(p, Direction.WEST, getPlayer().size())) {
					getPlayer().getMovementQueue().walk(Direction.WEST.getX(), Direction.WEST.getY());
				} else if(TraversalMap.isTraversable(p, Direction.EAST, getPlayer().size())) {
					getPlayer().getMovementQueue().walk(Direction.EAST.getX(), Direction.EAST.getY());
				}
				
				getPlayer().facePosition(p);
			}
		}
		t.cancel();
	}
	
	@Override
	public boolean init() {
		Position p = getPlayer().getPosition();
		Region reg = getPlayer().getRegion();
		if(reg == null)
			return false;
		if(!familiar && reg.getObjects(p).hasInteractive() || !TraversalMap.isTraversable(p, Direction.WEST, getPlayer().size()) && !TraversalMap.isTraversable(p, Direction.EAST, getPlayer().size()) || !getPlayer().getLocation().isFiremakingAllowed()) {
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
		if(!getPlayer().getLocation().isFiremakingAllowed()) {
			getPlayer().message("You cannot light fire here.");
			return false;
		}
		if(!getPlayer().getSkills()[Skills.FIREMAKING].reqLevel(log.getRequirement())) {
			getPlayer().message("You need a firemaking level of " + log.getRequirement() + " to light this log.");
			return false;
		}
		if(!familiar && !getPlayer().getInventory().contains(lighter.getItem())) {
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
