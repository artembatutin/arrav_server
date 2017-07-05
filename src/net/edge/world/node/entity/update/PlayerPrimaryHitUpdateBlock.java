package net.edge.world.node.entity.update;

import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.IncomingMsg;
import net.edge.world.Hit;
import net.edge.world.node.entity.player.Player;

/**
 * An {@link PlayerUpdateBlock} implementation that handles the primary {@link Hit} update block.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class PlayerPrimaryHitUpdateBlock extends PlayerUpdateBlock {
	
	/**
	 * Creates a new {@link PlayerPrimaryHitUpdateBlock}.
	 */
	public PlayerPrimaryHitUpdateBlock() {
		super(0x20, UpdateFlag.PRIMARY_HIT);
	}
	
	@Override
	public int write(Player player, Player mob, GameBuffer msg) {
		Hit hit = mob.getPrimaryHit();
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
