package com.rageps.content.poll;


import com.rageps.world.attr.Attr;
import com.rageps.world.attr.AttributeKey;
import com.rageps.world.attr.Attributes;

/**
 * Created by Jason MacKeigan on 2017-06-05 at 3:17 PM
 */
public final class PollAttributes {
	@Attr public static final AttributeKey SELECTED_POLL = Attributes.defineEmpty("selected_poll");
	@Attr public static final AttributeKey SELECTED_OPTION = Attributes.defineEmpty("selected_option");
	@Attr public static final AttributeKey CLOSE_POLL_ORDER = Attributes.defineEmpty("close_poll_order");
	@Attr public static final AttributeKey CREATE_POLL_ORDER = Attributes.defineEmpty("create_poll_order");

	@Attr public static final AttributeKey CREATE_POLL_QUESTION = Attributes.define("create_poll_question", "");
	@Attr public static final AttributeKey CREATE_POLL_DESCRIPTION = Attributes.define("create_poll_description", "");
	@Attr public static final AttributeKey CREATE_POLL_OPTION_ONE = Attributes.define("create_poll_option_one", "");
	@Attr public static final AttributeKey CREATE_POLL_OPTION_TWO = Attributes.define("create_poll_option_two", "");
	@Attr public static final AttributeKey CREATE_POLL_OPTION_THREE = Attributes.define("create_poll_option_three", "");
	@Attr public static final AttributeKey CREATE_POLL_OPTION_FOUR = Attributes.define("create_poll_option_four", "");
	@Attr public static final AttributeKey CREATE_POLL_END_DATE = Attributes.define("create_poll_end_date", 1);
}
