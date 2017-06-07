package net.edge.world.node.entity.update;

import net.edge.net.codec.ByteMessage;
import net.edge.net.codec.ByteOrder;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkState;

/**
 * A group of {@link UpdateBlock}s that will be encoded and written to the main update buffer.
 * @author Artem Batutin <artembatutin@gmail.com>
 * @author lare96 <http://github.org/lare96>
 */
public final class UpdateBlockSet<E extends EntityNode> {
	
	/**
	 * A global instance of the {@link Player} update block set.
	 */
	public static final UpdateBlockSet<Player> PLAYER_BLOCK_SET = new UpdateBlockSet<>();
	
	/**
	 * A global instance of the {@link Npc} update block set.
	 */
	public static final UpdateBlockSet<Npc> NPC_BLOCK_SET = new UpdateBlockSet<>();
	
	static {
		try {
			/* Build the player block set. */
			PLAYER_BLOCK_SET.add(new PlayerForceMovementUpdateBlock());
			PLAYER_BLOCK_SET.add(new PlayerGraphicUpdateBlock());
			PLAYER_BLOCK_SET.add(new PlayerAnimationUpdateBlock());
			PLAYER_BLOCK_SET.add(new PlayerForceChatUpdateBlock());
			PLAYER_BLOCK_SET.add(new PlayerChatUpdateBlock());
			PLAYER_BLOCK_SET.add(new PlayerFaceEntityUpdateBlock());
			PLAYER_BLOCK_SET.add(new PlayerAppearanceUpdateBlock());
			PLAYER_BLOCK_SET.add(new PlayerPositionUpdateBlock());
			PLAYER_BLOCK_SET.add(new PlayerPrimaryHitUpdateBlock());
			PLAYER_BLOCK_SET.add(new PlayerSecondaryHitUpdateBlock());
		
            /* Build the non-player character block set. */
			NPC_BLOCK_SET.add(new NpcForceMovementUpdateBlock());
			NPC_BLOCK_SET.add(new NpcGraphicUpdateBlock());
			NPC_BLOCK_SET.add(new NpcAnimationUpdateBlock());
			NPC_BLOCK_SET.add(new NpcForceChatUpdateBlock());
			NPC_BLOCK_SET.add(new NpcTransformUpdateBlock());
			NPC_BLOCK_SET.add(new NpcFaceEntityUpdateBlock());
			NPC_BLOCK_SET.add(new NpcFacePositionUpdateBlock());
			NPC_BLOCK_SET.add(new NpcPrimaryHitUpdateBlock());
			NPC_BLOCK_SET.add(new NpcSecondaryHitUpdateBlock());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * An ordered {@link Set} containing all of the {@link UpdateBlock}s that can be encoded.
	 */
	private final Set<UpdateBlock<E>> updateBlocks = new LinkedHashSet<>();
	
	/**
	 * Adds an {@link UpdateBlock} to this {@code UpdateBlockSet}. Throws an {@link IllegalStateException} if this {@code
	 * UpdateBlockSet} already contains {@code block}.
	 * @param block The {@link UpdateBlock} to add.
	 */
	private void add(UpdateBlock<E> block) {
		checkState(updateBlocks.add(block), "updateBlocks.contains " + block.toString());
	}
	
	/**
	 * Encodes the update blocks for {@code forEntity} and appends the data to {@code msg}.
	 * @param forEntity The {@link EntityNode} to encode update blocks for.
	 * @param msg    The main update buffer.
	 * @param state  The {@link UpdateState} that the underlying {@link Player} is in.
	 */
	public void encodeUpdateBlocks(Player player, E forEntity, ByteMessage msg, UpdateState state) {
		if(forEntity.getFlags().isEmpty() && state != UpdateState.ADD_LOCAL) {
			return;
		}
		if(forEntity.isPlayer()) {
			encodePlayerBlocks(player, forEntity, msg, state);
		} else if(forEntity.isNpc()) {
			encodeNpcBlocks(player, forEntity, msg, state);
		} else {
			throw new IllegalStateException("forEntity.type() must be PLAYER or NPC");
		}
	}
	
	/**
	 * Encodes update blocks specifically for a {@link Player}.
	 * @param thisPlayer The player for whom we are updating.
	 * @param forMob     The {@code Player} to encode update blocks for.
	 * @param msg        The main update buffer.
	 * @param state      The {@link UpdateState} that the underlying {@code Player} is in.
	 */
	private void encodePlayerBlocks(Player thisPlayer, E forMob, ByteMessage msg, UpdateState state) {
		Player player = (Player) forMob;
		boolean cacheBlocks = (state != UpdateState.ADD_LOCAL && state != UpdateState.UPDATE_SELF);
		
		if(player.getCachedUpdateBlock() != null && cacheBlocks) {
			msg.putBytes(player.getCachedUpdateBlock());
			return;
		}
		
		ByteMessage encodedBlocks = encodeBlocks(thisPlayer, forMob, state);
		msg.putBytes(encodedBlocks);
		if(cacheBlocks) {
			player.setCachedUpdateBlock(encodedBlocks);
		}
		encodedBlocks.release();
	}
	
	/**
	 * Encodes update blocks specifically for a {@link Npc}.
	 * @param forMob The {@code Npc} to encode update blocks for.
	 * @param msg    The main update buffer.
	 * @param state  The {@link UpdateState} that the underlying {@code Npc} is in.
	 */
	private void encodeNpcBlocks(Player player, E forMob, ByteMessage msg, UpdateState state) {
		ByteMessage encodedBlocks = encodeBlocks(player, forMob, state);
		msg.putBytes(encodedBlocks);
		encodedBlocks.release();
	}
	
	/**
	 * Encodes the {@link UpdateBlock}s for {@code forMob} and returns the buffer containing the data.
	 * @param player the player for whom we are updating.
	 * @param forMob The {@link EntityNode} to encode for.
	 * @param state  The {@link UpdateState} that the underlying {@link Player} is in.
	 * @return The buffer containing the data.
	 */
	private ByteMessage encodeBlocks(Player player, E forMob, UpdateState state) {
		ByteMessage encodedBlock = ByteMessage.message(player.getSession().alloc());
		
		int mask = 0;
		Set<UpdateBlock<E>> writeBlocks = new LinkedHashSet<>();
		for(UpdateBlock<E> updateBlock : updateBlocks) {
			if(state == UpdateState.ADD_LOCAL && updateBlock.getFlag() == UpdateFlag.APPEARANCE) {
				mask |= updateBlock.getMask();
				writeBlocks.add(updateBlock);
				continue;
			}
			if(state == UpdateState.UPDATE_SELF && updateBlock.getFlag() == UpdateFlag.CHAT) {
				continue;
			}
			if(!forMob.getFlags().get(updateBlock.getFlag())) {
				continue;
			}
			
			mask |= updateBlock.getMask();
			writeBlocks.add(updateBlock);
		}
		
		if(mask >= 0x100) {
			mask |= 0x40;
			encodedBlock.putShort(mask, ByteOrder.LITTLE);
		} else {
			encodedBlock.put(mask);
		}
		
		writeBlocks.forEach(it -> it.write(player, forMob, encodedBlock));
		return encodedBlock;
	}
}
