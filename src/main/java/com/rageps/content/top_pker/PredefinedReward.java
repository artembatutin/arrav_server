package com.rageps.content.top_pker;

import com.google.common.collect.Sets;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.EnumSet;
import java.util.Set;

/**
 * A predefined reward that is automatically appended to the winner upon winning a session.
 */
public enum PredefinedReward implements Reward {
    MYSTERY_BOX("Mystery Box") {
        @Override
        public void append(Player player) {
            player.getBank().add(0, getItem());//todo bank item
        }

        @Override
        public Item getItem() {
            return new Item(6199, 1);
        }
    },
    SUPER_MYSTERY_BOX("Super Mystery Box") {
        @Override
        public void append(Player player) {
            player.getBank().add(0, getItem());//todo bank tab
        }

        @Override
        public boolean appendable(Player player, ZonedDateTime endDate) {
            return endDate.getDayOfWeek() == DayOfWeek.SUNDAY;
        }

        @Override
        public Item getItem() {
            return new Item(6198, 1);
        }
    },
    NONE("") {
        @Override
        public void append(Player player) {

        }

        @Override
        public boolean appendable(Player player, ZonedDateTime endDate) {
            return false;
        }

        @Override
        public Item getItem() {
            return null;
        }
    };

    public static final Set<PredefinedReward> ALL = Sets.immutableEnumSet(EnumSet.allOf(PredefinedReward.class));

    /**
     * The description of the reward.
     */
    private final String description;

    /**
     * Creates a new reward with a description.
     *
     * @param description the description of the reward.
     */
    PredefinedReward(String description) {
        this.description = description;
    }

    /**
     * The description of the reward, indicating to the player what they will receive.
     *
     * @return the description.
     */
    public String getDescription() {
        return description;
    }
}
