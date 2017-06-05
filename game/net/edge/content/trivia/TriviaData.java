package net.edge.content.trivia;

import net.edge.util.rand.RandomUtils;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 3-6-2017.
 */
public enum TriviaData {
    _1("Who are the owners of Edgeville?", "avro and Stan", "avro & Stan"),
    _2("Name one game developer.", "avro", "stan"),
    _3("What is the maximum combat level?", "138"),
    _4("How much coins does a starterpack in Edgeville contain?", "200k", "200000"),
    _5("Who is the shop owner of the Blood money store?", "hazelmere"),
    _6("Who is the shop owner of the Edge tokens store?", "party Pete"),
    _7("How many Hitpoints do Sharks heal?", "20", "Twenty", "200", "two hundred"),
    _8("What Magic level is needed to cast Ice Barrage?", "94"),
    _9("What is the highest tier defender?", "dragon", "dragon defender"),
    _10("What is the required Attack level needed to wield the Abyssal whip?", "70", "seventy"),
    _11("What is the required Strength level needed to wield the Tzhaar-ket-om?", "60", "sixty"),
    _12("What is the required Ranged level needed to wield Red chinchompas?", "55", "fifty five"),
    _13("How many charges can an amulet of glory hold?", "6", "six"),
    _14("What herb is required to make a Prayer potion?", "ranarr"),
    _15("What boss drops the Elysian sigil?", "corporeal beast"),
    _16("The Amulet of fury is created using which gem?", "onyx"),
    _17("How many types of godswords are there?", "4", "four"),
    _18("What is the Prayer requirement needed to wield the Elysian spirit shield?", "75", "seventy five"),
    _19("What ore can be mined at level 70 Mining?", "adamantite", "adamantite ore", "adamamant ore"),
    _20("Which godsword has the strongest special attack in PvP?", "armadyl godsword"),
    _21("Which godswords special attack can heal you?", "saradomin godsword"),
    _22("Which godswords special attack drains your opponents stats?", "bandos godsword"),
    _23("Which godswords special attack freezes your enemy?", "zamorak godsword"),
    _24("Where can you quit the night's watch if you play in nightmare mode?", "quest tab"),
    _25("How many logs are needed to fire the double experience fire pit?", "1000"),
    _26("Where is the thieving located? Hint: introduction scene name.", "construction site"),
    _27("How can you get Blood money currency?", "pvp", "kill players"),
    _28("How many buildings does the home has? (included broken ones)", "5", "five"),
    _29("Who is the owner of the mansion at home?", "sir prysin"),
    _30("With who the make-over mage is sharing his house with?", "aubury"),
    _31("How many levels you need to go on second floor mansion?", "2000", "two thousands"),
    _32("If you want a new monster drop, where can you suggest a drop?", "monster database", "monster panel", "monster db")
    ;

    /**
     * Caches our enum values.
     */
    public static final TriviaData[] VALUES = values();

    /**
     * The question for this trivia.
     */
    public final String question;

    /**
     * The possible answers for this trivia
     */
    public final String[] answers;

    /**
     * Constructs a new {@link TriviaData}.
     * @param question  {@link #question}.
     * @param answers   {@link #answers}.
     */
    TriviaData(String question, String... answers) {
        this.question = question;
        this.answers = answers;
    }

    /**
     * Gets a random question.
     * @return a random question.
     */
    public static TriviaData random() {
        return RandomUtils.random(VALUES);
    }
}
