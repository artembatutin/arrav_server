package net.arrav.content.trivia;

import net.arrav.util.rand.RandomUtils;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 3-6-2017.
 */
public enum TriviaData {
	_1("raeldu", "Durael"),
	_2("lod is wenam", "Wise Old Man"),
	_3("RagePS", "Yanil;e"),
	_4("shan", "Hans"),
	_5("mizaze", "Zezima"),
	_6("eifr eunr", "fire rune"),
	_7("csrylat eky", "crystal key"),

	_8("What rank has a silver crown on Runescape?", "Moderator"),
	_9("What rank has a golden crown on Runescape?", "Administrator"),
	_10("What is the max exp. in a skill?", "200M"),
	_11("How much exp. do you need for level 99?", "13M"),
	_12("What is the largest state in the U.S.A?", "Alaska"),
	_13("What city is the most populated city on earth?", "Tokyo"),
	_14("What is the strongest prayer on Runescape?", "Turmoil"),
	_15("What Herblore level is required to make overloads on Runescape?", "96"),
	_16("What attack level is required to wear Chaotic Melee weapons?", "80"),
	_17("How many bones are there in an adult human body?", "206"),
	_18("What is the deadliest insect on the planet?", "Mosquito"),
	_19("What is the square root of 12 to the power of 2?", "12"),
	_20("What is the color of a 10M money stack?", "Green"),
	_21("What combat level is the almighty Jad?", "702"),
	_22("What is the best Dungeonering armour?", "Primal"),
	_23("How many brothers are there originally in Runescape?", "6"),
	_24("Varrock is the capital of which kingdom?", "Misthalin"),
	_25("What has four legs but can't walk?", "A table"),
	_26("Which NPC is wearing a 2H-Sword and a Dragon SQ Shield?", "Vannaka"),
	_27("What is the baby spider called?", "Spiderling"),
	_28("In what year did it snow in the Sahara Desert?", "1979"),
	_29("The more you take, the more you leave, who am I?", "Footsteps"),

	_30("Guess a number 1-10?", "5"),
	_31("Guess a number 1-10?", "3"),
	_32("Guess a number 1-10?", "9"),
	_33("Guess a number 1-10?", "9"),
	_34("Guess a number 1-10?", "3"),
	_35("Guess a number 1-10?", "5"),
	_36("Guess a number 1-20?", "5"),
	_37("Guess a number 1-20?", "14"),
	_38("Guess a number 1-20?", "16"),
	_39("Guess a number 1-20?", "12"),
	_40("Guess a number 1-20?", "8"),
	_41("Guess a number 1-20?", "13"),
	_42("Guess a number 1-5?", "5"),
	_43("Guess a number 1-5?", "4"),
	_44("Guess a number 1-5?", "1"),
	_45("Guess a number 1-5?", "4"),
	_46("Guess a number 1-5?", "5"),
	_47("Guess a number 1-5?", "2"),

	_48("type the following ::anwser jdj49a39ru357cnf", "jdj49a39ru357cnf"),
	_49("type the following ::anwser qpal29djeifh58cjid", "qpal29djeifh58cjid"),
	_50("type the following ::anwser qd85d4r0md42u2mssd", "qd85d4r0md42u2mssd"),
	_51("type the following ::anwser loski4893dhncbv7539", "loski4893dhncbv7539"),
	_52("type the following ::anwser 9esmf03na9admieutapdz9", "9esmf03na9admieutapdz9"),
	_53("type the following ::anwser djs83adm39s88s84masl", "djs83adm39s88s84masl"),
	_54("type the following ::anwser alskpwru39020dmsa3aeamap", "alskpwru39020dmsa3aeamap"),
	_55("Who invented the telephone?", "bell"),
	_56("What temperature does water boil at?", "100c"),
	_57("Who did Lady Diana Spencer marry?", "Prince Charles"),
	_58("Who lived at 221B, Baker Street, London?", "Sherlock Holmes"),
	_59("Who cut Van gogh's ear?", "himself"),
	_60("Who painted the Mona Lisa?", "Da vinci"),
	_61("What is the max level of a skill?", "99"),
	_62("What is the max level of combat?", "138"),
	_63("What is the hardest rock?", "Diamond"),
	_64("Which pokemon is the number 137?", "Porygon"),
	_65("What is the number of the pokemon Porygon-Z?", "474"),
	_66("What is the type of the following : True ?", "Boolean"),
	_67("What is the maximum value of a byte?", "127"),
	_68("How many possible values a byte can have?", "255")
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
	 * @param question {@link #question}.
	 * @param answers {@link #answers}.
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
