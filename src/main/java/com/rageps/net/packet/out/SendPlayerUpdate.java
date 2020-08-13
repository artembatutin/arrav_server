package com.rageps.net.packet.out;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.objects.ObjectList;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.model.Direction;
import com.rageps.world.entity.EntityState;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.update.UpdateManager;
import com.rageps.world.entity.actor.update.UpdateState;
import com.rageps.world.entity.region.Region;
import com.rageps.world.locale.Position;

import java.util.Iterator;

/**
 * An implementation that sends an update message containing the underlying {@link Player} and other {@code Player}s surrounding them.
 * @author Artem Batutin
 */
public final class SendPlayerUpdate implements OutgoingPacket {
	
	@Override
	public boolean onSent(Player player) {
		return true;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		//writing the update block.
		GamePacket out = new GamePacket(this, buf);
		out.message(81, GamePacketType.VARIABLE_SHORT);
		GamePacket blockMsg = new GamePacket(-1, Unpooled.buffer(64), GamePacketType.RAW);
		try {
			out.startBitAccess();
			handleMovement(player, out);
			UpdateManager.encode(player, player, blockMsg, UpdateState.UPDATE_SELF);
			
			out.putBits(8, player.getLocalPlayers().size());
			Iterator<Player> $it = player.getLocalPlayers().iterator();
			while($it.hasNext()) {
				Player other = $it.next();
				if(other.isVisible() && other.getInstance() == player.getInstance() && other.getPosition().isViewableFrom(player.getPosition()) && other.getState() == EntityState.ACTIVE && !other.isNeedsPlacement()) {
					handleMovement(other, out);
					UpdateManager.encode(player, other, blockMsg, UpdateState.UPDATE_LOCAL);
				} else {
					out.putBit(true);
					out.putBits(2, 3);
					$it.remove();
				}
			}
			
			int added = 0;
			Region r = player.getRegion();
			if(r != null) {
				processPlayers(r, player, blockMsg, out, added);
				ObjectList<Region> surrounding = r.getSurroundingRegions();
				if(surrounding != null) {
					for(Region s : surrounding) {
						processPlayers(s, player, blockMsg, out, added);
					}
				}
			}
			
			if(blockMsg.getPayload().writerIndex() > 0) {
				out.putBits(11, 2047);
				out.endBitAccess();
				out.putBytes(blockMsg);
			} else {
				out.endBitAccess();
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			blockMsg.getPayload().release();
		}
		out.endVarSize();
		return out;
	}
	
	/**
	 * Processing the addition of player from a region.
	 */
	private void processPlayers(Region region, Player player, GamePacket blockMsg, GamePacket msg, int added) {
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
	 * @param msg The main update message.
	 * @param player The {@link Player} this update message is being sent for.
	 * @param addPlayer The {@code Player} being added.
	 */
	private void addPlayer(GamePacket msg, Player player, Player addPlayer) {
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
	 * @param msg The main update message.
	 */
	private void handleMovement(Player player, GamePacket msg) {
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