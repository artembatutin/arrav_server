package com.rageps.world.text;

/**
 * An effecient implementation of {@link StringBuilder} to create nice messages
 * to be sent to players.
 * @author Tamatea Schofield <tamateea@gmail.com>
 */
public class MessageBuilder {

    /**
     * The {@link StringBuilder} used to construct messages.
     */
    private StringBuilder sb;

    /**
     * Creates a {@link MessageBuilder} and it's {@link StringBuilder} to create player messages.
     */
    public MessageBuilder() {
        sb = new StringBuilder();
    }

    /**
     * Appends a color to the string builder.
     * @param color The hex color code.
     * @return This {@link MessageBuilder}.
     */
    public MessageBuilder appendColorTag(String color) {
        append("<col=").append(color).append(">");
        return this;
    }

    /**
     * Appends an image icon to the String builder.
     * @param image The id of the image.
     * @return This {@link MessageBuilder}.
     */
    public MessageBuilder appendImage(int image) {
        append("<img=").append(image).append(">");
        return this;
    }

    /**
     * Appends a shade to the text.
     * @param color The color of the shading.
     * @return This {@link MessageBuilder}.
     */
    public MessageBuilder appendShade(int color) {
        append("<shad=").append(color).append(">");
        return this;
    }
    /**
     * Appends a black shade to the text.
     * @return This {@link MessageBuilder}.
     */
    public MessageBuilder appendShade() {
        append("<shad=").append(0).append(">");
        return this;
    }


    /**
     * Terminates the current color code.
     * @return This {@link MessageBuilder}.
     */
    public MessageBuilder terminateColor() {
        append("</col>");
        return this;
    }

    /**
     * Terminates the current shade code.
     * @return This {@link MessageBuilder}.
     */
    public MessageBuilder terminateShade() {
        append("</shad>");
        return this;
    }

    /**
     * Appends a prefix to the message.
     * @param content The message inside the prefix.
     * @return This {@link MessageBuilder}.
     */
    public MessageBuilder appendPrefix(String content) {
        append("[").append(content).append("]");
       return this;
    }

    /**
     * Appends a prefix to the message.
     * @param content The message inside the prefix.
     * @param tagColor The color of the prefix tags.
     * @param contentColor The color of the message inside the prefix.
     * @return This {@link MessageBuilder}.
     */
    public MessageBuilder appendPrefix(String content, String tagColor, String contentColor) {
        appendColorTag(tagColor).append("[").terminateColor().//bracket
                appendColorTag(contentColor).append(content).terminateColor().//message
        appendColorTag(tagColor).append("]").terminateColor();//bracket
       return this;
    }

    /**
     * Appends a {@link String} to the builder.
     * @param str The String being appended.
     * @return This {@link MessageBuilder}.
     */
    public MessageBuilder append(String str) {
        sb.append(str);
        return this;
    }
    /**
     * Appends a colored {@link String} to the builder.
     * @param str The message being appended.
     * @param color The color of the message.
     * @return This {@link MessageBuilder}.
     */
    public MessageBuilder append(String str, String color) {
        appendColorTag(color).append(str).terminateColor();
        return this;
    }


    /**
     * Appends a {@link Integer} to the builder.
     * @param integer The int being appended.
     * @return This {@link MessageBuilder}.
     */
    public MessageBuilder append(int integer) {
        sb.append(integer);
        return this;
    }

    /**
     * The message which has been built.
     * @return The completed message.
     */
    @Override
    public String toString() {
        return sb.toString();
    }
}
