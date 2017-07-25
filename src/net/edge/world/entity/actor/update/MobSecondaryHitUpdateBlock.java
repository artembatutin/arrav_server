package net.edge.world.entity.actor.update;

import net.edge.net.codec.GameBuffer;
import net.edge.world.Hit;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

/**
 * An {@link MobUpdateBlock} implementation that handles the {@code SECONDARY_HIT} update block.
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
		msg.putShort(hit.getDamage());
		msg.put(hit.getType().getId() + (hit.hasSource() && hit.getSource() != player.getSlot() ? 5 : 0));
		msg.put(hit.getIcon().getId());
		msg.putShort((int) Math.round((((double) mob.getCurrentHealth()) / ((double) mob.getMaxHealth())) * 100));
		msg.put(mob.getSpecial().isPresent() ? mob.getSpecial().getAsInt() : 101);
		return -1;
	}
}