package com.rageps.world.entity.actor.mob.drop.chance;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import com.rageps.content.item.pets.PetData;
import com.rageps.util.json.JsonLoader;


/**
 * Initiates and stores {@link NpcDropChanceData} associated with different drop chance
 * modifying equipment/pets etc.
 * @author Tamatea <tamateea@gmail.com>
 */
public class NpcDropChances {



    private static ObjectArrayList<NpcDropChanceData> EQUIPMENT = new ObjectArrayList<>();
    private static ObjectArrayList<NpcDropChanceData> ITEMS = new ObjectArrayList<>();
    private static ObjectArrayList<NpcDropChanceData> PETS = new ObjectArrayList<>();

    public static void init() {
        JsonLoader loader = new JsonLoader("./data/def/drop_enhancers.json") {
            @Override
            public void load(JsonObject reader, Gson builder) {

                int[] keys = null;
                PetData pet = null;
                NpcDropChancePolicy policy;
                double dropChance;
                double fakeDropChance;
                double doubleDropChance;
                double fakeDoubleDropChance;


                if(reader.has("keys"))
                    keys = builder.fromJson(reader.get("keys"), int[].class);
                else
                    pet = PetData.valueOf(reader.get("pet").getAsString());

                policy = NpcDropChancePolicy.valueOf(reader.get("type").getAsString());
                dropChance = reader.get("dropChance").getAsDouble();
                fakeDropChance = reader.get("fakeDropChance").getAsDouble();
                doubleDropChance = reader.get("doubleChance").getAsDouble();
                fakeDoubleDropChance = reader.get("fakeDoubleChance").getAsDouble();

                NpcDropChanceData data = new NpcDropChanceData();


                data.setDropChance(dropChance);
                data.setFakeDropChance(fakeDropChance);
                data.setDoubleDropChance(doubleDropChance);
                data.setFakeDoubleDropChance(fakeDoubleDropChance);
                data.setPolicy(policy);
                if(keys != null)
                    data.setKeys(keys);
                else
                    data.setPet(pet);

                switch (policy) {
                    case PET:
                        PETS.add(data);
                       break;

                    case EQUIPMENT:
                        EQUIPMENT.add(data);
                        break;

                    case INVENTORY:
                        ITEMS.add(data);
                        break;
                }
            }
        };
        loader.load();
    }

    public static ObjectArrayList<NpcDropChanceData> getEquipment() {
        return EQUIPMENT;
    }

    public static ObjectArrayList<NpcDropChanceData> getItems() {
        return ITEMS;
    }

    public static ObjectArrayList<NpcDropChanceData> getPets() {
        return PETS;
    }
}
