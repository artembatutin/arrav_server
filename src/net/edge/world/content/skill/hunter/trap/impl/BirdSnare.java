package net.edge.world.content.skill.hunter.trap.impl;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.task.Task;
import net.edge.utils.rand.RandomUtils;
import net.edge.world.World;
import net.edge.world.content.skill.Skills;
import net.edge.world.content.skill.hunter.trap.Trap;
import net.edge.world.locale.Position;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;
import net.edge.world.node.object.ObjectNode;

import java.util.EnumSet;
import java.util.Optional;

/**
 * The bird snare implementation of the {@link Trap} class which represents a single bird snare.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class BirdSnare extends Trap {
	
	/**
	 * Constructs a new {@link BirdSnare}.
	 * @param player {@link #getPlayer()}.
	 */
	public BirdSnare(Player player) {
		super(player, TrapType.BIRD_SNARE);
	}
	
	/**
	 * The npc trapped inside this box.
	 */
	private Optional<Npc> trapped = Optional.empty();
	
	/**
	 * Determines if a bird is going to the trap.
	 */
	private Optional<Task> event = Optional.empty();
	
	/**
	 * The distance the npc has to have from the snare before it gets triggered.
	 */
	private static final int DISTANCE_PORT = 3;
	
	/**
	 * A collection of all the npcs that can be caught with a bird snare.
	 */
	private static final ImmutableSet<Integer> NPC_IDS = ImmutableSet.of(BirdData.CRIMSON_SWIFT.npcId, BirdData.GOLDEN_WARBLER.npcId, BirdData.COPPER_LONGTAIL.npcId, BirdData.CERULEAN_TWITCH.npcId, BirdData.TROPICAL_WAGTAIL.npcId);
	
	/**
	 * Kills the specified {@code npc}.
	 * @param npc the npc to kill.
	 */
	private void kill(Npc npc) {
		npc.move(new Position(0, 0, 0));
		npc.appendDeath();
		trapped = Optional.of(npc);
	}
	
	@Override
	public boolean canCatch(Npc npc) {
		Optional<BirdData> data = BirdData.getBirdDataByNpcId(npc.getId());
		
		if(!data.isPresent()) {
			throw new IllegalStateException("Invalid bird id.");
		}
		
		if(player.getSkills()[Skills.HUNTER].getLevel() < data.get().requirement) {
			setState(TrapState.FALLEN);
			return false;
		}
		return true;
	}
	
	@Override
	public void onPickUp() {
		player.message("You pick up your bird snare.");
	}
	
	@Override
	public void onSetup() {
		player.message("You set-up your bird snare.");
	}
	
	@Override
	public void onCatch(Npc npc) {
		Optional<BirdData> data = BirdData.getBirdDataByNpcId(npc.getId());
		
		if(!data.isPresent()) {
			throw new IllegalStateException("Invalid bird id.");
		}
		
		if(event.isPresent()) {
			return;
		}
		
		BirdData bird = data.get();
		
		event = Optional.of(new Task(1, false) {
			
			@Override
			public void execute() {
				if(!getState().equals(TrapState.PENDING)) {
					this.cancel();
					return;
				}
				npc.getMovementQueue().smartWalk(getObject().getPosition().copy());
				if(isAbandoned()) {
					this.cancel();
					return;
				}
				if(npc.getPosition().getX() == getObject().getPosition().getX() && npc.getPosition().getY() == getObject().getPosition().getY()) {
					this.cancel();
					int count = RandomUtils.inclusive(150);
					int formula = successFormula(npc);
					if(count > formula) {
						setState(TrapState.FALLEN);
						this.cancel();
						return;
					}
					
					kill(npc);
					World.getRegions().getRegion(getObject().getPosition()).unregister(getObject());
					setObject(bird.objectId);
					World.getRegions().getRegion(getObject().getPosition()).register(getObject());
					setState(TrapState.CAUGHT);
				}
			}
			
			@Override
			public void onCancel() {
				event = Optional.empty();
			}
		});
		
		if(event.isPresent())
			World.submit(event.get());
	}
	
	@Override
	public void onSequence(Task t) {
		for(Npc npc : World.getNpcs()) {
			if(npc == null || npc.isDead()) {
				continue;
			}
			if(!NPC_IDS.stream().anyMatch(id -> npc.getId() == id)) {
				continue;
			}
			if(this.getObject().getPosition().withinDistance(npc.getPosition(), DISTANCE_PORT)) {
				if(RandomUtils.inclusive(100) < 20) {
					return;
				}
				if(this.isAbandoned()) {
					return;
				}
				trap(npc);
			}
		}
	}
	
	@Override
	public Item[] reward() {
		if(!trapped.isPresent()) {
			throw new IllegalStateException("No npc is trapped.");
		}
		Optional<BirdData> data = BirdData.getBirdDataByObjectId(getObject().getId());
		
		if(!data.isPresent()) {
			throw new IllegalStateException("Invalid object id.");
		}
		
		return data.get().reward;
	}
	
	@Override
	public double experience() {
		if(!trapped.isPresent()) {
			throw new IllegalStateException("No npc is trapped.");
		}
		Optional<BirdData> data = BirdData.getBirdDataByObjectId(getObject().getId());
		
		if(!data.isPresent()) {
			throw new IllegalStateException("Invalid object id.");
		}
		
		return data.get().experience;
	}
	
	@Override
	public boolean canClaim(ObjectNode object) {
		if(!trapped.isPresent()) {
			return false;
		}
		BirdData data = BirdData.getBirdDataByObjectId(object.getId()).orElse(null);
		
		return data != null;
		
	}
	
	@Override
	public void setState(TrapState state) {
		if(state.equals(TrapState.PENDING)) {
			throw new IllegalArgumentException("Cannot set trap state back to pending.");
		}
		if(state.equals(TrapState.FALLEN)) {
			World.getRegions().getRegion(getObject().getPosition()).unregister(getObject());
			this.setObject(TrapType.FAILED_BIRD_SNARE.getObjectId());
			World.getRegions().getRegion(getObject().getPosition()).register(getObject());
		}
		player.message("Your trap has been triggered by something...");
		super.setState(state);
	}
	
	/**
	 * The enumerated type whose elements represent a set of constants
	 * used for bird snaring.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private enum BirdData {
		CRIMSON_SWIFT(5073, 19180, 1, 34, 526, 10088, 9978),
		GOLDEN_WARBLER(5075, 19184, 5, 47, 526, 10090, 9978),
		COPPER_LONGTAIL(5076, 19186, 9, 61, 526, 10091, 9978),
		CERULEAN_TWITCH(5074, 19182, 11, 64.5, 526, 10089, 9978),
		TROPICAL_WAGTAIL(5072, 19178, 19, 95, 526, 10087, 9978);
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<BirdData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(BirdData.class));
		
		/**
		 * The npc id for this bird.
		 */
		private final int npcId;
		
		/**
		 * The object id for the catched bird.
		 */
		private final int objectId;
		
		/**
		 * The requirement for this bird.
		 */
		private final int requirement;
		
		/**
		 * The experience gained for this bird.
		 */
		private final double experience;
		
		/**
		 * The reward obtained for this bird.
		 */
		private final Item[] reward;
		
		/**
		 * Constructs a new {@link BirdData}.
		 * @param npcId       {@link #npcId}.
		 * @param objectId    {@link #objectId}
		 * @param requirement {@link #requirement}.
		 * @param experience  {@link #experience}.
		 * @param reward      {@link #reward}.
		 */
		BirdData(int npcId, int objectId, int requirement, double experience, int... reward) {
			this.npcId = npcId;
			this.objectId = objectId;
			this.requirement = requirement;
			this.experience = experience;
			this.reward = Item.convert(reward);
		}
		
		/**
		 * @return the npc id.
		 */
		public int getNpcId() {
			return npcId;
		}
		
		/**
		 * Retrieves a {@link BirdData} enumerator dependant on the specified {@code id}.
		 * @param id the npc id to return an enumerator from.
		 * @return a {@link BirdData} enumerator wrapped inside an optional, {@link Optional#empty()} otherwise.
		 */
		public static Optional<BirdData> getBirdDataByNpcId(int id) {
			return VALUES.stream().filter(bird -> bird.npcId == id).findAny();
		}
		
		/**
		 * Retrieves a {@link BirdData} enumerator dependant on the specified {@code id}.
		 * @param id the object id to return an enumerator from.
		 * @return a {@link BirdData} enumerator wrapped inside an optional, {@link Optional#empty()} otherwise.
		 */
		public static Optional<BirdData> getBirdDataByObjectId(int id) {
			return VALUES.stream().filter(bird -> bird.objectId == id).findAny();
		}
		
	}
	
}
