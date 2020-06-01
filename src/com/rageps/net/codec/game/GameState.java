package com.rageps.net.codec.game;

/**
 * An enumerated type whose elements represent all of the possible states of game protocol encoding.
 * @author Artem Batutin
 */
public enum GameState {
	
	/**
	 * Decoding opcode and packet type.
	 */
	OPCODE,
	
	/**
	 * Decoding packet variable size.
	 */
	SIZE,
	
	/**
	 * Processing packet payload.
	 */
	PAYLOAD
}
