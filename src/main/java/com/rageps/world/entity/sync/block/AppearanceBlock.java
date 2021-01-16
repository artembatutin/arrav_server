package com.rageps.world.entity.sync.block;


import com.rageps.world.entity.actor.player.PlayerAppearance;
import com.rageps.world.entity.item.container.ItemContainer;

/**
 * The appearance {@link SynchronizationBlock}. Only players can utilise this block.
 *
 * @author Graham
 */
public final class AppearanceBlock extends SynchronizationBlock {

	/**
	 * The player's appearance.
	 */
	private final PlayerAppearance appearance;

	/**
	 * The player's combat level.
	 */
	private final int combat;

	/**
	 * The player's equipment.
	 */
	private final ItemContainer equipment;

	/**
	 * Whether or not the player is skulled.
	 */
	private final int skull;

	/**
	 * The player's name.
	 */
	private final long name;

	/**
	 * The npc id this player is appearing as, if any.
	 */
	private final int npcId;

	/**
	 * The player's prayer icon.
	 */
	private final int headIcon;

	/**
	 * The player's total skill level (or 0).
	 */
	private final int skill;

	/**
	 * Creates the appearance block. Assumes that the player is not appearing as an npc.
	 *
	 * @param name The player's username, encoded to base 37.
	 * @param appearance The {@link PlayerAppearance}.
	 * @param combat The player's combat.
	 * @param skill The player's skill, or 0 if showing the combat level.
	 * @param equipment The player's equipment.
	 * @param headIcon The head icon id of the player.
	 * @param skull Whether or not the player is skulled.
	 */
	AppearanceBlock(long name, PlayerAppearance appearance, int combat, int skill, ItemContainer equipment, int headIcon, int skull) {
		this(name, appearance, combat, skill, equipment, headIcon, skull, -1);
	}

	/**
	 * Creates the appearance block.
	 *
	 * @param name The player's username, encoded to base 37.
	 * @param appearance The {@link PlayerAppearance}.
	 * @param combat The player's combat.
	 * @param skill The player's skill, or 0 if showing the combat level.
	 * @param equipment The player's equipment.
	 * @param headIcon The prayer icon id of this player.
	 * @param skull Whether or not the player is skulled.
	 * @param npcId The npc id of the player, if they are appearing as an npc, (otherwise {@code -1}).
	 */
	AppearanceBlock(long name, PlayerAppearance appearance, int combat, int skill, ItemContainer equipment, int headIcon, int skull, int npcId) {
		this.name = name;
		this.appearance = appearance;
		this.combat = combat;
		this.skill = skill;
		this.equipment = equipment;//equipment.duplicate(); todo - does it need to be cloned?
		this.headIcon = headIcon;
		this.skull = skull;
		this.npcId = npcId;
	}

	/**
	 * If the player is appearing as an npc or not.
	 *
	 * @return {@code true} if the player is appearing as an npc, otherwise {@code false}.
	 */
	public boolean appearingAsNpc() {
		return npcId != -1;
	}

	/**
	 * Gets the player's {@link PlayerAppearance}.
	 *
	 * @return The player's appearance.
	 */
	public PlayerAppearance getAppearance() {
		return appearance;
	}

	/**
	 * Gets the player's combat level.
	 *
	 * @return The player's combat level.
	 */
	public int getCombatLevel() {
		return combat;
	}

	/**
	 * Gets the player's equipment.
	 *
	 * @return The player's equipment.
	 */
	public ItemContainer getEquipment() {
		return equipment;
	}

	/**
	 * Whether or not the player is skulled.
	 *
	 * @return {@code true} if the player is skulled, otherwise {@code false}.
	 */
	public int getSkull() {
		return skull;
	}

	/**
	 * Gets the player's name.
	 *
	 * @return The player's name.
	 */
	public long getName() {
		return name;
	}

	/**
	 * Gets the npc id the player is appearing as, or {@code -1} if the player is not appearing as one.
	 *
	 * @return The npc id.
	 */
	public int getNpcId() {
		return npcId;
	}

	/**
	 * Gets the player's head icon.
	 *
	 * @return The head icon.
	 */
	public int getHeadIcon() {
		return headIcon;
	}

	/**
	 * Gets the player's skill level.
	 *
	 * @return The player's skill level.
	 */
	public int getSkillLevel() {
		return skill;
	}

}