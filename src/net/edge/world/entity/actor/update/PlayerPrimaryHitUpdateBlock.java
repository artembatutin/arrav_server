package net.edge.world.entity.actor.update;

import net.edge.content.combat.hit.Hit;
import net.edge.net.codec.GameBuffer;
import net.edge.world.entity.actor.player.Player;

/**
 * An {@link PlayerUpdateBlock} implementation that handles the primary {@link Hit} update block.
 *
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
		boolean local = other == player; // TODO: add local hits || (hit.hasSource() && hit.getSource() == player.getSlot());
		msg.putShort(hit.getDamage());
		msg.put(hit.getHitsplat().getId() + (!local ? 5 : 0));
		int change = msg.getBuffer().writerIndex() - 1;
		msg.put(hit.getHitIcon().getId());
		msg.putShort(0/*hit.getSoak()*/); // TODO: Soak
		msg.putShort(other.getMaximumHealth() / 10);
		msg.putShort(other.getCurrentHealth() / 10);
		if(local) {
			return change;
		}//dude, the other player hit queue is 0 so it returns null is how it looks like
		//and here its the other player. nop
		return -1;
	}
}
