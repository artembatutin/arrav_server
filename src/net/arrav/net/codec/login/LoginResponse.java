package net.arrav.net.codec.login;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;

/**
 * The login response given as {@link ByteBuf} to the client.
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
	 * @param rights The {@link Player}s authority level.
	 * @param iron The iron man flag condition.
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
	 * Converts this {@link LoginResponse} to a {@link ByteBuf}.
	 * @return response {@link ByteBuf}.
	 */
	public ByteBuf toBuf(ChannelHandlerContext ctx) {
		ByteBuf out = ctx.channel().alloc().buffer(2);
		out.writeByte(response.getCode());
		if(response == LoginCode.NORMAL) {
			out.writeByte(rights == Rights.PLAYER && iron ? Rights.IRON_MAN.getProtocolValue() : rights.getProtocolValue());
		}
		return out;
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