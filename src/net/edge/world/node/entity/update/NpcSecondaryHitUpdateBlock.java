package net.edge.world.node.entity.update;

import net.edge.net.codec.ByteMessage;
import net.edge.world.Hit;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;

/**
 * An {@link NpcUpdateBlock} implementation that handles the {@code SECONDARY_HIT} update block.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class NpcSecondaryHitUpdateBlock extends NpcUpdateBlock {

	/**
	 * Creates a new {@link NpcSecondaryHitUpdateBlock}.
	 */
	public NpcSecondaryHitUpdateBlock() {
		super(0x20, UpdateFlag.SECONDARY_HIT);
	}

	@Override
	public int write(Player player, Npc npc, ByteMessage msg) {
		Hit hit = npc.getSecondaryHit();
		msg.putShort(hit.getDamage());
		msg.put(hit.getType().getId() + (hit.hasSource() && hit.getSource() != player.getSlot() ? 5 : 0));
		msg.put(hit.getIcon().getId());
		msg.putShort((int) Math.round((((double) npc.getCurrentHealth()) / ((double) npc.getMaxHealth())) * 100));
		msg.put(npc.getSpecial().isPresent() ? npc.getSpecial().getAsInt() : 101);
		return -1;
	}
}
