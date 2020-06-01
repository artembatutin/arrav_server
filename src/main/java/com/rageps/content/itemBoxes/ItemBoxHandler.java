package com.rageps.content.itemBoxes;


import com.rageps.content.itemBoxes.impl.*;
import com.rageps.world.entity.actor.player.Player;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import com.rageps.content.itemBoxes.impl.*;
import com.rageps.util.rand.Chance;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.entity.item.Item;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Handles initiating the loading and in-game interactions with item boxes.
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemBoxHandler {

    /**
     * The map of lootboxes.
     */
    public static Object2ObjectLinkedOpenHashMap<Integer, ItemBox> boxes = new Object2ObjectLinkedOpenHashMap<>();

    /**
     * Instantiates all of the {@link ItemBox} classes, which loads their loots
     * then populates the boxes map.
     */
    public static void init() {
        boxes.put(BoxConstants.ABBADON_BOX_ID, new AbbadonMBox());
        boxes.put(BoxConstants.BEGINER_D_SLAYER_BOX_ID, new BeginnerMbox());
        boxes.put(BoxConstants.SPIDER_BOX_ID, new LeFoshMbox());
        boxes.put(BoxConstants.OBLIVION_BOX_ID, new OblivionBox());
        boxes.put(BoxConstants.UBER_BOX_ID, new UberBox());
       // World.getLogger().info("loaded {} loot boxes.", boxes.size());
    }

    /**
     * Handles opening a {@link ItemBox}.
     * @param player - The player opening the box.
     * @param id - The id of the box being opened.
     */
    public static void open(Player player, int id) {

        ItemBox box = boxes.get(id);

        if(box == null)
            return;

        if (player.getInventory().remaining() < box.freeSlotsRequired()) {
            player.message("You need at least " + box.freeSlotsRequired() + " free slots on your inventory to open this.");
            return;
        }
        int lootCount = box.lootCount(player);

        if (lootCount < 1) {
            return;
        }
        player.getInventory().remove(new Item(id, 1));

        for(int roll = 0; roll < lootCount; roll++) {
            Chance rewardType = Chance.getRandomChance(box.getChances());
           // if (player.hasAttribute("force_rare")) {
           //     rewardType = Chance.VERY_RARE;
             //   player.removeAttribute("force_rare");
             //}
            Chance finalRewardType = rewardType;
            List<BoxLoot> collect = box.getItems().stream().filter(boxLoot -> boxLoot.getChance().equals(finalRewardType)).collect(Collectors.toList());
            Optional<BoxLoot> loot = Optional.of(RandomUtils.random(collect));
            BoxLoot boxLoot = loot.get();
            Item reward = boxLoot.getItem();
            player.getInventory().add(reward);
            if (boxLoot.isRare()) {
            //    WorldBroadcaster.sendGlobalMessage(TextUtility.RARE_BOX_ICON, TextUtility.MAGENTA+"Rare Loot", TextUtility.BURGUNDY,
              //          TextUtility.GOLD+player.getUsername()+TextUtility.PALE_GREEN+" has just received "+TextUtility.DARK_BLUE+reward.getDefinition().getName()+TextUtility.PALE_GREEN+" from a "+box.name()+"!", TextUtility.PALE_GREEN);
            }
        }
        //TODO - add logging (we want analytics on how are products are rewarding.
    }

    /**
     * Get's an {@link ItemBox} by it's item id. Used for the
     * rewards viewer.
     * @param id The id of the box.
     * @return The {@link ItemBox}
     */
    public static ItemBox getBox(int id) {
        return boxes.get(id);
    }

    /**
     * Get's the id of an Item box.
     * @param itemBox The box which id you are getting.
     * @return The item id of the {@link ItemBox}
     */
    public static int getID(ItemBox itemBox) {
       AtomicInteger id = new AtomicInteger(995);
        boxes.forEach( (key, object) ->{
           if(object == itemBox)
               id.set(key);
       });
        return id.get();
    }
}
