package net.edge.content.combat.attack.listener;

import com.google.common.collect.ImmutableMap;
import net.edge.util.LoggerUtils;
import net.edge.util.Utility;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

import java.lang.annotation.IncompleteAnnotationException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author StanTheMan
 */
public final class CombatListenerDispatcher {

    public static ImmutableMap<Integer, CombatListenerSet> ITEM_LISTENERS;
    public static ImmutableMap<Integer, CombatListener<Mob>> NPC_LISTENERS;

    /**
     * The logger that will print important information.
     */
    private static Logger logger = LoggerUtils.getLogger(CombatListenerDispatcher.class);

    public static void load() {
        ITEM_LISTENERS = loadItems();
        NPC_LISTENERS = loadNpcs ();
    }

    /**
     * The method which loads all the item listeners.
     *
     * @return a map populated with all the item listeners chained to their item
     * id.
     */
    private static ImmutableMap<Integer, CombatListenerSet> loadItems() {
        Map<Integer, CombatListenerSet> listeners = new HashMap<>();
        logger.info("Loading item listeners...");
        for (String directory : Utility.getSubDirectories(CombatListenerDispatcher.class)) {
            List<CombatListener<Player>> listeners_class = Utility.getClassesInDirectory(CombatListenerDispatcher.class.getPackage().getName() + "." + directory).stream().map(clazz -> (CombatListener<Player>) clazz).collect(Collectors.toList());
            for (CombatListener<Player> l : listeners_class) {
                PlayerCombatListenerSignature meta = l.getClass().getAnnotation(PlayerCombatListenerSignature.class);
                if (meta == null) {
                    throw new IncompleteAnnotationException(PlayerCombatListenerSignature.class, l.getClass().getName() + " has no annotation.");
                }
                Arrays.stream(meta.items()).forEach(i -> listeners.put(i, new CombatListenerSet(meta.items(), l)));
            }
        }
        logger.info("Successfully loaded " + listeners.size() + " item listeners.");
        return ImmutableMap.copyOf(listeners);
    }

    /**
     * The method which loads all the npc listeners.
     *
     * @return a map populated with all the npc listeners chained to their npc
     * id.
     */
    private static ImmutableMap<Integer, CombatListener<Mob>> loadNpcs() {
        Map<Integer, CombatListener<Mob>> listeners = new HashMap<>();
        logger.info("Loading npc listeners...");
        for (String directory : Utility.getSubDirectories(CombatListenerDispatcher.class)) {
            List<CombatListener<Mob>> listeners_class = Utility.getClassesInDirectory(CombatListenerDispatcher.class.getPackage().getName() + "." + directory).stream().map(clazz-> (CombatListener<Mob>) clazz).collect(Collectors.toList());
            for (CombatListener<Mob> l : listeners_class) {
                NpcCombatListenerSignature meta = l.getClass().getAnnotation(NpcCombatListenerSignature.class);
                if (meta == null) {
                    throw new IncompleteAnnotationException(NpcCombatListenerSignature.class, l.getClass().getName() + " has no annotation.");
                }
                for (int i : meta.npcs()) {
                    listeners.put(i, l);
                }
            }
        }
        logger.info("Successfully loaded " + listeners.size() + " npc listeners.");
        return ImmutableMap.copyOf(listeners);
    }

    public static final class CombatListenerSet {

        public final int[] set;

        public final CombatListener<Player> listener;

        public CombatListenerSet(int[] set, CombatListener<Player> listener) {
            this.set = set;
            this.listener = listener;
        }
    }
}
