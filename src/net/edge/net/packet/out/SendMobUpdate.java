package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.PacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.Direction;
import net.edge.world.entity.EntityState;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.update.UpdateManager;
import net.edge.world.entity.actor.update.UpdateState;
import net.edge.world.entity.region.Region;

import java.util.Iterator;

/**
 * An implementation that sends an update message containing the underlying {@link Player} and {@link Mob}s surrounding them.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class SendMobUpdate implements OutgoingPacket {
	
	public ByteBuf write(Player player, GameBuffer msg) {
		ByteBufAllocator alloc = player.getSession().alloc();
		msg.message(65, PacketType.VARIABLE_SHORT);
		GameBuffer blockMsg = new GameBuffer(alloc.buffer(64));
		try {
			msg.startBitAccess();
			msg.putBits(8, player.getLocalMobs().size());
			Iterator<Mob> $it = player.getLocalMobs().iterator();
			while($it.hasNext()) {
				Mob mob = $it.next();
				if(mob.getState() == EntityState.ACTIVE && mob.isVisible() && player.getInstance() == mob.getInstance() && mob.getPosition().isViewableFrom(player.getPosition()) && !mob.isNeedsPlacement()) {
					handleMovement(mob, msg);
					UpdateManager.encode(player, mob, blockMsg, UpdateState.UPDATE_LOCAL);
				} else {
					msg.putBit(true);
					msg.putBits(2, 3);
					$it.remove();
				}
			}
			
			int added = 0;
			player.getRegion().ifPresent(r -> {
				processMobs(r, player, blockMsg, msg, added);
				ObjectList<Region> surrounding = r.getSurroundingRegions();
				if(surrounding != null) {
					for(Region s : surrounding) {
						processMobs(s, player, blockMsg, msg, added);
					}
				}
			});
			
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
		return msg.getBuffer();
	}
	
	/**
	 * Processing the addition of npc from a region.
	 */
	private void processMobs(Region region, Player player, GameBuffer blockMsg, GameBuffer msg, int added) {
		if(region != null) {
			if(!region.getMobs().isEmpty()) {
				for(Mob mob : region.getMobs()) {
					if(added == 15 || player.getLocalMobs().size() >= 255)
						break;
					if(mob == null)
						continue;
					if(mob.getState() != EntityState.ACTIVE)
						continue;
					if(mob.getInstance() != player.getInstance())
						continue;
					if(mob.isVisible() && mob.getPosition().isViewableFrom(player.getPosition())) {
						if(player.getLocalMobs().add(mob)) {
							addNpc(player, mob, msg);
							UpdateManager.encode(player, mob, blockMsg, UpdateState.ADD_LOCAL);
							added++;
						}
					}
				}
			}
		}
	}
	
	/**
	 * Adds {@code addMob} in the view of {@code player}.
	 * @param msg    The main update message.
	 * @param player The {@link Player} this update message is being sent for.
	 * @param addMob The {@link Mob} being added.
	 */
	private void addNpc(Player player, Mob addMob, GameBuffer msg) {
		boolean updateRequired = !addMob.getFlags().isEmpty();
		int deltaX = addMob.getPosition().getX() - player.getPosition().getX();
		int deltaY = addMob.getPosition().getY() - player.getPosition().getY();
		
		msg.putBits(14, addMob.getSlot());
		msg.putBits(5, deltaY);
		msg.putBits(5, deltaX);
		msg.putBit(updateRequired);
		msg.putBits(16, addMob.getId());
		msg.putBit(true);
	}
	
	/**
	 * Handles walking movement for {@code mob}.
	 * @param mob The {@link Player} to handle running and walking for.
	 * @param msg The main update message.
	 */
	private void handleMovement(Mob mob, GameBuffer msg) {
		boolean updateRequired = !mob.getFlags().isEmpty();
		if(mob.getPrimaryDirection() == Direction.NONE) {
			if(updateRequired) {
				msg.putBit(true);
				msg.putBits(2, 0);
			} else {
				msg.putBit(false);
			}
		} else {
			msg.putBit(true);
			msg.putBits(2, 1);
			msg.putBits(3, mob.getPrimaryDirection().getId());
			msg.putBit(updateRequired);
		}
	}
}