package com.rageps.util;

import java.util.Collection;
import java.util.function.Predicate;

public final class Predicates {

    public static final Predicate<String> EMPTY = String::isEmpty;
    public static final Predicate<String> NOT_EMPTY = not(EMPTY);

    public static <T> Predicate<T> doesNotContain(Collection<T> collection) {
        return not(collection::contains);
    }

    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return predicate.negate();
    }
}
