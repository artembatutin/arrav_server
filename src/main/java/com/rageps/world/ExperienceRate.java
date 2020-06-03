package com.rageps.world;


import com.rageps.util.StringUtil;
import com.rageps.world.entity.actor.player.Player;

import static com.rageps.content.skill.Skills.*;

public enum ExperienceRate {

    NORMAL(10) {
        @Override
        public int getMultiplier(Player player, int skill) {
            int base = 20;

            switch (skill) {
                case ATTACK:
                case DEFENCE:
                case STRENGTH:
                case HITPOINTS:
                case RANGED:
                case MAGIC:
                    base = 400;
                    if (player.isIronMan()) {
                        base = 40;
                    }
                    break;
                case MINING:
                case AGILITY:
                case SLAYER:
                case THIEVING:
                    base = 30;
                    break;
                case FLETCHING:
                case RUNECRAFTING:
                    base = 35;
                    break;
                case SMITHING:
                    base = 40;
                    break;
                case PRAYER:
                case CRAFTING:
                case HERBLORE:
                case FARMING:
                    base = 25;
                    break;
            }
            return base;
        }
    },

    EXTREME(10) {
        @Override
        public int getMultiplier(Player player, int skill) {
            int base = 10;

            switch (skill) {
                case SMITHING:
                    base = 25;
                    break;
            }
            return base;
        }
    },

    SUPREME(5) {
        @Override
        public int getMultiplier(Player player, int skill) {
            int base = 5;

            switch (skill) {
                case SMITHING:
                    base = 10;
                    break;
            }
            return base;
        }
    },

    REALISM(2) {
        @Override
        public int getMultiplier(Player player, int skill) {
            int base = 2;

            switch (skill) {
                case SMITHING:
                    base = 5;
                    break;
            }
            return base;
        }
    };

    private final int base;

    ExperienceRate(int base) {
        this.base = base;
    }

    public int getBase() {
        return base;
    }

    public abstract int getMultiplier(Player player, int skill);

    @Override
    public String toString() {
        return StringUtil.formatEnumString(this);
    }

}
