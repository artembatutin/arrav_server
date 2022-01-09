package com.rageps.combat.strategy.player.special.impl;

import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.locale.Position;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import com.rageps.task.Task;
import com.rageps.world.model.Animation;
import com.rageps.world.model.Graphic;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.combat.hit.HitIcon;
import com.rageps.world.entity.actor.combat.hit.Hitsplat;
import com.rageps.world.entity.actor.mob.Mob;

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
