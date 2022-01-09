package com.rageps.content.rewardslist;

import com.rageps.content.itemBoxes.ItemBoxHandler;
import com.rageps.content.rewardslist.impl.ItemBoxRewardable;
import com.rageps.world.entity.actor.player.Player;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import static com.rageps.content.itemBoxes.BoxConstants.*;

public class RewardListHandler {


    private static final ObjectArrayList<Rewardable> REWARDABLES = new ObjectArrayList<>();


    public static void init() {
        REWARDABLES.add(new ItemBoxRewardable(ItemBoxHandler.getBox(ABBADON_BOX_ID)));
        REWARDABLES.add(new ItemBoxRewardable(ItemBoxHandler.getBox(SPIDER_BOX_ID)));
        REWARDABLES.add(new ItemBoxRewardable(ItemBoxHandler.getBox(BEGINER_D_SLAYER_BOX_ID)));
        REWARDABLES.add(new ItemBoxRewardable(ItemBoxHandler.getBox(OBLIVION_BOX_ID)));
        REWARDABLES.add(new ItemBoxRewardable(ItemBoxHandler.getBox(UBER_BOX_ID)));
    }


    public static void open(Player player) {
       // player.getPacketSender().sendInterface(REWARDS_INTERFACE);
        select(player, 0);

        int i = 0;
        for(Rewardable rewardable : REWARDABLES) {
         //   player.getPacketSender().sendFrame126(rewardable.name(), i + TEXT_START);
       //     player.getPacketSender().sendItemOnInterface(i + ITEMS_START, rewardable.placeholderItem(), 1);
            i++;
        }
     //   player.getPacketSender().sendScrollBarSize(TABLE_CONTAINER, REWARDABLES.size() * 50);
    }

    public static void select(Player player, int idx) {
        if(idx > REWARDABLES.size() || idx < 0)
            return;
        int amt = 42 * (REWARDABLES.get(idx).getitems().size() / 7) + 42;
        //player.getPacketSender().sendFrame126(TITLE_ID, "Reward list from: @gre@"+REWARDABLES.get(idx).name());
        //player.getPacketSender().sendItemsOnInterface(REWARDS_CONTAINER, 100, REWARDABLES.get(idx).getitems(), true);
        //player.getPacketSender().sendScrollBarSize(REWARD_CONTAINER, amt);

    }


}
