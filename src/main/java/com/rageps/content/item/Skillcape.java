package com.rageps.content.item;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rageps.action.impl.ButtonAction;
import com.rageps.action.impl.MobAction;
import com.rageps.content.dialogue.Expression;
import com.rageps.content.dialogue.impl.GiveItemDialogue;
import com.rageps.content.dialogue.impl.NpcDialogue;
import com.rageps.content.dialogue.impl.OptionDialogue;
import com.rageps.content.dialogue.impl.PlayerDialogue;
import com.rageps.content.skill.Skills;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.container.impl.Equipment;
import com.rageps.world.model.Visualize;

import java.util.EnumSet;
import java.util.Optional;

/**
 * An enumeration of skill cape emotes.
 * @author Artem Batutin
 * @autho Tamatea
 */
public enum Skillcape {
	ATTACK_CAPE(new Item(9747), new Item(9749), new Visualize(4959, 823), Skills.ATTACK, 4288),
	STRENGTH_CAPE(new Item(9750), new Item(9752), new Visualize(4981, 828), Skills.STRENGTH, 8270),
	DEFENCE_CAPE(new Item(9753), new Item(9755), new Visualize(4961, 824), Skills.DEFENCE, 705),
	RANGING_CAPE(new Item(9756), new Item(9758), new Visualize(4973, 832), Skills.RANGED, 682),
	PRAYER_CAPE(new Item(9759), new Item(9761), new Visualize(4979, 829), Skills.PRAYER, 802),
	MAGIC_CAPE(new Item(9762), new Item(9764), new Visualize(4939, 813), Skills.MAGIC, 1658),
	RUNECRAFT_CAPE(new Item(9765), new Item(9767), new Visualize(4947, 817), Skills.RUNECRAFTING, 5913),
	HITPOINTS_CAPE(new Item(9768), new Item(9770), new Visualize(4971, 833), Skills.HITPOINTS, 6180),
	AGILITY_CAPE(new Item(9771), new Item(9773), new Visualize(4977, 830), Skills.AGILITY, 437),
	HERBLORE_CAPE(new Item(9774), new Item(9776), new Visualize(4969, 835), Skills.HERBLORE, 455),
	THIEVING_CAPE(new Item(9777), new Item(9779), new Visualize(4965, 826), Skills.THIEVING, 2270),
	CRAFTING_CAPE(new Item(9780), new Item(9782), new Visualize(4949, 818), Skills.CRAFTING, 805),
	FLETCHING_CAPE(new Item(9783), new Item(9785), new Visualize(4937, 812), Skills.FLETCHING, 1281),
	SLAYER_CAPE(new Item(9786), new Item(9788), new Visualize(4967, 1656), Skills.SLAYER, 8275),
	CONSTRUCT_CAPE(new Item(9789), new Item(9791), new Visualize(4953, 820), Skills.CONSTRUCTION, 4247),
	MINING_CAPE(new Item(9792), new Item(9794), new Visualize(4941, 814), Skills.MINING, 3295),
	SMITHING_CAPE(new Item(9795), new Item(9797), new Visualize(4943, 815), Skills.SMITHING, 604),
	FISHING_CAPE(new Item(9798), new Item(9800), new Visualize(4951, 819), Skills.FISHING, 308),
	COOKING_CAPE(new Item(9801), new Item(9803), new Visualize(4955, 821), Skills.COOKING, 847),
	FIREMAKING_CAPE(new Item(9804), new Item(9806), new Visualize(4975, 831), Skills.FIREMAKING, 4946),
	WOODCUTTING_CAPE(new Item(9807), new Item(9809), new Visualize(4957, 822), Skills.WOODCUTTING, 4906),
	FARMING_CAPE(new Item(9810), new Item(9812), new Visualize(4963, 825), Skills.FARMING, 3299),
	SUMMONING_CAPE(new Item(12169), new Item (12171), new Visualize(8525, 1515), Skills.SUMMONING, 6790),
	HUNTER_CAPE(new Item(9948), new Item(9950), new Visualize(5158, 907), Skills.HUNTER, 5113),
	VETERAN_CAPE(new Item(20763), new Item(20764), new Visualize(352, 1446), -1, -1);
	
	private static final ImmutableSet<Skillcape> VALUES = Sets.immutableEnumSet(EnumSet.allOf(Skillcape.class));
	
	/**
	 * The item identification for this skillcape.
	 */
	private final Item item;
	
	/**
	 * The hood item identification for this skillcape.
	 */
	private final Item hood;
	
	/**
	 * The visualization of the emote.
	 */
	private final Visualize visualize;
	
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
	 * @param item {@link #item}.
	 * @param hood {@link #hood}.
	 * @param visualize {@link #visualize}.
	 * @param skill {@link #skill}.
	 * @param master {@link #master}.
	 */
	Skillcape(Item item, Item hood, Visualize visualize, int skill, int master) {
		this.item = item;
		this.hood = hood;
		this.visualize = visualize;
		this.skill = skill;
		this.master = master;
	}
	
	/**
	 * Verifies if the player can wear the skill cape.
	 * @param player The player wearing a new item.
	 * @param item The item the player is trying to wear.
	 * @return {@code false} if the player hasn't met the criteria, {@code true} otherwise.
	 */
	public static boolean verifySkillCape(Player player, Item item) {
		Skillcape c = getSkillcape(item);
		
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
				Skillcape cape = getSkillcape(player.getEquipment().get(Equipment.CAPE_SLOT));
				if(cape == null) {
					player.message("You must be wearing a skillcape to do the emote.");
					return true;
				}
				cape.visualize.play(player);
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
										player.getInventory().add(c.hood);
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
	 * @param item the item id.
	 * @return {@code true} if the player got into the dialogue stage, {@code false} otherwise.
	 */
	public static boolean buy(Player player, Item item) {
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
	private static Skillcape getSkillcape(Item item) {
		if(item == null)
			return null;
		return VALUES.stream().filter(c -> c.item == item || c.item.getId() + 1 == item.getId() || c.hood == item).findAny().orElse(null);
	}
	
	public Item getItem() {
		return item;
	}

	public Visualize getVisualize() {
		return visualize;
	}

	public int getSkill() {
		return skill;
	}
	
	public int getMaster() {
		return master;
	}
}
