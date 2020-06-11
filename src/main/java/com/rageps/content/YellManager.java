package com.rageps.content;

import com.rageps.content.title.TitleData;
import com.rageps.util.StringUtil;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerAttributes;
import com.rageps.world.text.ColorConstants;
import com.rageps.world.text.YellBuilder;


/**
 * Handles the construction, and sending of global yell messages.
 *
 * todo - implement properly with player rights, cooldowns etc etc.
 * @author Tamatea Schofield <tamateea@gmail.com>
 */
public class YellManager {

    public static void yell(Player p, String message) {
        YellBuilder yb = new YellBuilder();

        TitleData title = p.getAttributeMap().getObject(PlayerAttributes.PLAYER_TITLE);
        if(title != TitleData.NONE) {
            int titleShade = p.getAttributeMap().getInt(PlayerAttributes.TITLE_SHADE);
            yb.getMb().appendShade(titleShade);
            int titleColor = p.getAttributeMap().getInt(PlayerAttributes.TITLE_COLOR);
            yb.appendTitle(StringUtil.formatEnumString(title), titleColor);
            yb.getMb().terminateShade();
        }

        int shadeColor = p.getAttributeMap().getInt(PlayerAttributes.YELL_SHADE);
        yb.getMb().appendShade(shadeColor);
        yb.appendname(p.getFormatUsername(), ColorConstants.MAGENTA);//todo - get name color from rights/rank/gm
        int yellColor = p.getAttributeMap().getInt(PlayerAttributes.YELL_COLOR);
        yb.getMb().terminateShade();
        yb.appentMessage(message, yellColor);

        World.get().message(yb.toString());
    }

}
