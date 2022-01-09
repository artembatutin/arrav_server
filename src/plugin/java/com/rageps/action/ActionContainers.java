package com.rageps.action;

import com.rageps.action.impl.*;

public class ActionContainers {

    public static final ActionContainer<ItemAction> ITEM_ACTION = new ActionContainer<>();

    /**
     * Buttons {@link ActionContainer} instance.
     */
    public static final ActionContainer<ButtonAction> BUTTONS = new ActionContainer<>();

    /**
     * Events called on item equip action.
     */
    public static final ActionContainer<ItemAction> EQUIP = new ActionContainer<>();

    public static final ActionContainer<ItemOnItemAction> ACTIONS = new ActionContainer<>();

    public static final ActionContainer<ItemOnObjectAction> ITEM_ON_OBJECT = new ActionContainer<>();
    public static final ActionContainer<ItemOnObjectAction> ITEMS = new ActionContainer<>();

    /*
     * All of the npc events.
     */
    public static final ActionContainer<MobAction> FIRST = new ActionContainer<>();
    public static final ActionContainer<MobAction> SECOND = new ActionContainer<>();
    public static final ActionContainer<MobAction> THIRD = new ActionContainer<>();
    public static final ActionContainer<MobAction> FOURTH = new ActionContainer<>();

    /*
     * All of the object events.
     */
    public static final ActionContainer<ObjectAction> OBJECT_FIRST = new ActionContainer<>();
    public static final ActionContainer<ObjectAction> OBJECT_SECOND = new ActionContainer<>();
    public static final ActionContainer<ObjectAction> OBJECT_THIRD = new ActionContainer<>();
    public static final ActionContainer<ObjectAction> OBJECT_FOURTH = new ActionContainer<>();
    public static final ActionContainer<ObjectAction> OBJECT_FIFTH = new ActionContainer<>();
    public static final ActionContainer<ObjectAction> CONSTRUCTION = new ActionContainer<>();


}
