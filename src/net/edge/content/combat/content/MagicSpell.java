package net.edge.content.combat.content;

import net.edge.content.combat.CombatEffect;
import net.edge.content.combat.CombatProjectileDefinition;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.item.Item;

import java.util.Optional;
import java.util.OptionalInt;

public enum MagicSpell {
    WIND_STRIKE(1152, 1, 5.5f, null,
        CombatProjectileDefinition.getDefinition("Wind Strike"),
        new RequiredRune[]{
            new RequiredRune(MagicRune.AIR_RUNE, 1),
            new RequiredRune(MagicRune.MIND_RUNE, 1)
        }
    ),

    WATER_STRIKE(1154, 5, 7.5f, null,
        CombatProjectileDefinition.getDefinition("Water Strike"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.AIR_RUNE, 1),
            new RequiredRune(MagicRune.MIND_RUNE, 1),
            new RequiredRune(MagicRune.WATER_RUNE, 1)
        }
    ),

    EARTH_STRIKE(1156, 9, 9.5f, null,
        CombatProjectileDefinition.getDefinition("Earth Strike"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.AIR_RUNE, 1),
            new RequiredRune(MagicRune.MIND_RUNE, 1),
            new RequiredRune(MagicRune.EARTH_RUNE, 2)
        }
    ),

    FIRE_STRIKE(1158, 13, 11.5f, null,
        CombatProjectileDefinition.getDefinition("Fire Strike"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.AIR_RUNE, 2),
            new RequiredRune(MagicRune.MIND_RUNE, 1),
            new RequiredRune(MagicRune.FIRE_RUNE, 3)
        }
    ),

    WIND_BOLT(1160, 17, 13.5f, null,
        CombatProjectileDefinition.getDefinition("Wind Bolt"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.AIR_RUNE, 2),
            new RequiredRune(MagicRune.CHAOS_RUNE, 1)
        }
    ),

    WATER_BOLT(1163, 23, 16.5f, null,
        CombatProjectileDefinition.getDefinition("Water Bolt"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.AIR_RUNE, 2),
            new RequiredRune(MagicRune.CHAOS_RUNE, 1),
            new RequiredRune(MagicRune.WATER_RUNE, 2)
        }
    ),

    EARTH_BOLT(1166, 29, 19.5f, null,
        CombatProjectileDefinition.getDefinition("Earth Bolt"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.AIR_RUNE, 2),
            new RequiredRune(MagicRune.CHAOS_RUNE, 1),
            new RequiredRune(MagicRune.EARTH_RUNE, 3)
        }
    ),

    FIRE_BOLT(1169, 35, 22.5f, null,
        CombatProjectileDefinition.getDefinition("Fire Bolt"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.AIR_RUNE, 3),
            new RequiredRune(MagicRune.CHAOS_RUNE, 1),
            new RequiredRune(MagicRune.FIRE_RUNE, 4)
        }
    ),

    WIND_BLAST(1172, 41, 25.5f, null,
        CombatProjectileDefinition.getDefinition("Wind Blast"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.AIR_RUNE, 3),
            new RequiredRune(MagicRune.DEATH_RUNE, 1)
        }
    ),

    WATER_BLAST(1175, 47, 28.5f, null,
        CombatProjectileDefinition.getDefinition("Water Blast"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.AIR_RUNE, 3),
            new RequiredRune(MagicRune.WATER_RUNE, 3),
            new RequiredRune(MagicRune.DEATH_RUNE, 1)
        }
    ),

    EARTH_BLAST(1177, 53, 31.5f, null,
        CombatProjectileDefinition.getDefinition("Earth Blast"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.AIR_RUNE, 3),
            new RequiredRune(MagicRune.EARTH_RUNE, 4),
            new RequiredRune(MagicRune.DEATH_RUNE, 1)
        }
    ),

    FIRE_BLAST(1181, 59, 34.5f, null,
        CombatProjectileDefinition.getDefinition("Fire Blast"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.AIR_RUNE, 4),
            new RequiredRune(MagicRune.FIRE_RUNE, 5),
            new RequiredRune(MagicRune.DEATH_RUNE, 1)
        }
    ),

    WIND_WAVE(1183, 62, 36.0f, null,
        CombatProjectileDefinition.getDefinition("Wind Wave"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.AIR_RUNE, 5),
            new RequiredRune(MagicRune.BLOOD_RUNE, 1)
        }
    ),

    WATER_WAVE(1185, 65, 37.5f, null,
        CombatProjectileDefinition.getDefinition("Water Wave"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.AIR_RUNE, 5),
            new RequiredRune(MagicRune.BLOOD_RUNE, 1),
            new RequiredRune(MagicRune.WATER_RUNE, 7)
        }
    ),

    EARTH_WAVE(1188, 70, 40.0f, null,
        CombatProjectileDefinition.getDefinition("Earth Wave"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.AIR_RUNE, 5),
            new RequiredRune(MagicRune.BLOOD_RUNE, 1),
            new RequiredRune(MagicRune.EARTH_RUNE, 7)
        }
    ),

    FIRE_WAVE(1189, 75, 42.5f, null,
        CombatProjectileDefinition.getDefinition("Fire Wave"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.AIR_RUNE, 5),
            new RequiredRune(MagicRune.BLOOD_RUNE, 1),
            new RequiredRune(MagicRune.FIRE_RUNE, 7)
        }
    ),

    SARADOMIN_STRIKE(1190, 60, 20.0f, null,
        CombatProjectileDefinition.getDefinition("Saradomin Strike"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.BLOOD_RUNE, 2),
            new RequiredRune(MagicRune.FIRE_RUNE, 2),
            new RequiredRune(MagicRune.AIR_RUNE, 4)
        }
    ),

    CLAWS_OF_GUTHIX(1191, 60, 20.0f, null,
        CombatProjectileDefinition.getDefinition("Claws of Guthix"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.BLOOD_RUNE, 2),
            new RequiredRune(MagicRune.FIRE_RUNE, 2),
            new RequiredRune(MagicRune.AIR_RUNE, 4)
        }
    ),

    FLAMES_OF_ZAMORAK(1192, 60, 20.0f, null,
        CombatProjectileDefinition.getDefinition("Flames of Zamorak"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.BLOOD_RUNE, 2),
            new RequiredRune(MagicRune.FIRE_RUNE, 2),
            new RequiredRune(MagicRune.AIR_RUNE, 4)
        }
    ),

    BIND(1572, 20, 30.0f, null,
        CombatProjectileDefinition.getDefinition("Bind"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.NATURE_RUNE, 2),
            new RequiredRune(MagicRune.EARTH_RUNE, 3),
            new RequiredRune(MagicRune.WATER_RUNE, 3)
        }
    ),

    SNARE(1582, 50, 60.0f, null,
        CombatProjectileDefinition.getDefinition("Snare"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.NATURE_RUNE, 3),
            new RequiredRune(MagicRune.EARTH_RUNE, 4),
            new RequiredRune(MagicRune.WATER_RUNE, 4)
        }
    ),

    ENTANGLE(1592, 79, 89.0f, null,
        CombatProjectileDefinition.getDefinition("Entangle"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.NATURE_RUNE, 4),
            new RequiredRune(MagicRune.EARTH_RUNE, 5),
            new RequiredRune(MagicRune.WATER_RUNE, 5)
        }
    ),

    CONFUSE(1153, 3, 13.0f, null,
        CombatProjectileDefinition.getDefinition("Confuse"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.WATER_RUNE, 3),
            new RequiredRune(MagicRune.EARTH_RUNE, 2),
            new RequiredRune(MagicRune.BODY_RUNE, 1)
        }
    ),

    WEAKEN(1157, 11, 20.5f, null,
        CombatProjectileDefinition.getDefinition("Weaken"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.WATER_RUNE, 3),
            new RequiredRune(MagicRune.EARTH_RUNE, 2),
            new RequiredRune(MagicRune.BODY_RUNE, 1)
        }
    ),

    CURSE(1161, 19, 29.0f, null,
        CombatProjectileDefinition.getDefinition("Curse"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.WATER_RUNE, 2),
            new RequiredRune(MagicRune.EARTH_RUNE, 3),
            new RequiredRune(MagicRune.BODY_RUNE, 1)
        }
    ),

    VULNERABILITY(1542, 66, 76.0f, null,
        CombatProjectileDefinition.getDefinition("Vulnerability"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.WATER_RUNE, 5),
            new RequiredRune(MagicRune.EARTH_RUNE, 5),
            new RequiredRune(MagicRune.SOUL_RUNE, 1)
        }
    ),

    ENFEEBLE(1543, 73, 83.0f, null,
        CombatProjectileDefinition.getDefinition("Enfeeble"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.WATER_RUNE, 8),
            new RequiredRune(MagicRune.EARTH_RUNE, 8),
            new RequiredRune(MagicRune.SOUL_RUNE, 1)
        }
    ),

    STUN(1562, 73, 90.0f, null,
        CombatProjectileDefinition.getDefinition("Stun"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.WATER_RUNE, 12),
            new RequiredRune(MagicRune.EARTH_RUNE, 12),
            new RequiredRune(MagicRune.SOUL_RUNE, 1)
        }
    ),

    TRIDENT_OF_THE_SWAMP(9998, 75, 3.0f, null,
        CombatProjectileDefinition.getDefinition("Trident of the Swamp"),
        new RequiredRune[] { }
    ),

    TRIDENT_OF_THE_SEAS(9999, 75, 2.0f, null,
        CombatProjectileDefinition.getDefinition("Trident of the Seas"),
        new RequiredRune[] { }
    ),

    TELE_BLOCK(12445, 85, 42.5f, null,
        CombatProjectileDefinition.getDefinition("Tele Block"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.LAW_RUNE, 1),
            new RequiredRune(MagicRune.DEATH_RUNE, 1),
            new RequiredRune(MagicRune.CHAOS_RUNE, 1)
        }
    ),

    ICE_RUSH(12861, 58, 34.0f, null,
        CombatProjectileDefinition.getDefinition("Ice Rush"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.WATER_RUNE, 2),
            new RequiredRune(MagicRune.DEATH_RUNE, 2),
            new RequiredRune(MagicRune.CHAOS_RUNE, 2)
        }
    ),

    ICE_BLITZ(12871, 82, 46.0f, null,
        CombatProjectileDefinition.getDefinition("Ice Blitz"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.WATER_RUNE, 3),
            new RequiredRune(MagicRune.DEATH_RUNE, 2),
            new RequiredRune(MagicRune.BLOOD_RUNE, 2)
        }
    ),

    ICE_BURST(12881, 70, 40.0f, null,
        CombatProjectileDefinition.getDefinition("Ice Burst"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.WATER_RUNE, 4),
            new RequiredRune(MagicRune.DEATH_RUNE, 2),
            new RequiredRune(MagicRune.CHAOS_RUNE, 4)
        }
    ),

    ICE_BARRAGE(12891, 94, 52.0f, null,
        CombatProjectileDefinition.getDefinition("Ice Barrage"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.DEATH_RUNE, 4),
            new RequiredRune(MagicRune.BLOOD_RUNE, 2),
            new RequiredRune(MagicRune.WATER_RUNE, 6)
        }
    ),

    BLOOD_RUSH(12901, 56, 33.0f, null,
        CombatProjectileDefinition.getDefinition("Blood Rush"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.BLOOD_RUNE, 1),
            new RequiredRune(MagicRune.DEATH_RUNE, 2),
            new RequiredRune(MagicRune.CHAOS_RUNE, 2)
        }
    ),

    BLOOD_BLITZ(12911, 80, 45.0f, null,
        CombatProjectileDefinition.getDefinition("Blood Blitz"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.DEATH_RUNE, 2),
            new RequiredRune(MagicRune.BLOOD_RUNE, 4)
        }
    ),

    BLOOD_BURST(12919, 68, 39.0f, null,
        CombatProjectileDefinition.getDefinition("Blood Burst"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.BLOOD_RUNE, 2),
            new RequiredRune(MagicRune.DEATH_RUNE, 2),
            new RequiredRune(MagicRune.CHAOS_RUNE, 4)
        }
    ),

    BLOOD_BARRAGE(12929, 92, 51.0f, null,
        CombatProjectileDefinition.getDefinition("Blood Barrage"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.DEATH_RUNE, 4),
            new RequiredRune(MagicRune.BLOOD_RUNE, 4),
            new RequiredRune(MagicRune.SOUL_RUNE, 1)
        }
    ),

    SMOKE_RUSH(12939, 50, 30.0f, null,
        CombatProjectileDefinition.getDefinition("Smoke Rush"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.DEATH_RUNE, 2),
            new RequiredRune(MagicRune.CHAOS_RUNE, 2),
            new RequiredRune(MagicRune.FIRE_RUNE, 1),
            new RequiredRune(MagicRune.AIR_RUNE, 1)
        }
    ),

    SMOKE_BLITZ(12951, 74, 42.0f, null,
        CombatProjectileDefinition.getDefinition("Smoke Blitz"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.DEATH_RUNE, 2),
            new RequiredRune(MagicRune.BLOOD_RUNE, 2),
            new RequiredRune(MagicRune.FIRE_RUNE, 2),
            new RequiredRune(MagicRune.AIR_RUNE, 2)
        }
    ),

    SMOKE_BURST(12963, 62, 36.0f, null,
        CombatProjectileDefinition.getDefinition("Smoke Burst"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.DEATH_RUNE, 2),
            new RequiredRune(MagicRune.CHAOS_RUNE, 4),
            new RequiredRune(MagicRune.FIRE_RUNE, 2),
            new RequiredRune(MagicRune.AIR_RUNE, 2)
        }
    ),

    SMOKE_BARRAGE(12975, 86, 48.0f, null,
        CombatProjectileDefinition.getDefinition("Smoke Barrage"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.DEATH_RUNE, 4),
            new RequiredRune(MagicRune.BLOOD_RUNE, 2),
            new RequiredRune(MagicRune.FIRE_RUNE, 4),
            new RequiredRune(MagicRune.AIR_RUNE, 4)
        }
    ),

    SHADOW_RUSH(12987, 52, 31.0f, null,
        CombatProjectileDefinition.getDefinition("Shadow Rush"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.DEATH_RUNE, 2),
            new RequiredRune(MagicRune.CHAOS_RUNE, 2),
            new RequiredRune(MagicRune.SOUL_RUNE, 1),
            new RequiredRune(MagicRune.AIR_RUNE, 1)
        }
    ),

    SHADOW_BLITZ(12999, 76, 43.0f, null,
        CombatProjectileDefinition.getDefinition("Shadow Blitz"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.DEATH_RUNE, 2),
            new RequiredRune(MagicRune.BLOOD_RUNE, 2),
            new RequiredRune(MagicRune.SOUL_RUNE, 2),
            new RequiredRune(MagicRune.AIR_RUNE, 2)
        }
    ),

    SHADOW_BURST(13011, 64, 37.0f, null,
        CombatProjectileDefinition.getDefinition("Shadow Burst"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.SOUL_RUNE, 2),
            new RequiredRune(MagicRune.DEATH_RUNE, 2),
            new RequiredRune(MagicRune.CHAOS_RUNE, 4),
            new RequiredRune(MagicRune.AIR_RUNE, 1)
        }
    ),

    SHADOW_BARRAGE(13023, 88, 49.0f, null,
        CombatProjectileDefinition.getDefinition("Shadow Barrage"),
        new RequiredRune[] {
            new RequiredRune(MagicRune.SOUL_RUNE, 3),
            new RequiredRune(MagicRune.BLOOD_RUNE, 2),
            new RequiredRune(MagicRune.DEATH_RUNE, 4),
            new RequiredRune(MagicRune.AIR_RUNE, 4)
        }
    );

    private final int id;
    private final int level;
    private final float baseExperience;
    private final RequiredRune[] runes;
    private final Item weapon;
    private final CombatProjectileDefinition projectileDefinition;

    MagicSpell(int id, int level, float baseExperience, Item weapon, CombatProjectileDefinition projectileDefinition, RequiredRune[] runes) {
        this.id = id;
        this.level = level;
        this.baseExperience = baseExperience;
        this.runes = runes;
        this.weapon = weapon;
        this.projectileDefinition = projectileDefinition;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return projectileDefinition.getName();
    }

    public int getLevel() {
        return level;
    }

    public int getMaxHit() {
        return projectileDefinition.getMaxHit();
    }

    public OptionalInt getHitDelay() {
        return projectileDefinition.getHitDelay();
    }

    public OptionalInt getHitsplatDelay() {
        return projectileDefinition.getHitsplatDelay();
    }

    public float getBaseExperience() {
        return baseExperience;
    }

    public Optional<CombatEffect> getEffect() {
        return projectileDefinition.getEffect();
    }

    public RequiredRune[] getRunes() {
        return runes;
    }

    public Optional<Animation> getAnimation() {
        return projectileDefinition.getAnimation();
    }

    public Optional<Graphic> getStart() {
        return projectileDefinition.getStart();
    }

    public Optional<Graphic> getEnd() {
        return projectileDefinition.getEnd();
    }

    public void sendProjectile(Actor attacker, Actor defender) {
        if (projectileDefinition != null) {
            projectileDefinition.sendProjectile(attacker, defender, true);
        }
    }

    public Item getWeapon() {
        return weapon;
    }

    public static MagicSpell forId(int id) {
        for (MagicSpell spell : values()) {
            if (spell.id == id) {
                return spell;
            }
        }

        return null;
    }

}
