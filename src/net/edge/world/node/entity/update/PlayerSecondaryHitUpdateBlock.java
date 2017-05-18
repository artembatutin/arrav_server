package net.edge.world.node.entity.update;

import net.edge.net.codec.ByteMessage;
import net.edge.world.node.entity.model.Hit;
import net.edge.world.node.entity.player.Player;

/**
 * An {@link PlayerUpdateBlock} implementation that handles the secondary {@link Hit} update block.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class PlayerSecondaryHitUpdateBlock extends PlayerUpdateBlock {

	/**
	 * Creates a new {@link PlayerSecondaryHitUpdateBlock}.
	 */
	public PlayerSecondaryHitUpdateBlock() {
		super(0x200, UpdateFlag.SECONDARY_HIT);
	}

	@Override
	public int write(Player player, Player mob, ByteMessage msg) {
		Hit hit = mob.getSecondaryHit();
		boolean local = mob == player || (hit.hasSource() && hit.getSource() == player.getSlot());
		msg.putShort(hit.getDamage());
		msg.put(hit.getType().getId() + (!local ? 5 : 0));
		int change = msg.getBuffer().writerIndex() - 1;
		msg.put(hit.getIcon().getId());
		msg.putShort(hit.getSoak());
		msg.putShort(mob.getMaximumHealth() / 10);
		msg.putShort(mob.getCurrentHealth() / 10);
		if(local) {
			return change;
		}
		return -1;
	}
}
