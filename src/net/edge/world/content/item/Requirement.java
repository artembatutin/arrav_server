package net.edge.world.content.item;

import net.edge.utils.TextUtils;
import net.edge.world.content.skill.SkillData;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * The container class that represents one equipment requirement.
 * @author lare96 <http://github.com/lare96>
 */
public final class Requirement {
	
	/**
	 * The hash collection of equipment requirements.
	 */
	public static final Map<Integer, Requirement[]> REQUIREMENTS = new HashMap<>();
	
	/**
	 * The level of this equipment requirement.
	 */
	private final int level;
	
	/**
	 * The skill identifier for this equipment requirement.
	 */
	private final SkillData skill;
	
	/**
	 * Creates a new {@link Requirement}.
	 * @param level the level of this equipment requirement.
	 * @param skill the skill identifier for this equipment requirement.
	 */
	public Requirement(int level, SkillData skill) {
		this.level = level;
		this.skill = skill;
	}
	
	/**
	 * A substitute for {@link Object#clone()} that creates another 'copy' of
	 * this instance. The created copy is <i>safe</i> meaning it does not hold
	 * <b>any</b> references to the original instance.
	 * @return a reference-free copy of this instance.
	 */
	public Requirement copy() {
		return new Requirement(level, skill);
	}
	
	/**
	 * Determines if {@code player} can equip {@code item} based on its
	 * equipment requirements.
	 * @param player the player that is equipping the item.
	 * @param item   the item being equipped.
	 * @return {@code true} if the player can equip the item, {@code false}
	 * otherwise.
	 */
	public static boolean canEquip(Player player, Item item) {
		if(item == null)
			return true;
		Requirement[] req = REQUIREMENTS.get(item.getId());
		if(req == null)
			return true;
		for(Requirement r : req) {
			if(player.getSkills()[r.skill.getId()].getRealLevel() < r.level) {
				String append = TextUtils.appendIndefiniteArticle(SkillData.values()[r.skill.getId()].toString());
				player.message("You need " + append + " " + "level of " + r.level + " to equip this item.");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Gets the level of this equipment requirement.
	 * @return the level.
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * Gets the skill identifier for this equipment requirement.
	 * @return the skill identifier.
	 */
	public SkillData getSkill() {
		return skill;
	}
}