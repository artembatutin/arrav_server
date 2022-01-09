package com.rageps.combat.strategy;

import com.rageps.combat.listener.ItemCombatListenerSignature;
import com.rageps.combat.strategy.basic.MeleeStrategy;
import com.rageps.combat.strategy.basic.RangedStrategy;
import com.rageps.combat.strategy.player.PlayerMagicStrategy;
import com.rageps.combat.strategy.player.PlayerMeleeStrategy;
import com.rageps.combat.strategy.player.PlayerRangedStrategy;
import com.rageps.combat.strategy.player.special.CombatSpecial;
import com.rageps.util.json.impl.CombatRangedBowLoader;
import com.rageps.world.entity.actor.combat.ranged.RangedAmmunition;
import com.rageps.world.entity.actor.combat.ranged.RangedWeaponDefinition;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.container.impl.Equipment;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

/**
 * This class handles the initialization of {@link CombatStrategy<Player>}'s They are picked up
 * using {@link Reflections} on types annotated with {@link ItemCombatListenerSignature}.
 *
 * The strategy map also handles the delegation of the players's {@link CombatStrategy}.
 *
 * @author Tamatea <tamateea@gmail.com>
 */
public class PlayerWeaponStrategyManager {

    /**
     * A default strategy used as a fallback for Players's if no strategy is assigned.
     */
    private static final MeleeStrategy<Player> DEFAULT = PlayerMeleeStrategy.get();

    /**
     * The ranged stategy returned if a player is using a non-custom ranged weapon.
     */
    private static final RangedStrategy<Player> DEFAULT_RANGED = PlayerRangedStrategy.get();


    /**
     * The map containing all of the strategies with the weapons id as a key.
     */
    private static Object2ObjectOpenHashMap<Integer, CombatStrategy<Player>> STRATEGY_MAP = new Object2ObjectOpenHashMap<>();

    /**
     * This class's logger.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * Initializes the map, and loads all of the data using {@link Reflections}.
     */
    public static void init() {
        STRATEGY_MAP = new Object2ObjectOpenHashMap<>();
        Set<Class<?>> clazzSet = new Reflections(PlayerWeaponStrategyManager.class.getPackage().getName()).getTypesAnnotatedWith(PlayerWeaponStrategyMeta.class);
        clazzSet.forEach(clazz ->{
            PlayerWeaponStrategyMeta annotation = clazz.getAnnotation(PlayerWeaponStrategyMeta.class);
            if(annotation != null) {
                CombatStrategy<Player> strategy = getDefaultMeleeStrategy();//shouldn't fall back to melee
                try {
                    Constructor<CombatStrategy<Player>> constructor = (Constructor<CombatStrategy<Player>>) clazz.getConstructor();
                    strategy = constructor.newInstance();
                }catch (Exception e) {
                    logger.error("Error creating combat strategy for weapons:{}", Arrays.toString(annotation.ids()), e);
                    e.printStackTrace();
                }
                for (int type : annotation.ids()) {
                    try {
                        STRATEGY_MAP.put(type, strategy);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        logger.info("Loaded {} custom weapon strategies.", STRATEGY_MAP.size());
    }

    /**
     * Delegates the players current {@link CombatStrategy}
     * @param player The player's combat strategy whom we are returning.
     * @return The combat strategy.
     */
    public static CombatStrategy<Player> getStrategy(Player player) {
        Item weapon = player.getEquipment().get(Equipment.WEAPON_SLOT);
        return loadStrategy(player, weapon).orElse(getDefaultMeleeStrategy());
    }

    /**
     * Tries to load a {@link CombatStrategy} for a specific mob.
     * @param player the {@link Player} for which this strategy is being returned.
     * @return combat strategy.
     */
    private static Optional<CombatStrategy<Player>> loadStrategy(Player player, Item weapon) {

        Optional<CombatStrategy<Player>> specialStrategy = getSpecialStrategy(player);//prioritize special attack first.
        if(specialStrategy.isPresent())
            return specialStrategy;

        Optional<CombatStrategy<Player>> customStrategy = getStrategyForWeapon(weapon);

        if(weapon != null) {
            RangedWeaponDefinition def = CombatRangedBowLoader.DEFINITIONS.get(weapon.getId());
            if(def != null) {
                RangedAmmunition ammo = RangedAmmunition.find(player.getEquipment().get(def.getSlot()));
                player.rangedDefinition = def;
                player.rangedAmmo = ammo;

                if(customStrategy.isPresent())//we want to set the ranged definitions first.
                    return customStrategy;
                else
                    return Optional.of(getDefaultRangedStrategy());
            }
        }

        if(player.isSingleCast() || player.isAutocast()) {
            return Optional.of(new PlayerMagicStrategy(player.isAutocast() ? player.getAutocastSpell() : player.getSingleCast()));
        }

        return customStrategy;
    }

    /**
     * Get's an {@link Optional} of a {@link CombatSpecial} strategy.
     * @param player the player who's special attack we are checking for.
     * @return A combat strategy of the players special attack.
     */
    private static Optional<CombatStrategy<Player>> getSpecialStrategy(Player player) {
        if (player.isSpecialActivated()) {
            if (player.getCombatSpecial() == null || player.getCombatSpecial().getStrategy() == null) {
                player.setSpecialActivated(false);
            } else {
                return Optional.of(player.getCombatSpecial().getStrategy());
            }
        }
        return Optional.empty();
    }

    /**
     * Get's an {@link Optional} of a {@link CombatStrategy} associated with
     * the players weapon. If the players weapon doesn't have a custom strategy,
     * it will return Optional.empty().
     * @param weapon The players weapon.
     * @return The combat strategy.
     */
    private static Optional<CombatStrategy<Player>> getStrategyForWeapon(Item weapon) {
        if (weapon != null)
            if (STRATEGY_MAP.containsKey(weapon.getId())) {
                return Optional.of(STRATEGY_MAP.get(weapon.getId()));
            }
        return Optional.empty();
    }


    /**
     * Get's the default fallback strategy.
     * @return The default melee strategy.
     */
    private static MeleeStrategy<Player> getDefaultMeleeStrategy() {
        return DEFAULT;
    }

    /**
     * Get's the default ranged strategy.
     * @return The ranged strategy.
     */
    private static RangedStrategy<Player> getDefaultRangedStrategy() {
        return DEFAULT_RANGED;
    }
}
