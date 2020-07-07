package com.rageps.world.locale.loc;

import com.rageps.world.entity.Entity;
import com.rageps.world.locale.Position;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class Locations {

    public static ObjectArrayList<Area> MULTI_AREA = new ObjectArrayList<>();

    /**
     * Determines if {@code entity} is in any of the multicombat areas.
     * @param entity the entity to determine if in the areas.
     * @return {@code true} if the entity is in any of these areas,
     * {@code false} otherwise.
     * @deprecated - use {@link Location} with multi boolean instead.
     */
    @Deprecated
    public static boolean inMultiCombat(Entity entity) {
        for(Area area : MULTI_AREA) {
            if(area.inArea(entity.getPosition()))
                return true;
        }
        return false;
    }

    /**
     * Determines if {@code pos} is in any of the wilderness areas.
     * @param pos the position to determine if in the areas.
     * @return {@code true} if the node is in any of these areas,
     * {@code false} otherwise.
     */
    public static boolean inWilderness(Position pos) {
        return pos.inside(Location.WILDERNESS) && !pos.inside(Location.CLAN_WARS);
    }
}
