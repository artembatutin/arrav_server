package com.rageps.content.activity;

import com.rageps.world.text.ColorConstants;

/**
 * An enumerated type of activities a player can do.
 * @author Tamatea Schofield <tamateea@gmail.com>
 */
public enum ActivityType {

    BOSS(ColorConstants.LIME_GREEN,false),
    TMNT_RAID(ColorConstants.LIME_GREEN,false),
    STAR_WARS_RAID(ColorConstants.LIME_GREEN, false),
    LICH_KING_RAID(ColorConstants.LIME_GREEN, false),
    DBZ_RAID(ColorConstants.LIME_GREEN, false),
    LEVEL_MAX(ColorConstants.LIME_GREEN, false),
    MAXED_ACCOUNT(ColorConstants.LIME_GREEN, true) {
        @Override
        public String getMessage() {
            return "";
        }
    };

    private int messageColor;

    private String keyword;

    private boolean broadcast;

    ActivityType(int messageColor, boolean broadcast) {
        this.messageColor = messageColor;
        this.broadcast = broadcast;
    }

    public String getMessage() {
        return null;
    }

    public int getMessageColor() {
        return messageColor;
    }

    public String getKeyword() {
        return keyword;
    }

    public boolean isBroadcast() {
        return broadcast;
    }
}
