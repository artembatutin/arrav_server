package com.rageps.world.entity.actor.mob.drop.chance;

import com.rageps.content.item.pets.PetData;
import com.rageps.world.entity.actor.player.Player;

import java.util.Optional;

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

        if(player.getPetManager().getPet().isPresent())
        for(NpcDropChanceData data : NpcDropChances.getPets()) {
            int id = player.getPetManager().getPet().get().getId();
            Optional<PetData> petData = PetData.getNpc(id);
            if(petData.isPresent() && petData.get() == data.getPet())
                base+= fake ? data.getFakeDropChance() : data.getDropChance();
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
        if(player.getPetManager().getPet().isPresent())
            for(NpcDropChanceData data : NpcDropChances.getPets()) {
                int id = player.getPetManager().getPet().get().getId();
                Optional<PetData> petData = PetData.getNpc(id);
                if(petData.isPresent() && petData.get() == data.getPet())
                    base+= fake ? data.getFakeDoubleDropChance() : data.getDoubleDropChance();
            }
        return Math.min(base, 100) / 100;
    }



}
