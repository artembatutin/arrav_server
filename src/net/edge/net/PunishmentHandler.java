package net.edge.net;

import com.google.common.collect.Sets;
import net.edge.world.entity.actor.player.Player;

import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Set;

/**
 * The net security that handles and validates all incoming connections
 * received by the server to ensure that the server does not fall victim to
 * attacks by a socket flooder or a connection from a banned host.
 * @author lare96 <http://github.com/lare96>
 * @author Artem Batutin <artembatutin@gmail.com>
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
	 */
	static boolean isIPBanned(String host) {
		for(String banned : IP_BANNED) {
			if(banned.contains(host))
				return true;
		}
		return false;
	}
	
	/**
	 * Determines if the {@code host} is IP-muted.
	 */
	public static boolean isIPMuted(Player player) {
		return IP_MUTED.contains(player.getSession().getHost()+"-"+player.getCredentials().getUsername());
	}
	
	/**
	 * Determines if the {@code host} is IP-muted.
	 */
	public static boolean recievedStarter(Player player) {
		return STARTERS.contains(player.getSession().getHost()+"-"+player.getCredentials().getUsername());
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
	 * Adds a banned ip
	 */
	public static void addIPBan(Player player) {
		IP_BANNED.add(player.getSession().getHost()+"-"+player.getCredentials().getUsername());
	}
	
	/**
	 * Removes an banned ip.
	 */
	public static boolean removeIPBan(String username) {
		String found = null;
		for(String b : IP_BANNED) {
			if(b.contains(username)) {
				found = b;
				break;
			}
		}
		return found != null && IP_BANNED.remove(found);
	}
	
	/**
	 * Saves the banned ip list.
	 */
	public static void saveIpBan() {
		try(FileWriter out = new FileWriter(Paths.get("./data/", "banned_ips.txt").toFile(), true)) {
			for(String b : IP_BANNED) {
				out.write(b);
				out.write(System.getProperty("line.separator"));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a muted ip
	 */
	public static void addIPMute(Player player) {
		IP_MUTED.add(player.getSession().getHost()+"-"+player.getCredentials().getUsername());
	}
	
	/**
	 * Removes an muted ip.
	 */
	public static boolean removeIPMute(String username) {
		String found = null;
		for(String b : IP_MUTED) {
			if(b.contains(username)) {
				found = b;
				break;
			}
		}
		return found != null && IP_MUTED.remove(found);
	}
	
	/**
	 * Saves the muted ip list.
	 */
	public static void saveIPMute() {
		try(FileWriter out = new FileWriter(Paths.get("./data/", "muted_ips.txt").toFile(), true)) {
			for(String b : IP_MUTED) {
				out.write(b);
				out.write(System.getProperty("line.separator"));
			}
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
		IP_BANNED.clear();
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
		IP_MUTED.clear();
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
		STARTERS.clear();
		try(Scanner s = new Scanner(Paths.get("./data/", "starters.txt").toFile())) {
			while(s.hasNextLine())
				STARTERS.add(s.nextLine());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
