package net.edge.world.node.entity.update;

import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.IncomingMsg;
import net.edge.world.Hit;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;

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
	public int write(Player player, Npc npc, GameBuffer msg) {
		Hit hit = npc.getPrimaryHit();
		msg.putShort(hit.getDamage());
		msg.put(hit.getType().getId() + (hit.hasSource() && hit.getSource() != player.getSlot() ? 5 : 0));
		msg.put(hit.getIcon().getId());
		msg.putShort((int) Math.round((((double) npc.getCurrentHealth()) / ((double) npc.getMaxHealth())) * 100));
		msg.put(npc.getSpecial().isPresent() ? npc.getSpecial().getAsInt() : 101);
		return -1;
	}
}