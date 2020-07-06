package com.rageps.content.activity;

import com.rageps.util.StringUtil;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.text.MessageBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;

import java.util.Optional;

/**
 * Handles logging and getting values associated with completed in-game.
 * activities.
 *
 * todo - persistance
 *
 * @author Tamatea Schofield <tamateea@gmail.com>
 */
public class ActivityLogger {

    /**
     * The player associated with this {@link ActivityLogger}.
     */
    private Player player;

    /**
     * Maps of the players activity data.
     */
    private Object2ObjectMap<ActivityType, Integer> ACTIVITY_MAP = new Object2ObjectArrayMap<>();

    /**
     * Constructs a new Activity Logger.
     * @param player the player it's being constructed for.
     */
    public ActivityLogger(Player player) {
        this.player = player;
    }

    /**
     * Logs an activity to the logger. Will either increment or add the value
     * to the map.
     * @param type The type of activity.
     * @param increment The amount the activity is being incremented by.
     */
    private void log(ActivityType type, int increment) {
        log(type, increment, false);
    }

    /**
     * Logs an activity to the logger. Will either increment or add the value
     * to the map.
     * @param type The type of activity.
     * @param increment The amount the activity is being incremented by.
     * @param silent Whether or not it should be sent as a message to the player.
     */
    private void log(ActivityType type, int increment, boolean silent) {
        int count = Optional.of(ACTIVITY_MAP.get(type)).orElse(0) + increment;
        ACTIVITY_MAP.put(type, count);

        if(!silent) {
            MessageBuilder mb = new MessageBuilder();
            mb.append("Your ").append(StringUtil.formatEnumString(type), type.getMessageColor()).append(" count is now");
            player.message(mb.toString());

            if(type.isBroadcast() && type.getMessage() != null) {
                World.get().message(type.getMessage());
            }
        }

    }

    /**
     * Get's the amount of {@link ActivityType} completed by the player.
     * @param type The type of activity count being returned.
     * @return The completed count.
     */
    private int getCount(ActivityType type) {
        return Optional.of(ACTIVITY_MAP.get(type)).orElse(0);
    }
}
