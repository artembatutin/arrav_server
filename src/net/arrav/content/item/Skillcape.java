package net.arrav.content.item;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.arrav.action.impl.ButtonAction;
import net.arrav.action.impl.MobAction;
import net.arrav.content.dialogue.Expression;
import net.arrav.content.dialogue.impl.GiveItemDialogue;
import net.arrav.content.dialogue.impl.NpcDialogue;
import net.arrav.content.dialogue.impl.OptionDialogue;
import net.arrav.content.dialogue.impl.PlayerDialogue;
import net.arrav.content.skill.Skills;
import net.arrav.world.Animation;
import net.arrav.world.Graphic;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;
import net.arrav.world.entity.item.container.impl.Equipment;

import java.util.EnumSet;
import java.util.Optional;

/**
 * An enumeration of skill cape emotes.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public enum Skillcape {
	ATTACK_CAPE(9747, 9749, 4959, 823, Skills.ATTACK, 4288),
	STRENGTH_CAPE(9750, 9752, 4981, 828, Skills.STRENGTH, 8270),
	DEFENCE_CAPE(9753, 9755, 4961, 824, Skills.DEFENCE, 705),
	RANGING_CAPE(9756, 9758, 4973, 832, Skills.RANGED, 682),
	PRAYER_CAPE(9759, 9761, 4979, 829, Skills.PRAYER, 802),
	MAGIC_CAPE(9762, 9764, 4939, 813, Skills.MAGIC, 1658),
	RUNECRAFT_CAPE(9765, 9767, 4947, 817, Skills.RUNECRAFTING, 5913),
	HITPOINTS_CAPE(9768, 9770, 4971, 833, Skills.HITPOINTS, 6180),
	AGILITY_CAPE(9771, 9773, 4977, 830, Skills.AGILITY, 437),
	HERBLORE_CAPE(9774, 9776, 4969, 835, Skills.HERBLORE, 455),
	THIEVING_CAPE(9777, 9779, 4965, 826, Skills.THIEVING, 2270),
	CRAFTING_CAPE(9780, 9782, 4949, 818, Skills.CRAFTING, 805),
	FLETCHING_CAPE(9783, 9785, 4937, 812, Skills.FLETCHING, 1281),
	SLAYER_CAPE(9786, 9788, 4967, 1656, Skills.SLAYER, 8275),
	CONSTRUCT_CAPE(9789, 9791, 4953, 820, Skills.CONSTRUCTION, 4247),
	MINING_CAPE(9792, 9794, 4941, 814, Skills.MINING, 3295),
	SMITHING_CAPE(9795, 9797, 4943, 815, Skills.SMITHING, 604),
	FISHING_CAPE(9798, 9800, 4951, 819, Skills.FISHING, 308),
	COOKING_CAPE(9801, 9803, 4955, 821, Skills.COOKING, 847),
	FIREMAKING_CAPE(9804, 9806, 4975, 831, Skills.FIREMAKING, 4946),
	WOODCUTTING_CAPE(9807, 9809, 4957, 822, Skills.WOODCUTTING, 4906),
	FARMING_CAPE(9810, 9812, 4963, 825, Skills.FARMING, 3299),
	SUMMONING_CAPE(12169, 12171, 8525, 1515, Skills.SUMMONING, 6790),
	HUNTER_CAPE(9948, 9950, 5158, 907, Skills.HUNTER, 5113),
	VETERAN_CAPE(20763, 20764, 352, 1446, -1, -1);
	
	private static final ImmutableSet<Skillcape> VALUES = Sets.immutableEnumSet(EnumSet.allOf(Skillcape.class));
	
	/**
	 * The item identification for this skillcape.
	 */
	private final int item;
	
	/**
	 * The hood item identification for this skillcape.
	 */
	private final int hood;
	
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
	 * @param hood      {@link #hood}.
	 * @param animation {@link #animation}.
	 * @param graphic   {@link #graphic}.
	 * @param skill     {@link #skill}.
	 * @param master    {@link #master}.
	 */
	Skillcape(int item, int hood, int animation, int graphic, int skill, int master) {
		this.item = item;
		this.hood = hood;
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
		Skillcape c = getSkillcape(item.getId());
		
		if(c == null) {
			return true;
		}
		
		if(c.getSkill() != -1 && player.getSkills()[c.getSkill()].getRealLevel() != 99) {
			player.dialogue(new NpcDialogue(c.getMaster(), Expression.CONFUSED, "You haven't mastered this skill yet."));
			return false;
		}
		return true;
	}
	
	public static void action() {
		ButtonAction b = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				Skillcape cape = getSkillcape(player.getEquipment().get(Equipment.CAPE_SLOT).getId());
				if(cape == null) {
					return true;
				}
				player.animation(new Animation(cape.getAnimation()));
				player.graphic(new Graphic(cape.getGraphic()));
				return true;
			}
		};
		b.register(74108);
		for(Skillcape c : Skillcape.values()) {
			MobAction e = new MobAction() {
				@Override
				public boolean click(Player player, Mob npc, int click) {
					if(c.getSkill() != -1 && player.getSkills()[c.getSkill()].getRealLevel() != 99) {
						player.dialogue(new NpcDialogue(c.getMaster(), Expression.CONFUSED, "You haven't mastered this skill yet."));
						return false;
					}
					player.getDialogueBuilder().append(new NpcDialogue(c.getMaster(), "You want to acquire the master skillcape for 500k?"), new OptionDialogue(t -> {
						if(t == OptionDialogue.OptionType.FIRST_OPTION) {
							if(player.getInventory().contains(new Item(995, 500_000))) {
								if(player.getInventory().remaining() >= 2) {
									int item = Skills.determineSkillcape(player, c);
									player.getDialogueBuilder().append(new GiveItemDialogue(new Item(item, 1), "You received the skill cape!", Optional.of(() -> {
										player.getInventory().remove(new Item(995, 500000));
										player.getInventory().add(new Item(c.hood, 1));
									})));
								} else {
									player.getDialogueBuilder().append(new PlayerDialogue(Expression.SAD, "I don't have enough inventory space for this skill cape."));
								}
							} else {
								player.getDialogueBuilder().append(new PlayerDialogue(Expression.SAD, "I don't have enough coins for this skill cape."));
							}
							
						} else if(t == OptionDialogue.OptionType.SECOND_OPTION) {
							player.closeWidget();
						}
					}, "Yes please!", "No thanks."));
					return true;
				}
			};
			e.registerSecond(c.getMaster());
		}
	}
	
	/**
	 * Attempts to reward the player by giving him the skillcape.
	 * @param player the player to give the skillcape to.
	 * @param item   the item id.
	 * @return {@code true} if the player got into the dialogue stage, {@code false} otherwise.
	 */
	public static boolean buy(Player player, int item) {
		Skillcape c = getSkillcape(item);
		
		if(c == null) {
			return false;
		}
		
		if(c.getSkill() != -1 && player.getSkills()[c.getSkill()].getRealLevel() != 99) {
			player.dialogue(new NpcDialogue(c.getMaster(), Expression.CONFUSED, "You haven't mastered this skill yet."));
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the skillcape by the specified {@code item}.
	 * @param item the item to grab the skillcape from.
	 * @return {@code Skillcape} matching the specified item.
	 */
	private static Skillcape getSkillcape(int item) {
		return VALUES.stream().filter(c -> c.item == item || c.item + 1 == item || c.hood == item).findAny().orElse(null);
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
