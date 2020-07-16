package com.rageps.world.entity.actor.player.persist.property;

/**
 * An enumerated type representing the type of object which is being persisted
 * so we know how to store it correctly.
 *
 * @author Tamatea <tamateea@gmail.com>
 */
public enum PersistancePropertyType {
    JSON,
    ENUM,
    BOOLEAN,
    STRING,
    LONG,
    INT;
}
