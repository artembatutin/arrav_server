package com.rageps.world.locale.instance;

/**
 * Set of disposing policies for instances.
 */
/*public class InstanceDisposePolicy {

	public static final Predicate<Instance> DISPOSE_ON_NO_PLAYERS = instance -> {
		if (!instance.hasPlayersUpdate())
			return false;

		instance.setHasPlayersUpdate(false);

		for (Player player : World.getPlayers()) {
			if (player != null && player.getInstance() == instance) {
				return false;
			}
		}
		return true;
	};

	public static final Predicate<Instance> DISPOSE_ON_NO_NPCS = instance -> {
		for (NPC npc: World.getNpcs()) {
			if (npc != null && npc.getInstance() == instance) {
				return false;
			}
		}

		return DISPOSE_ON_NO_PLAYERS.test(instance);
	};

}
*/