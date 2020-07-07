package com.rageps.world.entity.actor.player.assets.group;


import com.rageps.world.attr.Attr;
import com.rageps.world.attr.AttributeKey;
import com.rageps.world.attr.Attributes;

/**
 * Created by Jason M on 2017-04-04 at 1:44 PM
 * <p>
 * Attributes that are particularly accessed by privileged players.
 */
public final class PrivilegedAttributes {
    @Attr
    public static final AttributeKey DAILY_STATISTICS_GENERATOR_TASK = Attributes.defineEmpty("daily_stats_generator_task");
}
