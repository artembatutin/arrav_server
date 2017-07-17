package net.edge.world.entity.actor.player.assets;

import net.edge.net.packet.out.SendPrivateFriendUpdate;
import net.edge.net.packet.out.SendPrivateMessage;
import net.edge.util.MutableNumber;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;

import java.util.Iterator;

import static net.edge.world.entity.actor.player.assets.Rights.IRON_MAN;
import static net.edge.world.entity.actor.player.assets.Rights.PLAYER;

/**
 * The class that contains functions for managing private messaging lists.
 * @author lare96 <http://github.com/lare96>
 */
public final class PrivateMessage {
	
	/**
	 * The player this private messaging list belongs to.
	 */
	private final Player player;
	
	/**
	 * The last message identifier concealed within a counter.
	 */
	private final MutableNumber lastMessage = new MutableNumber(1);
	
	/**
	 * Creates a new {@link PrivateMessage}.
	 * @param player the player this private messaging list belongs to.
	 */
	public PrivateMessage(Player player) {
		this.player = player;
	}
	
	/**
	 * Updates the friend list for {@code player} with the online status of all
	 * their friends.
	 */
	public void updateThisList() {
		for(long name : player.getFriends()) {
			if(name == 0)
				continue;
			player.out(new SendPrivateFriendUpdate(name, World.get().getPlayer(name).isPresent()));
		}
	}
	
	/**
	 * Updates {@code player} friends lists with with whether they are online or
	 * offline.
	 * @param online the status to update the other players friends lists with.
	 */
	public void updateOtherList(boolean online) {
		Player p;
		Iterator<Player> it = World.get().getPlayers().iterator();
		while((p = it.next()) != null) {
			if(p.getFriends().contains(player.getCredentials().getUsernameHash()))
				p.out(new SendPrivateFriendUpdate(player.getCredentials().getUsernameHash(), online));
		}
	}
	
	/**
	 * Attempts to add {@code name} to the friends list.
	 * @param name the name of the new friend to add.
	 */
	public void addFriend(long name) {
		if(player.getFriends().size() >= 200) {
			player.message("Your friends list is full!");
			return;
		}
		if(player.getFriends().add(name)) {
			player.out(new SendPrivateFriendUpdate(name, World.get().getPlayer(name).isPresent()));
		} else {
			player.message("They are already on your friends" + " list!");
		}
	}
	
	/**
	 * Attempts to add {@code name} to the friends list.
	 * @param name the name of the new ignore to add.
	 */
	public void addIgnore(long name) {
		if(player.getIgnores().size() >= 100) {
			player.message("Your ignores list is full!");
			return;
		}
		if(!player.getIgnores().add(name))
			player.message("They are already on your ignores" + " list!");
	}
	
	/**
	 * Attempts to remove {@code name} from the friends list.
	 * @param name the name of the existing friend to remove.
	 */
	public void removeFriend(long name) {
		if(!player.getFriends().remove(name)) {
			player.message("They are not on your friends " + "list.");
		}
	}
	
	/**
	 * Attempts to remove {@code name} from the ignores list.
	 * @param name the name of the existing ignore to remove.
	 */
	public void removeIgnore(long name) {
		if(!player.getIgnores().remove(name)) {
			player.message("They are not on your ignores " + "list.");
		}
	}
	
	/**
	 * Attempts to send {@code message} to the player with {@code name}.
	 * @param name    the player with this name to send the message to.
	 * @param message the message to send to the player.
	 * @param size    the size of the message to send.
	 */
	public void sendPrivateMessage(long name, byte[] message, int size) {
		int rights = player.getRights() == PLAYER && player.isIronMan() ? IRON_MAN.getProtocolValue() : player.getRights().getProtocolValue();
		long hash = player.getCredentials().getUsernameHash();
		World.get().getPlayer(name).ifPresent(p -> p.out(new SendPrivateMessage(hash, rights, message, size)));
	}
	
	/**
	 * Gets the last message identifier concealed within a counter.
	 * @return the last message identifier.
	 */
	public MutableNumber getLastMessage() {
		return lastMessage;
	}
}
