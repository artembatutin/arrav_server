package com.rageps.world.entity.actor.player;

import com.rageps.content.minigame.rfd.RFDData;
import com.rageps.content.skill.cooking.CookingData;
import com.rageps.content.skill.cooking.DoughCreation;
import com.rageps.content.skill.crafting.Spinning;
import com.rageps.content.skill.fletching.BowCarving;
import com.rageps.world.attr.Attr;
import com.rageps.world.attr.AttributeKey;
import com.rageps.world.attr.Attributes;
import com.rageps.world.entity.actor.combat.attack.FightType;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.object.DynamicObject;
import com.rageps.world.entity.object.ObjectDirection;
import com.rageps.world.entity.object.ObjectType;
import com.rageps.world.locale.Position;

/**
 * A collection of {@link AttributeKey}'s associated with a {@link Player}.
 * @author Tamatea Schofield <tamateea@gmail.com>
 */
public class PlayerAttributes {


    //Banking
	@Attr  public static final AttributeKey ENTER_X_ITEM_TAB = Attributes.define("enter_x_item_tab", -1);
	@Attr  public static final AttributeKey ENTER_X_ITEM_SLOT = Attributes.define("enter_x_item_slot", -1);
	@Attr  public static final AttributeKey INSERT_ITEM = Attributes.define("insert_item", false);
	@Attr  public static final AttributeKey WITHDRAW_AS_NOTE = Attributes.define("withdraw_as_not", false);
	@Attr  public static final AttributeKey SHIFTING_REQ = Attributes.define("shifting_req", false);

    //Current action
    @Attr  public static final AttributeKey DESTROY_ITEM_SLOT = Attributes.define("destroy_item_slot", -1);
    @Attr  public static final AttributeKey BOB = Attributes.define("bob", false);
    @Attr  public static final AttributeKey BANKING = Attributes.define("banking", false);
    @Attr  public static final AttributeKey FISHING = Attributes.define("fishing", false);
    @Attr  public static final AttributeKey FLETCHING_BOWS = Attributes.define("fletching_bows", false);
    @Attr  public static final AttributeKey CRAFTING_POTS = Attributes.define("crafting_bows", false);
    @Attr  public static final AttributeKey CRAFTING_HIDES = Attributes.define("crafting_hides", false);
    @Attr  public static final AttributeKey CRAFTING_GLASS = Attributes.define("crafting_glass", false);
    @Attr  public static final AttributeKey CRAFTING_SPIN = Attributes.define("crafting_spin", false);


    @Attr  public static final AttributeKey FREE_SPEC_CHANGE = Attributes.definePersistent("free_spec_change", false);
    @Attr  public static final AttributeKey ACCEPT_AID = Attributes.definePersistent("accept_aid", true);
    @Attr  public static final AttributeKey INTRODUCTION_STAGE = Attributes.definePersistent("introduction_stage", 0);

    //minigame
    @Attr  public static final AttributeKey PARTICIPATION = Attributes.define("participation", 0);
    @Attr  public static final AttributeKey FIGHT_CAVES_ADVANCED = Attributes.definePersistent("fight_caves_advanced", false);
    @Attr  public static final AttributeKey RFD_WAVE  = Attributes.definePersistentObject("rfd_wave", RFDData.WAVE_ONE);

    //combat
    @Attr  public static final AttributeKey MASTER_ARCHERY = Attributes.define("master_archery", false);

    //shopping
    @Attr  public static final AttributeKey SHOP_ITEM = Attributes.define("shop_item", -1);
    @Attr  public static final AttributeKey BUYING_SHOP_ITEM = Attributes.define("buying_shop_item", -1);

    //prayer
    @Attr  public static final AttributeKey QUICK_PRAY_ON = Attributes.define("quick_pray_on", false);

    //lunar
    @Attr  public static final AttributeKey LUNAR_DREAM = Attributes.define("lunar_dream", false);
    @Attr  public static final AttributeKey LUNAR_SPELLBOOK_SWAP = Attributes.define("lunar_spellbook_swap", false);


    @Attr  public static final AttributeKey SESSION_DURATION = Attributes.define("session_duration", 0L);

    //skills

    @Attr  public static final AttributeKey GOAL_SETTING_SKILL = Attributes.define("goal_setting_skill", -1);
    @Attr  public static final AttributeKey SLAYER_TASKS = Attributes.definePersistent("slayer_tasks", 0);
    @Attr  public static final AttributeKey COOKING_DATA = Attributes.define("cooking_data", CookingData.SHRIMP);
    @Attr  public static final AttributeKey COOKING_OBJECT = Attributes.define("cooking_object", new DynamicObject(-1, new Position(0, 0), ObjectDirection.SOUTH, ObjectType.GENERAL_PROP, false, 0, 0));
    @Attr  public static final AttributeKey COOKING_USINGSTOVE = Attributes.define("cooking_usingStove", false);
    @Attr  public static final AttributeKey CREATING_DOUGH = Attributes.define("creating_dough", false);
    @Attr  public static final AttributeKey CREATING_DOUGH_DATA = Attributes.define("creating_dough_data", DoughCreation.DoughData.BREAD_DOUGH);
    @Attr  public static final AttributeKey CRAFTING_POTFIRED = Attributes.define("crafting_potfired", false);
    @Attr  public static final AttributeKey CRAFTING_SPINNING = Attributes.define("crafting_spinning", Spinning.SpinningData.WOOL);
    @Attr  public static final AttributeKey CRAFTING_HIDE = Attributes.define("crafting_hide", -1);
    @Attr  public static final AttributeKey FLETCHING_BOWCARVING = Attributes.define("crafting_hide", new BowCarving(null, null, false));
    @Attr  public static final AttributeKey SMITHING_EQUIPMENT = Attributes.define("smithing_equipment", new Item(-1, -1));
    @Attr  public static final AttributeKey SMITHING_POSITION = Attributes.define("smithing_position", new Position(0, 0));


}
