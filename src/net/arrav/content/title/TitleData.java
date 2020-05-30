package net.arrav.content.title;


import net.arrav.util.TextUtils;
import net.arrav.world.entity.actor.player.Player;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An enumerated type containing all data related to In-game titles.
 * @author Tamatea <tamateea@gmail.com>
 */
public enum TitleData {


    THE_GRINDER("Have 100 hours of ingame \\n play time.", 5_000_000, TitlePolicy.MISC) {
        @Override
        boolean hasReq(Player player) {
            return true;
        }

        @Override
        int getProgress(Player player) {
            return 89;
        }
    },
    QUIZ_MASTER("Successfully answer 50\\n Trivia questions.", 15_000_000_000L, TitlePolicy.MISC) {
        @Override
        boolean hasReq(Player player) {
            return true;
        }

        @Override
        int getProgress(Player player) {
            return 89;
        }
    },
    LEARNT_THE_ROPES("Complete all of the\\n Starter tasks.", 10_000_000, TitlePolicy.MISC) {
        @Override
        boolean hasReq(Player player) {
            return true;
        }

        @Override
        int getProgress(Player player) {
            return 89;
        }
    },

    IRONMAN("Be an ironman.", 0, TitlePolicy.MISC) {
        @Override
        boolean hasReq(Player player) {
            return player.isIronMan();
        }

        @Override
        int getProgress(Player player) {
            return 0;
        }
    },





    LITERAL_GOD("This is a special title unlocked \\n as a reward from management.", 5_000_000, TitlePolicy.MISC) {
        @Override
        boolean hasReq(Player player) {
            return false;
        }

        @Override
        int getProgress(Player player) {
            return 0;
        }
    },
    DRAGON_RIDER("consensually fuck children", 5_000_000, TitlePolicy.MISC) {
        @Override
        boolean hasReq(Player player) {
            return false;
        }

        @Override
        int getProgress(Player player) {
            return 50;
        }
    },
    PEcsdcsd("consensually fuck children", 5_000_000, TitlePolicy.PVM) {
        @Override
        boolean hasReq(Player player) {
            return false;
        }

        @Override
        int getProgress(Player player) {
            return 50;
        }
    },
    Pcsdfv("consensually fuck children", 5_000_000, TitlePolicy.PVM) {
        @Override
        boolean hasReq(Player player) {
            return false;
        }

        @Override
        int getProgress(Player player) {
            return 50;
        }
    },
    PEcsdcdsE5("consensually fuck children", 5_000_000, TitlePolicy.PVM) {
        @Override
        boolean hasReq(Player player) {
            return false;
        }

        @Override
        int getProgress(Player player) {
            return 50;
        }
    },




    ;


    /**
     * The requirements of the title which shall be displayed on the interface.
     */
    private String requirement;


    /**
     * The cost associated with unlocking the title once the player meets the criteria.
     */
    private long cost;


    /**
     * The type of title.
     */
    private TitlePolicy type;


    /**
     * Tests that the player has the requirement to unlock the title.
     */
    abstract boolean hasReq(Player player);


    /**
     * The players progress on unlocking the title, to be displayed on the title interface.
     */
    abstract int getProgress(Player player);




    TitleData(String requirement, long cost, TitlePolicy type) {
        this.requirement = requirement;
        this.cost = cost;
        this.type = type;
    }

    public long getCost() {
        return cost;
    }

    public String getRequirement() {
        return requirement;
    }

    public TitlePolicy getType() {
        return type;
    }

    String formattedName() {
        return TextUtils.capitalize(name().replaceAll("_", " "));
    }

    public static List<TitleData> getTitles(TitlePolicy type) {
        return Stream.of(VALUES).filter($it -> $it.getType() == type).collect(Collectors.toList());
    }

    /**
     * An enumerated type containing the different
     * types of unlockable titles available.
     */
    public enum TitlePolicy {
        MISC, PVM, SKILLING, RAIDS;

        /**
         * Gets an {@link TitlePolicy} based on it's ordinal value.
         * @param ordinal The ordinal value.
         * @return The desired title policy.
         */
        public static TitlePolicy ofOrdinal(int ordinal) {
            for(TitlePolicy data : VALUES) {
                if(data.ordinal() == ordinal)
                    return data;
            }
            return null;
        }

        public static final TitlePolicy[] VALUES = values();

    }


    public static final TitleData[] VALUES = values();

}
