package net.edge.content.shootingstar;

import net.edge.task.Task;
import net.edge.content.skill.SkillData;
import net.edge.content.skill.action.impl.HarvestingSkillAction;
import net.edge.content.skill.mining.PickaxeData;
import net.edge.world.Animation;
import net.edge.world.World;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents the star mining skill action.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class StarMining extends HarvestingSkillAction {
	
	/**
	 * The definition of the {@link PickaxeData} being used.
	 */
	private final PickaxeData pickaxe;
	
	/**
	 * The definition of the {@link ShootingStar} being mined.
	 */
	private final ShootingStar star;
	
	/**
	 * The random generator which will generate random values.
	 */
	private final ThreadLocalRandom random = ThreadLocalRandom.current();
	
	/**
	 * Represents the item id for stardust.
	 */
	public static final Item STARDUST = new Item(13727);
	
	/**
	 * An array holding all the possible gems which can be obtained while mining.
	 */
	private static final Item[] GEMS = Item.convert(1623, 1621, 1603, 1617);
	
	/**
	 * An array holding all the possible glory's a player can wield in order to mine for gems.
	 */
	private static final Item[] GLORY = Item.convert(1704, 1706, 1708, 1710, 1712);
	
	/**
	 * Constructs a new {@link StarMining}.
	 * @param player {@link #player}.
	 * @param star   the star rock.
	 */
	public StarMining(Player player, ShootingStar star) {
		super(player, Optional.of(star.getGlobalPos()));
		this.star = star;
		this.pickaxe = PickaxeData.getDefinition(player).orElse(null);
	}
	
	/**
	 * Starts the skill action for the mining skill.
	 * @param player the player we are starting the action for.
	 * @param star   the star that we're mining.
	 * @return <true> if the skill action started, <false> otherwise.
	 */
	public static boolean mine(Player player, ShootingStar star) {
		StarMining mining = new StarMining(player, star);
		mining.start();
		return true;
	}

	@Override
	public boolean harvestMessage() {
		return false;
	}

	@Override
	public void onSequence(Task t) {
		if(star.isDisabled()) {
			this.onStop();
			t.cancel();
		}
	}
	
	@Override
	public void onHarvest(Task t, Item[] items, boolean success) {
		if(success) {
			star.setElements(star.getElements() + 1);
		}
		if(star.getElements() >= star.data.stardust && !star.isDisabled()) {
			Optional<ShootingStarData> filter = star.data.getNext();
			if(filter.isPresent()) {
				star.data = filter.get();
				star.setId(star.data.objectId);
				star.setElements(0);
				star.publish();
			} else {
				World.getShootingStarEvent().getShootingStar().getStarSprite().spawn();
			}
			this.onStop();
			t.cancel();
		}
	}
	
	@Override
	public double successFactor() {
		double successFactor = pickaxe.getSpeed() + 1.0 - random.nextDouble(1.0);
		if(successFactor < 0) {
			return 0;
		}
		return successFactor;
	}
	
	@Override
	public Optional<Item[]> removeItems() {
		return Optional.empty();
	}
	
	@Override
	public Item[] harvestItems() {
		return new Item[]{STARDUST};
	}
	
	@Override
	public boolean instant() {
		return false;
	}
	
	@Override
	public boolean init() {
		if(!checkMining()) {
			return false;
		}
		
		if(star.getUsername().isEmpty()) {
			star.setUsername(player.getFormatUsername());
			player.message("@red@You were the first player to mine this star.");
			player.message("@red@Enjoy the double experience from this star.");
		}
		
		getPlayer().message("You begin to mine the rock...");
		getPlayer().animation(pickaxe.getAnimation());
		return true;
	}
	
	@Override
	public boolean canExecute() {
		return !star.isDisabled() && checkMining();
	}
	
	@Override
	public double experience() {
		return star.getUsername().equals(player.getFormatUsername()) ? star.data.experience * 2 : star.data.experience;
	}
	
	@Override
	public SkillData skill() {
		return SkillData.MINING;
	}
	
	@Override
	public void onStop() {
		getPlayer().animation(null);
	}
	
	@Override
	public Optional<Animation> animation() {
		return Optional.of(pickaxe.getAnimation());
	}
	
	private boolean checkMining() {
		if(star == null) {
			return false;
		}
		if(PickaxeData.getDefinition(player).orElse(null) == null) {
			getPlayer().message("You don't have a pickaxe.");
			return false;
		}
		if(!getPlayer().getSkills()[skill().getId()].reqLevel(star.data.levelRequirement)) {
			getPlayer().message("You need a level of " + star.data.levelRequirement + " to mine this star!");
			return false;
		}
		if(!getPlayer().getSkills()[skill().getId()].reqLevel(pickaxe.getRequirement())) {
			getPlayer().message("You need a level of " + pickaxe.getRequirement() + " to use this pickaxe!");
			return false;
		}
		if(getPlayer().getInventory().remaining() < 1 && !getPlayer().getInventory().contains(STARDUST)) {
			getPlayer().message("You do not have any space left in your inventory.");
			return false;
		}
		
		if(getPlayer().getEquipment().containsAny(GLORY)) {
			int chance = !getPlayer().getEquipment().contains(GLORY[0]) ? 282 : 250;
			
			if(random.nextInt(chance) == 1) {
				getPlayer().getInventory().add(GEMS[random.nextInt(GEMS.length)]);
				getPlayer().message("You found a gem.");
			}
		}
		return true;
	}
}
