package net.edge.world.node.entity.update;

import io.netty.buffer.Unpooled;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.ByteOrder;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;


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
	
	private static final NpcUpdateBlock[] NPC_BLOCKS = {
			new NpcForceMovementUpdateBlock(),
			new NpcGraphicUpdateBlock(),
			new NpcAnimationUpdateBlock(),
			new NpcForceChatUpdateBlock(),
			new NpcTransformUpdateBlock(),
			new NpcFaceEntityUpdateBlock(),
			new NpcFacePositionUpdateBlock(),
			new NpcPrimaryHitUpdateBlock(),
			new NpcSecondaryHitUpdateBlock()
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
		int size = PLAYER_BLOCKS.length;
		boolean[] flagged = new boolean[size];
		for(int i = 0; i < size; i++) {
			UpdateBlock updateBlock = PLAYER_BLOCKS[i];
			if(state == UpdateState.ADD_LOCAL && updateBlock.getFlag() == UpdateFlag.APPEARANCE) {
				mask |= updateBlock.getMask();
				flagged[i] = true;
				continue;
			}
			if(state == UpdateState.UPDATE_SELF && updateBlock.getFlag() == UpdateFlag.CHAT) {
				continue;
			}
			if(!other.getFlags().get(updateBlock.getFlag())) {
				continue;
			}
			
			mask |= updateBlock.getMask();
			flagged[i] = true;
		}
		
		if(mask >= 0x100) {
			mask |= 0x40;
			encodedBlock.putShort(mask, ByteOrder.LITTLE);
		} else {
			encodedBlock.put(mask);
		}
		
		for(int i = 0; i < size; i++) {
			if(flagged[i]) {
				PLAYER_BLOCKS[i].write(player, other, encodedBlock);
			}
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
		int size = NPC_BLOCKS.length;
		boolean[] flagged = new boolean[size];
		for(int i = 0; i < size; i++) {
			UpdateBlock updateBlock = NPC_BLOCKS[i];
			if(state == UpdateState.ADD_LOCAL && updateBlock.getFlag() == UpdateFlag.APPEARANCE) {
				mask |= updateBlock.getMask();
				flagged[i] = true;
				continue;
			}
			if(state == UpdateState.UPDATE_SELF && updateBlock.getFlag() == UpdateFlag.CHAT) {
				continue;
			}
			if(!npc.getFlags().get(updateBlock.getFlag())) {
				continue;
			}
			mask |= updateBlock.getMask();
			flagged[i] = true;
		}
		
		if(mask >= 0x100) {
			mask |= 0x40;
			encodedBlock.putShort(mask, ByteOrder.LITTLE);
		} else {
			encodedBlock.put(mask);
		}
		for(int i = 0; i < size; i++) {
			if(flagged[i]) {
				NPC_BLOCKS[i].write(player, npc, encodedBlock);
			}
		}
		msg.putBytes(encodedBlock);
		encodedBlock.release();
	}
	
}
