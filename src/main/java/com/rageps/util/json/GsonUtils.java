package com.rageps.util.json;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

/**
 * A static-utility class that contains functions for manipulating {@code Object}s related to {@link Gson}.
 * @author lare96 <http://github.org/lare96>
 */
public final class GsonUtils {
	
	/**
	 * A general purpose {@link Gson} instance that has no registered type adapters.
	 */
	public static final Gson GSON = new GsonBuilder().disableInnerClassSerialization().setPrettyPrinting().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

	public static final Gson JSON_ALLOW_NULL = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();

	public static final Gson JSON_PRETTY_ALLOW_NULL = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping()
			.serializeNulls().create();

	public static final Gson JSON_PRETTY_NO_NULLS = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping()
			.create();
	/**
	 * Gets {@code element} as {@code clazz} type.
	 * @param element The element to get as {@code clazz}.
	 * @param clazz The new type of {@code element}.
	 * @param <T> The underlying type.
	 * @return The {@code element} as {@code T}.
	 */
	public static <T> T getAsType(JsonElement element, Class<T> clazz) {
		return GSON.fromJson(element, clazz);
	}
}