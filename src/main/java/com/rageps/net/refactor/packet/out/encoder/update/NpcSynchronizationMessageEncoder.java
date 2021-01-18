package com.rageps.net.refactor.packet.out.encoder.update;


import com.rageps.net.refactor.codec.game.*;
import com.rageps.net.refactor.meta.PacketType;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.update.NpcSynchronizationPacket;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.sync.block.*;
import com.rageps.world.entity.sync.seg.AddNpcSegment;
import com.rageps.world.entity.sync.seg.MovementSegment;
import com.rageps.world.entity.sync.seg.SegmentType;
import com.rageps.world.entity.sync.seg.SynchronizationSegment;
import com.rageps.world.locale.Position;
import com.rageps.world.model.Animation;
import com.rageps.world.model.Direction;
import com.rageps.world.model.Graphic;

/**
 * A {@link PacketEncoder} for the {@link NpcSynchronizationPacket}.
 *
 * @author Major
 */
public final class NpcSynchronizationMessageEncoder implements PacketEncoder<NpcSynchronizationPacket> {

	@Override
	public GamePacket encode(NpcSynchronizationPacket message) {
		GamePacketBuilder builder = new GamePacketBuilder(65, PacketType.VARIABLE_SHORT);
		builder.switchToBitAccess();

		GamePacketBuilder blockBuilder = new GamePacketBuilder();
		builder.putBits(8, message.getLocalNpcCount());

		for (SynchronizationSegment segment : message.getSegments()) {
			SegmentType type = segment.getType();
			if(type != SegmentType.REMOVE_MOB && type != SegmentType.ADD_MOB && type != SegmentType.NO_MOVEMENT && type != SegmentType.WALK)
				System.out.println("");
			if (type == SegmentType.REMOVE_MOB) {
				putRemoveMobUpdate(builder);
			} else if (type == SegmentType.ADD_MOB) {
				putAddNpcUpdate((AddNpcSegment) segment, message, builder);
				putBlocks(segment, blockBuilder);
			} else {
				putMovementUpdate(segment, message, builder);
				putBlocks(segment, blockBuilder);
			}
		}

		if (blockBuilder.getLength() > 0) {
			builder.putBits(14, 16383);
			builder.switchToByteAccess();
			builder.putRawBuilder(blockBuilder);
		} else {
			builder.switchToByteAccess();
		}
		return builder.toGamePacket();
	}

	/**
	 * Puts an add npc update.
	 *
	 * @param seg The segment.
	 * @param message The message.
	 * @param builder The builder.
	 */
	private static void putAddNpcUpdate(AddNpcSegment seg, NpcSynchronizationPacket message, GamePacketBuilder builder) {
		boolean updateRequired = seg.getBlockSet().size() > 0;
		Position npc = message.getPosition();
		Position other = seg.getPosition();
		builder.putBits(14, seg.getIndex());
		builder.putBits(5, other.getY() - npc.getY());
		builder.putBits(5, other.getX() - npc.getX());
		builder.putBits(1, 0); // discard walking queue
		builder.putBits(16, seg.getNpcId());
		builder.putBits(1, updateRequired ? 1 : 0);
	}

	/**
	 * Puts an animation block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putAnimationBlock(AnimationBlock block, GamePacketBuilder builder) {
		Animation animation = block.getAnimation();
		builder.put(DataType.SHORT, DataOrder.LITTLE, animation.getId());
		builder.put(DataType.BYTE, animation.getDelay());
	}

	/**
	 * Puts the blocks for the specified segment.
	 *
	 * @param segment The segment.
	 * @param builder The block builder.
	 */
	private static void putBlocks(SynchronizationSegment segment, GamePacketBuilder builder) {
		SynchronizationBlockSet blockSet = segment.getBlockSet();
		if (blockSet.size() > 0) {
			int mask = 0;


			if(blockSet.contains(ForceMovementBlock.class)) {
				mask |= 0x400;
			}

			if (blockSet.contains(GraphicBlock.class)) {
				mask |= 0x100;
			}

			if (blockSet.contains(AnimationBlock.class)) {
				mask |= 8;
			}

			if (blockSet.contains(ForceChatBlock.class)) {
				mask |= 4;
			}

			if (blockSet.contains(TransformBlock.class)) {
				mask |= 0x80;
			}

			if (blockSet.contains(InteractingMobBlock.class)) {
				mask |= 0x10;
			}

			if (blockSet.contains(TurnToPositionBlock.class)) {
				mask |= 1;
			}

			if (blockSet.contains(HitUpdateBlock.class)) {
				mask |= 2;
			}

			if (blockSet.contains(SecondaryHitUpdateBlock.class)) {
				mask |= 0x20;
			}

			if(mask > 0x100) {
				mask |= 0x40;
				builder.putShort(mask, DataOrder.LITTLE);
			} else {
				builder.put(DataType.BYTE, mask);
			}

			blockSet.printBlocks();

			if(blockSet.contains(ForceMovementBlock.class)) {
				putForceMovementBlock(blockSet.get(ForceMovementBlock.class), builder);
			}

			if (blockSet.contains(GraphicBlock.class)) {
				putGraphicBlock(blockSet.get(GraphicBlock.class), builder);
			}

			if (blockSet.contains(AnimationBlock.class)) {
				putAnimationBlock(blockSet.get(AnimationBlock.class), builder);
			}

			if (blockSet.contains(ForceChatBlock.class)) {
				putForceChatBlock(blockSet.get(ForceChatBlock.class), builder);
			}

			if (blockSet.contains(TransformBlock.class)) {
				putTransformBlock(blockSet.get(TransformBlock.class), builder);
			}

			if (blockSet.contains(InteractingMobBlock.class)) {
				putInteractingMobBlock(blockSet.get(InteractingMobBlock.class), builder);
			}

			if (blockSet.contains(TurnToPositionBlock.class)) {
				putTurnToPositionBlock(blockSet.get(TurnToPositionBlock.class), builder);
			}

			if (blockSet.contains(HitUpdateBlock.class)) {
				putHitUpdateBlock(blockSet.get(HitUpdateBlock.class), builder);
			}

			if (blockSet.contains(SecondaryHitUpdateBlock.class)) {
				putSecondHitUpdateBlock(blockSet.get(SecondaryHitUpdateBlock.class), builder);
			}




		}
	}

	/**
	 * Puts a force movement block in the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putForceMovementBlock(ForceMovementBlock block, GamePacketBuilder builder) {
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, block.getInitialX());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, block.getInitialY());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, block.getFinalX());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, block.getFinalY());
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, block.getTravelDurationX());
		builder.put(DataType.SHORT, DataTransformation.ADD, block.getTravelDurationY());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, block.getDirection().getId());
	}

	/**
	 * Puts a force chat block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putForceChatBlock(ForceChatBlock block, GamePacketBuilder builder) {
		builder.putString(block.getMessage());
	}

	/**
	 * Puts a graphic block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putGraphicBlock(GraphicBlock block, GamePacketBuilder builder) {
		Graphic graphic = block.getGraphic();
		builder.put(DataType.SHORT, graphic.getId());
		builder.put(DataType.INT, graphic.getHeight() << 16 | graphic.getDelay() & 0xFFFF);
	}

	/**
	 * Puts an interacting mob block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putInteractingMobBlock(InteractingMobBlock block, GamePacketBuilder builder) {
		builder.put(DataType.SHORT, block.getIndex());
	}

	/**
	 * Puts a movement update for the specified segment.
	 *
	 * @param segment The segment.
	 * @param message The message.
	 * @param builder The builder.
	 */
	private static void putMovementUpdate(SynchronizationSegment segment, NpcSynchronizationPacket message, GamePacketBuilder builder) {
		boolean updateRequired = segment.getBlockSet().size() > 0;
		if (segment.getType() == SegmentType.RUN) {
			Direction[] directions = ((MovementSegment) segment).getDirections();
			builder.putBits(1, 1);
			builder.putBits(2, 2);
			builder.putBits(3, directions[0].toInteger());
			builder.putBits(3, directions[1].toInteger());
			builder.putBits(1, updateRequired ? 1 : 0);
		} else if (segment.getType() == SegmentType.WALK) {
			Direction[] directions = ((MovementSegment) segment).getDirections();
			builder.putBits(1, 1);
			builder.putBits(2, 1);
			builder.putBits(3, directions[0].toInteger());
			builder.putBits(1, updateRequired ? 1 : 0);
		} else {
			if (updateRequired) {
				builder.putBits(1, 1);
				builder.putBits(2, 0);
			} else {
				builder.putBits(1, 0);
			}
		}
	}

	/**
	 * Puts a remove mob update.
	 *
	 * @param builder The builder.
	 */
	private static void putRemoveMobUpdate(GamePacketBuilder builder) {
		//builder.putBits(1, 1);
		builder.putBit(true);
		builder.putBits(2, 3);
	}

	/**
	 * Puts a second hit update block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putSecondHitUpdateBlock(SecondaryHitUpdateBlock block, GamePacketBuilder builder) {
		Hit hit = block.getHit();
		builder.putShort(hit.getDamage());
		builder.put(hit.getHitsplat().getId());// todo local
		builder.put(hit.getHitIcon().getId());
		builder.putShort( (int) (block.getCurrentHealth() * 100.0 / block.getMaximumHealth()));
		builder.put(101);//todo special percentage
	}

	/**
	 * Puts a hit update block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putHitUpdateBlock(HitUpdateBlock block, GamePacketBuilder builder) {
		Hit hit = block.getHit();
		builder.putShort(hit.getDamage());
		builder.put(hit.getHitsplat().getId());// todo local
		builder.put(hit.getHitIcon().getId());
		builder.putShort( (int) (block.getCurrentHealth() * 100.0 / block.getMaximumHealth()));
		builder.put(101);//todo special percentage
	}

	/**
	 * Puts a transform block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putTransformBlock(TransformBlock block, GamePacketBuilder builder) {
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, block.getId());
	}

	/**
	 * Puts a turn to position block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putTurnToPositionBlock(TurnToPositionBlock block, GamePacketBuilder builder) {
		Position position = block.getPosition();
		builder.put(DataType.SHORT, DataOrder.LITTLE, position.getX() * 2 + 1);
		builder.put(DataType.SHORT, DataOrder.LITTLE, position.getY() * 2 + 1);
	}

}