package com.rageps.world.text;

/**
 * @author Tamatea Schofield <tamateea@gmail.com>
 */
public class YellBuilder {

    private MessageBuilder mb;

    public YellBuilder() {
        this.mb = new MessageBuilder();
        mb.append("#yell#");
    }

    public YellBuilder appendname(String name, int color) {
        mb.append(name, color);
        return this;
    }

    public YellBuilder appendTitle(String title, int color) {
        mb.appendPrefix(title, ColorConstants.BLACK, color);
        return this;
    }

    public YellBuilder appentMessage(String message, int color) {
        mb.append(message, color);
        return this;
    }

    public MessageBuilder getMb() {
        return mb;
    }

    @Override
    public String toString() {
        return mb.toString();
    }
}
