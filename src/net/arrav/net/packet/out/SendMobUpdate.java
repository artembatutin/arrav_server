package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.Direction;
import net.arrav.world.entity.EntityState;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.update.UpdateManager;
import net.arrav.world.entity.actor.update.UpdateState;
import net.arrav.world.entity.region.Region;

import java.util.Iterator;

/**
 * An implementation that sends an update message containing the underlying {@link Player} and {@link Mob}s surrounding them.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class SendMobUpdate implements OutgoingPacket {
	
	public ByteBuf write(Player player, ByteBuf buf) {
		ByteBufAllocator alloc = player.getSession().alloc();
		buf.message(65, GamePacketType.VARIABLE_SHORT);
		ByteBuf blockMsg = alloc.buffer(64);
		try {
			buf.startBitAccess();
			buf.putBits(8, player.getLocalMobs().size());
			Iterator<Mob> $it = player.getLocalMobs().iterator();
			while($it.hasNext()) {
				Mob mob = $it.next();
				if(mob.getState() == EntityState.ACTIVE && mob.isVisible() && player.getInstance() == mob.getInstance() && mob.getPosition().isViewableFrom(player.getPosition()) && !mob.isNeedsPlacement()) {
					handleMovement(mob, buf);
					UpdateManager.encode(player, mob, blockMsg, UpdateState.UPDATE_LOCAL);
				} else {
					buf.putBit(true);
					buf.putBits(2, 3);
					$it.remove();
				}
			}
			
			int added = 0;
			player.getRegion().ifPresent(r -> {
				processMobs(r, player, blockMsg, buf, added);
				ObjectList<Region> surrounding = r.getSurroundingRegions();
				if(surrounding != null) {
					for(Region s : surrounding) {
						processMobs(s, player, blockMsg, buf, added);
					}
				}
			});
			
			if(blockMsg.writerIndex() > 0) {
				buf.putBits(14, 16383);
				buf.endBitAccess();
				buf.putBytes(blockMsg);
			} else {
				buf.endBitAccess();
			}
		} catch(Exception e) {
			buf.release();
			throw e;
		} finally {
			blockMsg.release();
		}
		buf.endVarSize();
		return buf;
	}
	
	/**
	 * Processing the addition of npc from a region.
	 */
	private void processMobs(Region region, Player player, ByteBuf blockMsg, ByteBuf msg, int added) {
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
	private void addNpc(Player player, Mob addMob, ByteBuf msg) {
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
	private void handleMovement(Mob mob, ByteBuf msg) {
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