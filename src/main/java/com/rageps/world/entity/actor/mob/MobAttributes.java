package com.rageps.world.entity.actor.mob;

import com.rageps.world.Direction;
import com.rageps.world.attr.Attr;
import com.rageps.world.attr.AttributeKey;
import com.rageps.world.attr.Attributes;

/**
 * A collection of {@link AttributeKey}'s associated with a {@link Mob}.
 * @author Tamatea Schofield <tamateea@gmail.com>
 */
public class MobAttributes {

    @Attr public static final AttributeKey NPC_FACE = Attributes.define("npc_facing", Direction.NONE);
    @Attr public static final AttributeKey IGNORED_AGGRESSION_LEVEL = Attributes.define("ignored_aggression_level", false);
    @Attr public static final AttributeKey IS_RETREATING = Attributes.define("is_retreating", false);
    @Attr public static final AttributeKey NPC_INFORMATION = Attributes.define("npc_information", 0);

}
