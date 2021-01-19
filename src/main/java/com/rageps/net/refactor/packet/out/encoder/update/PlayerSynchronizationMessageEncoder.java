package com.rageps.net.refactor.packet.out.encoder.update;


import com.google.common.collect.ImmutableMap;
import com.rageps.net.refactor.codec.game.*;
import com.rageps.net.refactor.meta.PacketType;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.update.PlayerSynchronizationPacket;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.combat.hit.Hitsplat;
import com.rageps.world.entity.actor.player.PlayerAppearance;
import com.rageps.world.entity.item.container.ItemContainer;
import com.rageps.world.entity.item.container.impl.Equipment;
import com.rageps.world.entity.sync.block.*;
import com.rageps.world.entity.sync.seg.*;
import com.rageps.world.locale.Position;
import com.rageps.world.model.Animation;
import com.rageps.world.model.Direction;
import com.rageps.world.model.Graphic;

/**
 * A {@link PacketEncoder} for the {@link PlayerSynchronizationPacket}.
 *
 * @author Graham
 * @author Major
 */
public final class PlayerSynchronizationMessageEncoder implements PacketEncoder<PlayerSynchronizationPacket> {

	@Override
	public GamePacket encode(PlayerSynchronizationPacket message) {
		GamePacketBuilder builder = new GamePacketBuilder(81, PacketType.VARIABLE_SHORT);
		builder.switchToBitAccess();
		GamePacketBuilder blockBuilder = new GamePacketBuilder();

		putMovementUpdate(message.getSegment(), message, builder);
		putBlocks(message.getSegment(), blockBuilder);

		builder.putBits(8, message.getLocalPlayers());

		for (SynchronizationSegment segment : message.getSegments()) {
			SegmentType type = segment.getType();
			if (type == SegmentType.REMOVE_MOB) {
				putRemovePlayerUpdate(builder);
			} else if (type == SegmentType.ADD_MOB) {
				putAddPlayerUpdate((AddPlayerSegment) segment, message, builder);
				putBlocks(segment, blockBuilder);
			} else {
				putMovementUpdate(segment, message, builder);
				putBlocks(segment, blockBuilder);
			}
		}

		if (blockBuilder.getLength() > 0) {
			builder.putBits(11, 2047);
			builder.switchToByteAccess();
			builder.putRawBuilder(blockBuilder);
		} else {
			builder.switchToByteAccess();
		}

		return builder.toGamePacket();
	}

	/**
	 * Puts an add player update.
	 *
	 * @param seg The segment.
	 * @param message The message.
	 * @param builder The builder.
	 */
	private static void putAddPlayerUpdate(AddPlayerSegment seg, PlayerSynchronizationPacket message, GamePacketBuilder builder) {
		boolean updateRequired = seg.getBlockSet().size() > 0;
		Position player = message.getPosition();
		Position other = seg.getPosition();
		builder.putBits(11, seg.getIndex());
		builder.putBits(1, updateRequired ? 1 : 0);
		builder.putBits(1, 1); // discard walking queue?
		builder.putBits(5, other.getY() - player.getY());
		builder.putBits(5, other.getX() - player.getX());
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
		builder.put(DataType.BYTE, DataTransformation.NEGATE, animation.getDelay());
	}

	private static final ImmutableMap<Integer, Integer> NEW_HALF_BODY_APPEARANCES = ImmutableMap.<Integer, Integer>builder().
			put(443, 614).
			put(444, 599).
			put(445, 590).
			put(446, 598).
			put(447, 610).
			put(448, 611).
			put(449, 612).
			put(450, 609).
			put(451, 602).
			put(452, 595).
			put(453, 604).
			put(454, 605).
			put(455, 606).
			put(456, 619).build();



	/**
	 * Puts an appearance block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putAppearanceBlock(AppearanceBlock block, GamePacketBuilder builder) {
		PlayerAppearance appearance = block.getAppearance();
		GamePacketBuilder playerProperties = new GamePacketBuilder();

		playerProperties.put(DataType.BYTE, appearance.getGender());
		playerProperties.put(DataType.BYTE, block.getHeadIcon());
		playerProperties.put(DataType.BYTE, block.getSkull());

		if (block.appearingAsNpc()) {
			playerProperties.put(DataType.BYTE, 255);
			playerProperties.put(DataType.BYTE, 255);
			playerProperties.put(DataType.SHORT, block.getNpcId());
		} else {
			ItemContainer equipment = block.getEquipment();

			if(equipment.getId(Equipment.HEAD_SLOT) > 1) {
				playerProperties.putShort(0x8000 + equipment.getId(Equipment.HEAD_SLOT));
			} else {
				playerProperties.put(0);
			}
			if(equipment.getId(Equipment.CAPE_SLOT) > 1) {
				playerProperties.putShort(0x8000 + equipment.getId(Equipment.CAPE_SLOT));
			} else {
				playerProperties.put(0);
			}
			if(equipment.getId(Equipment.AMULET_SLOT) > 1) {
				playerProperties.putShort(0x8000 + equipment.getId(Equipment.AMULET_SLOT));
			} else {
				playerProperties.put(0);
			}
			if(equipment.getId(Equipment.WEAPON_SLOT) > 1) {
				playerProperties.putShort(0x8000 + equipment.getId(Equipment.WEAPON_SLOT));
			} else {
				playerProperties.put(0);
			}
			if(equipment.getId(Equipment.CHEST_SLOT) > 1) {
				playerProperties.putShort(0x8000 + equipment.getId(Equipment.CHEST_SLOT));
			} else {
				playerProperties.putShort(0x100 + appearance.getChest());
			}
			if(equipment.getId(Equipment.SHIELD_SLOT) > 1) {
				playerProperties.putShort(0x8000 + equipment.getId(Equipment.SHIELD_SLOT));
			} else {
				playerProperties.put(0);
			}
			if(equipment.getId(Equipment.CHEST_SLOT) > 1) {
				if(!equipment.get(Equipment.CHEST_SLOT).getDefinition().isPlatebody()) {
					playerProperties.putShort(appearance.getArms() == 0 ? 0x100 + NEW_HALF_BODY_APPEARANCES.get(appearance.getChest()) : 0x100 + appearance.getArms());
				} else {
					playerProperties.put(0);
				}
			} else {
				playerProperties.putShort(0x100 + appearance.getArms());
			}
			if(equipment.getId(Equipment.LEGS_SLOT) > 1) {
				playerProperties.putShort(0x8000 + equipment.getId(Equipment.LEGS_SLOT));
			} else {
				playerProperties.putShort(0x100 + appearance.getLegs());
			}
			if(equipment.getId(Equipment.HEAD_SLOT) > 1 && (equipment.get(Equipment.HEAD_SLOT).getDefinition().isFullHelm() || equipment.get(Equipment.HEAD_SLOT).getDefinition().isFullMask())) {
				playerProperties.put(0);
			} else {
				playerProperties.putShort(0x100 + appearance.getHead());
			}
			if(equipment.getId(Equipment.HANDS_SLOT) > 1) {
				playerProperties.putShort(0x8000 + equipment.getId(Equipment.HANDS_SLOT));
			} else {
				playerProperties.putShort(0x100 + appearance.getHands());
			}
			if(equipment.getId(Equipment.FEET_SLOT) > 1) {
				playerProperties.putShort(0x8000 + equipment.getId(Equipment.FEET_SLOT));
			} else {
				playerProperties.putShort(0x100 + appearance.getFeet());
			}
			if(appearance.isMale()) {
				if(equipment.getId(Equipment.HEAD_SLOT) > 1 && equipment.get(Equipment.HEAD_SLOT).getDefinition().isFullMask()) {
					playerProperties.put(0);
				} else {
					playerProperties.putShort(0x100 + appearance.getBeard());
				}
			} else {
				playerProperties.put(0);
			}
		}

		playerProperties.put(appearance.getHairColor());
		playerProperties.put(appearance.getTorsoColor());
		playerProperties.put(appearance.getLegColor());
		playerProperties.put(appearance.getFeetColor());
		playerProperties.put(appearance.getSkinColor());

		playerProperties.put(DataType.SHORT, 0x328); // stand
		playerProperties.put(DataType.SHORT, 0x337); // stand turn
		playerProperties.put(DataType.SHORT, 0x333); // walk
		playerProperties.put(DataType.SHORT, 0x334); // turn 180
		playerProperties.put(DataType.SHORT, 0x335); // turn 90 cw
		playerProperties.put(DataType.SHORT, 0x336); // turn 90 ccw
		playerProperties.put(DataType.SHORT, 0x338); // run


		playerProperties.put(DataType.LONG, block.getName());
		playerProperties.put(DataType.BYTE, block.getCombatLevel());
		playerProperties.put(DataType.BYTE, block.isIronman() ? 1 : 0);

		builder.put(DataType.BYTE, DataTransformation.NEGATE, playerProperties.getLength());

		builder.putRawBuilder(playerProperties);
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

			//blockSet.printBlocks();

			if (blockSet.contains(ForceMovementBlock.class)) {
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
			if (blockSet.contains(ChatBlock.class)) {
				mask |= 0x80;
			}
			if (blockSet.contains(InteractingMobBlock.class)) {
				mask |= 1;
			}
			if (blockSet.contains(AppearanceBlock.class)) {
				mask |= 0x10;
			}
			if (blockSet.contains(TurnToPositionBlock.class)) {
				mask |= 0x2;
			}
			if (blockSet.contains(HitUpdateBlock.class)) {
				mask |= 0x20;
			}
			if (blockSet.contains(SecondaryHitUpdateBlock.class)) {
				mask |= 0x200;
			}

			if (mask >= 0x100) {
				mask |= 0x40;
				builder.put(DataType.SHORT, DataOrder.LITTLE, mask);
			} else {
				builder.put(DataType.BYTE, mask);
			}

			if (blockSet.contains(ForceMovementBlock.class)) {
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
			if (blockSet.contains(ChatBlock.class)) {
				putChatBlock(blockSet.get(ChatBlock.class), builder);
			}
			if (blockSet.contains(InteractingMobBlock.class)) {
				putInteractingMobBlock(blockSet.get(InteractingMobBlock.class), builder);
			}
			if (blockSet.contains(AppearanceBlock.class)) {
				putAppearanceBlock(blockSet.get(AppearanceBlock.class), builder);
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
	 * Puts a chat block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putChatBlock(ChatBlock block, GamePacketBuilder builder) {
		byte[] bytes = block.getCompressedMessage();
		builder.put(DataType.SHORT, DataOrder.LITTLE, block.getTextColor() << 8 | block.getTextEffects());
		builder.put(DataType.BYTE, block.getPrivilegeLevel().getId());
		builder.put(DataType.BYTE, DataTransformation.NEGATE, bytes.length);
		builder.putBytesReverse(bytes);
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
	 * Puts a graphic block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putGraphicBlock(GraphicBlock block, GamePacketBuilder builder) {
		Graphic graphic = block.getGraphic();
		builder.put(DataType.SHORT, DataOrder.LITTLE, graphic.getId());
		builder.put(DataType.INT, graphic.getHeight() << 16 | graphic.getDelay() & 0xFFFF);
	}



	/**
	 * Puts an interacting mob block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putInteractingMobBlock(InteractingMobBlock block, GamePacketBuilder builder) {
		builder.put(DataType.SHORT, DataOrder.LITTLE, block.getIndex());
	}

	/**
	 * Puts a movement update for the specified segment.
	 *
	 * @param seg The segment.
	 * @param message The message.
	 * @param builder The builder.
	 */
	private static void putMovementUpdate(SynchronizationSegment seg, PlayerSynchronizationPacket message, GamePacketBuilder builder) {
		boolean updateRequired = seg.getBlockSet().size() > 0;
		if (seg.getType() == SegmentType.TELEPORT) {
			Position position = ((TeleportSegment) seg).getDestination();
			builder.putBits(1, 1);
			builder.putBits(2, 3);
			builder.putBits(2, position.getZ());
			builder.putBits(1, message.hasRegionChanged() ? 0 : 1);
			builder.putBits(1, updateRequired ? 1 : 0);
			builder.putBits(7, position.getLocalY(message.getLastKnownRegion()));
			builder.putBits(7, position.getLocalX(message.getLastKnownRegion()));
		} else if (seg.getType() == SegmentType.RUN) {
			Direction[] directions = ((MovementSegment) seg).getDirections();
			builder.putBits(1, 1);
			builder.putBits(2, 2);
			builder.putBits(3, directions[0].toInteger());
			builder.putBits(3, directions[1].toInteger());
			builder.putBits(1, updateRequired ? 1 : 0);
		} else if (seg.getType() == SegmentType.WALK) {
			Direction[] directions = ((MovementSegment) seg).getDirections();
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
	 * Puts a remove player update.
	 *
	 * @param builder The builder.
	 */
	private static void putRemovePlayerUpdate(GamePacketBuilder builder) {
		builder.putBits(1, 1);
		builder.putBits(2, 3);
	}

	/**
	 * Puts a hit update block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putHitUpdateBlock(HitUpdateBlock block, GamePacketBuilder builder) {
		Hit hit = block.getHit();
		boolean local = (hit.hasSource() && hit.getSource() == block.getSource().getSlot());
		builder.putShort(hit.getDamage());
		//builder.put(hit.getHitsplat().getId() + (!local ? (hit.getHitsplat() != Hitsplat.NORMAL_LOCAL ? 5 : 0) : 0)); todo local
		builder.put(hit.getHitsplat().getId());

		builder.put(hit.getHitIcon().getId());
		builder.putShort(hit.getSoak());
		builder.putShort(block.getMaximumHealth() / 10);
		builder.putShort(block.getCurrentHealth() / 10);
	}

	/**
	 * Puts a Second Hit Update block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putSecondHitUpdateBlock(SecondaryHitUpdateBlock block, GamePacketBuilder builder) {
		Hit hit = block.getHit();
		boolean local = (hit.hasSource() && hit.getSource() == block.getSource().getSlot());
		builder.putShort(hit.getDamage());
		//builder.put(hit.getHitsplat().getId() + (!local ? (hit.getHitsplat() != Hitsplat.NORMAL_LOCAL ? 5 : 0) : 0));//todo local
		builder.put(hit.getHitsplat().getId());

		builder.put(hit.getHitIcon().getId());
		builder.putShort(hit.getSoak());
		builder.putShort(block.getMaximumHealth() / 10);
		builder.putShort(block.getCurrentHealth() / 10);
	}

	/**
	 * Puts a Turn To Position block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putTurnToPositionBlock(TurnToPositionBlock block, GamePacketBuilder builder) {
		Position pos = block.getPosition();
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, pos.getX() * 2 + 1);
		builder.put(DataType.SHORT, DataOrder.LITTLE, pos.getY() * 2 + 1);
	}

}