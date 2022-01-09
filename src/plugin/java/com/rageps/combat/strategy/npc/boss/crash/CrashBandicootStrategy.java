package com.rageps.combat.strategy.npc.boss.crash;

import com.rageps.combat.strategy.MobCombatStrategyMeta;
import com.rageps.combat.strategy.npc.MultiStrategy;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.model.Animation;
import com.rageps.world.World;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.CombatType;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.region.TraversalMap;
import com.rageps.world.locale.Position;
import it.unimi.dsi.fastutil.objects.ObjectList;


/**
 * @author Tamatea <tamateea@gmail.com>
 */
@MobCombatStrategyMeta(ids = 6227)
public class CrashBandicootStrategy extends MultiStrategy {

    private final int AKU_AKU_ID = 0;

    private final Animation SPIN_ANIMATION = new Animation(0);

    private boolean hasFamiliar;

    private int familiarStage = 0;

    private Mob akuAku;

    @Override
    public void block(Actor attacker, Mob crash, Hit hit, CombatType combatType) {
        if(shouldSpawnMinion(crash))
            if(attacker.isPlayer())
            spawnAkuAku(crash, attacker);

        super.block(attacker, crash, hit, combatType);
    }

    private boolean shouldSpawnMinion(Mob crash) {
        if(hasFamiliar)
            return false;
        int health = crash.getCurrentHealth();
        return health <= (crash.getMaxHealth() - crash.getMaxHealth() * (Math.max(1, familiarStage * 0.25)));
    }

    private void spawnAkuAku(Mob crash, Actor player) {
        crash.forceChat("Aku Aku, help me out!");
        crash.animation(SPIN_ANIMATION);


        Position position;
        ObjectList<Position> positions = TraversalMap.getNearbyTraversableTiles(crash.getPosition().copy(), 2);
        if(positions.isEmpty())
            position = crash.getPosition().copy();
        else
            position = RandomUtils.random(positions);

        akuAku = new Mob(AKU_AKU_ID, position) {
            @Override
            public Mob create() {
                return this;
            }
            @Override
            public void appendDeath() {
                super.appendDeath();
                hasFamiliar = false;
            }
        };
        World.get().getMobRepository().add(akuAku);
        hasFamiliar = true;
        familiarStage++;
        akuAku.delay(1, () -> {
            akuAku.forceChat("Ugga Bugga!");
            akuAku.getCombat().attack(player);
        });
    }
}
