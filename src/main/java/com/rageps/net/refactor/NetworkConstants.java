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
	public static final BigInteger RSA_MODULUS = new BigInteger("94306533927366675756465748344550949689550982334568289470527341681445613288505954291473168510012417401156971344988779343797488043615702971738296505168869556915772193568338164756326915583511871429998053169912492097791139829802309908513249248934714848531624001166946082342750924060600795950241816621880914628143");

	/**
	 * The exponent used when decrypting the RSA block.
	 */
	public static final BigInteger RSA_EXPONENT = new BigInteger("58942123322685908809689084302625256728774551587748168286651364002223076520293763732441711633712538400732268844501356343764421742749024359146319836858905124072353297696448255112361453630421295623429362610999525258756790291981270575779800669035081348981858658116089267888135561190976376091835832053427710797233");

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