package net.edge.world.node.entity.npc;

import io.netty.buffer.ByteBufAllocator;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.MessageType;
import net.edge.world.World;
import net.edge.world.node.NodeState;
import net.edge.world.Direction;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.update.UpdateBlockSet;
import net.edge.world.node.entity.update.UpdateState;
import net.edge.world.node.region.Region;
import net.edge.world.node.region.RegionManager;

import java.util.Iterator;

/**
 * An implementation that sends an update message containing the underlying {@link Player} and {@link Npc}s surrounding them.
 * @author Artem Batutin <artembatutin@gmail.com>
 * @author lare96 <http://github.org/lare96>
 */
public final class NpcUpdater {
	public static void write(Player player) {
		UpdateBlockSet<Npc> blockSet = UpdateBlockSet.NPC_BLOCK_SET;

		ByteBufAllocator alloc = player.getSession().alloc();
		GameBuffer msg = player.getSession().getStream();
		msg.message(65, MessageType.VARIABLE_SHORT);
		GameBuffer blockMsg = new GameBuffer(alloc.buffer(64));
		try {
			msg.startBitAccess();
			msg.putBits(8, player.getLocalNpcs().size());
			Iterator<Npc> $it = player.getLocalNpcs().iterator();
			while($it.hasNext()) {
				Npc npc = $it.next();
				if(npc.getState() == NodeState.ACTIVE && npc.isVisible() && player.getInstance() == npc.getInstance() && npc.getPosition().isViewableFrom(player.getPosition()) && !npc.isNeedsPlacement()) {
					handleMovement(npc, msg);
					blockSet.encodeUpdateBlocks(player, npc, blockMsg, UpdateState.UPDATE_LOCAL);
				} else {
					msg.putBit(true);
					msg.putBits(2, 3);
					$it.remove();
				}
			}
			
			int added = 0;
			int x = player.getPosition().getX() & 63;
			int y = player.getPosition().getY() & 63;
			int regionId = player.getPosition().getRegion();
			RegionManager m = World.getRegions();
			processNpcs(m.getRegion(regionId), player, blockMsg, msg, blockSet, added);
			if(y > 48) {
				//top part of region.
				if(m.exists(regionId + 1))
					processNpcs(m.getRegion(regionId + 1), player, blockMsg, msg, blockSet, added);
				if(x > 48) {
					//top-right of region.
					if(m.exists(regionId + 256))
						processNpcs(m.getRegion(regionId + 256), player, blockMsg, msg, blockSet, added);
					if(m.exists(regionId + 257))
						processNpcs(m.getRegion(regionId + 257), player, blockMsg, msg, blockSet, added);
				} else if(x < 16) {
					//top-left of region.
					if(m.exists(regionId - 256))
						processNpcs(m.getRegion(regionId - 256), player, blockMsg, msg, blockSet, added);
					if(m.exists(regionId - 255))
						processNpcs(m.getRegion(regionId - 255), player, blockMsg, msg, blockSet, added);
				}
			} else if(y < 16) {
				//bottom part of region.
				if(m.exists(regionId - 1))
					processNpcs(m.getRegion(regionId - 1), player, blockMsg, msg, blockSet, added);
				if(x > 48) {
					//bottom-right of region.
					if(m.exists(regionId + 256))
						processNpcs(m.getRegion(regionId + 256), player, blockMsg, msg, blockSet, added);
					if(m.exists(regionId + 255))
						processNpcs(m.getRegion(regionId + 255), player, blockMsg, msg, blockSet, added);
				} else if(x < 16) {
					//bottom-left of region.
					if(m.exists(regionId - 256))
						processNpcs(m.getRegion(regionId - 256), player, blockMsg, msg, blockSet, added);
					if(m.exists(regionId - 257))
						processNpcs(m.getRegion(regionId - 257), player, blockMsg, msg, blockSet, added);
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

		msg.endVarSize();
	}
	
	/**
	 * Processing the addition of npc from a region.
	 */
	private static void processNpcs(Region region, Player player, GameBuffer blockMsg, GameBuffer msg, UpdateBlockSet<Npc> blockSet, int added) {
		if(!region.getNpcs().isEmpty()) {
			for(Npc npc : region.getNpcs()) {
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
		}
	}
	
	/**
	 * Adds {@code addNpc} in the view of {@code player}.
	 * @param msg    The main update message.
	 * @param player The {@link Player} this update message is being sent for.
	 * @param addNpc The {@link Npc} being added.
	 */
	private static void addNpc(Player player, Npc addNpc, GameBuffer msg) {
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
	private static void handleMovement(Npc npc, GameBuffer msg) {
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