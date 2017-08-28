package net.edge.world.entity.actor.update;

import net.edge.content.combat.hit.Hit;
import net.edge.net.codec.GameBuffer;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

/**
 * An {@link MobUpdateBlock} implementation that handles the {@code SECONDARY_HIT} update block.
 *
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class MobSecondaryHitUpdateBlock extends MobUpdateBlock {

	/**
	 * Creates a new {@link MobSecondaryHitUpdateBlock}.
	 */
	public MobSecondaryHitUpdateBlock() {
		super(0x20, UpdateFlag.SECONDARY_HIT);
	}

	@Override
	public int write(Player player, Mob mob, GameBuffer msg) {
		Hit hit = mob.getSecondaryHit();
//		System.out.println("Second hit: " + hit.getDamage());
		msg.putShort(hit.getDamage());
		msg.put(hit.getHitsplat().getId()); // TODO: add (hit.hasSource() && hit.getSource() != player.getSlot() ? 5 : 0)
		msg.put(hit.getHitIcon().getId());
		msg.putShort((int) (mob.getCurrentHealth() * 100.0 / mob.getMaxHealth()));
		msg.put(mob.getSpecial().isPresent() ? mob.getSpecial().getAsInt() : 101);
		return -1;
	}
}