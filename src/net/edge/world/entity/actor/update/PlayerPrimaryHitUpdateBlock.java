package net.edge.world.entity.actor.update;

import net.edge.net.codec.GameBuffer;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.combat.hit.Hitsplat;
import net.edge.world.entity.actor.player.Player;

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
	public int write(Player player, Player other, GameBuffer msg) {
		Hit hit = other.getPrimaryHit();
		boolean local = other == player || (hit.hasSource() && hit.getSource() == player.getSlot());
		msg.putShort(hit.getDamage());
		msg.put(hit.getHitsplat().getId() + (!local ? (hit.getHitsplat() != Hitsplat.NORMAL_LOCAL ? 5 : 0) : 0));
		int change = msg.getBuffer().writerIndex() - 1;
		msg.put(hit.getHitIcon().getId());
		msg.putShort(hit.getSoak());
		msg.putShort(other.getMaximumHealth() / 10);
		msg.putShort(other.getCurrentHealth() / 10);
		if(local) {
			return change;
		}
		return -1;
	}
}
