package net.edge.world.content.skill.mining;

import net.edge.task.Task;
import net.edge.world.World;
import net.edge.world.content.skill.SkillData;
import net.edge.world.content.skill.action.TransformableObject;
import net.edge.world.content.skill.action.impl.HarvestingSkillAction;
import net.edge.world.model.node.entity.model.Animation;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;
import net.edge.world.model.node.object.ObjectNode;
import net.edge.world.model.node.region.Region;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public final class Mining extends HarvestingSkillAction {

	/**
	 * The definition of the {@link PickaxeData} being used.
	 */
	private final PickaxeData pickaxe;

	/**
	 * The definition of the {@link RockData} being mined.
	 */
	private final RockData rock;

	/**
	 * The object we're interfering with.
	 */
	private final ObjectNode object;

	/**
	 * The random generator which will generate random values.
	 */
	private final ThreadLocalRandom random = ThreadLocalRandom.current();

	/**
	 * An array holding all the possible gems which can be obtained while mining.
	 */
	private static final Item[] GEMS = Item.convert(1623, 1621, 1603, 1617);

	/**
	 * An array holding all the possible glory's a player can wield in order to mine for gems.
	 */
	private static final Item[] GLORY = Item.convert(1704, 1706, 1708, 1710, 1712);

	/**
	 * Constructs a new {@link Mining}.
	 * @param player {@link #player}.
	 * @param rock   the mining rock.
	 * @param object the rock object.
	 */
	public Mining(Player player, RockData rock, ObjectNode object) {
		super(player, Optional.of(object.getPosition()));

		this.object = object;
		this.rock = rock;
		this.pickaxe = PickaxeData.getDefinition(player).orElse(null);
	}

	/**
	 * Starts the skill action for the mining skill.
	 * @param player the player we are starting the action for.
	 * @param object the object that we're getting the definition from.
	 * @return <true> if the skill action started, <false> otherwise.
	 */
	public static boolean produce(Player player, ObjectNode object) {
		Optional<RockData> rock = RockData.getDefinition(object.getId());

		if(!rock.isPresent()) {
			return false;
		}

		Mining mining = new Mining(player, rock.get(), object);
		mining.start();
		return true;
	}

	public static boolean prospect(Player player, ObjectNode object) {
		Optional<RockData> rock = RockData.getDefinition(object.getId());

		if(!rock.isPresent()) {
			return false;
		}

		player.message("You examine the rock for ores...");

		String message = rock.get().toString().concat(" ore").replace("_", " ");

		World.submit(new Task(rock.get().prospectDelay(), false) {

			@Override
			public void execute() {
				player.message("... this rock contains @red@" + message + "@bla@.");
				cancel();
			}
		}.attach(player));
		return true;
	}

	@Override
	public void onSequence(Task t) {
		if(object.isDisabled()) {
			this.onStop();
			t.cancel();
		}
	}

	@Override
	public void onHarvest(Task t, Item[] items, boolean success) {
		if(rock == RockData.ESSENCE || rock == RockData.PURE_ESSENCE)
			return;
		if(success) {
			randomEvent();
			object.setProducingCount(object.getProducingCount() - 1);
		}
		if(object.getProducingCount() <= 0 && !object.isDisabled()) {
			Optional<TransformableObject> filter = Arrays.stream(rock.getObject()).filter(p -> p.getObjectId() == object.getId()).findFirst();

			if(filter.isPresent()) {
				ObjectNode emptyRock = new ObjectNode(filter.get().getTransformable(), object.getPosition(), object.getDirection(), object.getObjectType());
				Region reg = World.getRegions().getRegion(object.getPosition());
				reg.unregister(object);
				object.setDisabled(true);
				reg.register(emptyRock, rock.getRespawnTime(), n -> {
					reg.unregister(emptyRock);
					reg.register(object);
					object.setDisabled(false);
				});
			}
			this.onStop();
			t.cancel();
		}
	}

	@Override
	public double successFactor() {
		double successFactor = pickaxe.getSpeed() + rock.getSuccess() - random.nextDouble(1.0);
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
		return new Item[]{rock.getItem()};
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
		if(object.getProducingCount() <= 0) {
			object.setProducingCount(rock.getOreCount());
		}
		getPlayer().message("You begin to mine the rock...");
		getPlayer().animation(pickaxe.getAnimation());
		return true;
	}

	@Override
	public boolean canExecute() {
		return !object.isDisabled() && checkMining();
	}

	@Override
	public double experience() {
		return rock.getExperience();
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

	private void randomEvent() {
	    /*if((random.nextInt(1000) - (pickaxe.ordinal() * 10)) > 900) {
	        if(random.nextBoolean()) {
				if(getPlayer().getEquipment().contains(pickaxe.getItem())) {
					getPlayer().getEquipment().unequipItem(pickaxe.getItem(), false);
				} else {
					getPlayer().getInventory().remove(pickaxe.getItem());
				}
				if(getPlayer().getInventory().spaceFor(PickaxeData.PICKAXE_HANDLE)) {
					getPlayer().getInventory().add(PickaxeData.PICKAXE_HANDLE);
				} else {
					ItemNode node = new ItemNode(PickaxeData.PICKAXE_HANDLE, getPlayer().getPosition(), getPlayer());
					ItemNodeManager.register(node);
				}
				ItemNode node = new ItemNode(pickaxe.getHead(), getPlayer().getPosition(), getPlayer());
				ItemNodeManager.register(node);
				getPlayer().message("Your pickaxe dismantled during the mining process.");
			} else {
				if(getPlayer().getEquipment().contains(pickaxe.getItem())) {
					getPlayer().getEquipment().remove(pickaxe.getItem());
					getPlayer().getEquipment().add(pickaxe.getBrokenPickaxe(), Equipment.WEAPON_SLOT);
				} else {
					getPlayer().getInventory().remove(pickaxe.getItem());
					getPlayer().getInventory().add(pickaxe.getBrokenPickaxe());
				}
				getPlayer().message("Your pickaxe has broken!");
			}
			return true;
		}*/
		return; //FIXME
	}

	private boolean checkMining() {
		if(rock == null) {
			getPlayer().message("Rock is null.");
			return false;
		}
		if(PickaxeData.getDefinition(player).orElse(null) == null) {
			getPlayer().message("You don't have a pickaxe.");
			return false;
		}
		if(!getPlayer().getSkills()[skill().getId()].reqLevel(rock.getRequirement())) {
			getPlayer().message("You need a level of " + rock.getRequirement() + " to mine this rock!");
			return false;
		}
		if(!getPlayer().getSkills()[skill().getId()].reqLevel(pickaxe.getRequirement())) {
			getPlayer().message("You need a level of " + pickaxe.getRequirement() + " to use this pickaxe!");
			return false;
		}
		if(getPlayer().getInventory().remaining() < 1) {
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
