package net.edge.world.model.node.entity.npc;

import net.edge.net.codec.ByteMessage;
import net.edge.net.codec.MessageType;
import net.edge.world.World;
import net.edge.world.model.node.NodeState;
import net.edge.world.model.node.entity.model.Direction;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.entity.update.UpdateBlock;
import net.edge.world.model.node.entity.update.UpdateBlockSet;
import net.edge.world.model.node.entity.update.UpdateState;

import java.util.Iterator;

/**
 * An implementation that sends an update message containing the underlying {@link Player} and {@link Npc}s surrounding them.
 * @author Artem Batutin <artembatutin@gmail.com>
 * @author lare96 <http://github.org/lare96>
 */
public final class NpcUpdater {
	
	/**
	 * The {@link UpdateBlockSet} that will manage all of the {@link UpdateBlock}s.
	 */
	private final UpdateBlockSet<Npc> blockSet = UpdateBlockSet.NPC_BLOCK_SET;
	
	public ByteMessage write(Player player) {
		ByteMessage msg = ByteMessage.message(65, MessageType.VARIABLE_SHORT);
		ByteMessage blockMsg = ByteMessage.message();
		
		try {
			msg.startBitAccess();
			msg.putBits(8, player.getLocalNpcs().size());
			Iterator<Npc> $it = player.getLocalNpcs().iterator();
			while($it.hasNext()) {
				Npc npc = $it.next();
				if(npc.getState() == NodeState.ACTIVE && npc.isVisible() && player.getInstance() == npc.getInstance() && npc.getPosition().isViewableFrom(player.getPosition())) {
					handleMovement(npc, msg);
					blockSet.encodeUpdateBlocks(player, npc, blockMsg, UpdateState.UPDATE_LOCAL);
				} else {
					msg.putBit(true);
					msg.putBits(2, 3);
					$it.remove();
				}
			}
			int added = 0;
			for(Npc npc : World.getRegions().getPriorityNpcs(player)) {
				if(added == 15 || player.getLocalNpcs().size() >= 255)
					break;
				if(npc == null)
					continue;
				if(npc.getState() != NodeState.ACTIVE)
					continue;
				if(npc.getInstance() != player.getInstance())
					continue;
				if(npc.isVisible() && npc.getPosition().isViewableFrom(player.getPosition())) {
					if(player.getLocalNpcs().add(npc)) {
						addNpc(player, npc, msg);
						blockSet.encodeUpdateBlocks(player, npc, blockMsg, UpdateState.ADD_LOCAL);
						added++;
					}
				}
			}
			
			if(blockMsg.getBuffer().writerIndex() > 0) {
				msg.putBits(14, 16383);
				msg.endBitAccess();
				msg.putBytes(blockMsg);
			} else {
				msg.endBitAccess();
			}
		} catch(Exception e) {
			msg.release();
			throw e;
		} finally {
			blockMsg.release();
		}
		return msg;
	}
	
	/**
	 * Adds {@code addNpc} in the view of {@code player}.
	 * @param msg    The main update message.
	 * @param player The {@link Player} this update message is being sent for.
	 * @param addNpc The {@link Npc} being added.
	 */
	private void addNpc(Player player, Npc addNpc, ByteMessage msg) {
		boolean updateRequired = !addNpc.getFlags().isEmpty();
		int deltaX = addNpc.getPosition().getX() - player.getPosition().getX();
		int deltaY = addNpc.getPosition().getY() - player.getPosition().getY();
		msg.putBits(14, addNpc.getSlot());
		msg.putBits(5, deltaY);
		msg.putBits(5, deltaX);
		msg.putBit(updateRequired);
		msg.putBits(16, addNpc.getId());
		msg.putBit(true);
	}
	
	/**
	 * Handles walking movement for {@code npc}.
	 * @param npc The {@link Player} to handle running and walking for.
	 * @param msg The main update message.
	 */
	private void handleMovement(Npc npc, ByteMessage msg) {
		boolean updateRequired = !npc.getFlags().isEmpty();
		if(npc.getPrimaryDirection() == Direction.NONE) {
			if(updateRequired) {
				msg.putBit(true);
				msg.putBits(2, 0);
			} else {
				msg.putBit(false);
			}
		} else {
			msg.putBit(true);
			msg.putBits(2, 1);
			msg.putBits(3, npc.getPrimaryDirection().getId());
			msg.putBit(updateRequired);
		}
	}
}