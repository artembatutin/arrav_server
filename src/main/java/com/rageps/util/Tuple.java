package com.rageps.util;

/**
 * 
 * @author Pb600
 *
 * @param <V1> First Value Type
 * @param <V2> Second Value Type
 */
public class Tuple<V1, V2> {

	private final V1 value1;
	private final V2 value2;
	
	public Tuple(V1 value1, V2 value2) {
		this.value1 = value1;
		this.value2 = value2;
	}
	
	public V1 getValue1() {
		return value1;
	}
	
	public V2 getValue2() {
		return value2;
	}
	
	public static <V1, V2> Tuple<V1, V2> of(V1 value1, V2 value2) {
		return new Tuple<>(value1, value2);
	}

	@Override
	public String toString() {
		return "Tuple [value1=" + value1 + ", value2=" + value2 + "]";
	}
	
	
}
