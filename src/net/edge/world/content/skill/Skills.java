package net.edge.world.content.skill;

import net.edge.utils.TextUtils;
import net.edge.world.GameConstants;
import net.edge.world.World;
import net.edge.world.content.skill.agility.impl.Shortcuts;
import net.edge.world.content.skill.agility.impl.barb.BarbarianOutpostAgility;
import net.edge.world.content.skill.agility.impl.gnome.GnomeStrongholdAgility;
import net.edge.world.content.skill.agility.impl.wild.WildernessAgility;
import net.edge.world.content.skill.crafting.PotClaying;
import net.edge.world.content.skill.hunter.Hunter;
import net.edge.world.content.skill.mining.Mining;
import net.edge.world.content.skill.runecrafting.Runecrafting;
import net.edge.world.content.skill.smithing.Smelting;
import net.edge.world.content.skill.thieving.impl.Stalls;
import net.edge.world.content.skill.woodcutting.Woodcutting;
import net.edge.world.model.node.entity.model.Graphic;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.entity.update.UpdateFlag;
import net.edge.world.model.node.object.ObjectNode;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * The class that contains methods to handle the functionality of skills.
 * @author lare96 <http://github.com/lare96>
 */
public final class Skills {
	
	public static final int MAXED_GRAPHICS = 1637;
	
	/**
	 * The attack skill identifier for the skill array.
	 */
	public static final int ATTACK = 0;
	
	/**
	 * The defence skill identifier for the skill array.
	 */
	public static final int DEFENCE = 1;
	
	/**
	 * The strength skill identifier for the skill array.
	 */
	public static final int STRENGTH = 2;
	
	/**
	 * The hitpoints skill identifier for the skill array.
	 */
	public static final int HITPOINTS = 3;
	
	/**
	 * The ranged skill identifier for the skill array.
	 */
	public static final int RANGED = 4;
	
	/**
	 * The prayer skill identifier for the skill array.
	 */
	public static final int PRAYER = 5;
	
	/**
	 * The magic skill identifier for the skill array.
	 */
	public static final int MAGIC = 6;
	
	/**
	 * The cooking skill identifier for the skill array.
	 */
	public static final int COOKING = 7;
	
	/**
	 * The woodcutting skill identifier for the skill array.
	 */
	public static final int WOODCUTTING = 8;
	
	/**
	 * The fletching skill identifier for the skill array.
	 */
	public static final int FLETCHING = 9;
	
	/**
	 * The fishing skill identifier for the skill array.
	 */
	public static final int FISHING = 10;
	
	/**
	 * The firemaking skill identifier for the skill array.
	 */
	public static final int FIREMAKING = 11;
	
	/**
	 * The crafting skill identifier for the skill array.
	 */
	public static final int CRAFTING = 12;
	
	/**
	 * The smithing skill identifier for the skill array.
	 */
	public static final int SMITHING = 13;
	
	/**
	 * The mining skill identifier for the skill array.
	 */
	public static final int MINING = 14;
	
	/**
	 * The herblore skill identifier for the skill array.
	 */
	public static final int HERBLORE = 15;
	
	/**
	 * The agility skill identifier for the skill array.
	 */
	public static final int AGILITY = 16;
	
	/**
	 * The thieving skill identifier for the skill array.
	 */
	public static final int THIEVING = 17;
	
	/**
	 * The slayer skill identifier for the skill array.
	 */
	public static final int SLAYER = 18;
	
	/**
	 * The farming skill identifier for the skill array.
	 */
	public static final int FARMING = 19;
	
	/**
	 * The runecrafting skill identifier for the skill array.
	 */
	public static final int RUNECRAFTING = 20;
	
	/**
	 * The hunter skill identifier for the skill array.
	 */
	public static final int HUNTER = 21;
	
	/**
	 * The construction skill identifier for the skill array.
	 */
	public static final int CONSTRUCTION = 22;
	
	/**
	 * The summoning skill identifier for the skill array.
	 */
	public static final int SUMMONING = 23;
	
	/**
	 * The dungeoneering skill identifier for the skill array.
	 */
	public static final int DUNGEONEERING = 24;
	
	/**
	 * The array containing all the combat skills in order.
	 */
	public static final int[] COMBAT_SKILLS = new int[]{ATTACK, DEFENCE, HITPOINTS, STRENGTH, RANGED, MAGIC};
	
	/**
	 * The experience multiplier that skill related skills experience will be calculated with.
	 */
	private static final int SKILL_EXPERIENCE_MULTIPLIER = 40;
	
	/**
	 * The experience multiplier that combat related skills experience will be calculated with.
	 */
	private static final int PRAYER_EXPERIENCE_MULTIPLIER = 300;
	
	/**
	 * The experience multiplier that combat related skills experience will be calculated with.
	 */
	private static final int COMBAT_EXPERIENCE_MULTIPLER = 150;
	
	/**
	 * The default constructor.
	 * @throws UnsupportedOperationException if this class is instantiated.
	 */
	private Skills() {
		throw new UnsupportedOperationException("This class cannot be " + "instantiated!");
	}
	
	/**
	 * Executes a certain skill.
	 * @param player the player executing the skill.
	 * @param object the object interacted with.
	 * @param id     the id of the object.
	 * @return <true> if the skill was started, <false> otherwise.
	 */
	public static boolean executeObjectantSkills(Player player, ObjectNode object, int id) {
		switch(id) {
			case 1:
				if(Hunter.claim(player, object)) {
					return true;
				}
				if(Hunter.pickup(player, object)) {
					return true;
				}
				if(Woodcutting.produce(player, object)) {
					return true;
				}
				if(Mining.produce(player, object)) {
					return true;
				}
				if(Runecrafting.produce(player, object)) {
					return true;
				}
				if(GnomeStrongholdAgility.execute(player, object)) {
					return true;
				}
				if(BarbarianOutpostAgility.execute(player, object)) {
					return true;
				}
				if(WildernessAgility.execute(player, object)) {
					return true;
				}
				if(Shortcuts.execute(player, object)) {
					return true;
				}
				if(PotClaying.openInterface(player, object)) {
					return true;
				}
				break;
			case 2:
				if(Stalls.steal(player, object)) {
					return true;
				}
				if(Mining.prospect(player, object)) {
					return true;
				}
				if(Smelting.openInterface(player, object)) {
					return true;
				}
				break;
		}
		return false;
	}
	
	/**
	 * Attempts to add {@code amount} of experience for {@code player}.
	 * @param player the player to add the experience for.
	 * @param amount the amount of experience that will be added.
	 * @param skill  the skill to add the experience for.
	 */
	public static void experience(Player player, double amount, int skill) {
		if(amount <= 0)
			return;
		int oldLevel = player.getSkills()[skill].getRealLevel();
		boolean combatLevel = IntStream.rangeClosed(0, COMBAT_SKILLS.length).anyMatch(id -> id == skill);
		amount *= skill == Skills.PRAYER ? Skills.PRAYER_EXPERIENCE_MULTIPLIER : combatLevel ? Skills.COMBAT_EXPERIENCE_MULTIPLER : Skills.SKILL_EXPERIENCE_MULTIPLIER;
		amount *= GameConstants.EXPERIENCE_MULTIPLIER;
		player.getSkills()[skill].increaseExperience(amount);
		if(oldLevel < 99) {
			int newLevel = player.getSkills()[skill].getLevelForExperience();
			if(oldLevel < newLevel) {
				if(skill != 3) {
					player.getSkills()[skill].setLevel(newLevel, true);
				} else {
					int old = player.getSkills()[skill].getLevel();
					if(old + 10 < 990)
						player.getSkills()[skill].setLevel(old + 10, false);
				}
				SkillData data = SkillData.values()[skill];
				String append = TextUtils.appendIndefiniteArticle(data.toString());
				player.getMessages().sendString("@dre@Congratulations, you've just advanced " + append + " level!", 4268);
				player.getMessages().sendString("You have now reached level " + newLevel + "!", 4269);
				player.message("Congratulations, you've just advanced " + append + " level!");
				player.getMessages().sendChatInterface(data.getChatbox());
				if(newLevel == 99 || newLevel == 120) {
					String append_max = TextUtils.appendIndefiniteArticleNoVowel(data.toString());
					player.graphic(new Graphic(MAXED_GRAPHICS));
					World.message("@blu@[Global Announcements] @red@" + player.getFormatUsername() + "@blu@ has just reached level " + newLevel + " " + append_max + ".");
				} else {
					player.graphic(new Graphic(199));
				}
				player.getFlags().flag(UpdateFlag.APPEARANCE);
				if(newLevel >= player.getSkills()[skill].getGoal()) {
					player.getSkills()[skill].setGoal(0);
					player.getMessages().sendSkillGoal(skill, 0);
				}
			}
		}
		Skills.refresh(player, skill);
	}
	
	/**
	 * Gets the minimum experience in said level.
	 * @param level The level to get minimum experience for.
	 * @return The least amount of experience needed to achieve said level.
	 */
	public static int getExperienceForLevel(int level) {
		int points = 0;
		int output = 0;
		for(int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if(lvl >= level) {
				return output;
			}
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}
	
	/**
	 * Sends {@code skill} to the client which will refresh it for
	 * {@code player}.
	 * @param player the player to refresh the skill for.
	 * @param skill  the skill that will be refreshed.
	 */
	public static void refresh(Player player, int skill) {
		Skill s = player.getSkills()[skill];
		if(s == null) {
			s = new Skill();
			if(skill == Skills.HITPOINTS) {
				s.setLevel(100, false);
				s.setExperience(1300);
			}
			player.getSkills()[skill] = s;
		}
		
		player.getMessages().sendSkill(skill, s.getLevel(), (int) s.getExperience());
	}
	
	/**
	 * Sends {@code skills} to the client which will refresh it for
	 * {@code player}.
	 * @param player the player to refresh the skills for.
	 * @param skills the skills that will be refreshed.
	 */
	public static void refresh(Player player, int... skills) {
		Arrays.stream(skills).forEach(it -> refresh(player, it));
	}
	
	/**
	 * Sends skills to the client which will refresh them for {@code player}.
	 * @param player the player to refresh the skill for.
	 */
	public static void refreshAll(Player player) {
		for(int i = 0; i < player.getSkills().length; i++) {
			refresh(player, i);
			player.getMessages().sendSkillGoal(i, player.getSkills()[i].getGoal());
		}
	}
	
	/**
	 * Creates an array of skills for {@code player}.
	 * @param player the player to register it for.
	 */
	public static void create(Player player) {
		for(int i = 0; i < player.getSkills().length; i++) {
			player.getSkills()[i] = new Skill();
			if(i == Skills.HITPOINTS) {
				player.getSkills()[i].setRealLevel(10);
				player.getSkills()[i].setLevel(100, false);
				player.getSkills()[i].setExperience(1300);
			}
		}
	}
	
	/**
	 * Restores {@code skill} back to its original level for {@code player}.
	 * @param player the player to restore the skill for.
	 * @param skill  the skill to restore.
	 */
	public static void restore(Player player, int skill) {
		player.getSkills()[skill].setLevel(player.getSkills()[skill].getRealLevel() * (skill == 3 ? 10 : 1), skill != 3);
		refresh(player, skill);
	}
	
	/**
	 * Restores skills back their its original levels for {@code player}.
	 * @param player the player to restore the skills for.
	 */
	public static void restoreAll(Player player) {
		IntStream.range(0, player.getSkills().length).forEach(it -> restore(player, it));
	}
}
