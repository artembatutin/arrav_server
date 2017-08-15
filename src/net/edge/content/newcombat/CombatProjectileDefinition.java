package net.edge.content.newcombat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.edge.content.newcombat.content.ProjectileEffect;
import net.edge.util.json.JsonLoader;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.Projectile;
import net.edge.world.entity.actor.Actor;

import java.util.HashMap;
import java.util.Optional;

public final class CombatProjectileDefinition {
    private final String name;
    private int maxHit;
    private int hitDelay;
    private int hitsplatDelay;
    private CombatEffect effect;
    private Animation animation;
    private Graphic start;
    private Graphic end;
    private ProjectileBuilder projectile;

    private CombatProjectileDefinition(String name) {
        this.name = name;
    }

    private static HashMap<String, CombatProjectileDefinition> DEFINITIONS;

    public String getName() {
        return name;
    }

    public int getMaxHit() {
        return maxHit;
    }

    public int getHitDelay() {
        return hitDelay;
    }

    public int getHitsplatDelay() {
        return hitsplatDelay;
    }

    public Optional<CombatEffect> getEffect() {
        return Optional.of(effect);
    }

    public Optional<Animation> getAnimation() {
        return Optional.of(animation);
    }

    public Optional<Graphic> getStart() {
        return Optional.of(start);
    }

    public Optional<Graphic> getEnd() {
        return Optional.of(end);
    }

    public void sendProjectile(Actor attacker, Actor defender) {
        Optional.of(projectile).ifPresent(projectile -> projectile.send(attacker, defender));
    }

    public static CombatProjectileDefinition getDefinition(String name) {
        return DEFINITIONS.get(name);
    }

    public static JsonLoader createLoader() {
        return new JsonLoader("./data/def/combat/projectile_definitions.json") {
            @Override
            protected void initialize(int size) {
                DEFINITIONS = new HashMap<>(size);
            }

            @Override
            public void load(JsonObject reader, Gson builder) {
                CombatProjectileDefinition definition = new CombatProjectileDefinition(reader.get("name").getAsString());

                definition.hitDelay = 2;
                if (reader.has("hit-delay")) {
                    definition.hitDelay = reader.get("hit-delay").getAsInt();
                }

                definition.hitsplatDelay = 1;
                if (reader.has("hitsplat-delay")) {
                    definition.hitsplatDelay = reader.get("hitsplat-delay").getAsInt();
                }

                definition.maxHit = 0;
                if (reader.has("max-hit")) {
                    definition.maxHit = reader.get("max-hit").getAsInt();
                }

                definition.effect = null;
                if (reader.has("combat-effect")) {
                    definition.effect = ProjectileEffect.valueOf(reader.get("combat-effect").getAsString()).getEffect();
                }

                definition.animation = null;
                if (reader.has("animation")) {
                    definition.animation = builder.fromJson(reader.get("animation"), Animation.class);
                }

                definition.start = null;
                if (reader.has("start")) {
                    definition.start = builder.fromJson(reader.get("start"), Graphic.class);
                }

                definition.end = null;
                if (reader.has("end")) {
                    definition.end = builder.fromJson(reader.get("end"), Graphic.class);
                }

                definition.projectile = null;
                if (reader.has("projectile")) {
                    definition.projectile = builder.fromJson(reader.get("projectile"), ProjectileBuilder.class);
                }

                DEFINITIONS.put(definition.name, definition);
            }
        };
    }

    private final class ProjectileBuilder {
        private short id;
        private byte size;
        private byte delay;
        private byte duration;
        private byte startHeight;
        private byte endHeight;
        private byte curve;

        public void send(Actor attacker, Actor defender) {
            Projectile projectile = new Projectile(attacker, defender, id, duration, delay, startHeight, endHeight, curve);
            projectile.sendProjectile();
        }

    }

}
