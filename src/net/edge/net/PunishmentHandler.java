package net.edge.net;

import com.google.common.collect.Sets;
import net.edge.world.node.actor.player.Player;

import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Set;

/**
 * The net security that handles and validates all incoming connections
 * received by the server to ensure that the server does not fall victim to
 * attacks by a socket flooder or a connection from a banned host.
 * @author lare96 <http://github.com/lare96>
 */
public final class PunishmentHandler {
	
	/**
	 * The synchronized set of banned hosts.
	 */
	private static final Set<String> IP_BANNED = Sets.newConcurrentHashSet();
	
	/**
	 * The synchronized set of muted hosts.
	 */
	private static final Set<String> IP_MUTED = Sets.newConcurrentHashSet();
	
	/**
	 * The synchronized set of received starter pack hosts.
	 */
	private static final Set<String> STARTERS = Sets.newConcurrentHashSet();
	
	/**
	 * The default constructor.
	 * @throws UnsupportedOperationException if this class is instantiated.
	 */
	private PunishmentHandler() {
		throw new UnsupportedOperationException("This class cannot be instantiated!");
	}
	
	/**
	 * Determines if the {@code host} is IP-banned.
	 * @param host the host to determine this for.
	 * @return {@code true} if the host is, {@code false} otherwise.
	 */
	static boolean isIPBanned(String host) {
		return IP_BANNED.contains(host);
	}
	
	/**
	 * Determines if the {@code host} is IP-muted.
	 * @param host the host to determine this for.
	 * @return {@code true} if the host is, {@code false} otherwise.
	 */
	public static boolean isIPMuted(String host) {
		return IP_MUTED.contains(host);
	}
	
	/**
	 * Determines if the {@code host} is IP-muted.
	 * @param host the host to determine this for.
	 * @return {@code true} if the host is, {@code false} otherwise.
	 */
	public static boolean recievedStarter(String host) {
		return STARTERS.contains(host);
	}

	/**
	 * Determines if two players are connected from the same network.
	 * @param player	the player to determine for.
	 * @param other		the other player to determine for.
	 * @return {@code true} if the two players are connected from same network, {@code false} otherwise.
	 */
	public static boolean same(Player player, Player other) {
		return player.getSession().getHost().equals(other.getSession().getHost());
	}
	
	/**
	 * Adds a banned host to the internal set and {@code banned_ips.txt} file.
	 * @param host     the new host to add to the database of banned IP addresses.
	 * @param username the username of the ip banned person.
	 * @throws IllegalStateException if the host is already banned.
	 */
	public static void addIPBan(String host, String username) {
		if(IP_BANNED.contains(host))
			return;
		try(FileWriter out = new FileWriter(Paths.get("./data/", "banned_ips.txt").toFile(), true)) {
			out.write(host + "|" + username);
			out.write(System.getProperty("line.separator"));
			IP_BANNED.add(host);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a muted host to the internal set and {@code muted_ips.txt} file.
	 * @param host     the new host to add to the database of muted IP addresses.
	 * @param username the username of the ip banned person.
	 * @throws IllegalStateException if the host is already muted.
	 */
	public static void addIPMute(String host, String username) {
		if(IP_MUTED.contains(host))
			return;
		try(FileWriter out = new FileWriter(Paths.get("./data/", "muted_ips.txt").toFile(), true)) {
			out.write(host + "|" + username);
			out.write(System.getProperty("line.separator"));
			IP_MUTED.add(host);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a starter host to the internal set and {@code starters.txt} file.
	 * @param host the new host to add to the database of starter addresses.
	 * @throws IllegalStateException if the host is already muted.
	 */
	public static void addStarter(String host) {
		if(STARTERS.contains(host))
			return;
		try(FileWriter out = new FileWriter(Paths.get("./data/", "starters.txt").toFile(), true)) {
			out.write(host);
			out.write(System.getProperty("line.separator"));
			STARTERS.add(host);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads all of the banned hosts from the {@code banned_ips.txt} file.
	 */
	public static void parseIPBans() {
		try(Scanner s = new Scanner(Paths.get("./data/", "banned_ips.txt").toFile())) {
			while(s.hasNextLine()) {
				String[] ban = s.nextLine().split("|");
				IP_BANNED.add(ban[0]);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads all of the muted hosts from the {@code muted_ips.txt} file.
	 */
	public static void parseIPMutes() {
		try(Scanner s = new Scanner(Paths.get("./data/", "muted_ips.txt").toFile())) {
			while(s.hasNextLine()) {
				String[] mute = s.nextLine().split("|");
				IP_MUTED.add(mute[0]);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads all of the started hosts from the {@code starters.txt} file.
	 */
	public static void parseStarters() {
		try(Scanner s = new Scanner(Paths.get("./data/", "starters.txt").toFile())) {
			while(s.hasNextLine())
				STARTERS.add(s.nextLine());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
