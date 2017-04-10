package net.edge.world.content.skill.hunter.trap.impl;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.world.content.skill.Skills;
import net.edge.world.World;
import net.edge.world.content.skill.hunter.trap.Trap;
import net.edge.world.model.locale.Position;
import net.edge.world.model.node.entity.npc.Npc;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;
import net.edge.world.model.node.object.ObjectNode;
import net.edge.task.Task;
import net.edge.utils.rand.RandomUtils;

import java.util.EnumSet;
import java.util.Optional;

/**
 * The box trap implementation of the {@link Trap} class which represents a single box trap.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class BoxTrap extends Trap {
	
	/**
	 * Constructs a new {@link BoxTrap}.
	 * @param player {@link #getPlayer()}.
	 */
	public BoxTrap(Player player) {
		super(player, TrapType.BOX_TRAP);
	}
	
	/**
	 * The npc trapped inside this box.
	 */
	private Optional<Npc> trapped = Optional.empty();
	
	/**
	 * Determines if an animal is going to the trap.
	 */
	private Optional<Task> event = Optional.empty();
	
	/**
	 * The object identification for a caught box trap.
	 */
	private static final int CAUGHT_ID = 19190;
	
	/**
	 * The distance the npc has to have from the box trap before it gets triggered.
	 */
	private static final int DISTANCE_PORT = 2;
	
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
		Optional<BoxTrapData> data = BoxTrapData.getBoxTrapDataByNpcId(npc.getId());
		
		if(!data.isPresent()) {
			throw new IllegalStateException("Invalid box trap id.");
		}
		
		if(player.getSkills()[Skills.HUNTER].getLevel() < data.get().requirement) {
			setState(TrapState.FALLEN);
			return false;
		}
		return true;
	}
	
	@Override
	public void onPickUp() {
		player.message("You pick up your box trap.");
	}
	
	@Override
	public void onSetup() {
		player.message("You set-up your box trap.");
	}
	
	@Override
	public void onCatch(Npc npc) {
		if(event.isPresent()) {
			return;
		}
		
		event = Optional.of(new Task(1, false) {
			
			@Override
			public void execute() {
				npc.getMovementQueue().smartWalk(getObject().getPosition().copy());
				if(isAbandoned()) {
					this.cancel();
					return;
				}
				if(npc.getPosition().getX() == getObject().getPosition().getX() && npc.getPosition().getY() == getObject().getPosition().getY()) {
					this.cancel();
					
					int count = RandomUtils.inclusive(180);
					int formula = successFormula(npc);
					if(count > formula) {
						setState(TrapState.FALLEN);
						this.cancel();
						return;
					}
					kill(npc);
					World.getRegions().getRegion(getObject().getPosition()).unregister(getObject());
					setObject(CAUGHT_ID);
					World.getRegions().getRegion(getObject().getPosition()).register(getObject());
					setState(TrapState.CAUGHT);
				}
			}
			
			@Override
			public void onCancel() {
				event = Optional.empty();
			}
		});
		World.submit(event.get());
	}
	
	@Override
	public void onSequence(Task t) {
		for(Npc npc : player.getLocalNpcs()) {
			if(npc == null || npc.isDead()) {
				continue;
			}
			if(!BoxTrapData.VALUES.stream().anyMatch(id -> id.npcId == npc.getId())) {
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
		
		Optional<BoxTrapData> data = BoxTrapData.getBoxTrapDataByNpcId(trapped.get().getId());
		
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
		
		Optional<BoxTrapData> data = BoxTrapData.getBoxTrapDataByNpcId(trapped.get().getId());
		
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
		Optional<BoxTrapData> data = BoxTrapData.getBoxTrapDataByNpcId(trapped.get().getId());
		
		return data != null;
		
	}
	
	@Override
	public void setState(TrapState state) {
		if(state.equals(TrapState.PENDING)) {
			throw new IllegalArgumentException("Cannot set trap state back to pending.");
		}
		if(state.equals(TrapState.FALLEN)) {
			World.getRegions().getRegion(getObject().getPosition()).unregister(getObject());
			this.setObject(TrapType.FAILED_BOX_TRAP.getObjectId());
			World.getRegions().getRegion(getObject().getPosition()).register(getObject());
		}
		player.message("Your trap has been triggered by something...");
		super.setState(state);
	}
	
	/**
	 * The enumerated type whose elements represent a set of constants
	 * used for box trapping.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private enum BoxTrapData {
		GECKO(7289, 27, 100, 12488),
		MONKEY(7228, 27, 100, 12201),
		RACCOON(6997, 27, 100, 12199),
		FERRET(5081, 27, 115, 10092),
		CHINCHOMPA(5079, 53, 198.25, 10033),
		CARNIVOROUS_CHINCHOMPA(5080, 63, 265, 10034),
		PAWYA(7012, 66, 400, 12535),
		GRENWALL(7010, 77, 415, 12539);
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<BoxTrapData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(BoxTrapData.class));
		
		/**
		 * The npc id for this box trap.
		 */
		private final int npcId;
		
		/**
		 * The requirement for this box trap.
		 */
		private final int requirement;
		
		/**
		 * The experience gained for this box trap.
		 */
		private final double experience;
		
		/**
		 * The reward obtained for this box trap.
		 */
		private final Item[] reward;
		
		/**
		 * Constructs a new {@link BoxTrapData}.
		 * @param npcId       {@link #npcId}.
		 * @param requirement {@link #requirement}.
		 * @param experience  {@link #experience}.
		 * @param reward      {@link #reward}.
		 */
		BoxTrapData(int npcId, int requirement, double experience, int... reward) {
			this.npcId = npcId;
			this.requirement = requirement;
			this.experience = experience;
			this.reward = Item.convert(reward);
		}
		
		/**
		 * Retrieves a {@link BoxTrapData} enumerator dependant on the specified {@code id}.
		 * @param id the npc id to return an enumerator from.
		 * @return a {@link BoxTrapData} enumerator wrapped inside an optional, {@link Optional#empty()} otherwise.
		 */
		public static Optional<BoxTrapData> getBoxTrapDataByNpcId(int id) {
			return VALUES.stream().filter(box -> box.npcId == id).findAny();
		}
	}
}