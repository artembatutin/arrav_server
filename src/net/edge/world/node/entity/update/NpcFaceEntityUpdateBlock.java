package net.edge.world.node.entity.update;

import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.IncomingMsg;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;

/**
 * An {@link NpcUpdateBlock} implementation that handles the {@code FACE_ENTITY} update block.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class NpcFaceEntityUpdateBlock extends NpcUpdateBlock {
	
	/**
	 * Creates a new {@link NpcFaceEntityUpdateBlock}.
	 */
	public NpcFaceEntityUpdateBlock() {
		super(0x10, UpdateFlag.FACE_ENTITY);
	}
	
	@Override
	public int write(Player player, Npc mob, GameBuffer msg) {
		msg.putShort(mob.getFaceIndex());
		return -1;
	}
}
