package net.edge.content.skill;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.net.packet.out.SendEnterAmount;
import net.edge.net.packet.out.SendSkillGoal;
import net.edge.util.TextUtils;
import net.edge.world.entity.actor.player.Player;

import java.util.EnumSet;
import java.util.Optional;

/**
 * The enumerated type whose elements represent data for the skills.
 * @author lare96 <http://github.com/lare96>
 */
public enum SkillData {
	ATTACK(Skills.ATTACK, 6247, 33206),
	DEFENCE(Skills.DEFENCE, 6253, 33212),
	STRENGTH(Skills.STRENGTH, 6206, 33209),
	HITPOINTS(Skills.HITPOINTS, 6216, 33207),
	RANGED(Skills.RANGED, 4443, 33215),
	PRAYER(Skills.PRAYER, 6242, 33218),
	MAGIC(Skills.MAGIC, 6211, 33221),
	COOKING(Skills.COOKING, 6226, 33217),
	WOODCUTTING(Skills.WOODCUTTING, 4272, 33223),
	FLETCHING(Skills.FLETCHING, 6231, 33222),
	FISHING(Skills.FISHING, 6258, 33214),
	FIREMAKING(Skills.FIREMAKING, 4282, 33220),
	CRAFTING(Skills.CRAFTING, 6263, 33219),
	SMITHING(Skills.SMITHING, 6221, 33211),
	MINING(Skills.MINING, 4416, 33208),
	HERBLORE(Skills.HERBLORE, 6237, 34157),
	AGILITY(Skills.AGILITY, 4277, 33210),
	THIEVING(Skills.THIEVING, 4261, 33216),
	SLAYER(Skills.SLAYER, 12122, 47130),
	FARMING(Skills.FARMING, 5267, 54104),
	RUNECRAFTING(Skills.RUNECRAFTING, 4267, 33224),
	CONSTRUCTION(Skills.CONSTRUCTION, 7267, 105243),
	HUNTER(Skills.HUNTER, 8267, 105244),
	SUMMONING(Skills.SUMMONING, 9267, 105245),
	DUNGEONEERING(Skills.DUNGEONEERING, 10267, 105246);
	
	/**
	 * Caches our enum values.
	 */
	private static final ImmutableSet<SkillData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(SkillData.class));
	
	/**
	 * The identification for this skill in the skills array.
	 */
	private final int id;
	
	/**
	 * The chatbox interface displayed on level up.
	 */
	private final int chatbox;
	
	/**
	 * The skill interface button click.
	 */
	private final int button;
	
	/**
	 * Creates a new {@link SkillData}.
	 * @param id      the identification for this skill in the skills array.
	 * @param chatbox the chatbox interface displayed on level up.
	 * @param button  the skill interface button identifier.
	 */
	SkillData(int id, int chatbox, int button) {
		this.id = id;
		this.chatbox = chatbox;
		this.button = button;
	}
	
	/**
	 * Gets the Skill value which id matches {@code id}.
	 * @param id the id of the skill to fetch Skill instance for.
	 * @return the Skill instance.
	 */
	public static SkillData forId(int id) {
		return VALUES.stream().filter(skill -> skill.getId() == id).findAny().get();
	}
	
	/**
	 * Gets the Skill value which button matches {@code id}.
	 * @param button the button of the skill to fetch Skill instance for.
	 * @return the Skill instance.
	 */
	public static Optional<SkillData> forButton(int button) {
		return VALUES.stream().filter(skill -> skill.getButton() == button).findAny();
	}
	
	/**
	 * Sends the input box for the goal levels.
	 * @param player the player to send for.
	 * @param button the button id clicked.
	 * @return <true> if the box was sent, <false> otherwise.
	 */
	public static boolean sendEnterGoalLevel(Player player, int button) {
		SkillData data = forButton(button).orElse(null);
		
		if(data == null) {
			return false;
		}
		
		player.getAttr().get("goalSettingSkill").set(data.getId());
		player.out(new SendEnterAmount("What level you would like to accomplish?", t -> () -> {
			int skill = data.id;
			int amount = Integer.parseInt(t);
			if(amount <= player.getSkills()[skill].getRealLevel() || amount > 99) {
				player.message("You cannot set this goal.");
				return;
			}
			player.out(new SendSkillGoal(skill, amount));
			player.getSkills()[skill].setGoal(amount);
		}));
		return true;
	}
	
	@Override
	public final String toString() {
		return TextUtils.capitalize(name().toLowerCase().replaceAll("_", " "));
	}
	
	/**
	 * Gets the identification for this skill in the skills array.
	 * @return the identification for this skill.
	 */
	public final int getId() {
		return id;
	}
	
	/**
	 * Gets the chatbox interface displayed on level up.
	 * @return the chatbox interface.
	 */
	public final int getChatbox() {
		return chatbox;
	}
	
	/**
	 * Gets the skill interface button identifier of this skill.
	 * @return the button identifier.
	 */
	public final int getButton() {
		return button;
	}
}