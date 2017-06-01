package net.edge.content.item;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.content.container.impl.Equipment;
import net.edge.content.dialogue.Expression;
import net.edge.content.dialogue.impl.GiveItemDialogue;
import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.dialogue.impl.PlayerDialogue;
import net.edge.content.skill.Skills;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.EnumSet;
import java.util.Optional;

/**
 * An enumeration of skill cape emotes.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public enum Skillcape {
	ATTACK_CAPE(9747, 4959, 823, Skills.ATTACK, 4288),
	STRENGTH_CAPE(9750, 4981, 828, Skills.STRENGTH, 8270),
	DEFENCE_CAPE(9753, 4961, 824, Skills.DEFENCE, 705),
	RANGING_CAPE(9756, 4973, 832, Skills.RANGED, 682),
	PRAYER_CAPE(9759, 4979, 829, Skills.PRAYER, 802),
	MAGIC_CAPE(9762, 4939, 813, Skills.MAGIC, 1658),
	RUNECRAFT_CAPE(9765, 4947, 817, Skills.RUNECRAFTING, 5913),
	HITPOINTS_CAPE(9768, 4971, 833, Skills.HITPOINTS, 961),
	AGILITY_CAPE(9771, 4977, 830, Skills.AGILITY, 437),
	HERBLORE_CAPE(9774, 4969, 835, Skills.HERBLORE, 455),
	THIEVING_CAPE(9777, 4965, 826, Skills.THIEVING, 2270),
	CRAFTING_CAPE(9780, 4949, 818, Skills.CRAFTING, 805),
	FLETCHING_CAPE(9783, 4937, 812, Skills.FLETCHING, 1281),
	SLAYER_CAPE(9786, 4967, 1656, Skills.SLAYER, 8275),
	CONSTRUCT_CAPE(9768, 4953, 820, Skills.CONSTRUCTION, 4247),
	MINING_CAPE(9792, 4941, 814, Skills.MINING, 3295),
	SMITHING_CAPE(9795, 4943, 815, Skills.SMITHING, 604),
	FISHING_CAPE(9798, 4951, 819, Skills.FISHING, 308),
	COOKING_CAPE(9801, 4955, 821, Skills.COOKING, 847),
	FIREMAKING_CAPE(9804, 4975, 831, Skills.FIREMAKING, 4946),
	WOODCUTTING_CAPE(9807, 4957, 822, Skills.WOODCUTTING, 4906),
	FARMING_CAPE(9810, 4963, 825, Skills.FARMING, 3299),
	SUMMONING_CAPE(12169, 8525, 1515, Skills.SUMMONING, 6790),
	HUNTER_CAPE(9948, 5158, 907, Skills.HUNTER, 5113),
	VETERAN_CAPE(20763, 352, 1446, -1, -1);
	
	private static final ImmutableSet<Skillcape> VALUES = Sets.immutableEnumSet(EnumSet.allOf(Skillcape.class));
	
	/**
	 * The item identification for this skillcape.
	 */
	private final int item;
	
	/**
	 * The animation id of the emote.
	 */
	private final int animation;
	
	/**
	 * The graphic id of the emote.
	 */
	private final int graphic;
	
	/**
	 * The skill id the cape is representing.
	 */
	private final int skill;
	
	/**
	 * The skill master for this skill.
	 */
	private final int master;
	
	/**
	 * Constructs a new {@link Skillcape}.
	 * @param item      {@link #item}.
	 * @param animation {@link #animation}.
	 * @param graphic   {@link #graphic}.
	 * @param skill     {@link #skill}.
	 * @param master    {@link #master}.
	 */
	Skillcape(int item, int animation, int graphic, int skill, int master) {
		this.item = item;
		this.animation = animation;
		this.graphic = graphic;
		this.skill = skill;
		this.master = master;
	}
	
	/**
	 * Verifies if the player can wear the skill cape.
	 * @param player The player wearing a new item.
	 * @param item   The item the player is trying to wear.
	 * @return {@code false} if the player hasn't met the criteria, {@code true} otherwise.
	 */
	public static boolean verifySkillCape(Player player, Item item) {
		Skillcape c = VALUES.stream().filter(t -> t.getItem() == item.getId()).findAny().orElse(null);
		
		if(c == null) {
			return true;
		}
		
		if(c.getSkill() != -1 && player.getSkills()[c.getSkill()].getRealLevel() != 99) {
			player.message("You need to max out the specific skill before wearing this cape.");
			return false;
		}
		return true;
	}
	
	/**
	 * Handles the skill cape emote.
	 * @param player The player clicking the button.
	 * @param button The button clicked.
	 * @return {@code true} if the emote was handled, {@code false} otherwise.
	 */
	public static boolean handle(Player player, int button) {
		if(button != 74108) {
			return false;
		}
		
		Skillcape cape = VALUES.stream().filter(c -> player.getEquipment().get(Equipment.CAPE_SLOT).getId() == c.getItem()).findAny().orElse(null);
		
		if(cape == null) {
			return false;
		}
		
		player.animation(new Animation(cape.getAnimation()));
		player.graphic(new Graphic(cape.getGraphic()));
		return true;
	}
	
	/**
	 * Attempts to reward the player by giving him the skillcape.
	 * @param player the player to give the skillcape to.
	 * @param npcId  the npc id the player interacted with.
	 * @param option the action option handled.
	 * @return {@code true} if the player got into the dialogue stage, {@code false} otherwise.
	 */
	public static boolean reward(Player player, int npcId, int option) {
		Skillcape c = VALUES.stream().filter(cape -> cape.getMaster() == npcId).findAny().orElse(null);
		
		if(c == null || (option == 3 && !c.equals(Skillcape.RUNECRAFT_CAPE))) {
			return false;
		}
		
		if(c.getSkill() != -1 && player.getSkills()[c.getSkill()].getRealLevel() != 99) {
			player.message("You haven't mastered this skill yet.");
			return false;
		}
		
		player.getDialogueBuilder().append(new NpcDialogue(c.getMaster(), "You want to acquire the master skillcape for 100k?"), new OptionDialogue(t -> {
			if(t == OptionDialogue.OptionType.FIRST_OPTION) {
				if(player.getInventory().contains(new Item(995, 100_000))) {
					if(player.getInventory().remaining() >= 1) {
						player.getDialogueBuilder().append(new GiveItemDialogue(new Item(c.getItem(), 1), "You received the skill cape!", Optional.of(() -> player.getInventory().remove(new Item(995, 100000)))));
					}
				} else {
					player.getDialogueBuilder().append(new PlayerDialogue(Expression.SAD, "I don't have enough for this skill cape."));
				}
				
			} else if(t == OptionDialogue.OptionType.SECOND_OPTION) {
				player.getMessages().sendCloseWindows();
			}
		}, "Yes please!", "No thanks."));
		return true;
	}
	
	/**
	 * Attempts to reward the player by giving him the skillcape.
	 * @param player the player to give the skillcape to.
	 * @param item   the item id.
	 * @return {@code true} if the player got into the dialogue stage, {@code false} otherwise.
	 */
	public static boolean buy(Player player, int item) {
		Skillcape c = VALUES.stream().filter(cape -> cape.getItem() == item).findAny().orElse(null);
		
		if(c == null) {
			return false;
		}
		
		if(c.getSkill() != -1 && player.getSkills()[c.getSkill()].getRealLevel() != 99) {
			player.message("You haven't mastered this skill yet.");
			return true;
		}
		return false;
	}
	
	public int getItem() {
		return item;
	}
	
	public int getAnimation() {
		return animation;
	}
	
	public int getGraphic() {
		return graphic;
	}
	
	public int getSkill() {
		return skill;
	}
	
	public int getMaster() {
		return master;
	}
}
