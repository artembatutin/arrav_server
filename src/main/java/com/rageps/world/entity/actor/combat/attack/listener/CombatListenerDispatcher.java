package com.rageps.world.entity.actor.combat.attack.listener;

import com.rageps.util.Utility;
import com.rageps.world.entity.actor.player.Player;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import com.rageps.world.entity.actor.mob.Mob;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author StanTheWoman
 */
public final class CombatListenerDispatcher {
	
	public static Int2ObjectArrayMap<CombatListenerSet> ITEM_LISTENERS;
	public static Int2ObjectArrayMap<CombatListener<Mob>> NPC_LISTENERS;
	
	/**
	 * The logger that will print important information.
	 */
	private static final Logger LOGGER = LogManager.getLogger();

	
	public static void load() {
		ITEM_LISTENERS = loadItems();
		NPC_LISTENERS = loadNpcs();
	}

	/**
	 * The method which loads all the item listeners.
	 * @return a map populated with all the item listeners chained to their item
	 * id.
	 */
	private static Int2ObjectArrayMap<CombatListenerSet> loadItems() {
		Int2ObjectArrayMap<CombatListenerSet> listeners = new Int2ObjectArrayMap<>();
		LOGGER.info("Loading item listeners...");
		Set<Class<?>> clazzSet = new Reflections(CombatListenerDispatcher.class.getPackage().getName()).getTypesAnnotatedWith(ItemCombatListenerSignature.class);
		List<CombatListener<Player>> listeners_class = Utility.getClassesInSet(clazzSet).stream().map(clazz -> (CombatListener<Player>) clazz).collect(Collectors.toList());
		for(CombatListener<Player> l : listeners_class) {
			ItemCombatListenerSignature meta = l.getClass().getAnnotation(ItemCombatListenerSignature.class);
			if(meta != null) {
				Arrays.stream(meta.items()).forEach(i -> listeners.put(i, new CombatListenerSet(meta.items(), l)));
			}
		}

		LOGGER.info("Successfully loaded {} item listeners.", listeners.size());
		return listeners;
	}

	/**
	 * The method which loads all the npc listeners.
	 * @return a map populated with all the npc listeners chained to their npc
	 * id.
	 */
	private static Int2ObjectArrayMap<CombatListener<Mob>> loadNpcs() {
		Set<Class<?>> clazzSet = new Reflections(CombatListenerDispatcher.class.getPackage().getName()).getTypesAnnotatedWith(NpcCombatListenerSignature.class);
		Int2ObjectArrayMap<CombatListener<Mob>> listeners = new Int2ObjectArrayMap<>();
		LOGGER.info("Loading npc listeners...");
		List<CombatListener<Mob>> listeners_class = Utility.getClassesInSet(clazzSet).stream().map(clazz -> (CombatListener<Mob>) clazz).collect(Collectors.toList());
		for(CombatListener<Mob> l : listeners_class) {
			NpcCombatListenerSignature meta = l.getClass().getAnnotation(NpcCombatListenerSignature.class);
			if(meta != null) {
				Arrays.stream(meta.npcs()).forEach(i -> listeners.put(i, l));
				for(int i : meta.npcs()) {
					listeners.put(i, l);
				}
			}
		}
		LOGGER.info("Successfully loaded {} npc listeners.", listeners.size());
		return listeners;
	}
	
	public static final class CombatListenerSet {
		
		public final int[] set;
		
		public final CombatListener<Player> listener;
		
		CombatListenerSet(int[] set, CombatListener<Player> listener) {
			this.set = set;
			this.listener = listener;
		}
	}
}
