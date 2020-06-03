package com.rageps.world.attr;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * A helper class for boxed and un-boxed primitive types.
 */
public final class Primitives {

	// TODO: Add un-boxed types?
	private static final Set<Class<?>> TWOS_COMPLEMENT_PRIMIVITES = ImmutableSet.of(Byte.class, Short.class, Integer.class, Long.class);

	private static final Set<Class<?>> FLOATING_POINT_PRIMITIVES = ImmutableSet.of(Float.class, Double.class);

	/**
	 * Tests whether or not the specified Class is a whole numeric primitive type.
	 *
	 * @param clazz The Class to test.
	 * @return {@code true} iff the specified class is a whole numeric primitive type.
	 */
	public static boolean isTwosComplementPrimitive(Class<?> clazz) {
		return TWOS_COMPLEMENT_PRIMIVITES.contains(clazz);
	}

	/**
	 * Tests whether or not the specified Class is a floating point primitive type.
	 *
	 * @param clazz The Class to test.
	 * @return {@code true} iff the specified class is a floating point primitive type.
	 */
	public static boolean isFloatingPointPrimitive(Class<?> clazz) {
		return FLOATING_POINT_PRIMITIVES.contains(clazz);
	}

}
