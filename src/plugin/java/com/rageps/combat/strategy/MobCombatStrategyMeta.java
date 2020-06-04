package com.rageps.combat.strategy;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MobCombatStrategyMeta {

	int[] ids();

}
