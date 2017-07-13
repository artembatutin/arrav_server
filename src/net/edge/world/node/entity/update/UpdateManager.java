package net.edge.world.node.entity.update;

import io.netty.buffer.Unpooled;
import net.edge.locale.Position;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.ByteOrder;
import net.edge.world.Hit;
import net.edge.world.node.entity.move.ForcedMovement;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.PlayerAppearance;
import net.edge.world.node.item.container.impl.Equipment;

import static net.edge.world.node.entity.player.assets.Rights.IRON_MAN;
import static net.edge.world.node.entity.player.assets.Rights.PLAYER;

public final class UpdateManager {
	
	private static final PlayerUpdateBlock[] PLAYER_BLOCKS = {
			new PlayerForceMovementUpdateBlock(),
			new PlayerGraphicUpdateBlock(),
			new PlayerAnimationUpdateBlock(),
			new PlayerForceChatUpdateBlock(),
			new PlayerChatUpdateBlock(),
			new PlayerFaceEntityUpdateBlock(),
			new PlayerAppearanceUpdateBlock(),
			new PlayerPositionUpdateBlock(),
			new PlayerPrimaryHitUpdateBlock(),
			new PlayerSecondaryHitUpdateBlock()
	};
	
	public static void encode(Player player, Player other, GameBuffer msg, UpdateState state) {
		if(other.getFlags().isEmpty() && state != UpdateState.ADD_LOCAL) {
			return;
		}
		boolean cacheBlocks = (state != UpdateState.ADD_LOCAL && state != UpdateState.UPDATE_SELF);
		if(other.getCachedUpdateBlock() != null && cacheBlocks) {
			//msg.putBytes(other.getCachedUpdateBlock());
			//return;
		}
		GameBuffer encodedBlock = new GameBuffer(Unpooled.buffer(64));
		int mask = 0;
		if(other.getFlags().get(UpdateFlag.FORCE_MOVEMENT)) {
			mask |= 0x400;
		}
		if(other.getFlags().get(UpdateFlag.GRAPHIC)) {
			mask |= 0x100;
		}
		if(other.getFlags().get(UpdateFlag.ANIMATION)){
			mask |= 8;
		}
		if(other.getFlags().get(UpdateFlag.FORCE_CHAT) && state != UpdateState.UPDATE_SELF) {
			mask |= 4;
		}
		if(other.getFlags().get(UpdateFlag.CHAT)) {
			mask |= 0x80;
		}
		if(other.getFlags().get(UpdateFlag.FACE_ENTITY)) {
			mask |= 1;
		}
		if(other.getFlags().get(UpdateFlag.APPEARANCE)) {
			mask |= 0x10;
		}
		if(other.getFlags().get(UpdateFlag.FACE_COORDINATE)) {
			mask |= 2;
		}
		if(other.getFlags().get(UpdateFlag.PRIMARY_HIT)) {
			mask |= 0x20;
		}
		if(other.getFlags().get(UpdateFlag.SECONDARY_HIT)) {
			mask |= 0x200;
		}
		
		if(mask >= 0x100) {
			mask |= 0x40;
			encodedBlock.putShort(mask, ByteOrder.LITTLE);
		} else {
			encodedBlock.put(mask);
		}
		
		if(other.getFlags().get(UpdateFlag.FORCE_MOVEMENT)) {
			PLAYER_BLOCKS[0].write(player, other, encodedBlock);
		}
		if(other.getFlags().get(UpdateFlag.GRAPHIC)) {
			PLAYER_BLOCKS[1].write(player, other, encodedBlock);
		}
		if(other.getFlags().get(UpdateFlag.ANIMATION)){
			PLAYER_BLOCKS[2].write(player, other, encodedBlock);
		}
		if(other.getFlags().get(UpdateFlag.FORCE_CHAT)) {
			PLAYER_BLOCKS[3].write(player, other, encodedBlock);
		}
		if(other.getFlags().get(UpdateFlag.CHAT)) {
			PLAYER_BLOCKS[4].write(player, other, encodedBlock);
		}
		if(other.getFlags().get(UpdateFlag.FACE_ENTITY)) {
			PLAYER_BLOCKS[5].write(player, other, encodedBlock);
		}
		if(other.getFlags().get(UpdateFlag.APPEARANCE)) {
			PLAYER_BLOCKS[6].write(player, other, encodedBlock);
		}
		if(other.getFlags().get(UpdateFlag.FACE_COORDINATE)) {
			PLAYER_BLOCKS[7].write(player, other, encodedBlock);
		}
		if(other.getFlags().get(UpdateFlag.PRIMARY_HIT)) {
			PLAYER_BLOCKS[8].write(player, other, encodedBlock);
		}
		if(other.getFlags().get(UpdateFlag.SECONDARY_HIT)) {
			PLAYER_BLOCKS[9].write(player, other, encodedBlock);
		}
		
		msg.putBytes(encodedBlock);
		if(cacheBlocks) {
			//	other.setCachedUpdateBlock(encodedBlock);
		}
		//encodedBlock.release();
	}
	
	
	public static void encode(Player player, Npc npc, GameBuffer msg, UpdateState state) {
		if(npc.getFlags().isEmpty() && state != UpdateState.ADD_LOCAL) {
			return;
		}
		GameBuffer encodedBlock = new GameBuffer(player.getSession().alloc().buffer(64));
		int mask = 0;
		
		if(npc.getFlags().get(UpdateFlag.FORCE_MOVEMENT)) {
			mask |= 0x400;
		}
		if(npc.getFlags().get(UpdateFlag.GRAPHIC)) {
			mask |= 0x100;
		}
		if(npc.getFlags().get(UpdateFlag.ANIMATION)){
			mask |= 8;
		}
		if(npc.getFlags().get(UpdateFlag.FORCE_CHAT)) {
			mask |= 4;
		}
		if(npc.getFlags().get(UpdateFlag.TRANSFORM)) {
			mask |= 0x80;
		}
		if(npc.getFlags().get(UpdateFlag.FACE_ENTITY)) {
			mask |= 1;
		}
		if(npc.getFlags().get(UpdateFlag.FACE_COORDINATE)) {
			mask |= 2;
		}
		if(npc.getFlags().get(UpdateFlag.PRIMARY_HIT)) {
			mask |= 0x20;
		}
		if(npc.getFlags().get(UpdateFlag.SECONDARY_HIT)) {
			mask |= 0x200;
		}
		
		if(mask >= 0x100) {
			mask |= 0x40;
			encodedBlock.putShort(mask, ByteOrder.LITTLE);
		} else {
			encodedBlock.put(mask);
		}
		
		if(npc.getFlags().get(UpdateFlag.FORCE_MOVEMENT)) {
			ForcedMovement movement = npc.getForcedMovement();
			Position lastRegion = player.getLastRegion();
			Position position = npc.getPosition();
			int firstVelocity = (movement.getFirstSpeed());
			int secondVelocity = (movement.getSecondSpeed());
			int direction = movement.getDirection().getId();
			int firstX = movement.getFirst().getX() - position.getX();
			int firstY = movement.getFirst().getY() - position.getY();
			int secondX = movement.getSecond().getX() - position.getX();
			int secondY = movement.getSecond().getY() - position.getY();
			msg.put(position.getLocalX(lastRegion) + firstX, ByteTransform.S);
			msg.put(position.getLocalY(lastRegion) + firstY, ByteTransform.S);
			msg.put(position.getLocalX(lastRegion) + secondX, ByteTransform.S);
			msg.put(position.getLocalY(lastRegion) + secondY, ByteTransform.S);
			msg.putShort(firstVelocity, ByteTransform.A, ByteOrder.LITTLE);
			msg.putShort(secondVelocity, ByteTransform.A);
			msg.put(direction, ByteTransform.S);
		}
		if(npc.getFlags().get(UpdateFlag.GRAPHIC)) {
			msg.putShort(npc.getGraphic().getId());
			msg.putInt(npc.getGraphic().getHeight() << 16 | npc.getGraphic().getDelay() & 0xFFFF);
		}
		if(npc.getFlags().get(UpdateFlag.ANIMATION)){
			msg.putShort(npc.getAnimation().getId(), ByteOrder.LITTLE);
			msg.put(npc.getAnimation().getDelay());
		}
		if(npc.getFlags().get(UpdateFlag.FORCE_CHAT)) {
			msg.putCString(npc.getForcedText());
		}
		if(npc.getFlags().get(UpdateFlag.TRANSFORM)) {
			msg.putShort(npc.getTransform().orElse(-1), ByteTransform.A, ByteOrder.LITTLE);
		}
		if(npc.getFlags().get(UpdateFlag.FACE_ENTITY)) {
			msg.putShort(npc.getFaceIndex());
		}
		if(npc.getFlags().get(UpdateFlag.FACE_COORDINATE)) {
			msg.putShort(npc.getFacePosition().getX(), ByteOrder.LITTLE);
			msg.putShort(npc.getFacePosition().getY(), ByteOrder.LITTLE);
		}
		if(npc.getFlags().get(UpdateFlag.PRIMARY_HIT)) {
			Hit hit = npc.getPrimaryHit();
			msg.putShort(hit.getDamage());
			msg.put(hit.getType().getId() + (hit.hasSource() && hit.getSource() != player.getSlot() ? 5 : 0));
			msg.put(hit.getIcon().getId());
			msg.putShort((int) Math.round((((double) npc.getCurrentHealth()) / ((double) npc.getMaxHealth())) * 100));
			msg.put(npc.getSpecial().isPresent() ? npc.getSpecial().getAsInt() : 101);
		}
		if(npc.getFlags().get(UpdateFlag.SECONDARY_HIT)) {
			Hit hit = npc.getSecondaryHit();
			msg.putShort(hit.getDamage());
			msg.put(hit.getType().getId() + (hit.hasSource() && hit.getSource() != player.getSlot() ? 5 : 0));
			msg.put(hit.getIcon().getId());
			msg.putShort((int) Math.round((((double) npc.getCurrentHealth()) / ((double) npc.getMaxHealth())) * 100));
			msg.put(npc.getSpecial().isPresent() ? npc.getSpecial().getAsInt() : 101);
		}
		msg.putBytes(encodedBlock);
		encodedBlock.release();
	}
	
}
