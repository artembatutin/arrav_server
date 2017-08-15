package net.edge.content.newcombat.weapon;

import net.edge.content.newcombat.CombatProjectileDefinition;
import net.edge.content.newcombat.strategy.CombatStrategy;
import net.edge.content.newcombat.strategy.player.melee.*;
import net.edge.content.newcombat.strategy.player.ranged.impl.*;
import net.edge.world.entity.actor.player.Player;

import java.util.function.BiFunction;

public enum WeaponType {
    DEFAULT(5855, 5857, new SADefinition(7749, 7761, 7737), new FistStrategy()),
    DART(4446, 4449, new SADefinition(7649, 7661, 7637), DartWeapon::new),
    KNIFE(4446, 4449, new SADefinition(7649, 7661, 7637), KnifeWeapon::new),
    THROWNAXE(4446, 4449, new SADefinition(7649, 7661, 7637), ThrownaxeWeapon::new),
    SHORTBOW(1764, 1767, new SADefinition(7549, 7561, 7548), ShortbowWeapon::new),
    LONGBOW(1764, 1767, new SADefinition(7549, 7561, 7548), LongbowWeapon::new),
    COMP_BOW(1764, 1767, new SADefinition(7549, 7561, 7548), CompBowWeapon::new),
    CROSSBOW(1764, 1767, new SADefinition(7549, 7561, 7548), CrossbowWeapon::new),
    WHIP(12290, 12293, new SADefinition(12323, 12335, 12322), new WhipWeapon()),
    WARHAMMER_OR_MAUL(425, 428, new SADefinition(7474, 7486, 7473), new WarhammerOrMaulWeapon()),
    SPEAR(4679, 4682, new SADefinition(7674, 7686, 7662), new SpearWeapon()),
    STAFF(6103, 6132, new SADefinition(6117, 6129, 6104), new StaffWeapon()),
    MAGIC_STAFF(328, 355, new SADefinition(18566, 18569, 340), new StaffWeapon()),
    HALBERD(8460, 8463, new SADefinition(8493, 8505, 8481), new HalberdWeapon()),
    SWORD_OR_DAGGER(2276, 2279, new SADefinition(7574, 7586, 7562), new SwordOrDaggerWeapon()),
    TWO_HANDED(4705, 4708, new SADefinition(7699, 7711, 7687), new TwoHandedWeapon()),
    LONGSWORD_OR_SCIMITAR(2423, 2426, new SADefinition(7599, 7611, 7587), new LongswordOrScimitarWeapon()),
    PICKAXE(5570, 5573, new SADefinition(7724, 7736, 7723), new PickaxeWeapon()),
    BATTLEAXE(1698, 1701, new SADefinition(7499, 7591, 7498), new BattleaxeWeapon()),
    CLAWS(7762, 7765, new SADefinition(7800, 7812, 7788), new ClawsWeapon()),
    MACE(3796, 3799, new SADefinition(7624, 7636, 7623), new MaceWeapon());

    private final int interfaceId;
    private final int stringId;
    private final SADefinition special;
    private final BiFunction<RangedWeaponDefinition, CombatProjectileDefinition, CombatStrategy<Player>> combatStrategy;

    WeaponType(int interfaceId, int stringId, SADefinition special, CombatStrategy<Player> combatStrategy) {
        this.interfaceId = interfaceId;
        this.stringId = stringId;
        this.special = special;
        this.combatStrategy = (rdef, proj) -> combatStrategy;
    }

    WeaponType(int interfaceId, int stringId, SADefinition special, BiFunction<RangedWeaponDefinition, CombatProjectileDefinition, CombatStrategy<Player>> combatStrategy) {
        this.interfaceId = interfaceId;
        this.stringId = stringId;
        this.special = special;
        this.combatStrategy = combatStrategy;
    }

    public int getInterfaceId() {
        return interfaceId;
    }

    public int getStringId() {
        return stringId;
    }

    public int getLayerId() {
        return special.layerId;
    }

    public int getSpecialStringId() {
        return special.stringId;
    }

    public int getButtonId() {
        return special.buttonId;
    }

    public CombatStrategy<Player> getCombatStrategy() {
        return combatStrategy.apply(null, null);
    }

    public CombatStrategy<Player> getCombatStrategy(RangedWeaponDefinition rangedDefinition, CombatProjectileDefinition projectileDefinition) {
        return combatStrategy.apply(rangedDefinition, projectileDefinition);
    }

    private static class SADefinition {
        private final int layerId;
        private final int stringId;
        private final int buttonId;

        SADefinition(int layerId, int stringId, int buttonId) {
            this.layerId = layerId;
            this.stringId = stringId;
            this.buttonId = buttonId;
        }

    }

}