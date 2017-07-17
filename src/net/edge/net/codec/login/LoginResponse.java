package net.edge.net.codec.login;

import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

/**
 * An immutable message that is written through a channel and forwarded to the {@link LoginEncoder} where it is encoded and
 * sent to the client.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class LoginResponse {
	
	/**
	 * The actual login response.
	 */
	private final LoginCode response;
	
	/**
	 * The {@link Player}s authority level.
	 */
	private final Rights rights;
	
	/**
	 * If the player is iron man.
	 */
	private final boolean iron;
	
	/**
	 * Creates a new {@link LoginResponse}.
	 * @param response The actual login response.
	 * @param rights   The {@link Player}s authority level.
	 * @param iron     The iron man flag condition.
	 */
	public LoginResponse(LoginCode response, Rights rights, boolean iron) {
		this.response = response;
		this.rights = rights;
		this.iron = iron;
	}
	
	/**
	 * Creates a new {@link LoginResponse} with an authority level of {@code PLAYER} and a {@code flagged} value of
	 * {@code false}.
	 * @param response The actual login response.
	 */
	public LoginResponse(LoginCode response) {
		this(response, Rights.PLAYER, false);
	}
	
	/**
	 * @return The actual login response.
	 */
	public LoginCode getResponse() {
		return response;
	}
	
	/**
	 * @return The {@link Player}s authority level.
	 */
	public Rights getRights() {
		return rights;
	}
	
	/**
	 * If the player is iron man.
	 * @return iron man.
	 */
	public boolean isIron() {
		return iron;
	}
	
}