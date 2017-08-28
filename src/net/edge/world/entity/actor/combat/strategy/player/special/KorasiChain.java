package net.edge.world.entity.actor.combat.strategy.player.special;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.task.Task;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.combat.hit.HitIcon;
import net.edge.world.entity.actor.combat.hit.Hitsplat;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.Position;

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
	private ObjectList<Actor> hitted = new ObjectArrayList<>();

	/**
	 * The chained damage in the korasi hits.
	 */
	private int chainedDamage;

	/**
	 * the first hit position.
	 */
	private Position position;

	KorasiChain(Player player, Actor victim, int damage) {
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
		Optional<Mob> npc = player.getLocalMobs().stream().filter(n -> n.getPosition().withinDistance(position, 1) && !hitted.contains(n)).findAny();
		if(npc.isPresent()) {
			Mob n = npc.get();
			n.damage(new Hit(chainedDamage / 2, Hitsplat.NORMAL, HitIcon.MAGIC));
			n.animation(new Animation(n.getDefinition().getDefenceAnimation()));
			n.graphic(new Graphic(1730));
			n.getCombat().attack(player);
			hitted.add(n);
			chainedDamage = chainedDamage / 2;
			position = n.getPosition();
		} else {
			this.cancel();
		}
	}
}
