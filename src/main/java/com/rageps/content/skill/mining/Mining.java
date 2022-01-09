package com.rageps.content.skill.mining;

import com.rageps.action.impl.ObjectAction;
import com.rageps.content.achievements.Achievement;
import com.rageps.content.skill.SkillData;
import com.rageps.content.skill.Skills;
import com.rageps.content.skill.action.TransformableObject;
import com.rageps.content.skill.action.impl.HarvestingSkillAction;
import com.rageps.task.Task;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.object.DynamicObject;
import com.rageps.world.entity.object.GameObject;
import com.rageps.world.model.Animation;

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
	private final DynamicObject object;

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
	 * @param rock the mining rock.
	 * @param object the rock object.
	 */
	public Mining(Player player, RockData rock, GameObject object) {
		super(player, Optional.of(object.getPosition()));
		if(rock == RockData.ESSENCE && player.getSkills()[Skills.MINING].getRealLevel() >= 30)
			rock = RockData.PURE_ESSENCE;
		this.rock = rock;
		this.pickaxe = PickaxeData.getDefinition(player).orElse(null);
		this.object = object.toDynamic();
	}

	public static void action() {
		for(RockData rock : RockData.values()) {
			ObjectAction mine = new ObjectAction() {
				@Override
				public boolean click(Player player, GameObject object, int click) {
					Mining mining = new Mining(player, rock, object);
					mining.start();
					return true;
				}
			};
			ObjectAction prospect = new ObjectAction() {
				@Override
				public boolean click(Player player, GameObject object, int click) {
					player.message("You examine the rock for ores...");
					String message = rock.toString().concat(" ore").replace("_", " ");
					World.get().submit(new Task(rock.prospectDelay(), false) {
						@Override
						public void execute() {
							player.message("... this rock contains @red@" + message + "@bla@.");
							cancel();
						}
					}.attach(player));
					return true;
				}
			};
			for(TransformableObject o : rock.getObject()) {
				mine.registerFirst(o.getObjectId());
				prospect.registerSecond(o.getObjectId());
			}
		}
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
			object.setElements(object.getElements() - 1);
		}
		if(object.getElements() <= 0 && !object.isDisabled()) {
			Achievement.MINER.inc(player);
			TransformableObject obj = null;
			for(TransformableObject ob : rock.getObject()) {
				if(ob.getObjectId() == object.getId()) {
					obj = ob;
					break;
				}
			}
			if(obj != null) {
				int id = object.getId();//filled rock.
				GameObject emptyRock = object.setId(obj.getTransformable());
				object.setDisabled(true);
				emptyRock.publish(rock.getRespawnTime(), n -> {
					object.setId(id);
					object.publish();
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
		if(object.getElements() <= 0) {
			object.setElements(rock.getOreCount());
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
					GroundItem node = new GroundItem(PickaxeData.PICKAXE_HANDLE, getPlayer().getPosition(), getPlayer());
					ItemNodeManager.register(node);
				}
				GroundItem node = new GroundItem(pickaxe.getHead(), getPlayer().getPosition(), getPlayer());
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
