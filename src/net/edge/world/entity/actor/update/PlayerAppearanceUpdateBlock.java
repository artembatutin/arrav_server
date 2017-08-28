package net.edge.world.entity.actor.update;

import com.google.common.collect.ImmutableMap;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.PlayerAppearance;
import net.edge.world.entity.item.container.impl.Equipment;

/**
 * An {@link PlayerUpdateBlock} implementation that handles the updating of the appearance of {@link Player}s.
 *
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class PlayerAppearanceUpdateBlock extends PlayerUpdateBlock {

	/**
	 * Creates a new {@link PlayerAppearanceUpdateBlock}.
	 */
	PlayerAppearanceUpdateBlock() {
		super(0x10, UpdateFlag.APPEARANCE);
	}

	private final ImmutableMap<Integer, Integer> NEW_HALF_BODY_APPEARANCES = ImmutableMap.<Integer, Integer>builder().
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

	@Override
	public int write(Player player, Player other, GameBuffer msg) {
		PlayerAppearance appearance = other.getAppearance();
		GameBuffer buf = new GameBuffer(player.getSession().alloc().buffer(32));
		try {
			buf.put(appearance.getGender());
			buf.put(other.headIcon);
			buf.put(other.skullIcon);
			if(other.getPlayerNpc() == -1) {
				if(other.getEquipment().getId(Equipment.HEAD_SLOT) > 1) {
					buf.putShort(0x8000 + other.getEquipment().getId(Equipment.HEAD_SLOT));
				} else {
					buf.put(0);
				}
				if(other.getEquipment().getId(Equipment.CAPE_SLOT) > 1) {
					buf.putShort(0x8000 + other.getEquipment().getId(Equipment.CAPE_SLOT));
				} else {
					buf.put(0);
				}
				if(other.getEquipment().getId(Equipment.AMULET_SLOT) > 1) {
					buf.putShort(0x8000 + other.getEquipment().getId(Equipment.AMULET_SLOT));
				} else {
					buf.put(0);
				}
				if(other.getEquipment().getId(Equipment.WEAPON_SLOT) > 1) {
					buf.putShort(0x8000 + other.getEquipment().getId(Equipment.WEAPON_SLOT));
				} else {
					buf.put(0);
				}
				if(other.getEquipment().getId(Equipment.CHEST_SLOT) > 1) {
					buf.putShort(0x8000 + other.getEquipment().getId(Equipment.CHEST_SLOT));
				} else {
					buf.putShort(0x100 + appearance.getChest());
				}
				if(other.getEquipment().getId(Equipment.SHIELD_SLOT) > 1) {
					buf.putShort(0x8000 + other.getEquipment().getId(Equipment.SHIELD_SLOT));
				} else {
					buf.put(0);
				}
				if(other.getEquipment().getId(Equipment.CHEST_SLOT) > 1) {
					if(!other.getEquipment().get(Equipment.CHEST_SLOT).getDefinition().isPlatebody()) {
						buf.putShort(appearance.getArms() == 0 ? 0x100 + NEW_HALF_BODY_APPEARANCES.get(appearance.getChest()) : 0x100 + appearance
								.getArms());
					} else {
						buf.put(0);
					}
				} else {
					buf.putShort(0x100 + appearance.getArms());
				}
				if(other.getEquipment().getId(Equipment.LEGS_SLOT) > 1) {
					buf.putShort(0x8000 + other.getEquipment().getId(Equipment.LEGS_SLOT));
				} else {
					buf.putShort(0x100 + appearance.getLegs());
				}
				if(other.getEquipment().getId(Equipment.HEAD_SLOT) > 1 && (other.getEquipment()
						.get(Equipment.HEAD_SLOT)
						.getDefinition()
						.isFullHelm() || other.getEquipment().get(Equipment.HEAD_SLOT).getDefinition().isFullMask())) {
					buf.put(0);
				} else {
					buf.putShort(0x100 + appearance.getHead());
				}
				if(other.getEquipment().getId(Equipment.HANDS_SLOT) > 1) {
					buf.putShort(0x8000 + other.getEquipment().getId(Equipment.HANDS_SLOT));
				} else {
					buf.putShort(0x100 + appearance.getHands());
				}
				if(other.getEquipment().getId(Equipment.FEET_SLOT) > 1) {
					buf.putShort(0x8000 + other.getEquipment().getId(Equipment.FEET_SLOT));
				} else {
					buf.putShort(0x100 + appearance.getFeet());
				}
				if(appearance.isMale()) {
					if(other.getEquipment().getId(Equipment.HEAD_SLOT) > 1 && other.getEquipment()
							.get(Equipment.HEAD_SLOT)
							.getDefinition()
							.isFullMask()) {
						buf.put(0);
					} else {
						buf.putShort(0x100 + appearance.getBeard());
					}
				} else {
					buf.put(0);
				}
			} else {
				buf.putShort(-1);
				buf.putShort(other.getPlayerNpc());
			}
			buf.put(appearance.getHairColor());
			buf.put(appearance.getTorsoColor());
			buf.put(appearance.getLegColor());
			buf.put(appearance.getFeetColor());
			buf.put(appearance.getSkinColor());

			buf.putShort(other.getStandIndex() != 0x328 ? other.getStandIndex() : other.getWeaponAnimation().getStanding());
			buf.putShort(other.getTurnIndex());
			buf.putShort(other.getWalkIndex() != 0x333 ? other.getWalkIndex() : other.getWeaponAnimation().getWalking());
			buf.putShort(other.getTurn180Index());
			buf.putShort(other.getTurn90CWIndex());
			buf.putShort(other.getTurn90CCWIndex());
			buf.putShort(other.getRunIndex() != 0x338 ? other.getRunIndex() : other.getWeaponAnimation().getRunning());

			buf.putLong(other.credentials.usernameHash);
			buf.put(other.determineCombatLevel() < 3 ? 3 : other.determineCombatLevel());
			buf.put(other.isIronMan() ? 1 : 0);
			msg.put(buf.getBuffer().writerIndex(), ByteTransform.C);
			msg.putBytes(buf.getBuffer());
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			buf.release();
		}
		return -1;
	}

}
