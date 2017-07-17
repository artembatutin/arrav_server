package net.edge.world.node.actor.mob.strategy.impl.gwd;

import net.edge.content.combat.CombatHit;
import net.edge.util.rand.RandomUtils;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.magic.CombatNormalSpell;
import net.edge.world.node.EntityState;
import net.edge.world.node.actor.Actor;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.Projectile;
import net.edge.world.node.actor.mob.impl.gwd.CommanderZilyana;
import net.edge.world.node.actor.mob.strategy.DynamicCombatStrategy;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.item.Item;

import java.util.Optional;

/**
 * The dynamic combat strategy for the commander zilyana boss.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class CommanderZilyanaCombatStrategy extends DynamicCombatStrategy<CommanderZilyana> {
	
	/**
	 * Constructs a new {@link CommanderZilyanaCombatStrategy}.
	 * @param npc the npc this strategy is for.
	 */
	public CommanderZilyanaCombatStrategy(CommanderZilyana npc) {
		super(npc);
	}
	
	@Override
	public boolean canOutgoingAttack(Actor victim) {
		return victim.isPlayer() && CommanderZilyana.CHAMBER.inLocation(victim.getPosition());
	}
	
	@Override
	public CombatHit outgoingAttack(Actor victim) {
		CombatType[] data = npc.getPosition().withinDistance(victim.getPosition(), 2) ? new CombatType[]{CombatType.MELEE, CombatType.MAGIC} : new CombatType[]{CombatType.MAGIC};
		CombatType c = RandomUtils.random(data);
		CommanderZilyana.MINIONS.forEach(minion -> {
			if(!minion.isDead() && minion.getState() == EntityState.ACTIVE) {
				minion.getCombatBuilder().attack(victim);
			}
		});
		return type(victim, c);
	}
	
	private CombatHit melee(Actor victim) {
		npc.animation(new Animation(6964));
		return new CombatHit(npc, victim, 1, CombatType.MELEE, true);
	}
	
	private CombatHit magic(Actor victim) {
		npc.setCurrentlyCasting(SPELL);
		npc.animation(new Animation(6967));
		npc.graphic(new Graphic(1220));
		return new CombatHit(npc, victim, 2, CombatType.MAGIC, true, 3);
	}
	
	private CombatHit type(Actor victim, CombatType type) {
		switch(type) {
			case MELEE:
				return melee(victim);
			case MAGIC:
				return magic(victim);
			default:
				return magic(victim);
		}
	}
	
	private static final CombatNormalSpell SPELL = new CombatNormalSpell() {
		
		@Override
		public int spellId() {
			return 0;
		}
		
		@Override
		public int maximumHit() {
			return 310;
		}
		
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.empty();
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.empty();
		}
		
		@Override
		public int levelRequired() {
			return 0;
		}
		
		@Override
		public double baseExperience() {
			return 0;
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}
		
	};
	
	@Override
	public void incomingAttack(Actor attacker, CombatHit data) {
		
	}
	
	@Override
	public int attackDelay() {
		return npc.getAttackSpeed();
	}
	
	@Override
	public int attackDistance() {
		return 7;
	}
}
