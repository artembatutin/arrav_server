package net.edge.net.codec.login;

/**
 * An enumerated type whose elements represent the various stages of the login protocol decoding.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public enum LoginState {
	
	/**
	 * Initial handshake state.
	 */
	HANDSHAKE,
	
	/**
	 * Finalization process of the login block.
	 */
	LOGIN_BLOCK
}
