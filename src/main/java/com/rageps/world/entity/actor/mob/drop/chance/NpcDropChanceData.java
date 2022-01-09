package com.rageps.world.entity.actor.mob.drop.chance;

import com.rageps.content.item.pets.PetData;
import lombok.Data;

/**
 * A data class with properties associated with drop modifiers.
 *
 * @author Tamatea <tamateea@gmail.com>
 */
@Data
class NpcDropChanceData {

    /**
     * A pet that can be associated with this.
     */
    private PetData pet;

    /**
     * The type of drop chance policy.
     */
    private NpcDropChancePolicy policy;

    /**
     * The ids associated with this.
     */
    private int[] keys;

    /**
     * A drop chance modifier associated with this.
     */
    private double dropChance;

    /**
     * A double drop chance modifier associated with this.
     */
    private double doubleDropChance;

    /**
     * A fake drop chance modifier associated with this.
     */
    private double fakeDropChance;

    /**
     * A faked double drop chance modifier associated with this.
     */
    private double fakeDoubleDropChance;

}
