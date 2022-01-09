package com.rageps.net.refactor;

import java.math.BigInteger;

/**
 * Holds various network-related constants such as port numbers.
 *
 * @author Graham
 * @author Major
 */
public final class NetworkConstants {

	/**
	 * The HTTP port.
	 */
	public static final int HTTP_PORT = 80;

	/**
	 * The number of seconds before a connection becomes idle.
	 */
	public static final int IDLE_TIME = 15;

	/**
	 * The modulus used when decrypting the RSA block.
	 */
	public static final BigInteger RSA_MODULUS = new BigInteger("116594337766357727767531649630523053499900404611184165273631199626521717296430080122311817245989116683706635837790426872327658286160904966101726944165040914108060391743201907910055865970560142406108033643270714107271152004612451768206307171314997817695901718733338762518764633839451201192092904527075512820107");

	/**
	 * The exponent used when decrypting the RSA block.
	 */
	public static final BigInteger RSA_EXPONENT = new BigInteger("79018784903650530792112634847327646127402480601955473775021794751233480254485839124656326575794629089270395919424135983662744258024059004445951202405274215760761776069035257231008392456369670096962988959418642860564507172115744403693316401377998964396595885812387916930594911493540337952557698535087945265153");

	/**
	 * The service port.
	 */
	public static final int SERVICE_PORT = 43594;


	/**
	 * Sole private constructor to prevent instantiation.
	 */
	private NetworkConstants() {

	}

}