package net.edge.net;

import com.google.common.collect.Sets;
import net.edge.world.World;

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
		return !PunishmentHandler.isLocal(host) && IP_BANNED.contains(host);
	}
	
	/**
	 * Determines if the {@code host} is IP-muted.
	 * @param host the host to determine this for.
	 * @return {@code true} if the host is, {@code false} otherwise.
	 */
	public static boolean isIPMuted(String host) {
		return !PunishmentHandler.isLocal(host) && IP_MUTED.contains(host);
	}
	
	/**
	 * Determines if the {@code host} is IP-muted.
	 * @param host the host to determine this for.
	 * @return {@code true} if the host is, {@code false} otherwise.
	 */
	public static boolean recievedStarter(String host) {
		return !PunishmentHandler.isLocal(host) && STARTERS.contains(host);
	}
	
	/**
	 * Adds a banned host to the internal set and {@code banned_ips.txt} file.
	 * @param host     the new host to add to the database of banned IP addresses.
	 * @param username the username of the ip banned person.
	 * @throws IllegalStateException if the host is already banned.
	 */
	public static void addIPBan(String host, String username) {
		if(PunishmentHandler.isLocal(host)) {
			return;
		}
		World.getService().submit(() -> {
			if(IP_BANNED.contains(host))
				return;
			try(FileWriter out = new FileWriter(Paths.get("./data/", "banned_ips.txt").toFile(), true)) {
				out.write(host + "|" + username);
				out.write(System.getProperty("line.separator"));
				IP_BANNED.add(host);
			} catch(Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	/**
	 * Adds a muted host to the internal set and {@code muted_ips.txt} file.
	 * @param host     the new host to add to the database of muted IP addresses.
	 * @param username the username of the ip banned person.
	 * @throws IllegalStateException if the host is already muted.
	 */
	public static void addIPMute(String host, String username) {
		if(PunishmentHandler.isLocal(host))
			return;
		World.getService().submit(() -> {
			if(IP_MUTED.contains(host))
				return;
			try(FileWriter out = new FileWriter(Paths.get("./data/", "muted_ips.txt").toFile(), true)) {
				out.write(host + "|" + username);
				out.write(System.getProperty("line.separator"));
				IP_MUTED.add(host);
			} catch(Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	/**
	 * Adds a starter host to the internal set and {@code starters.txt} file.
	 * @param host the new host to add to the database of starter addresses.
	 * @throws IllegalStateException if the host is already muted.
	 */
	public static void addStarter(String host) {
		if(PunishmentHandler.isLocal(host))
			return;
		World.getService().submit(() -> {
			if(STARTERS.contains(host))
				return;
			try(FileWriter out = new FileWriter(Paths.get("./data/", "starters.txt").toFile(), true)) {
				out.write(host);
				out.write(System.getProperty("line.separator"));
				STARTERS.add(host);
			} catch(Exception e) {
				e.printStackTrace();
			}
		});
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
	
	/**
	 * Determines if the specified host is connecting locally.
	 * @param host the host to check if connecting locally.
	 * @return {@code true} if the host is connecting locally, {@code false}
	 * otherwise.
	 */
	public static boolean isLocal(String host) {
		return host.equals("127.0.0.1") || host.equals("localhost");
	}
	
}
