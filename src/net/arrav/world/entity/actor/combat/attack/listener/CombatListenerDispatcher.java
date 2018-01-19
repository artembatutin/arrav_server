package net.arrav.world.entity.actor.combat.attack.listener;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.arrav.util.LoggerUtils;
import net.arrav.util.Utility;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.player.Player;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
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
	private static Logger logger = LoggerUtils.getLogger(CombatListenerDispatcher.class);
	
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
		logger.info("Loading item listeners...");
		for(String directory : Utility.getSubDirectories(CombatListenerDispatcher.class)) {
			List<CombatListener<Player>> listeners_class = Utility.getClassesInDirectory(CombatListenerDispatcher.class.getPackage().getName() + "." + directory).stream().map(clazz -> (CombatListener<Player>) clazz).collect(Collectors.toList());
			for(CombatListener<Player> l : listeners_class) {
				ItemCombatListenerSignature meta = l.getClass().getAnnotation(ItemCombatListenerSignature.class);
				if(meta != null) {
					Arrays.stream(meta.items()).forEach(i -> listeners.put(i, new CombatListenerSet(meta.items(), l)));
				}
			}
		}
		logger.info("Successfully loaded " + listeners.size() + " item listeners.");
		return listeners;
	}
	
	/**
	 * The method which loads all the npc listeners.
	 * @return a map populated with all the npc listeners chained to their npc
	 * id.
	 */
	private static Int2ObjectArrayMap<CombatListener<Mob>> loadNpcs() {
		Int2ObjectArrayMap<CombatListener<Mob>> listeners = new Int2ObjectArrayMap<>();
		logger.info("Loading npc listeners...");
		for(String directory : Utility.getSubDirectories(CombatListenerDispatcher.class)) {
			List<CombatListener<Mob>> listeners_class = Utility.getClassesInDirectory(CombatListenerDispatcher.class.getPackage().getName() + "." + directory).stream().map(clazz -> (CombatListener<Mob>) clazz).collect(Collectors.toList());
			for(CombatListener<Mob> l : listeners_class) {
				NpcCombatListenerSignature meta = l.getClass().getAnnotation(NpcCombatListenerSignature.class);
				if(meta != null) {
					Arrays.stream(meta.npcs()).forEach(i -> listeners.put(i, l));
					for(int i : meta.npcs()) {
						listeners.put(i, l);
					}
				}
			}
		}
		logger.info("Successfully loaded " + listeners.size() + " npc listeners.");
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
