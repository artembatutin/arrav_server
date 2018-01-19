package net.arrav.content.dialogue;

/**
 * The enumerated type whose elements represent the expressions a character can
 * take on.
 * @author lare96 <http://github.com/lare96>
 */
public enum Expression {
	CALM(9760),
	CRYING(9765),
	SHY(9770),
	SAD(9775),
	SCARED(9780),
	MAD(9785),
	VERY_ANGRY(9790),
	CONFUSED(9830),
	ANGRY(9795),
	VERY_VERY_ANGRY(9800),
	SAYS_NOTHING(9805),
	SERIOUS(9810),
	AGREEING(9815),
	QUESTIONING(9827),
	DRUNK(9835),
	LAUGH(9840),
	HEAD_SWAYS_TALKS_FAST(9845),
	HAPPY(9850),
	STIFF(9855),
	//
	STIFF_EYES_MOVE(9860),
	PRIDEFUL(9865),
	DEMENTED(9870),
	//
	REFUSING(9811),
	INTERROGATING(9820);
	
	/**
	 * The identification for this expression.
	 */
	private final int expression;
	
	/**
	 * Creates a new {@link Expression}.
	 * @param expression the identification for this expression.
	 */
	Expression(int expression) {
		this.expression = expression;
	}
	
	/**
	 * Gets the identification for this expression.
	 * @return the expression.
	 */
	public final int getExpression() {
		return expression;
	}
}