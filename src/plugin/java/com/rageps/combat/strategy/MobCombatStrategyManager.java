package com.rageps.combat.strategy;

import com.rageps.combat.strategy.basic.MeleeStrategy;
import com.rageps.combat.strategy.npc.NpcMagicStrategy;
import com.rageps.combat.strategy.npc.NpcMeleeStrategy;
import com.rageps.combat.strategy.npc.NpcRangedStrategy;
import com.rageps.world.entity.actor.combat.CombatType;
import com.rageps.world.entity.actor.combat.projectile.CombatProjectile;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.mob.MobDefinition;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

/**
 * This class handles the initialization of {@link CombatStrategy<Mob>}'s They are picked up
 * using {@link Reflections} on types annotated with {@link MobCombatStrategyMeta}.
 *
 * The strategy map also handles the delegation of the Mob's {@link CombatStrategy}.
 *
 * @author Tamatea <tamateea@gmail.com>
 */
public class MobCombatStrategyManager {

    /**
     * A default strategy used as a fallback for Mob's if no strategy is assigned.
     */
    private static final MeleeStrategy<Mob> DEFAULT = NpcMeleeStrategy.get();


    /**
     * The map containing all of the strategies with the {@link Mob}'s id as a key.
     */
    private static Object2ObjectOpenHashMap<Integer, CombatStrategy<Mob>> STRATEGY_MAP = new Object2ObjectOpenHashMap<>();

    /**
     * This class's logger.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * Initializes the map, and loads all of the data using {@link Reflections}.
     */
    public static void init() {
        STRATEGY_MAP = new Object2ObjectOpenHashMap<>();
        Set<Class<?>> clazzSet = new Reflections(MobCombatStrategyManager.class.getPackage().getName()).getTypesAnnotatedWith(MobCombatStrategyMeta.class);
        clazzSet.forEach(clazz ->{
            MobCombatStrategyMeta annotation = clazz.getAnnotation(MobCombatStrategyMeta.class);
            if(annotation != null) {
                CombatStrategy<Mob> strategy = getDefaultMeleeStrategy();//shouldn't fall back to melee
                try {
                    Constructor<CombatStrategy<Mob>> constructor = (Constructor<CombatStrategy<Mob>>) clazz.getConstructor();
                    strategy = constructor.newInstance();
                }catch (Exception e) {
                    logger.error("Error creating combat strategy for mobs:{}", Arrays.toString(annotation.ids()), e);
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
        logger.info("Loaded {} Mob strategies.", STRATEGY_MAP.size());
    }

    public static CombatStrategy<Mob> getStrategy(Mob mob) {
        if(STRATEGY_MAP.containsKey(mob.getId())) {
            return STRATEGY_MAP.get(mob.getId());
        }
        return loadStrategy(mob).orElse(getDefaultMeleeStrategy());
    }

    /**
     * Tries to load a {@link CombatStrategy} for a specific mob.
     * @param mob mob
     * @return combat strategy.
     */
    private static Optional<CombatStrategy<Mob>> loadStrategy(Mob mob) {
        if(!mob.getDefinition().getCombatAttackData().isPresent()) {
            return Optional.empty();
        }
        MobDefinition.CombatAttackData data = mob.getDefinition().getCombatAttackData().get();
        CombatType type = data.type;
        switch(type) {
            case RANGED: {
                CombatProjectile definition = data.getDefinition();
                if(definition == null) {
                    throw new AssertionError("Could not find ranged projectile for Mob[id=" + mob.getId() + ", name=" + mob.getDefinition().getName() + "]");
                }
                return Optional.of(new NpcRangedStrategy(definition));
            }
            case MAGIC: {
                CombatProjectile definition = data.getDefinition();
                if(definition == null) {
                    throw new AssertionError("Could not find magic projectile for Mob[id=" + mob.getId() + ", name=" + mob.getDefinition().getName() + "]");
                }
                return Optional.of(new NpcMagicStrategy(definition));
            }
            case MELEE:
                return Optional.of(NpcMeleeStrategy.get());
        }
        return Optional.empty();
    }



    /**
     * Get's the default fallback strategy.
     * @return The default melee strategy.
     */
    private static MeleeStrategy<Mob> getDefaultMeleeStrategy() {
        return DEFAULT;
    }
}
