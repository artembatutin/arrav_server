package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.net.codec.game.GamePacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.Direction;
import net.edge.world.entity.EntityState;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.update.UpdateManager;
import net.edge.world.entity.actor.update.UpdateState;
import net.edge.world.entity.region.Region;
import net.edge.world.locale.Position;

import java.util.Iterator;

/**
 * An implementation that sends an update message containing the underlying {@link Player} and other {@code Player}s surrounding them.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class SendPlayerUpdate implements OutgoingPacket {
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		ByteBufAllocator alloc = player.getSession().alloc();
		buf.message(81, GamePacketType.VARIABLE_SHORT);
		ByteBuf blockMsg = alloc.buffer(64);
		try {
			buf.startBitAccess();
			handleMovement(player, buf);
			UpdateManager.encode(player, player, blockMsg, UpdateState.UPDATE_SELF);
			
			buf.putBits(8, player.getLocalPlayers().size());
			Iterator<Player> $it = player.getLocalPlayers().iterator();
			while($it.hasNext()) {
				Player other = $it.next();
				if(other.isVisible() && other.getInstance() == player.getInstance() && other.getPosition().isViewableFrom(player.getPosition()) && other.getState() == EntityState.ACTIVE && !other.isNeedsPlacement()) {
					handleMovement(other, buf);
					UpdateManager.encode(player, other, blockMsg, UpdateState.UPDATE_LOCAL);
				} else {
					buf.putBit(true);
					buf.putBits(2, 3);
					$it.remove();
				}
			}
			
			int added = 0;
			player.getRegion().ifPresent(r -> {
				processPlayers(r, player, blockMsg, buf, added);
				ObjectList<Region> surrounding = r.getSurroundingRegions();
				if(surrounding != null) {
					for(Region s : surrounding) {
						processPlayers(s, player, blockMsg, buf, added);
					}
				}
			});
			
			if(blockMsg.writerIndex() > 0) {
				buf.putBits(11, 2047);
				buf.endBitAccess();
				buf.putBytes(blockMsg);
			} else {
				buf.endBitAccess();
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			blockMsg.release();
		}
		buf.endVarSize();
		return buf;
	}
	
	/**
	 * Processing the addition of player from a region.
	 */
	private void processPlayers(Region region, Player player, ByteBuf blockMsg, ByteBuf msg, int added) {
		if(region != null) {
			if(!region.getPlayers().isEmpty()) {
				for(Player other : region.getPlayers()) {
					if(added == 15 || player.getLocalPlayers().size() >= 255)
						break;
					if(other == null || other.same(player))
						continue;
					if(other.getState() != EntityState.ACTIVE)
						continue;
					if(other.getInstance() != player.getInstance())
						continue;
					if(other.isVisible() && other.getPosition().isViewableFrom(player.getPosition())) {
						if(player.getLocalPlayers().add(other)) {
							added++;
							addPlayer(msg, player, other);
							UpdateManager.encode(player, other, blockMsg, UpdateState.ADD_LOCAL);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Adds {@code addPlayer} in the view of {@code player}.
	 * @param msg       The main update message.
	 * @param player    The {@link Player} this update message is being sent for.
	 * @param addPlayer The {@code Player} being added.
	 */
	private void addPlayer(ByteBuf msg, Player player, Player addPlayer) {
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
	private void handleMovement(Player player, ByteBuf msg) {
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