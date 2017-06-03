package net.edge.world.node.entity.player;

import io.netty.buffer.ByteBufAllocator;
import net.edge.net.codec.ByteMessage;
import net.edge.net.codec.MessageType;
import net.edge.locale.Position;
import net.edge.world.World;
import net.edge.world.node.NodeState;
import net.edge.world.Direction;
import net.edge.world.node.entity.update.UpdateBlockSet;
import net.edge.world.node.entity.update.UpdateState;

import java.util.Iterator;

/**
 * An implementation that sends an update message containing the underlying {@link Player} and other
 * {@code Player}s surrounding them.
 * @author Artem Batutin <artembatutin@gmail.com>
 * @author lare96 <http://github.org/lare96>
 */
public final class PlayerUpdater {
	
	/**
	 * The set of {@link UpdateBlockSet} that will manage all of the updates.
	 */
	private final UpdateBlockSet<Player> blockSet = UpdateBlockSet.PLAYER_BLOCK_SET;
	
	public ByteMessage write(Player player) {
		ByteBufAllocator alloc = player.getSession().alloc();
		ByteMessage msg = ByteMessage.message(alloc, 81, MessageType.VARIABLE_SHORT);
		ByteMessage blockMsg = ByteMessage.message(alloc);
		
		try {
			msg.startBitAccess();
			
			handleMovement(player, msg);
			blockSet.encodeUpdateBlocks(player, player, blockMsg, UpdateState.UPDATE_SELF);
			
			msg.putBits(8, player.getLocalPlayers().size());
			Iterator<Player> $it = player.getLocalPlayers().iterator();
			while($it.hasNext()) {
				Player other = $it.next();
				if(other.isVisible() && other.getInstance() == player.getInstance() && other.getPosition().isViewableFrom(player.getPosition()) && other.getState() == NodeState.ACTIVE && !other.isNeedsPlacement()) {
					handleMovement(other, msg);
					blockSet.encodeUpdateBlocks(player, other, blockMsg, UpdateState.UPDATE_LOCAL);
				} else {
					msg.putBit(true);
					msg.putBits(2, 3);
					$it.remove();
				}
			}
			
			int added = 0;
			for(Player other : World.getRegions().getPriorityPlayers(player)) {
				if(added == 15 || player.getLocalPlayers().size() >= 255)
					break;
				if(other == null || other.equals(player))
					continue;
				if(other.getState() != NodeState.ACTIVE)
					continue;
				if(other.getInstance() != player.getInstance())
					continue;
				if(other.isVisible() && other.getPosition().isViewableFrom(player.getPosition())) {
					if(player.getLocalPlayers().add(other)) {
						added++;
						addPlayer(msg, player, other);
						blockSet.encodeUpdateBlocks(player, other, blockMsg, UpdateState.ADD_LOCAL);
					}
				}
			}
			
			if(blockMsg.getBuffer().writerIndex() > 0) {
				msg.putBits(11, 2047);
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
	 * Adds {@code addPlayer} in the view of {@code player}.
	 * @param msg       The main update message.
	 * @param player    The {@link Player} this update message is being sent for.
	 * @param addPlayer The {@code Player} being added.
	 */
	private void addPlayer(ByteMessage msg, Player player, Player addPlayer) {
		msg.putBits(11, addPlayer.getSlot());
		msg.putBit(true);
		msg.putBit(true);
		
		int deltaX = addPlayer.getPosition().getX() - player.getPosition().getX();
		int deltaY = addPlayer.getPosition().getY() - player.getPosition().getY();
		msg.putBits(5, deltaY);
		msg.putBits(5, deltaX);
	}
	
	/**
	 * Handles running, walking, and teleportation movement for {@code player}.
	 * @param player The {@link Player} to handle running and walking for.
	 * @param msg    The main update message.
	 */
	private void handleMovement(Player player, ByteMessage msg) {
		boolean needsUpdate = !player.getFlags().isEmpty();
		if(player.isNeedsPlacement()) {
			Position position = player.getPosition();
			msg.putBit(true);
			msg.putBits(2, 3);
			msg.putBits(2, position.getZ());
			msg.putBit(player.isTeleporting());
			msg.putBit(needsUpdate);
			msg.putBits(7, position.getLocalY(player.getLastRegion()));
			msg.putBits(7, position.getLocalX(player.getLastRegion()));
			return;
		}
		
		Direction walkingDirection = player.getPrimaryDirection();
		Direction runningDirection = player.getSecondaryDirection();
		if(walkingDirection != Direction.NONE) {
			msg.putBit(true);
			if(runningDirection != Direction.NONE) {
				msg.putBits(2, 2);
				msg.putBits(3, walkingDirection.getId());
				msg.putBits(3, runningDirection.getId());
				msg.putBit(needsUpdate);
			} else {
				msg.putBits(2, 1);
				msg.putBits(3, walkingDirection.getId());
				msg.putBit(needsUpdate);
			}
		} else {
			if(needsUpdate) {
				msg.putBit(true);
				msg.putBits(2, 0);
			} else {
				msg.putBit(false);
			}
		}
	}
}