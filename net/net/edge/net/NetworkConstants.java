package net.edge.net;

import com.google.common.collect.ImmutableList;
import io.netty.util.AttributeKey;
import io.netty.util.ResourceLeakDetector.Level;
import net.edge.net.packet.PacketReader;
import net.edge.net.session.Session;

import java.math.BigInteger;

/**
 * The class that contains a collection of constants related to the net.
 * This class serves no other purpose than to hold constants.
 * @author lare96 <http://github.org/lare96>
 */
public final class NetworkConstants {
	
	/**
	 * The online port that this server will bind to.
	 */
	public static final int PORT_ONLINE = 43594;
	
	/**
	 * The developer port that this server will bind to.
	 */
	public static final int PORT_DEV = 43593;
	
	/**
	 * The input timeout value that determines how long a session can go without
	 * reading data from the client in {@code SECONDS}.
	 */
	static final int INPUT_TIMEOUT = 5;
	
	/**
	 * The maximum amount of connections that can be active at a time, or in
	 * other words how many clients can be logged in at once per connection.
	 */
	static final int CONNECTION_AMOUNT = 2;
	
	/**
	 * The maximum amount of incoming messages per cycle.
	 */
	public static final int MESSAGE_LIMIT = 15;
	
	/**
	 * The resource leak detection level when not running the server in debug
	 * mode.
	 */
	static final Level RESOURCE_DETECTION = Level.DISABLED;
	
	/**
	 * An array of the message opcodes mapped to their respective listeners.
	 */
	public static final PacketReader[] MESSAGES = new PacketReader[257];
	
	/**
	 * An array of message opcodes mapped to their respective sizes.
	 */
	public static final int MESSAGE_SIZES[] = new int[257];
	
	/**
	 * The {@link AttributeKey} value that is used to retrieve the session
	 * instance from the attribute tool.mapviewer of a {@link io.netty.channel.Channel}.
	 */
	public static final AttributeKey<Session> SESSION_KEY = AttributeKey.valueOf("session.KEY");
	
	/**
	 * The list of exceptions that are ignored and discarded by the
	 * {@link EdgevilleUpstreamHandler}.
	 */
	static final ImmutableList<String> IGNORED_EXCEPTIONS = ImmutableList.of("An existing connection was forcibly closed by the remote host", "An established connection was aborted by the software in your host machine");
	
	/**
	 * The private RSA modulus value.
	 */
	public static final BigInteger RSA_MODULUS = new BigInteger("94306533927366675756465748344550949689550982334568289470527341681445613288505954291473168510012417401156971344988779343797488043615702971738296505168869556915772193568338164756326915583511871429998053169912492097791139829802309908513249248934714848531624001166946082342750924060600795950241816621880914628143");
	
	/**
	 * The private RSA exponent value.
	 */
	public static final BigInteger RSA_EXPONENT = new BigInteger("58942123322685908809689084302625256728774551587748168286651364002223076520293763732441711633712538400732268844501356343764421742749024359146319836858905124072353297696448255112361453630421295623429362610999525258756790291981270575779800669035081348981858658116089267888135561190976376091835832053427710797233");
	
	/**
	 * The default constructor.
	 * @throws UnsupportedOperationException if this class is instantiated.
	 */
	private NetworkConstants() {
		throw new UnsupportedOperationException("This class cannot be instantiated!");
	}
}
