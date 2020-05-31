package net.arrav.world.entity.actor.mob.drop.chance;

import net.arrav.world.entity.actor.player.Player;

/**
 * todo add pets
 * Handles the calculations of drop modifiers, with an option for adding
 * a fake modifier to make things like drop simulations look more appealing.
 * @author Tamatea <tamateea@gmail.com>
 */
public class NpcDropChanceHandler {


    private final Player player;


    public NpcDropChanceHandler(Player player) {
        this.player = player;
    }

    public int getDropRate(boolean fake) {
        int base = 0;

        for(NpcDropChanceData data : NpcDropChances.getEquipment()) {
            if(player.getEquipment().containsAll(data.getKeys()))
                base += fake ? data.getFakeDropChance() : data.getDropChance();
        }
        for(NpcDropChanceData data : NpcDropChances.getItems()) {
            if (player.getInventory().containsAll(data.getKeys()))
                base += fake ? data.getFakeDropChance() : data.getDropChance();
        }
        return Math.min(base, 100) / 100;
    }

    public int getDoubleDropRate(boolean fake) {
        int base = 0;

        for(NpcDropChanceData data : NpcDropChances.getEquipment()) {
            if(player.getEquipment().containsAll(data.getKeys()))
                base += fake ? data.getFakeDoubleDropChance() : data.getFakeDoubleDropChance();
        }
        for(NpcDropChanceData data : NpcDropChances.getItems()) {
            if (player.getInventory().containsAll(data.getKeys()))
                base += fake ? data.getFakeDoubleDropChance() : data.getFakeDoubleDropChance();
        }
        return Math.min(base, 100) / 100;
    }



}
