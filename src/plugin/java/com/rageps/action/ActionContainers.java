package com.rageps.action;

import com.rageps.action.impl.ButtonAction;
import com.rageps.action.impl.ItemAction;

public class ActionContainers {

    public static final ActionContainer<ItemAction> ITEM_ACTION = new ActionContainer<>();

    /**
     * Buttons {@link ActionContainer} instance.
     */
    public static final ActionContainer<ButtonAction> BUTTONS = new ActionContainer<>();


}
