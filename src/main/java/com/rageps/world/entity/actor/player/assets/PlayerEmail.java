package com.rageps.world.entity.actor.player.assets;

/**
 * Created by Ryley Kimmel on 9/25/2016.
 */
public class PlayerEmail {
	public static final String DEFAULT_EMAIL_DNS = "@rageps.io";

	private final String address;

	public PlayerEmail(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public String getAddressMasked() {
		return address.replaceAll("(?<=.{3}).(?=[^@]*?.@)", "*");
	}

	public boolean isInvalid() {
		return address.endsWith(DEFAULT_EMAIL_DNS);
	}

}
