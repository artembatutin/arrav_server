package net.edge.world.model.node.entity.update;

import net.edge.net.codec.ByteMessage;
import net.edge.world.content.container.impl.Equipment;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.entity.player.PlayerAppearance;
import net.edge.net.codec.ByteTransform;

/**
 * An {@link PlayerUpdateBlock} implementation that handles the updating of the appearance of {@link Player}s.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class PlayerAppearanceUpdateBlock extends PlayerUpdateBlock {

	/**
	 * Creates a new {@link PlayerAppearanceUpdateBlock}.
	 */
	public PlayerAppearanceUpdateBlock() {
		super(0x10, UpdateFlag.APPEARANCE);
	}

	@Override
	public int write(Player player, Player mob, ByteMessage msg) {
		PlayerAppearance appearance = mob.getAppearance();
		ByteMessage buf = ByteMessage.message();
		buf.put(appearance.getGender());
		buf.put(mob.getHeadIcon());
		buf.put(mob.getSkullIcon());
		if(mob.getPlayerNpc() == -1) {
			if(mob.getEquipment().getId(Equipment.HEAD_SLOT) > 1) {
				buf.putShort(0x8000 + mob.getEquipment().getId(Equipment.HEAD_SLOT));
			} else {
				buf.put(0);
			}
			if(mob.getEquipment().getId(Equipment.CAPE_SLOT) > 1) {
				buf.putShort(0x8000 + mob.getEquipment().getId(Equipment.CAPE_SLOT));
			} else {
				buf.put(0);
			}
			if(mob.getEquipment().getId(Equipment.AMULET_SLOT) > 1) {
				buf.putShort(0x8000 + mob.getEquipment().getId(Equipment.AMULET_SLOT));
			} else {
				buf.put(0);
			}
			if(mob.getEquipment().getId(Equipment.WEAPON_SLOT) > 1) {
				buf.putShort(0x8000 + mob.getEquipment().getId(Equipment.WEAPON_SLOT));
			} else {
				buf.put(0);
			}
			if(mob.getEquipment().getId(Equipment.CHEST_SLOT) > 1) {
				buf.putShort(0x8000 + mob.getEquipment().getId(Equipment.CHEST_SLOT));
			} else {
				buf.putShort(0x100 + appearance.getChest());
			}
			if(mob.getEquipment().getId(Equipment.SHIELD_SLOT) > 1) {
				buf.putShort(0x8000 + mob.getEquipment().getId(Equipment.SHIELD_SLOT));
			} else {
				buf.put(0);
			}
			if(mob.getEquipment().getId(Equipment.CHEST_SLOT) > 1) {
				if(!mob.getEquipment().get(Equipment.CHEST_SLOT).getDefinition().isPlatebody()) {
					buf.putShort(0x100 + appearance.getArms());
				} else {
					buf.put(0);
				}
			} else {
				buf.putShort(0x100 + appearance.getArms());
			}
			if(mob.getEquipment().getId(Equipment.LEGS_SLOT) > 1) {
				buf.putShort(0x8000 + mob.getEquipment().getId(Equipment.LEGS_SLOT));
			} else {
				buf.putShort(0x100 + appearance.getLegs());
			}
			if(mob.getEquipment().getId(Equipment.HEAD_SLOT) > 1 && (mob.getEquipment().get(Equipment.HEAD_SLOT).getDefinition().isFullHelm() || mob.getEquipment().get(Equipment.HEAD_SLOT).getDefinition().isFullMask())) {
				buf.put(0);
			} else {
				buf.putShort(0x100 + appearance.getHead());
			}
			if(mob.getEquipment().getId(Equipment.HANDS_SLOT) > 1) {
				buf.putShort(0x8000 + mob.getEquipment().getId(Equipment.HANDS_SLOT));
			} else {
				buf.putShort(0x100 + appearance.getHands());
			}
			if(mob.getEquipment().getId(Equipment.FEET_SLOT) > 1) {
				buf.putShort(0x8000 + mob.getEquipment().getId(Equipment.FEET_SLOT));
			} else {
				buf.putShort(0x100 + appearance.getFeet());
			}
			if(appearance.isMale()) {
				if(mob.getEquipment().getId(Equipment.HEAD_SLOT) > 1 && mob.getEquipment().get(Equipment.HEAD_SLOT).getDefinition().isFullMask()) {
					buf.put(0);
				} else {
					buf.putShort(0x100 + appearance.getBeard());
				}
			} else {
				buf.put(0);
			}
		} else {
			buf.putShort(-1);
			buf.putShort(mob.getPlayerNpc());
		}
		buf.put(appearance.getHairColor());
		buf.put(appearance.getTorsoColor());
		buf.put(appearance.getLegColor());
		buf.put(appearance.getFeetColor());
		buf.put(appearance.getSkinColor());

		buf.putShort(mob.getStandIndex() != 0x328 ? mob.getStandIndex() : mob.getWeaponAnimation().getStanding());
		buf.putShort(mob.getTurnIndex());
		buf.putShort(mob.getWalkIndex() != 0x333 ? mob.getWalkIndex() : mob.getWeaponAnimation().getWalking());
		buf.putShort(mob.getTurn180Index());
		buf.putShort(mob.getTurn90CWIndex());
		buf.putShort(mob.getTurn90CCWIndex());
		buf.putShort(mob.getRunIndex() != 0x338 ? mob.getRunIndex() : mob.getWeaponAnimation().getRunning());

		buf.putLong(mob.getUsernameHash());
		buf.put(mob.determineCombatLevel() < 3 ? 3 : mob.determineCombatLevel());
		buf.putShort(0);
		msg.put(buf.getBuffer().writerIndex(), ByteTransform.C);
		msg.putBytes(buf);
		buf.release();
		return -1;
	}

}
