package com.rageps.content.drops;

import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.mob.MobDefinition;
import com.rageps.world.entity.actor.mob.drop.DropManager;
import com.rageps.world.entity.actor.mob.drop.DropTable;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Tamatea <tamateea@gmail.com>
 *
 * Handles interactions with the drop simulator interface. Used for displaying a simulated
 * amount of drops from xx {@link Mob}.
 */
public class DropSimulator {

    public static final String KEY_SIMULATED_AMOUNT = "SIMULATED_AMT";
    public static final String KEY_SELECTED_NPC = "SIMULATED_NPC";
    public static final String KEY_SEARCHED_NPCS = "SIMULATED_NPC_SEARCH";
    public static final String KEY_RARES_ONLY = "SIMULATED_RARES";
    public static final String KEY_DROP_RARE = "SIMULATED_DR";
    public static final String KEY_DOUBLE_DROPS = "SIMULATED_DDR";

    public static final int STRING_NAME = 72504;
    public static final int STRING_SIMULATED = 72505;
    public static final int STRING_VALUE = 72506;


    public static final int INTERFACE_ID = 72500;
    public static final int ITEM_CONTAINER = 72521;
    public static final int ITEMS_SCROLL = 72520;
    public static final int NPCS_SCROLL = 72522;
    public static final int NPCS_START = 72524;




    public static final int SIMULATE_BUTTON = 6971;
    public static final int NPC_BUTTON_START = 6988;

    public static final int INPUT_NPCS = 72523;
    public static final int INPUT_AMT = 72503;

    public static final int MAX_ITEMS = 80;


    public static void open(Player player) {
       /* player.getPacketSender().sendInterface(INTERFACE_ID);
        player.getPacketSender().sendScrollBarSize(NPCS_SCROLL, 273);


        for (int i = 0; i < 20; i++) {
            player.getPacketSender().sendString(NPCS_START + i, "");
        }
        player.getPacketSender().sendString(STRING_NAME, "None");
        player.getPacketSender().sendString(STRING_SIMULATED, "Simulated drops: @gre@" + 0);
        player.getPacketSender().resetItemsOnInterface(ITEM_CONTAINER, MAX_ITEMS);
        player.getPacketSender().sendString(72503, "1");*/
       player.getAttributeMap().set(DropAttributes.SIMULATED_AMOUNT, 1);
    }

    public static void simulate(Player player) {
        Optional<DropTable> table = DropManager.getDroptable(player.getAttributeMap().getInt(DropAttributes.VIEWING_NPC));
        if(!table.isPresent())
            return;
        int amount = player.getAttributeMap().getInt(DropAttributes.SIMULATED_AMOUNT);
        //player.getPacketSender().sendString(STRING_NAME, NpcDefinition.forId(table.getNpc()).getName());
        //player.getPacketSender().sendString(STRING_SIMULATED, "Simulated drops: @gre@"+amount);
        Object2ObjectMap<Integer, Integer> data = table.get().simulateRoll(player, amount);
        List<Item> items = data.entrySet().stream().map(e -> new Item(e.getKey(), e.getValue())).collect(Collectors.toList());

        //player.getPacketSender().sendItemsOnInterface(ITEM_CONTAINER, MAX_ITEMS, items, true);
    }
    public static void searchNpcName(Player player, String search) {
        List<String> npc = new ArrayList<>();
        List<Integer> searched = new ArrayList<>();
        for (DropTable drop : DropManager.TABLES) {

            String name;

            int id = drop.getNpcId();
            if(id == -1)
                continue;
            MobDefinition def = MobDefinition.DEFINITIONS[id];
            if (def== null)
                continue;
            name = def.getName();
            if(name == null || name.equals(""))
                continue;

                if (name.toLowerCase().contains(search)) {
                    if (!npc.contains(name)) {
                        npc.add(name);
                        searched.add(id);
                    }
            }
        }
        if (searched.isEmpty()) {
          //  player.getPacketSender().sendScrollBarSize(NPCS_SCROLL, 273);
            //player.sendMessage("@red@No Results found.");
            for (int i = 0; i < 20; i++) {
              //  player.getPacketSender().sendString(NPCS_START + i, "");
            }
            return;
        }

        for(int i = 0; i < 20; i++) {
            String npcName = "";
            if(npc.size() > i)
                npcName = npc.get(i);
            //player.getPacketSender().sendString(NPCS_START + i, npcName);
        }
        player.getAttributeMap().set(DropAttributes.SEARCHED_NPCS, searched);
        //player.getPacketSender().sendScrollBarSize(NPCS_SCROLL, Math.max((npc.size() * 15) + 15, 273));
    }

    public static void selectNpc(Player player, int idx) {
        if(player.getAttributeMap().has(DropAttributes.SEARCHED_NPCS))
            return;
        List<Integer> searched = player.getAttributeMap().getObject(DropAttributes.SEARCHED_NPCS);
        player.getAttributeMap().set(DropAttributes.SELECTED_NPC, searched.get(idx));
        //player.getPacketSender().sendString(72503, "1");
        player.getAttributeMap().set(DropAttributes.SIMULATED_AMOUNT, 1);
        simulate(player);
    }

}
