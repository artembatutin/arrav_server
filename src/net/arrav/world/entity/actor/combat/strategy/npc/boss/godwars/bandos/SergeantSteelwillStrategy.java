package net.arrav.world.entity.actor.combat.strategy.npc.boss.godwars.bandos;

import net.arrav.world.Animation;
import net.arrav.world.Graphic;
import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.CombatType;
import net.arrav.world.entity.actor.combat.attack.FightType;
import net.arrav.world.entity.actor.combat.hit.CombatHit;
import net.arrav.world.entity.actor.combat.projectile.CombatProjectile;
import net.arrav.world.entity.actor.combat.projectile.ProjectileBuilder;
import net.arrav.world.entity.actor.combat.strategy.CombatStrategy;
import net.arrav.world.entity.actor.combat.strategy.npc.NpcMagicStrategy;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.mob.impl.godwars.GeneralGraardor;

import static net.arrav.world.entity.actor.combat.projectile.CombatProjectile.getDefinition;

/**
 * Created by Dave/Ophion
 * Date: 04/02/2018
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 */
public class SergeantSteelwillStrategy extends NpcMagicStrategy {

    public SergeantSteelwillStrategy() {
        super(getDefinition("Sergeant steelwill"));
    }

    @Override
    public boolean canAttack(Mob attacker, Actor defender) {
        return defender.isPlayer() && GeneralGraardor.CHAMBER.inLocation(defender.getPosition());

    }

    @Override
    public boolean hitBack() {
        return false;
    }
}
