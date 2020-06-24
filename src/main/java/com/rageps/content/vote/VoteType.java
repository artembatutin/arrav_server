package com.rageps.content.vote;

/**
 * An enumerated type containing all the different types of votes,
 * and their policies associated with them.
 *
 * @author Tamatea <tamateea@gmail.com>
 */
public enum VoteType {

    RUNELOCUS(0, 3);

    /**
     * The amount of points this vote is worth.
     */
    private int points;

    /**
     * The identifier of the vote type when the vote is transmitted.
     */
    private int protocolValue;


    VoteType(int protocolValue, int points) {
        this.points = points;
        this.protocolValue = protocolValue;
    }

    public int getPoints() {
        return points;
    }

    public int getProtocolValue() {
        return protocolValue;
    }

    /**
     * Constructs a {@link VoteType} from the transmitted value of the vote.
     * @param protocolValue The identifier of the vote type.
     * @return the {@link VoteType}.
     */
    public static VoteType ofValue(int protocolValue) {
        for(VoteType type : VALUES) {
            if(type.protocolValue == protocolValue)
                return type;
        }
        throw new UnsupportedOperationException("This value type doesn't exist!");
    }

    /**
     * Cached values.
     */
    public static final VoteType[] VALUES = values();

}
