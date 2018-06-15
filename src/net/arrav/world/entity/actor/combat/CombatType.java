package net.arrav.world.entity.actor.combat;

import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.formula.FormulaModifier;
import net.arrav.world.entity.actor.combat.formula.MagicFormula;
import net.arrav.world.entity.actor.combat.formula.MeleeFormula;
import net.arrav.world.entity.actor.combat.formula.RangedFormula;

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
