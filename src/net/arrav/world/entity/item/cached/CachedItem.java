package net.arrav.world.entity.item.cached;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.arrav.util.rand.Chance;

/**
 * Data associated with an item which will be cached, and used to reward certain actions/minigames etc.
 *
 * @author Tamatea <tamateea@gmail.com>
 */
@Data
@RequiredArgsConstructor
public class CachedItem {

    private final int id;

    private final int minimum;

    private final int maximum;

    private final Chance chance;

}
