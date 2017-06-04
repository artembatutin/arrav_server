package net.edge.content.trivia;

import com.google.common.collect.ImmutableSet;
import net.edge.util.rand.RandomUtils;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 3-6-2017.
 */
public enum TriviaData {
    _1("Who are the owners of Edgeville?", "Avro and Stan", "Avro & Stan", "Stan & Avro", "Stan and Avro"),
    _2("Name one game developer.", "Avro", "Stan"),
    _3("What is the maximum combat level?", "138"),
    _4("How much coins does a starterpack in Edgeville contain?", "200k", "200000", "200_000"),
    _5("Who is the shop owner of the Blood money store?", "Hazelmere"),
    _6("Who is the shop owner of the Edge tokens store?", "Party Pete"),
    _7("How many Hitpoints do Sharks heal?", "20", "Twenty", "200", "Two-hundred", "Two hundred"),
    _8("What Magic level is needed to cast Ice Barrage?", "94"),
    _9("What is the highest tier defender?", "dragon", "dragon defender"),
    _10("What is the required Attack level needed to wield the Abyssal whip?", "70", "Seventy"),
    _11("What is the required Strength level needed to wield the Tzhaar-ket-om?", "60", "Sixty"),
    _12("What is the required Ranged level needed to wield Red chinchompas?", "55", "Fifty-five", "Fifty five"),
    _13("How many charges can an amulet of glory hold?", "6", "Six"),
    _14("What herb is required to make a Prayer potion?", "Ranarr"),
    _15("What boss drops the Elysian sigil?", "Corporeal Beast"),
    _16("The Amulet of fury is created using which gem?", "Onyx"),
    _17("How many types of godswords are there?", "4", "Four"),
    _18("What is the Prayer requirement needed to wield the Elysian spirit shield?", "75", "Seventy-five", "Seventy five"),
    _19("What ore can be mined at level 70 Mining?", "Adamantite", "Adamantite ore", "Adamamant ore"),
    _20("Which godsword has the strongest special attack in PvP?", "Armadyl godsword"),
    _21("Which godswords special attack can heal you?", "Saradomin godsword"),
    _22("Which godswords special attack drains your opponents stats?", "Bandos godsword"),
    _23("Which godswords special attack freezes your enemy?", "Zamorak godsword"),
    _24("Where can you quit the night's watch if you play in nightmare mode?", "Quest tab", "quest tab")
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
