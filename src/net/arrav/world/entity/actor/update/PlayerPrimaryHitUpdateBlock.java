package net.arrav.world.entity.actor.update;

import io.netty.buffer.ByteBuf;
import net.arrav.world.entity.actor.combat.hit.Hit;
import net.arrav.world.entity.actor.combat.hit.Hitsplat;
import net.arrav.world.entity.actor.player.Player;

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
	public int write(Player player, Player other, ByteBuf buf) {
		Hit hit = other.getPrimaryHit();
		boolean local = other == player || (hit.hasSource() && hit.getSource() == player.getSlot());
		buf.putShort(hit.getDamage());
		buf.put(hit.getHitsplat().getId() + (!local ? (hit.getHitsplat() != Hitsplat.NORMAL_LOCAL ? 5 : 0) : 0));
		int change = buf.writerIndex() - 1;
		buf.put(hit.getHitIcon().getId());
		buf.putShort(hit.getSoak());
		buf.putShort(other.getMaximumHealth() / 10);
		buf.putShort(other.getCurrentHealth() / 10);
		if(local) {
			return change;
		}
		return -1;
	}
}
