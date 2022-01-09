package com.rageps.content.drops;

import com.rageps.world.attr.Attr;
import com.rageps.world.attr.AttributeKey;
import com.rageps.world.attr.Attributes;

/**
 * @author Tamatea Schofield <tamateea@gmail.com>
 */
public class DropAttributes {

    @Attr public static final AttributeKey VIEWING_NPC = Attributes.defineEmpty("viewing_npc");
    @Attr public static final AttributeKey SIMULATED_AMOUNT = Attributes.define("simulated_amount", 1);
    @Attr public static final AttributeKey SEARCHED_NPCS = Attributes.defineEmpty("searched_npcs");
    @Attr public static final AttributeKey SELECTED_NPC = Attributes.defineEmpty("selected_npc");

}
