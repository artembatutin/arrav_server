package net.edge.world.content.combat.effect.potion;

import net.edge.world.content.combat.effect.CombatEffect;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.AntifireDetails;
import net.edge.world.node.entity.player.assets.AntifireDetails.AntifireType;

import java.util.Optional;

/**
 * The class which is responsible for the effect when you drink an anti-fire potion.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class CombatAntifireEffect extends CombatEffect {
	
	/**
	 * The type of antifire this player has drunk.
	 */
	private final AntifireType type;
	
	/**
	 * Constructs a new {@link CombatAntifireEffect}.
	 * @param type {@link #type}.
	 */
	public CombatAntifireEffect(AntifireType type) {
		super(1);
		this.type = type;
	}
	
	@Override
	public boolean apply(EntityNode c) {
		if(!c.isPlayer()) {
			return false;
		}
		
		Player player = c.toPlayer();
		
		if(player.getAntifireDetails().isPresent()) {
			player.setAntifireDetail(new AntifireDetails(type));
			return false;
		}
		player.setAntifireDetail(new AntifireDetails(type));
		return true;
	}
	
	@Override
	public boolean removeOn(EntityNode c) {
		if(c.isPlayer()) {
			Player player = c.toPlayer();
			
			if(!player.getAntifireDetails().isPresent()) {
				return true;
			}
			
			return false;
		}
		return true;
	}
	
	@Override
	public void process(EntityNode c) {
		if(c.isPlayer() && c.toPlayer().getAntifireDetails().isPresent()) {
			Player player = c.toPlayer();
			AntifireDetails detail = player.getAntifireDetails().get();
			int count = detail.getAntifireDelay().decrementAndGet();
			if(count == 30) {
				player.message("@red@Your resistance to dragon fire is about to wear off!");
			}
			if(count < 1) {
				player.setAntifireDetail(Optional.empty());
				player.message("Your resistance to dragon fire has worn off!");
			}
		}
	}
	
	@Override
	public boolean onLogin(EntityNode c) {
		return c.isPlayer() && c.toPlayer().getAntifireDetails().isPresent();
	}
	
}