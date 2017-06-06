package net.edge.world.node.entity.npc.strategy.impl.glacor;

import net.edge.content.combat.CombatSessionData;
import net.edge.content.combat.CombatType;
import net.edge.content.skill.Skills;
import net.edge.world.World;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.npc.impl.glacor.GlacorExplodeTask;
import net.edge.world.node.entity.npc.impl.glacor.Glacyte;
import net.edge.world.node.entity.npc.impl.glacor.GlacyteData;
import net.edge.world.node.entity.npc.strategy.DynamicCombatStrategy;

import java.util.Arrays;

/**
 * The combat strategy for glacytes.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class GlacyteCombatStrategy extends DynamicCombatStrategy<Glacyte> {
	
	/**
	 * Constructs a new {@link GlacyteCombatStrategy}.
	 * @param npc the npc to create this strategy for.
	 */
	public GlacyteCombatStrategy(Glacyte npc) {
		super(npc);
	}
	
	@Override
	public boolean canOutgoingAttack(EntityNode victim) {
		return victim.isPlayer();
	}
	
	@Override
	public CombatSessionData outgoingAttack(EntityNode victim) {
		CombatSessionData session = new CombatSessionData(npc, victim, 1, CombatType.MELEE, true);
		
		if(npc.getGlacyteData().equals(GlacyteData.UNSTABLE)) {
			if(!npc.getSpecial().isPresent()) {
				npc.setSpecial(0);
			}
			
			int special = npc.getSpecial().getAsInt();
			
			if(special == 100) {
				session.ignore();
				World.get().submit(new GlacorExplodeTask(victim.toPlayer(), npc, false));
			}
			
			npc.setSpecial(special + 10);
		} else if(npc.getGlacyteData().equals(GlacyteData.SAPPING)) {
			victim.toPlayer().getSkills()[Skills.PRAYER].decreaseLevel(2, true);
			Skills.refresh(victim.toPlayer(), Skills.PRAYER);
		}
		
		return session;
	}
	
	@Override
	public void incomingAttack(EntityNode victim, CombatSessionData data) {
		if(npc.getId() == GlacyteData.ENDURING.getNpcId()) {
			int distance = npc.getPosition().getLongestDelta(npc.getGlacor().getPosition());
			
			if(distance < 3) {
				victim.toPlayer().message("@red@You should lure the glacyte further away from the glacor.");
				Arrays.stream(data.getHits()).forEach(h -> h.setAccurate(false));
				return;
			}
			
			if(distance > 3 && distance < 7) {
				Arrays.stream(data.getHits()).forEach(h -> h.setDamage(h.getDamage() / 2));
			}
			
		}
	}
	
	@Override
	public int attackDelay() {
		return npc.getAttackSpeed();
	}
	
	@Override
	public int attackDistance() {
		return 1;
	}
	
}
