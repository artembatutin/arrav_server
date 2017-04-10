package net.edge.world.model.node.entity.update;

import net.edge.net.codec.ByteMessage;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.entity.model.Hit;
import net.edge.world.model.node.entity.npc.Npc;

/**
 * An {@link NpcUpdateBlock} implementation that handles the {@code PRIMARY_HIT} update block.
 * @author lare96 <http://github.org/lare96>
 */
public final class NpcPrimaryHitUpdateBlock extends NpcUpdateBlock {

	/**
	 * Creates a new {@link NpcPrimaryHitUpdateBlock}.
	 */
	public NpcPrimaryHitUpdateBlock() {
		super(2, UpdateFlag.PRIMARY_HIT);
	}

	@Override
	public int write(Player player, Npc npc, ByteMessage msg) {
		Hit hit = npc.getPrimaryHit();
		msg.putShort(hit.getDamage());
		msg.put(hit.getType().getId() + (hit.hasSource() && hit.getSource() != player.getSlot() ? 5 : 0));
		msg.put(hit.getIcon().getId());
		msg.putShort((int) Math.round((((double) npc.getCurrentHealth()) / ((double) npc.getMaxHealth())) * 100));
		msg.put(npc.getSpecial().isPresent() ? npc.getSpecial().getAsInt() : 101);
		return -1;
	}
}