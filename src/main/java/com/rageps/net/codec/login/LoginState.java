package com.rageps.net.codec.login;

/**
 * An enumerated type whose elements represent the various stages of the login protocol decoding.
 * @author Artem Batutin
 */
public enum LoginState {
	
	/**
	 * Initial handshake state.
	 */
	HANDSHAKE,
	
	/**
	 * Login header with it's size.
	 */
	LOGIN_HEADER,
	
	/**
	 * Finalization process of the login block.
	 */
	LOGIN_BLOCK
}
