package com.rageps.world.locale.instance;

/**
 * Instance policy that determines which entities can be viewed in
 * the current player's instance.
 */
public enum InstancePolicy {

	DISPLAY_INSTANCED_ENTITIES_ONLY,//Only entities from the same instance.
	DISPLAY_NORMAL_SPAWNED_ENTITIES,//NPCs, Objects, Items
	DISPLAY_ALL_NORMAL_ENTITIES,//Players, NPCs, Objects, Items
	DISPLAY_NORMAL_OBJECTS_ONLY,
	DISPLAY_NORMAL_NPCS_ONLY;

}
