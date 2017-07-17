package net.edge.world.node.actor.update;

import net.edge.net.codec.GameBuffer;
import net.edge.world.node.actor.npc.Npc;
import net.edge.world.node.actor.player.Player;

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