package com.rageps.content.skill.prayer;

import com.google.common.collect.ImmutableList;
import com.rageps.combat.listener.CombatListener;
import com.rageps.combat.listener.other.prayer.curses.DeflectListener;
import com.rageps.combat.listener.other.prayer.curses.SoulsplitListener;
import com.rageps.combat.listener.other.prayer.curses.TurmoilListener;
import com.rageps.combat.listener.other.prayer.curses.WrathListener;
import com.rageps.combat.listener.other.prayer.curses.leech.*;
import com.rageps.combat.listener.other.prayer.regular.ChivalryListener;
import com.rageps.combat.listener.other.prayer.regular.PietyListener;
import com.rageps.combat.listener.other.prayer.regular.ProtectionPrayerListener;
import com.rageps.combat.listener.other.prayer.regular.RetributionListener;
import com.rageps.combat.listener.other.prayer.regular.attack.ClarityOfThoughtListener;
import com.rageps.combat.listener.other.prayer.regular.attack.ImprovedReflexesListener;
import com.rageps.combat.listener.other.prayer.regular.attack.IncredibleReflexesListener;
import com.rageps.combat.listener.other.prayer.regular.defence.RockSkinListener;
import com.rageps.combat.listener.other.prayer.regular.defence.SteelSkinListener;
import com.rageps.combat.listener.other.prayer.regular.defence.ThickSkinListener;
import com.rageps.combat.listener.other.prayer.regular.magic.MysticLoreListener;
import com.rageps.combat.listener.other.prayer.regular.magic.MysticMightListener;
import com.rageps.combat.listener.other.prayer.regular.magic.MysticWillListener;
import com.rageps.combat.listener.other.prayer.regular.ranged.EagleEyeListener;
import com.rageps.combat.listener.other.prayer.regular.ranged.HawkEyeListener;
import com.rageps.combat.listener.other.prayer.regular.ranged.SharpListener;
import com.rageps.combat.listener.other.prayer.regular.strength.BurstOfStrengthListener;
import com.rageps.combat.listener.other.prayer.regular.strength.SuperhumanStrengthListener;
import com.rageps.combat.listener.other.prayer.regular.strength.UltimateStrengthListener;
import com.rageps.content.TabInterface;
import com.rageps.content.minigame.MinigameHandler;
import com.rageps.content.skill.Skills;
import com.rageps.net.refactor.packet.out.model.ConfigPacket;
import com.rageps.net.refactor.packet.out.model.ForceTabPacket;
import com.rageps.util.TextUtils;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerAttributes;
import com.rageps.world.entity.actor.update.UpdateFlag;
import com.rageps.world.model.Animation;
import com.rageps.world.model.Graphic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * The enumerated type whose elements represent the prayers that can be
 * activated and deactivated. This currently only has support for prayers
 * present in the {@code 317} protocol.
 * @author lare96 <http://github.com/lare96>
 */
public enum Prayer {
	/* REGULAR */
	THICK_SKIN(PrayerBook.NORMAL, new ThickSkinListener(), 5609, 20, -1, 1, 83, 17202, 630) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{ROCK_SKIN, STEEL_SKIN});
		}
	}, BURST_OF_STRENGTH(PrayerBook.NORMAL, new BurstOfStrengthListener(), 5610, 20, -1, 4, 84, 17203, 631) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH, MYSTIC_WILL, MYSTIC_LORE, MYSTIC_MIGHT, SHARP_EYE, HAWK_EYE, EAGLE_EYE, CHIVALRY, PIETY});
		}
	}, CLARITY_OF_THOUGHT(PrayerBook.NORMAL, new ClarityOfThoughtListener(), 5611, 20, -1, 7, 85, 17204, 632) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, MYSTIC_WILL, MYSTIC_LORE, MYSTIC_MIGHT, SHARP_EYE, HAWK_EYE, EAGLE_EYE, CHIVALRY, PIETY});
		}
	}, SHARP_EYE(PrayerBook.NORMAL, new SharpListener(), 19812, 20, -1, 8, 700, 17205, 633) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH, MYSTIC_WILL, MYSTIC_LORE, MYSTIC_MIGHT, HAWK_EYE, EAGLE_EYE, CHIVALRY, PIETY});
		}
	}, MYSTIC_WILL(PrayerBook.NORMAL, new MysticWillListener(), 19814, 20, -1, 8, 701, 17206, 634) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH, SHARP_EYE, HAWK_EYE, EAGLE_EYE, MYSTIC_LORE, MYSTIC_MIGHT, CHIVALRY, PIETY});
		}
	}, ROCK_SKIN(PrayerBook.NORMAL, new RockSkinListener(), 5612, 10, -1, 10, 86, 17207, 635) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{THICK_SKIN, STEEL_SKIN});
		}
	}, SUPERHUMAN_STRENGTH(PrayerBook.NORMAL, new SuperhumanStrengthListener(), 5613, 10, -1, 13, 87, 17208, 636) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{BURST_OF_STRENGTH, ULTIMATE_STRENGTH, MYSTIC_WILL, MYSTIC_LORE, MYSTIC_MIGHT, SHARP_EYE, HAWK_EYE, EAGLE_EYE, CHIVALRY, PIETY});
		}
	}, IMPROVED_REFLEXES(PrayerBook.NORMAL, new ImprovedReflexesListener(), 5614, 10, -1, 16, 88, 17209, 637) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{CLARITY_OF_THOUGHT, INCREDIBLE_REFLEXES, MYSTIC_WILL, MYSTIC_LORE, MYSTIC_MIGHT, SHARP_EYE, HAWK_EYE, EAGLE_EYE, CHIVALRY, PIETY});
		}
	}, RAPID_RESTORE(PrayerBook.NORMAL, 5615, 60, -1, 19, 89, 17210, 638) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.empty();
		}
	}, RAPID_HEAL(PrayerBook.NORMAL, 5616, 30, -1, 22, 90, 17211, 639) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.empty();
		}
	}, PROTECT_ITEM(PrayerBook.NORMAL, 5617, 30, -1, 25, 91, 17212, 640) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.empty();
		}
	}, HAWK_EYE(PrayerBook.NORMAL, new HawkEyeListener(), 19816, 10, -1, 6, 702, 17213, 641) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH, MYSTIC_WILL, MYSTIC_LORE, MYSTIC_MIGHT, SHARP_EYE, EAGLE_EYE, CHIVALRY, PIETY});
		}
	}, MYSTIC_LORE(PrayerBook.NORMAL, new MysticLoreListener(), 19618, 10, -1, 6, 703, 17214, 642) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH, SHARP_EYE, HAWK_EYE, EAGLE_EYE, MYSTIC_WILL, MYSTIC_MIGHT, CHIVALRY, PIETY});
		}
	}, STEEL_SKIN(PrayerBook.NORMAL, new SteelSkinListener(), 5618, 5, -1, 28, 92, 17215, 643) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{THICK_SKIN, ROCK_SKIN});
		}
	}, ULTIMATE_STRENGTH(PrayerBook.NORMAL, new UltimateStrengthListener(), 5619, 5, -1, 31, 93, 17216, 644) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, MYSTIC_WILL, MYSTIC_LORE, MYSTIC_MIGHT, SHARP_EYE, HAWK_EYE, EAGLE_EYE, CHIVALRY, PIETY});
		}
	}, INCREDIBLE_REFLEXES(PrayerBook.NORMAL, new IncredibleReflexesListener(), 5620, 5, -1, 34, 94, 17217, 645) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, MYSTIC_WILL, MYSTIC_LORE, MYSTIC_MIGHT, SHARP_EYE, HAWK_EYE, EAGLE_EYE, CHIVALRY, PIETY});
		}
	}, PROTECT_FROM_MAGIC(PrayerBook.NORMAL, new ProtectionPrayerListener(), 5621, 5, 2, 37, 95, 17218, 646) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{PROTECT_FROM_MISSILES, PROTECT_FROM_MELEE, RETRIBUTION, REDEMPTION, SMITE});
		}
	}, PROTECT_FROM_MISSILES(PrayerBook.NORMAL, new ProtectionPrayerListener(), 5622, 5, 1, 40, 96, 17219, 647) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{PROTECT_FROM_MAGIC, PROTECT_FROM_MELEE, RETRIBUTION, REDEMPTION, SMITE});
		}
	}, PROTECT_FROM_MELEE(PrayerBook.NORMAL, new ProtectionPrayerListener(), 5623, 5, 0, 43, 97, 17220, 648) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{PROTECT_FROM_MAGIC, PROTECT_FROM_MISSILES, RETRIBUTION, REDEMPTION, SMITE});
		}
	}, EAGLE_EYE(PrayerBook.NORMAL, new EagleEyeListener(), 19821, 5, -1, 44, 704, 17221, 649) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH, MYSTIC_WILL, MYSTIC_LORE, MYSTIC_MIGHT, SHARP_EYE, HAWK_EYE, CHIVALRY, PIETY});
		}
	}, MYSTIC_MIGHT(PrayerBook.NORMAL, new MysticMightListener(), 19823, 5, -1, 45, 705, 17222, 650) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH, SHARP_EYE, HAWK_EYE, EAGLE_EYE, MYSTIC_LORE, MYSTIC_WILL, CHIVALRY, PIETY});
		}
	}, RETRIBUTION(PrayerBook.NORMAL, new RetributionListener(), 683, 20, 3, 46, 98, 17223, 651) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{PROTECT_FROM_MELEE, PROTECT_FROM_MAGIC, PROTECT_FROM_MISSILES, REDEMPTION, SMITE});
		}
	}, REDEMPTION(PrayerBook.NORMAL, 684, 10, 5, 49, 99, 17224, 652) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{PROTECT_FROM_MELEE, PROTECT_FROM_MAGIC, PROTECT_FROM_MISSILES, RETRIBUTION, SMITE});
		}
	}, SMITE(PrayerBook.NORMAL, 685, 3, 4, 52, 100, 17225, 653) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{PROTECT_FROM_MELEE, PROTECT_FROM_MAGIC, PROTECT_FROM_MISSILES, REDEMPTION, RETRIBUTION});
		}
	}, CHIVALRY(PrayerBook.NORMAL, new ChivalryListener(), 19825, 2.5, -1, 60, 706, 17226, 654) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{THICK_SKIN, ROCK_SKIN, STEEL_SKIN, BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH, CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, MYSTIC_WILL, MYSTIC_LORE, MYSTIC_MIGHT, SHARP_EYE, EAGLE_EYE, HAWK_EYE, PIETY});
		}
	}, PIETY(PrayerBook.NORMAL, new PietyListener(), 19827, 2.5, -1, 70, 707, 17227, 655) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{THICK_SKIN, ROCK_SKIN, STEEL_SKIN, BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH, CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, MYSTIC_WILL, MYSTIC_LORE, MYSTIC_MIGHT, SHARP_EYE, EAGLE_EYE, HAWK_EYE, CHIVALRY});
		}
	}, /* CURSES */
	CURSES_PROTECT_ITEM(PrayerBook.CURSES, 21357, 30, -1, 50, 724, 17202, 630) {
		@Override
		public boolean onActivation(Player player) {
			player.animation(new Animation(12567));
			player.graphic(new Graphic(2213));
			return true;
		}
		
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.empty();
		}
	}, SAP_WARRIOR(PrayerBook.CURSES, 21359, 4.16, -1, 50, 725, 17203, 631) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{LEECH_ATTACK, TURMOIL});
		}
	}, SAP_RANGER(PrayerBook.CURSES, 21361, 4.16, -1, 52, 726, 17204, 632) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{LEECH_RANGED, TURMOIL});
		}
	}, SAP_MAGE(PrayerBook.CURSES, 21363, 4.16, -1, 54, 727, 17205, 633) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{LEECH_MAGIC, TURMOIL});
		}
	}, SAP_SPIRIT(PrayerBook.CURSES, 21365, 4.16, -1, 56, 728, 17206, 634) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{LEECH_SPECIAL_ATTACK, TURMOIL});
		}
	}, BERSERKER(PrayerBook.CURSES, 21367, 30, -1, 59, 729, 17207, 635) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.empty();
		}
		
		@Override
		public boolean onActivation(Player player) {
			player.animation(new Animation(12589));
			player.graphic(new Graphic(2266));
			return true;
		}
	}, DEFLECT_SUMMONING(PrayerBook.CURSES, 21369, 5, 12, 62, 730, 17208, 636) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{WRATH, SOUL_SPLIT});
		}
	}, DEFLECT_MAGIC(PrayerBook.CURSES, new DeflectListener(),21371, 5, 10, 65, 731, 17209, 637) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{DEFLECT_MISSILES, DEFLECT_MELEE, WRATH, SOUL_SPLIT});
		}
	}, DEFLECT_MISSILES(PrayerBook.CURSES, new DeflectListener(), 21373, 5, 11, 68, 732, 17210, 638) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{DEFLECT_MAGIC, DEFLECT_MELEE, WRATH, SOUL_SPLIT});
		}
	}, DEFLECT_MELEE(PrayerBook.CURSES, new DeflectListener(), 21375, 5, 9, 71, 733, 17211, 639) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{DEFLECT_MISSILES, DEFLECT_MAGIC, WRATH, SOUL_SPLIT});
		}
	}, LEECH_ATTACK(PrayerBook.CURSES, new LeechAttackListener(), 21377, 5.83, -1, 74, 734, 17212, 640) {
		@Override
		public boolean onDeactivation(Player player) {
			player.curseManager.reset(this);
			return true;
		}
		
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{SAP_WARRIOR, TURMOIL});
		}
	}, LEECH_RANGED(PrayerBook.CURSES, new LeechRangedListener(), 21379, 5.83, -1, 76, 735, 17213, 641) {
		@Override
		public boolean onDeactivation(Player player) {
			player.curseManager.reset(this);
			return true;
		}
		
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{SAP_RANGER, TURMOIL});
		}
	}, LEECH_MAGIC(PrayerBook.CURSES, new LeechMagicListener(), 21381, 5.83, -1, 78, 736, 17214, 642) {
		@Override
		public boolean onDeactivation(Player player) {
			player.curseManager.reset(this);
			return true;
		}
		
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{SAP_MAGE, TURMOIL});
		}
	}, LEECH_DEFENCE(PrayerBook.CURSES, new LeechDefenceListener(), 21383, 5.83, -1, 80, 737, 17215, 643) {
		@Override
		public boolean onDeactivation(Player player) {
			player.curseManager.reset(this);
			return true;
		}
		
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{TURMOIL});
		}
	}, LEECH_STRENGTH(PrayerBook.CURSES, new LeechStrengthListener(), 21385, 5.83, -1, 82, 738, 17216, 644) {
		@Override
		public boolean onDeactivation(Player player) {
			player.curseManager.reset(this);
			return true;
		}
		
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{TURMOIL});
		}
	}, LEECH_ENERGY(PrayerBook.CURSES, 21387, 5.5, -1, 84, 739, 17217, 645) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{TURMOIL});
		}
	}, LEECH_SPECIAL_ATTACK(PrayerBook.CURSES, 21389, 6.6, -1, 86, 740, 17218, 646) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{TURMOIL});
		}
	}, WRATH(PrayerBook.CURSES, new WrathListener(), 21391, 16.6, 16, 89, 741, 17219, 647) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{DEFLECT_SUMMONING, DEFLECT_MAGIC, DEFLECT_MISSILES, DEFLECT_MELEE, SOUL_SPLIT});
		}
	}, SOUL_SPLIT(PrayerBook.CURSES, new SoulsplitListener(), 21393, 3.33, 17, 92, 742, 17220, 648) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{DEFLECT_SUMMONING, DEFLECT_MAGIC, DEFLECT_MISSILES, DEFLECT_MELEE, WRATH});
		}
	}, TURMOIL(PrayerBook.CURSES, new TurmoilListener(), 21395, 3.66, -1, 95, 743, 17221, 649) {
		@Override
		public Optional<Prayer[]> deactivate() {
			return Optional.of(new Prayer[]{SAP_WARRIOR, SAP_RANGER, SAP_MAGE, SAP_SPIRIT, LEECH_ATTACK, LEECH_RANGED, LEECH_MAGIC, LEECH_DEFENCE, LEECH_STRENGTH, LEECH_ENERGY, LEECH_SPECIAL_ATTACK});
		}
		
		@Override
		public boolean onActivation(Player player) {
			player.animation(new Animation(12565));
			player.graphic(new Graphic(2226));
			return true;
		}
	};
	
	/**
	 * The cached array that will contain mappings of all the elements to their
	 * identifiers.
	 */
	public static final ImmutableList<Prayer> VALUES = ImmutableList.copyOf(values());
	
	/**
	 * The type of this prayer.
	 */
	private final PrayerBook type;
	
	/**
	 * The prayer's modifier to combat formula.
	 */
	private final CombatListener<Player> formulaModifier;
	
	/**
	 * The button identification for this prayer.
	 */
	private final int buttonId;
	
	/**
	 * The amount of ticks it takes for prayer to be drained.
	 */
	private final double drainRate;
	
	/**
	 * The head icon present when this prayer is activated.
	 */
	private final int headIcon;
	
	/**
	 * The level required to use this prayer.
	 */
	private final int level;
	
	/**
	 * The config to make the prayer button light up when activated.
	 */
	private final int config;
	
	/**
	 * The button identification for quick prayer.
	 */
	private final int quickPrayer;
	
	/**
	 * The checkmark identification for quick prayer.
	 */
	private final int checkmark;
	
	/**
	 * The combat prayers that will be automatically deactivated when this one
	 * is activated.
	 */
	public abstract Optional<Prayer[]> deactivate();
	
	/**
	 * Creates a new {@link Prayer}.
	 * @param buttonId the identification for this prayer.
	 * @param drainRate the amount of ticks it takes for prayer to be drained.
	 * @param headIcon the head icon present when this prayer is activated.
	 * @param level the level required to use this prayer.
	 * @param config the config to make the prayer button light up when activated.
	 * @param quickPrayer the quick prayer button id.
	 * @param checkmark the quick prayer check mark condition.
	 */
	Prayer(PrayerBook type, int buttonId, double drainRate, int headIcon, int level, int config, int quickPrayer, int checkmark) {
		this(type, null, buttonId, drainRate, headIcon, level, config, quickPrayer, checkmark);
		//System.out.println(this.name()+" null");
	}
	
	/**
	 * Creates a new {@link Prayer}.
	 * @param buttonId the identification for this prayer.
	 * @param drainRate the amount of ticks it takes for prayer to be drained.
	 * @param headIcon the head icon present when this prayer is activated.
	 * @param level the level required to use this prayer.
	 * @param config the config to make the prayer button light up when activated.
	 * @param quickPrayer the quick prayer button id.
	 * @param checkmark the quick prayer check mark condition.
	 */
	Prayer(PrayerBook type, CombatListener<Player> formulaModifier, int buttonId, double drainRate, int headIcon, int level, int config, int quickPrayer, int checkmark) {
		this.type = type;
		this.formulaModifier = formulaModifier;
		this.buttonId = buttonId;
		this.drainRate = drainRate;
		this.headIcon = headIcon;
		this.level = level;
		this.config = config;
		this.quickPrayer = quickPrayer;
		this.checkmark = checkmark;
	}
	
	@Override
	public String toString() {
		return TextUtils.capitalize(name().toLowerCase().replaceAll("_", " "));
	}
	
	/**
	 * Executed dynamically when this combat prayer is activated for
	 * {@code player}.
	 * @param player the player that activated this prayer.
	 * @return {@code true} if this prayer can activated, {@code false}
	 * otherwise.
	 */
	public boolean onActivation(Player player) {
		return true;
	}
	
	/**
	 * Executed dynamically when this combat prayer is deactivated for
	 * {@code player}.
	 * @param player the player that deactivated this prayer.
	 * @return {@code true} if this prayer can deactivated, {@code false}
	 * otherwise.
	 */
	public boolean onDeactivation(Player player) {
		return true;
	}
	
	/**
	 * Checks if the {@code prayer$buttonId} matches the identification.
	 * @param id The identification to check for.
	 * @return Optional.present if the identification matched, optional.empty otherwise.
	 */
	public static Optional<Prayer> getCombatPrayer(int id) {
		return VALUES.stream().filter(prayer -> prayer.buttonId == id).findAny();
	}
	
	/**
	 * Checks if the {@code prayer$quickPrayer} identification matches the identification.
	 * @param id The identification to check for.
	 * @return Optional.present if the identification matched, optional.empty otherwise.
	 */
	public static Optional<Prayer> getQuickPrayer(int id, PrayerBook book) {
		for(Prayer pray : VALUES) {
			if(pray.getType().equals(book) && pray.quickPrayer == id) {
				return Optional.of(pray);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Activates this combat prayer for {@code player}. If
	 * {@code deactivateIfActivated} is flagged {@code true} then if this prayer
	 * is already activated it will be deactivated instead.
	 * @param player the player to activate this prayer for.
	 * @param deactivateIfActivated if this prayer should be deactivated.
	 * @return <true> if the prayer was activated, <false> otherwise.
	 */
	public static boolean activate(Player player, boolean deactivateIfActivated, int buttonId) {
		Optional<Prayer> prayer = getCombatPrayer(buttonId);
		
		if(!prayer.isPresent()) {
			return false;
		}
		
		if(!MinigameHandler.execute(player, m -> m.canPray(player, prayer.get()))) {
			player.send(new ConfigPacket(prayer.get().getConfig(), 0));
			return false;
		}
		
		if(Prayer.isActivated(player, prayer.get())) {
			if(deactivateIfActivated) {
				prayer.get().deactivate(player);
			}
			return false;
		}
		StringBuilder sb = new StringBuilder();
		if(player.getSkills()[Skills.PRAYER].getRealLevel() < prayer.get().getLevel()) {
			sb.append("You need a Prayer " + "level of " + prayer.get().getLevel() + " to use " + prayer.get().toString() + ".");
		} else if(player.getSkills()[Skills.PRAYER].getCurrentLevel() < 1) {
			sb.append("You need to recharge your prayer at an altar!");
		}
		if(sb.length() > 0) {
			player.send(new ConfigPacket(prayer.get().getConfig(), 0));
			player.message(sb.toString());
			return false;
		}
		if(!prayer.get().onActivation(player))
			return false;
		if(player.getPrayerDrain() == null || !player.getPrayerDrain().isRunning()) {
			player.setPrayerDrain(new PrayerTask(player));
			World.get().submit(player.getPrayerDrain());
		}
		if(prayer.get().deactivate().isPresent()) {
			Arrays.stream(prayer.get().deactivate().get()).forEach(it -> it.deactivate(player));
		}
		player.getPrayerActive().add(prayer.get());

		player.getCombat().addListener(prayer.get().formulaModifier);

		player.send(new ConfigPacket(prayer.get().getConfig(), 1));
		if(prayer.get().getHeadIcon() != -1) {
			player.headIcon = prayer.get().getHeadIcon();
			player.updateAppearance();
		}
		return true;
	}
	
	/**
	 * Activates the quick prayers for specified {@link Player}.
	 * @param player The player we are activating the quick prayers for.
	 * @param buttonId The button identifier to activate the prayer.
	 */
	public static boolean activateQuickPrayer(Player player, int buttonId) {
		//TODO:
		if(buttonId == 48) {//Turn on.
			if(player.getQuickPrayers().isEmpty()) {
				player.message("Please select some quick prayers in order to activate them.");
			} else {
				if(player.getAttributeMap().getBoolean(PlayerAttributes.QUICK_PRAY_ON)) {
					player.getQuickPrayers().forEach(prayer -> prayer.deactivate(player));
					player.send(new ConfigPacket(175, 0));
					player.getAttributeMap().reset(PlayerAttributes.QUICK_PRAY_ON);
				} else {
					if(player.getSkills()[Skills.PRAYER].getCurrentLevel() < 1) {
						player.message("You need to recharge your prayer at an altar!");
						return true;
					}
					player.getQuickPrayers().forEach(prayer -> Prayer.activate(player, false, prayer.buttonId));
					player.send(new ConfigPacket(175, 1));
					player.getAttributeMap().set(PlayerAttributes.QUICK_PRAY_ON, true);
				}
			}
			return true;
		}
		if(buttonId == 49) {//selecting
			TabInterface.PRAYER.sendInterface(player, player.getPrayerBook() == PrayerBook.CURSES ? 18200 : 17200);
			player.send(new ForceTabPacket(TabInterface.PRAYER));
			for(Prayer pray : VALUES) {
				if(pray.getType() == player.getPrayerBook()) {
					if(player.getQuickPrayers().contains(pray)) {
						player.send(new ConfigPacket(pray.getCheckmark(), 1));
					} else {
						player.send(new ConfigPacket(pray.getCheckmark(), 0));
					}
				}
			}
			return true;
		}
		if(buttonId == 261) {//confirm
			TabInterface.PRAYER.sendInterface(player, player.getPrayerBook().getId());
			player.getQuickPrayers().clear();
			player.getQuickPrayers().addAll(player.getSelectedQuickPrayers());
			return true;
		}
		return false;
	}
	
	/**
	 * Toggles this combat prayer for {@code player}.
	 * @param player the player to toggle this prayer for.
	 * @param buttonId The button identification to check for.
	 */
	public static boolean toggleQuickPrayer(Player player, int buttonId) {
		Optional<Prayer> prayer = getQuickPrayer(buttonId, player.getPrayerBook());
		
		if(!prayer.isPresent())
			return false;
		Prayer pray = prayer.get();
		if(Prayer.isSelected(player, prayer.get())) {
			pray.deselectQuickPrayer(player);
			return true;
		}
		
		if(prayer.get().deactivate().isPresent()) {
			for(Prayer de : pray.deactivate().get()) {
				de.deselectQuickPrayer(player);
			}
		}
		player.getSelectedQuickPrayers().add(prayer.get());
		player.send(new ConfigPacket(prayer.get().getCheckmark(), 1));
		return true;
	}
	
	/**
	 * Attempts to deselect this prayer for {@code player}. If this prayer is
	 * already deselected then invoking this method does nothing.
	 * @param player the player to deselect this prayer for.
	 */
	public final void deselectQuickPrayer(Player player) {
		if(!Prayer.isSelected(player, this))
			return;
		player.getSelectedQuickPrayers().remove(this);
		player.send(new ConfigPacket(checkmark, 0));
	}
	
	/**
	 * Attempts to deactivate this prayer for {@code player}. If this prayer is
	 * already deactivated then invoking this method does nothing.
	 * @param player the player to deactivate this prayer for.
	 */
	public final void deactivate(Player player) {
		if(!Prayer.isActivated(player, this))
			return;
		if(!onDeactivation(player))
			return;
		player.getPrayerActive().remove(this);
		player.getCombat().removeListener(formulaModifier);
		player.send(new ConfigPacket(config, 0));
		if(headIcon != -1) {
			player.headIcon = -1;
			player.updateAppearance();
		}
		if(player.getPrayerActive().isEmpty()) {
			player.send(new ConfigPacket(175, 0));
			player.getAttributeMap().reset(PlayerAttributes.QUICK_PRAY_ON);
		}
	}
	
	/**
	 * Deactivates activated combat prayers for {@code player}. Combat prayers
	 * that are already deactivated will be ignored.
	 * @param player the player to deactivate prayers for.
	 */
	public static void deactivateAll(Player player) {
		VALUES.forEach(it -> it.deactivate(player));
	}
	
	/**
	 * Determines if the {@code prayer} is activated for the {@code player}.
	 * @param player the player's prayers to check.
	 * @param prayer the prayer to check is active.
	 * @return {@code true} if the prayer is activated for the player,
	 * {@code false} otherwise.
	 */
	public static boolean isActivated(Player player, Prayer prayer) {
		return player.getPrayerActive().contains(prayer);
	}
	
	public static Prayer[] getActivatedPrayers(Player player, Prayer... prayers) {
		List<Prayer> active_prayers = new ArrayList<>();
		
		for(Prayer prayer : prayers) {
			if(isActivated(player, prayer)) {
				active_prayers.add(prayer);
			}
		}
		
		return active_prayers.toArray(new Prayer[active_prayers.size()]);
	}
	
	/**
	 * Determines if any of the specified prayers are activated.
	 * @param player the player's prayers to check.
	 * @param prayers the prayers to check if active.
	 * @return {@code true} if any of the prayers are active for the player,
	 * {@code false} otherwise.
	 */
	public static boolean isAnyActivated(Player player, Prayer[] prayers) {
		return Arrays.stream(prayers).anyMatch(p -> isActivated(player, p));
	}
	
	/**
	 * Determines if the {@code prayer} is selected for the {@code player}.
	 * @param player the player's prayers to check.
	 * @param prayer the prayer to check is selected.
	 * @return <true> if the prayer is selected for the player, <false> otherwise.
	 */
	public static boolean isSelected(Player player, Prayer prayer) {
		return player.getSelectedQuickPrayers().contains(prayer);
	}
	
	/**
	 * Gets the type of this prayer.
	 * @return the buttonId
	 */
	public PrayerBook getType() {
		return type;
	}
	
	/**
	 * Gets the prayer button click toggle.
	 * @return the buttonId
	 */
	public int getButtonId() {
		return buttonId;
	}
	
	/**
	 * Gets the amount of ticks it takes for prayer to be drained.
	 * @return the amount of ticks.
	 */
	public final double getDrainRate() {
		return drainRate;
	}
	
	/**
	 * Gets the head icon present when this prayer is activated.
	 * @return the head icon.
	 */
	public final int getHeadIcon() {
		return headIcon;
	}
	
	/**
	 * Gets the level required to use this prayer.
	 * @return the level required.
	 */
	public final int getLevel() {
		return level;
	}
	
	/**
	 * Gets the config to make the prayer button light up when activated.
	 * @return the config for the prayer button.
	 */
	public final int getConfig() {
		return config;
	}
	
	/**
	 * @return the quickPrayer
	 */
	public int getQuickPrayer() {
		return quickPrayer;
	}
	
	/**
	 * @return the checkmark
	 */
	public int getCheckmark() {
		return checkmark;
	}
}