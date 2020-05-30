package net.arrav.content.itemBoxes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.arrav.util.json.JsonLoader;
import net.arrav.util.rand.Chance;
import net.arrav.world.entity.item.Item;

/**
 * @author Tamatea <tamateea@gmail.com>
 * A class used for loading loots for
 * item boxes.
 */
public class ItemBoxLootParser extends JsonLoader {

    /**
     * A map of the loots loaded from the file.
     */
    private ObjectArrayList<BoxLoot> loot = new ObjectArrayList<>();

    /**
     * The name of the thile which is to be parsed.
     */
    private String file;

    /**
     * Constructs an {@link ItemBoxLootParser}.
     * @param file The name of the file being parsed.
     */
    public ItemBoxLootParser(String file)  {
        super("./data/def/content/boxes/"+file);
    }

    /**
     * Returns the loot available from the box, if there isn't an always chance available
     * It will be possible for the box to receive no loot.
     * @return the loot list.
     */
    public ObjectArrayList<BoxLoot> getLoot() {
       load();
        boolean valid = loot.stream().anyMatch(t-> t.getChance() == Chance.ALWAYS);
       if(!valid)
                throw new UnsupportedOperationException("List must contain an ALWAYS member.");

        return loot;
    }

    @Override
    public void load(JsonObject reader, Gson builder) {
        int itemID = reader.get("id").getAsInt();
        int amt = reader.has("amount") ? reader.get("amount").getAsInt() : 1;
        boolean isRare = reader.has("rare");
        Chance chance = Chance.valueOf(reader.get("chance").getAsString());
        loot.add(new BoxLoot(new Item(itemID, amt), chance, isRare));
    }

}
