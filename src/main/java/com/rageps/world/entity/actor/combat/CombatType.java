package com.rageps.world.entity.actor.combat;

import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.formula.FormulaModifier;
import com.rageps.world.entity.actor.combat.formula.MagicFormula;
import com.rageps.world.entity.actor.combat.formula.MeleeFormula;
import com.rageps.world.entity.actor.combat.formula.RangedFormula;

public enum CombatType {
	MELEE(new MeleeFormula()), RANGED(new RangedFormula()), MAGIC(new MagicFormula()), NONE(null);
	
	private final FormulaModifier<Actor> formula;
	
	CombatType(FormulaModifier<Actor> formula) {
		this.formula = formula;
	}
	
	public FormulaModifier<Actor> getFormula() {
		return formula;
	}
	
}
