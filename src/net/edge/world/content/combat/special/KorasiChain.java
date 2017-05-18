package net.edge.world.content.combat.special;

import net.edge.task.Task;
import net.edge.world.locale.Position;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.model.Animation;
import net.edge.world.node.entity.model.Graphic;
import net.edge.world.node.entity.model.Hit;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles the Korasi sword special chained hits in multi areas.
 */
class KorasiChain extends Task {
	
	/**
	 * The player doing the special.
	 */
	private final Player player;
	
	/**
	 * The entities being already hitted by the lightning.
	 */
	private List<EntityNode> hitted = new ArrayList<>();
	
	/**
	 * The chained damage in the korasi hits.
	 */
	private int chainedDamage;
	
	/**
	 * the first hit position.
	 */
	private Position position;
	
	KorasiChain(Player player, EntityNode victim, int damage) {
		super(3, false);
		this.player = player;
		this.position = victim.getPosition();
		chainedDamage = damage;
	}
	
	@Override
	protected void execute() {
		if(chainedDamage <= 0) {
			this.cancel();
			return;
		}
		Optional<Npc> npc = player.getLocalNpcs().stream().filter(n -> n.getPosition().withinDistance(position, 1) && !hitted.contains(n)).findAny();
		if(npc.isPresent()) {
			Npc n = npc.get();
			n.damage(new Hit(chainedDamage / 2, Hit.HitType.NORMAL, Hit.HitIcon.MAGIC, player.getSlot()));
			n.animation(new Animation(n.getDefinition().getDefenceAnimation()));
			n.graphic(new Graphic(1730));
			n.getCombatBuilder().attack(player);
			hitted.add(n);
			chainedDamage = chainedDamage / 2;
			position = n.getPosition();
		} else {
			this.cancel();
		}
	}
}
