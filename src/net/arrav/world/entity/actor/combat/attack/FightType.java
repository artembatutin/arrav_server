package net.arrav.world.entity.actor.combat.attack;

import net.arrav.world.entity.actor.combat.CombatConstants;

/**
 * The enumerated type whose elements represent the fighting types.
 * @author lare96 <http://github.com/lare96>
 */
public enum FightType {
	STAFF_BASH(401, 43, 0, CombatConstants.ATTACK_CRUSH, FightStyle.ACCURATE),
	STAFF_POUND(406, 43, 1, CombatConstants.ATTACK_CRUSH, FightStyle.AGGRESSIVE),
	STAFF_FOCUS(406, 43, 2, CombatConstants.ATTACK_CRUSH, FightStyle.DEFENSIVE),
	WARHAMMER_POUND(401, 43, 0, CombatConstants.ATTACK_CRUSH, FightStyle.ACCURATE),
	WARHAMMER_PUMMEL(401, 43, 1, CombatConstants.ATTACK_CRUSH, FightStyle.AGGRESSIVE),
	WARHAMMER_BLOCK(401, 43, 2, CombatConstants.ATTACK_CRUSH, FightStyle.DEFENSIVE),
	SCYTHE_REAP(414, 43, 0, CombatConstants.ATTACK_SLASH, FightStyle.ACCURATE),
	SCYTHE_CHOP(382, 43, 1, CombatConstants.ATTACK_STAB, FightStyle.AGGRESSIVE),
	SCYTHE_JAB(2066, 43, 2, CombatConstants.ATTACK_CRUSH, FightStyle.CONTROLLED),
	SCYTHE_BLOCK(382, 43, 3, CombatConstants.ATTACK_SLASH, FightStyle.DEFENSIVE),
	BATTLEAXE_CHOP(401, 43, 0, CombatConstants.ATTACK_SLASH, FightStyle.ACCURATE),
	BATTLEAXE_HACK(401, 43, 1, CombatConstants.ATTACK_SLASH, FightStyle.AGGRESSIVE),
	BATTLEAXE_SMASH(401, 43, 2, CombatConstants.ATTACK_CRUSH, FightStyle.AGGRESSIVE),
	BATTLEAXE_BLOCK(401, 43, 3, CombatConstants.ATTACK_SLASH, FightStyle.DEFENSIVE),
	CROSSBOW_ACCURATE(4230, 43, 0, CombatConstants.ATTACK_RANGED, FightStyle.ACCURATE),
	CROSSBOW_RAPID(4230, 43, 1, CombatConstants.ATTACK_RANGED, FightStyle.AGGRESSIVE),
	CROSSBOW_LONGRANGE(4230, 43, 2, CombatConstants.ATTACK_RANGED, FightStyle.DEFENSIVE),
	SHORTBOW_ACCURATE(426, 43, 0, CombatConstants.ATTACK_RANGED, FightStyle.ACCURATE),
	SHORTBOW_RAPID(426, 43, 1, CombatConstants.ATTACK_RANGED, FightStyle.AGGRESSIVE),
	SHORTBOW_LONGRANGE(426, 43, 2, CombatConstants.ATTACK_RANGED, FightStyle.DEFENSIVE),
	LONGBOW_ACCURATE(426, 43, 0, CombatConstants.ATTACK_RANGED, FightStyle.ACCURATE),
	LONGBOW_RAPID(426, 43, 1, CombatConstants.ATTACK_RANGED, FightStyle.AGGRESSIVE),
	LONGBOW_LONGRANGE(426, 43, 2, CombatConstants.ATTACK_RANGED, FightStyle.DEFENSIVE),
	DAGGER_STAB(13049, 43, 0, CombatConstants.ATTACK_STAB, FightStyle.ACCURATE),
	DAGGER_LUNGE(13049, 43, 1, CombatConstants.ATTACK_STAB, FightStyle.AGGRESSIVE),
	DAGGER_SLASH(13048, 43, 2, CombatConstants.ATTACK_STAB, FightStyle.AGGRESSIVE),
	DAGGER_BLOCK(13049, 43, 3, CombatConstants.ATTACK_STAB, FightStyle.DEFENSIVE),
	SWORD_STAB(15072, 43, 0, CombatConstants.ATTACK_STAB, FightStyle.ACCURATE),
	SWORD_LUNGE(12310, 43, 1, CombatConstants.ATTACK_STAB, FightStyle.AGGRESSIVE),
	SWORD_SLASH(12310, 43, 2, CombatConstants.ATTACK_SLASH, FightStyle.AGGRESSIVE),
	SWORD_BLOCK(12310, 43, 3, CombatConstants.ATTACK_STAB, FightStyle.DEFENSIVE),
	SCIMITAR_CHOP(15071, 43, 0, CombatConstants.ATTACK_SLASH, FightStyle.ACCURATE),
	SCIMITAR_SLASH(15071, 43, 1, CombatConstants.ATTACK_SLASH, FightStyle.AGGRESSIVE),
	SCIMITAR_LUNGE(15072, 43, 2, CombatConstants.ATTACK_STAB, FightStyle.CONTROLLED),
	SCIMITAR_BLOCK(15071, 43, 3, CombatConstants.ATTACK_SLASH, FightStyle.DEFENSIVE),
	LONGSWORD_CHOP(12310, 43, 0, CombatConstants.ATTACK_SLASH, FightStyle.ACCURATE),
	LONGSWORD_SLASH(12310, 43, 1, CombatConstants.ATTACK_SLASH, FightStyle.AGGRESSIVE),
	LONGSWORD_LUNGE(12310, 43, 2, CombatConstants.ATTACK_STAB, FightStyle.CONTROLLED),
	LONGSWORD_BLOCK(12310, 43, 3, CombatConstants.ATTACK_SLASH, FightStyle.DEFENSIVE),
	MACE_POUND(1665, 43, 0, CombatConstants.ATTACK_CRUSH, FightStyle.ACCURATE),
	MACE_PUMMEL(1665, 43, 1, CombatConstants.ATTACK_CRUSH, FightStyle.AGGRESSIVE),
	MACE_SPIKE(13036, 43, 2, CombatConstants.ATTACK_STAB, FightStyle.CONTROLLED),
	MACE_BLOCK(1665, 43, 3, CombatConstants.ATTACK_CRUSH, FightStyle.DEFENSIVE),
	KNIFE_ACCURATE(929, 43, 0, CombatConstants.ATTACK_RANGED, FightStyle.ACCURATE),
	KNIFE_RAPID(929, 43, 1, CombatConstants.ATTACK_RANGED, FightStyle.AGGRESSIVE),
	KNIFE_LONGRANGE(929, 43, 2, CombatConstants.ATTACK_RANGED, FightStyle.DEFENSIVE),
	SPEAR_LUNGE(13045, 43, 0, CombatConstants.ATTACK_STAB, FightStyle.CONTROLLED),
	SPEAR_SWIPE(13047, 43, 1, CombatConstants.ATTACK_SLASH, FightStyle.CONTROLLED),
	SPEAR_POUND(13044, 43, 2, CombatConstants.ATTACK_CRUSH, FightStyle.CONTROLLED),
	SPEAR_BLOCK(13044, 43, 3, CombatConstants.ATTACK_STAB, FightStyle.DEFENSIVE),
	TWOHANDEDSWORD_CHOP(11981, 43, 0, CombatConstants.ATTACK_SLASH, FightStyle.ACCURATE),
	TWOHANDEDSWORD_SLASH(true, 11979, 43, 1, CombatConstants.ATTACK_SLASH, FightStyle.AGGRESSIVE),
	TWOHANDEDSWORD_SMASH(11980, 43, 2, CombatConstants.ATTACK_CRUSH, FightStyle.AGGRESSIVE),
	TWOHANDEDSWORD_BLOCK(11978, 43, 3, CombatConstants.ATTACK_SLASH, FightStyle.DEFENSIVE),
	PICKAXE_SPIKE(400, 43, 0, CombatConstants.ATTACK_STAB, FightStyle.ACCURATE),
	PICKAXE_IMPALE(400, 43, 1, CombatConstants.ATTACK_STAB, FightStyle.AGGRESSIVE),
	PICKAXE_SMASH(401, 43, 2, CombatConstants.ATTACK_CRUSH, FightStyle.AGGRESSIVE),
	PICKAXE_BLOCK(400, 43, 3, CombatConstants.ATTACK_STAB, FightStyle.DEFENSIVE),
	CLAWS_CHOP(393, 43, 0, CombatConstants.ATTACK_SLASH, FightStyle.ACCURATE),
	CLAWS_SLASH(393, 43, 1, CombatConstants.ATTACK_SLASH, FightStyle.AGGRESSIVE),
	CLAWS_LUNGE(393, 43, 2, CombatConstants.ATTACK_STAB, FightStyle.CONTROLLED),
	CLAWS_BLOCK(393, 43, 3, CombatConstants.ATTACK_SLASH, FightStyle.DEFENSIVE),
	HALBERD_JAB(440, 43, 0, CombatConstants.ATTACK_STAB, FightStyle.CONTROLLED),
	HALBERD_SWIPE(440, 43, 1, CombatConstants.ATTACK_SLASH, FightStyle.AGGRESSIVE),
	HALBERD_FEND(440, 43, 2, CombatConstants.ATTACK_STAB, FightStyle.DEFENSIVE),
	UNARMED_PUNCH(422, 43, 0, CombatConstants.ATTACK_CRUSH, FightStyle.ACCURATE),
	UNARMED_KICK(423, 43, 1, CombatConstants.ATTACK_CRUSH, FightStyle.AGGRESSIVE),
	UNARMED_BLOCK(422, 43, 2, CombatConstants.ATTACK_CRUSH, FightStyle.DEFENSIVE),
	WHIP_FLICK(11968, 43, 0, CombatConstants.ATTACK_SLASH, FightStyle.ACCURATE),
	WHIP_LASH(11969, 43, 1, CombatConstants.ATTACK_SLASH, FightStyle.CONTROLLED),
	WHIP_DEFLECT(11970, 43, 2, CombatConstants.ATTACK_SLASH, FightStyle.DEFENSIVE),
	THROWNAXE_ACCURATE(929, 43, 0, CombatConstants.ATTACK_RANGED, FightStyle.ACCURATE),
	THROWNAXE_RAPID(929, 43, 1, CombatConstants.ATTACK_RANGED, FightStyle.AGGRESSIVE),
	THROWNAXE_LONGRANGE(929, 43, 2, CombatConstants.ATTACK_RANGED, FightStyle.DEFENSIVE),
	DART_ACCURATE(929, 43, 0, CombatConstants.ATTACK_RANGED, FightStyle.ACCURATE),
	DART_RAPID(929, 43, 1, CombatConstants.ATTACK_RANGED, FightStyle.AGGRESSIVE),
	DART_LONGRANGE(929, 43, 2, CombatConstants.ATTACK_RANGED, FightStyle.DEFENSIVE),
	JAVELIN_ACCURATE(929, 43, 0, CombatConstants.ATTACK_RANGED, FightStyle.ACCURATE),
	JAVELIN_RAPID(929, 43, 2, CombatConstants.ATTACK_RANGED, FightStyle.AGGRESSIVE),
	JAVELIN_LONGRANGE(929, 43, 3, CombatConstants.ATTACK_RANGED, FightStyle.DEFENSIVE),
	SHORT_FUSE(2779, 43, 0, CombatConstants.ATTACK_RANGED, FightStyle.ACCURATE),
	MEDIUM_FUSE(2779, 43, 2, CombatConstants.ATTACK_RANGED, FightStyle.AGGRESSIVE),
	LONG_FUSE(2779, 43, 3, CombatConstants.ATTACK_RANGED, FightStyle.DEFENSIVE),
	SCORCH(5247, 43, 0, CombatConstants.ATTACK_SLASH, FightStyle.ACCURATE),
	FLARE(5247, 43, 2, CombatConstants.ATTACK_RANGED, FightStyle.ACCURATE),
	BLAZE(5247, 43, 3, CombatConstants.ATTACK_MAGIC, FightStyle.ACCURATE);
	
	/**
	 * Determines if the animation of this constant is prioritized over the
	 * cached set of weapon animations.
	 * <p>This is used because certain items such as godswords, have 2 the same fight styles
	 * (smash and slash which is both aggressive) but they don't use the same attack animation.</p>
	 */
	private final boolean animationPrioritized;
	
	/**
	 * The animation executed when this type is active.
	 */
	private final int animation;
	
	/**
	 * The parent config identification.
	 */
	private final int parent;
	
	/**
	 * The child config identification.
	 */
	private final int child;
	
	/**
	 * The type of bonus this type attributes to.
	 */
	private final int bonus;
	
	/**
	 * The style active when this type is active.
	 */
	private final FightStyle style;
	
	/**
	 * Creates a new {@link FightType}.
	 * @param animationPrioritized {@link #animationPrioritized}.
	 * @param animation            the animation executed when this type is active.
	 * @param parent               the parent config identification.
	 * @param child                the child config identification.
	 * @param bonus                the type of bonus this type will apply.
	 * @param style                the style active when this type is active.
	 */
	FightType(boolean animationPrioritized, int animation, int parent, int child, int bonus, FightStyle style) {
		this.animationPrioritized = animationPrioritized;
		this.animation = animation;
		this.parent = parent;
		this.child = child;
		this.bonus = bonus;
		this.style = style;
	}
	
	/**
	 * Creates a new {@link FightType}.
	 * @param animation the animation executed when this type is active.
	 * @param parent    the parent config identification.
	 * @param child     the child config identification.
	 * @param bonus     the type of bonus this type will apply.
	 * @param style     the style active when this type is active.
	 */
	FightType(int animation, int parent, int child, int bonus, FightStyle style) {
		this.animationPrioritized = false;
		this.animation = animation;
		this.parent = parent;
		this.child = child;
		this.bonus = bonus;
		this.style = style;
	}
	
	/**
	 * Determines the corresponding bonus for this fight type.
	 * @return the corresponding.
	 */
	public final int getCorrespondingBonus() {
		switch(getBonus()) {
			case CombatConstants.ATTACK_CRUSH:
				return CombatConstants.DEFENCE_CRUSH;
			case CombatConstants.ATTACK_MAGIC:
				return CombatConstants.DEFENCE_MAGIC;
			case CombatConstants.ATTACK_RANGED:
				return CombatConstants.DEFENCE_RANGED;
			case CombatConstants.ATTACK_SLASH:
				return CombatConstants.DEFENCE_SLASH;
			case CombatConstants.ATTACK_STAB:
				return CombatConstants.DEFENCE_STAB;
			default:
				return CombatConstants.DEFENCE_CRUSH;
		}
	}
	
	/**
	 * Determines if this animation is prioritized.
	 * @return {@code true} if it is, {@code false} otherwise.
	 */
	public final boolean isAnimationPrioritized() {
		return animationPrioritized;
	}
	
	/**
	 * Gets the animation executed when this type is active.
	 * @return the animation executed.
	 */
	public final int getAnimation() {
		return animation;
	}
	
	/**
	 * Gets the parent config identification.
	 * @return the parent config.
	 */
	public final int getParent() {
		return parent;
	}
	
	/**
	 * Gets the child config identification.
	 * @return the child config.
	 */
	public final int getChild() {
		return child;
	}
	
	/**
	 * The type of bonus this type will apply.
	 */
	/**
	 * Gets the type of bonus this type will apply
	 * @return the bonus type.
	 */
	public final int getBonus() {
		return bonus;
	}
	
	/**
	 * Gets the style active when this type is active.
	 * @return the fighting style.
	 */
	public final FightStyle getStyle() {
		return style;
	}
}