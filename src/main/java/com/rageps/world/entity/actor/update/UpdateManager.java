package com.rageps.world.entity.actor.update;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.codec.game.GamePacketType;
import io.netty.buffer.Unpooled;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;

public final class UpdateManager {
	
	private static final PlayerUpdateBlock[] PLAYER_BLOCKS = {new PlayerForceMovementUpdateBlock(), new PlayerGraphicUpdateBlock(), new PlayerAnimationUpdateBlock(), new PlayerForceChatUpdateBlock(), new PlayerChatUpdateBlock(), new PlayerFaceEntityUpdateBlock(), new PlayerAppearanceUpdateBlock(), new PlayerPositionUpdateBlock(), new PlayerPrimaryHitUpdateBlock(), new PlayerSecondaryHitUpdateBlock()};
	
	private static final MobUpdateBlock[] NPC_BLOCKS = {new MobForceMovementUpdateBlock(), new MobGraphicUpdateBlock(), new MobAnimationUpdateBlock(), new MobForceChatUpdateBlock(), new MobTransformUpdateBlock(), new MobFaceEntityUpdateBlock(), new MobFacePositionUpdateBlock(), new MobPrimaryHitUpdateBlock(), new MobSecondaryHitUpdateBlock(),};
	
	public static void prepare(Player other) {
		if(other.getLocalPlayers().size() == 0) {
			return;
		}
		if(other.getFlags().get(UpdateFlag.CHAT)) {
			return;
		}
		if(other.getFlags().get(UpdateFlag.FORCE_MOVEMENT)) {
			return;
		}
		int mask = 0;
		GamePacket encodedBlock = new GamePacket(-1, Unpooled.buffer(64), GamePacketType.RAW);
		if(other.getFlags().get(UpdateFlag.GRAPHIC)) {
			mask |= 0x100;
		}
		if(other.getFlags().get(UpdateFlag.ANIMATION)) {
			mask |= 8;
		}
		if(other.getFlags().get(UpdateFlag.FORCE_CHAT)) {
			mask |= 4;
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
		
		if(other.getFlags().get(UpdateFlag.GRAPHIC)) {
			PLAYER_BLOCKS[1].write(null, other, encodedBlock);
		}
		if(other.getFlags().get(UpdateFlag.ANIMATION)) {
			PLAYER_BLOCKS[2].write(null, other, encodedBlock);
		}
		if(other.getFlags().get(UpdateFlag.FORCE_CHAT)) {
			PLAYER_BLOCKS[3].write(null, other, encodedBlock);
		}
		if(other.getFlags().get(UpdateFlag.CHAT)) {
			PLAYER_BLOCKS[4].write(null, other, encodedBlock);
		}
		if(other.getFlags().get(UpdateFlag.FACE_ENTITY)) {
			PLAYER_BLOCKS[5].write(null, other, encodedBlock);
		}
		if(other.getFlags().get(UpdateFlag.APPEARANCE)) {
			PLAYER_BLOCKS[6].write(null, other, encodedBlock);
		}
		if(other.getFlags().get(UpdateFlag.FACE_COORDINATE)) {
			PLAYER_BLOCKS[7].write(null, other, encodedBlock);
		}
		if(other.getFlags().get(UpdateFlag.PRIMARY_HIT)) {
			PLAYER_BLOCKS[8].write(null, other, encodedBlock);
		}
		if(other.getFlags().get(UpdateFlag.SECONDARY_HIT)) {
			PLAYER_BLOCKS[9].write(null, other, encodedBlock);
		}
		other.setCachedUpdateBlock(encodedBlock);
	}
	
	public static void encode(Player player, Player other, GamePacket msg, UpdateState state) {
		if(other.getFlags().isEmpty() && state != UpdateState.ADD_LOCAL) {
			return;
		}
		boolean cacheBlocks = (state != UpdateState.ADD_LOCAL && state != UpdateState.UPDATE_SELF);
		if(other.getCachedUpdateBlock() != null && cacheBlocks) {
			//msg.putBytes(other.getCachedUpdateBlock().array());
			//return;
		}
		GamePacket encodedBlock = new GamePacket(-1, Unpooled.buffer(64), GamePacketType.RAW);
		int mask = 0;
		if(other.getFlags().get(UpdateFlag.FORCE_MOVEMENT)) {
			mask |= 0x400;
		}
		if(other.getFlags().get(UpdateFlag.GRAPHIC)) {
			mask |= 0x100;
		}
		if(other.getFlags().get(UpdateFlag.ANIMATION)) {
			mask |= 8;
		}
		if(other.getFlags().get(UpdateFlag.FORCE_CHAT)) {
			mask |= 4;
		}
		if(other.getFlags().get(UpdateFlag.CHAT) && state != UpdateState.UPDATE_SELF) {
			mask |= 0x80;
		}
		if(other.getFlags().get(UpdateFlag.FACE_ENTITY)) {
			mask |= 1;
		}
		if(other.getFlags().get(UpdateFlag.APPEARANCE) || state == UpdateState.ADD_LOCAL) {
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
			cacheBlocks = false;
		}
		if(other.getFlags().get(UpdateFlag.GRAPHIC)) {
			PLAYER_BLOCKS[1].write(player, other, encodedBlock);
		}
		if(other.getFlags().get(UpdateFlag.ANIMATION)) {
			PLAYER_BLOCKS[2].write(player, other, encodedBlock);
		}
		if(other.getFlags().get(UpdateFlag.FORCE_CHAT)) {
			PLAYER_BLOCKS[3].write(player, other, encodedBlock);
		}
		if(other.getFlags().get(UpdateFlag.CHAT) && state != UpdateState.UPDATE_SELF) {
			PLAYER_BLOCKS[4].write(player, other, encodedBlock);
		}
		if(other.getFlags().get(UpdateFlag.FACE_ENTITY)) {
			PLAYER_BLOCKS[5].write(player, other, encodedBlock);
		}
		if(other.getFlags().get(UpdateFlag.APPEARANCE) || state == UpdateState.ADD_LOCAL) {
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
			//other.setCachedUpdateBlock(encodedBlock);
		}
	}
	
	public static void encode(Player player, Mob mob, GamePacket msg, UpdateState state) {
		if(mob.getFlags().isEmpty() && state != UpdateState.ADD_LOCAL) {
			return;
		}
		GamePacket encodedBlock = new GamePacket(-1, Unpooled.buffer(64), GamePacketType.RAW);
		int mask = 0;
		
		if(mob.getFlags().get(UpdateFlag.FORCE_MOVEMENT)) {
			mask |= 0x400;
		}
		if(mob.getFlags().get(UpdateFlag.GRAPHIC)) {
			mask |= 0x100;
		}
		if(mob.getFlags().get(UpdateFlag.ANIMATION)) {
			mask |= 8;
		}
		if(mob.getFlags().get(UpdateFlag.FORCE_CHAT)) {
			mask |= 4;
		}
		if(mob.getFlags().get(UpdateFlag.TRANSFORM)) {
			mask |= 0x80;
		}
		if(mob.getFlags().get(UpdateFlag.FACE_ENTITY)) {
			mask |= 0x10;
		}
		if(mob.getFlags().get(UpdateFlag.FACE_COORDINATE)) {
			mask |= 1;
		}
		if(mob.getFlags().get(UpdateFlag.PRIMARY_HIT)) {
			mask |= 2;
		}
		if(mob.getFlags().get(UpdateFlag.SECONDARY_HIT)) {
			mask |= 0x20;
		}
		
		if(mask >= 0x100) {
			mask |= 0x40;
			encodedBlock.putShort(mask, ByteOrder.LITTLE);
		} else {
			encodedBlock.put(mask);
		}
		
		if(mob.getFlags().get(UpdateFlag.FORCE_MOVEMENT)) {
			NPC_BLOCKS[0].write(player, mob, encodedBlock);
		}
		if(mob.getFlags().get(UpdateFlag.GRAPHIC)) {
			NPC_BLOCKS[1].write(player, mob, encodedBlock);
		}
		if(mob.getFlags().get(UpdateFlag.ANIMATION)) {
			NPC_BLOCKS[2].write(player, mob, encodedBlock);
		}
		if(mob.getFlags().get(UpdateFlag.FORCE_CHAT)) {
			NPC_BLOCKS[3].write(player, mob, encodedBlock);
		}
		if(mob.getFlags().get(UpdateFlag.TRANSFORM)) {
			NPC_BLOCKS[4].write(player, mob, encodedBlock);
		}
		if(mob.getFlags().get(UpdateFlag.FACE_ENTITY)) {
			NPC_BLOCKS[5].write(player, mob, encodedBlock);
		}
		if(mob.getFlags().get(UpdateFlag.FACE_COORDINATE)) {
			NPC_BLOCKS[6].write(player, mob, encodedBlock);
		}
		if(mob.getFlags().get(UpdateFlag.PRIMARY_HIT)) {
			NPC_BLOCKS[7].write(player, mob, encodedBlock);
		}
		if(mob.getFlags().get(UpdateFlag.SECONDARY_HIT)) {
			NPC_BLOCKS[8].write(player, mob, encodedBlock);
		}
		msg.putBytes(encodedBlock);
		encodedBlock.getPayload().release();
	}
	
}
