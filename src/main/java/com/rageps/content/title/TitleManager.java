package com.rageps.content.title;


import com.rageps.world.entity.actor.player.Player;

import java.util.List;

/**
 * The manager for handling interactions with the title interface.
 * @author Tamatea <tamateea@gmail.com>
 */
public class TitleManager {

    private static final int TITLES_STRINGS_START = 48116;

    private static final String KEY_OPEN_TITLED = "SELECTED_TITLE";
    private static final String KEY_SELECTED_CAT = "SELECTED_TITLE_CAT";

    private static final int PROGRESS_BAR = 48105;
    private static final int AVAILIBILITY = 48111;
    private static final int SELECTED_TITLE = 48110;
    private static final int DESCRIPTION = 48114;
    private static final int COST = 48112;

    /**
     * Opens the title interface and displays information of a specified title
     * by it's index.
     * @param player The player opening the title interface.
     * @param idx The index of the titles which is being selected.
     */
    public static void openInterface(Player player, int idx) {
       // player.getPacketSender().sendInterface(48100);


        /*if(player.getAttributeMap().exist(KEY_SELECTED_CAT))
            player.getAttributeMap().get(KEY_SELECTED_CAT).set(TitleData.TitlePolicy.MISC);
        player.getAttributeMap().get(KEY_OPEN_TITLED).set(idx);
*/

//        List<TitleData> titles_selected = TitleData.getTitles((TitleData.TitlePolicy) player.getAttributeMap().get(KEY_SELECTED_CAT).get());

            /*for(int i = 0; i < 50; i++)
                if(i < titles_selected.size())
                player.getPacketSender().sendString(TITLES_STRINGS_START + i, titles_selected.get(i).formattedName());
                 else
                    player.getPacketSender().sendString(TITLES_STRINGS_START + i, "");

            TitleData title = titles_selected.get((Integer) player.getAttr().get(KEY_OPEN_TITLED).get());

            player.getPacketSender().sendString(COST, "Cost:@gre@"+ TextUtils.formatPrice(title.getCost()));
            player.getPacketSender().sendString(SELECTED_TITLE, "Selected Title:@gre@"+title.formattedName());
            player.getPacketSender().sendString(AVAILIBILITY, "Availibility: "+(title.getProgress(player) == 100 ? "@gre@Available" : "@red@Locked"));
            player.getPacketSender().sendProgressAmount(PROGRESS_BAR, title.getProgress(player));
            player.getPacketSender().sendString(DESCRIPTION, title.getRequirement());*/

    }

    public static void select(Player player, int idx) {
        openInterface(player, idx);//TODO DON'T NEED TO OPEN IT AGAIN
    }

    /**
     * Opens a specified category of the titles interface.
     * @param player The player selecting the category
     * @param type The category selected.
     */
    public static void selectCat(Player player, TitleData.TitlePolicy type) {
//        player.getAttributeMap().get(KEY_SELECTED_CAT).set(type);
        openInterface(player, 1);
    }

}
