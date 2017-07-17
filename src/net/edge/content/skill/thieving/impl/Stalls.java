package net.edge.content.skill.thieving.impl;

import net.edge.event.impl.ObjectEvent;
import net.edge.task.Task;
import net.edge.util.TextUtils;
import net.edge.content.skill.thieving.Thieving;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Animation;
import net.edge.world.Hit;
import net.edge.world.node.actor.mob.Mob;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.item.Item;
import net.edge.world.object.DynamicObject;
import net.edge.world.object.ObjectNode;

import java.util.Optional;

/**
 * Represents functionality for stealing from a stall.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Stalls extends Thieving {
	
	/**
	 * The definition for this stall.
	 */
	private final StallData stall;
	
	/**
	 * The object node this player is interacting with.
	 */
	private final DynamicObject object;
	
	/**
	 * Constructs a new {@link Stalls}.
	 * @param player {@link #getPlayer()}.
	 * @param stall  the stall this player is stealing from.
	 * @param object the object this player is interacting with.
	 */
	private Stalls(Player player, StallData stall, DynamicObject object) {
		super(player, object.getGlobalPos());
		this.stall = stall;
		this.object = object;
	}
	
	public static void event() {
		for(StallData data : StallData.values()) {
			ObjectEvent steal = new ObjectEvent() {
				@Override
				public boolean click(Player player, ObjectNode object, int click) {
					if(object.isDynamic() && object.toDynamic().isDisabled()) {
						return false;
					}
					Stalls stall = new Stalls(player, data, object.toDynamic());
					stall.start();
					return true;
				}
			};
			for(int o : data.objectId) {
				steal.registerSecond(o);
			}
		}
	}
	
	/**
	 * The animation when stealing from stalls
	 */
	private static final Animation ANIMATION = new Animation(832);
	
	/**
	 * Object ids of empty stalls
	 */
	private static final int[] EMPTY_STALLS = new int[]{634, 620};
	
	@Override
	public int requirement() {
		return stall.requirement;
	}
	
	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(ANIMATION);
	}
	
	@Override
	public boolean canInit() {
//		if(object.isDisabled()) {
//			return false;
//		}
		String name = object.getDefinition().getName();
		if(!getPlayer().getSkills()[skill().getId()].reqLevel(requirement())) {
			getPlayer().message("You need a thieving level of " + requirement() + " to steal from " + TextUtils.appendIndefiniteArticle(name) + ".");
			return false;
		}

		if(!getPlayer().getInventory().hasCapacityFor(stall.loot)) {
			player.message("You don't have enough inventory space for the loot.");
			return false;
		}
		
		if(!player.getSkills()[skill().getId()].getDelay().elapsed(1800)) {
			return false;
		}
		player.getSkills()[skill().getId()].getDelay().reset();
		return true;
	}
	
	@Override
	public void onSubmit() {
		if(stall.requirement > 40 && RandomUtils.inclusive(200) < 10) {
			Optional<Mob> guard = player.getLocalMobs().stream().filter(g -> g.getId() == 3408 && !g.getCombatBuilder().inCombat()).findFirst();
			if(guard.isPresent()) {
				guard.get().forceChat("Get your hands off there!");
				guard.get().faceEntity(player);
				player.damage(new Hit(10, Hit.HitType.DISEASE, Hit.HitIcon.NONE));
			}
		}
	}
	
	@Override
	public void onExecute(Task t) {
		t.cancel();
	}
	
	@Override
	public void onStop(boolean success) {
//		if(success)
//			World.get().submit(new StallTask(this, object));
	}
	
	@Override
	public Item[] loot() {
		return stall.loot;
	}
	
	@Override
	public int delay() {
		return 1;
	}
	
	@Override
	public boolean instant() {
		return false;
	}
	
	@Override
	public boolean canExecute() {
		return !object.isDisabled();
	}
	
	@Override
	public double experience() {
		return stall.experience;
	}
	
	/**
	 * The enumerated type whose elements represent the data for stealing from a stall.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private enum StallData {
		FOOD_STALL(new int[]{4875}, EMPTY_STALLS[1], 1, new Item[]{new Item(1963, 1)}, 23, 3),
		CRAFTING_STALL(new int[]{4874}, EMPTY_STALLS[1], 35, new Item[]{new Item(19650, 1)}, 35, 3),
		GENERAL_STALL(new int[]{4876}, EMPTY_STALLS[1], 50, new Item[]{new Item(4073, 1)}, 70, 4),
		MAGIC_STALL(new int[]{4877}, EMPTY_STALLS[1], 75, new Item[]{new Item(14061, 1)}, 150, 8),
		SCIMITAR_STALL(new int[]{4878}, EMPTY_STALLS[1], 90, new Item[]{new Item(6611, 1)}, 220, 12),
		VEGETABLE(new int[]{4706}, EMPTY_STALLS[0], 1, new Item[]{new Item(995, 50)}, 10, 2),
		BAKERS(new int[]{2561}, EMPTY_STALLS[0], 5, new Item[]{new Item(995, 100)}, 16, 3),
		TEA(new int[]{635}, EMPTY_STALLS[0], 5, new Item[]{new Item(995, 500)}, 16, 7),
		SILK(new int[]{2560}, EMPTY_STALLS[0], 20, new Item[]{new Item(995, 1000)}, 24, 8),
		WINE(new int[]{14011}, EMPTY_STALLS[0], 22, new Item[]{new Item(995, 2500)}, 27, 16),
		SEED(new int[]{7053}, EMPTY_STALLS[0], 27, new Item[]{new Item(995, 5000)}, 10, 11),
		FUR(new int[]{2563}, EMPTY_STALLS[0], 35, new Item[]{new Item(995, 75000)}, 36, 15),
		FISH(new int[]{4277}, EMPTY_STALLS[0], 42, new Item[]{new Item(995, 10000)}, 42, 16),
		CROSSBOW(new int[]{17031}, EMPTY_STALLS[0], 49, new Item[]{new Item(995, 12500)}, 52, 11),
		SILVER(new int[]{2565}, EMPTY_STALLS[0], 50, new Item[]{new Item(995, 15000)}, 54, 20),
		CUSTOMS_EVIDENCE_FILES(new int[]{-1}, -1, 63, new Item[]{new Item(995, 17500)}, 75, 20),
		SPICE(new int[]{2564}, EMPTY_STALLS[0], 65, new Item[]{new Item(995, 20000)}, 81, 40),
		GEM(new int[]{2562}, EMPTY_STALLS[0], 75, new Item[]{new Item(995, 35000)}, 160, 80);
		
		/**
		 * The object identification for this stall.
		 */
		private final int[] objectId;
		
		/**
		 * The object identification for an empty stall.
		 */
		private final int emptyStallId;
		
		/**
		 * The required level to steal from this stall.
		 */
		private final int requirement;
		
		/**
		 * The loot you get from stealing for this stall.
		 */
		private final Item[] loot;
		
		/**
		 * The experience gained for stealing from this stall.
		 */
		private final double experience;
		
		/**
		 * The time it takes for the stall to respawn.
		 */
		private final int respawnTime;
		
		/**
		 * Constructs a new {@link StallData} enumerator.
		 * @param objectId     {@link #objectId}.
		 * @param emptyStallId {@link #emptyStallId}.
		 * @param requirement  {@link #requirement}.
		 * @param loot         {@link #loot}.
		 * @param experience   {@link #experience}.
		 * @param respawnTime  {@link #respawnTime}.
		 */
		StallData(int[] objectId, int emptyStallId, int requirement, Item[] loot, int experience, int respawnTime) {
			this.objectId = objectId;
			this.emptyStallId = emptyStallId;
			this.requirement = requirement;
			this.loot = loot;
			this.experience = experience;
			this.respawnTime = respawnTime;
		}
		
	}
	
	/**
	 * The class which submits respawning tasks for stalls.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private static class StallTask extends Task {
		
		/**
		 * The stall we're submitting this task for.
		 */
		private final Stalls stall;
		
		/**
		 * The main stall object.
		 */
		private final DynamicObject object;
		
		/**
		 * The saved stall id.
		 */
		private final int id;
		
		/**
		 * Constructs a new {@link StallTask}.
		 * @param stall  the stall being used.
		 * @param object the stall's object node.
		 */
		StallTask(Stalls stall, DynamicObject object) {
			super(stall.stall.respawnTime, false);
			this.stall = stall;
			this.object = object;
			this.id = object.getId();
		}
		
		@Override
		public void onSubmit() {
			object.setDisabled(true);
			object.setId(stall.stall.emptyStallId);
			object.publish();
		}
		
		@Override
		public void execute() {
			object.setDisabled(false);
			object.setId(id);
			stall.object.publish();
			this.cancel();
		}
		
	}
}
