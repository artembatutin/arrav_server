package com.rageps.world;

import com.rageps.net.packet.out.SendBroadcast;
import com.rageps.net.packet.out.SendYell;
import com.rageps.net.refactor.packet.out.model.BroadcastPacket;
import com.rageps.net.refactor.packet.out.model.YellPacket;
import com.rageps.util.TextUtils;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.text.ColorConstants;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.*;
import java.util.function.Predicate;

/**
 * Handles actions relating to the {@link World}. And the {@link com.rageps.world.entity.Entity}'s inside of it.
 *
 * @author Tamatea <tamateea@gmail.com>
 */
public class WorldUtil {


    private final World world;

    public WorldUtil(World world) {
        this.world = world;
    }

    /**
     * Sends a broadcast to the entire world.
     * @param time
     * @param message
     * @param countdown
     */
    public void sendBroadcast(int time, String message, boolean countdown) {
        BroadcastPacket broadcastPacket = new BroadcastPacket(countdown ? 0 : 1, time, message);
        world.getPlayers().stream().forEach($it -> {
            $it.send(broadcastPacket);
            $it.message("<img=29>["+ ColorConstants.MAGENTA +"RagePS</col>]" + (message));
        });
    }

    /**
     * Sends {@code message} to all online players with an announcement dependent of {@code announcement}.
     * @param message the message to send to all online players.
     * @param announcement determines if this message is an announcement.
     */
    public void message(String message, boolean announcement) {
        Player p;
        Iterator<Player> it = world.getPlayers().iterator();
        while((p = it.next()) != null) {
            p.message((announcement ? "@red@[ANNOUNCEMENT]: " : "") + message);
        }
    }

    /**
     * Sends {@code message} to all online players.
     * @param message the message to send to all online players.
     */
    public void message(String message) {
        message(message, false);
    }

    /**
     * Sends {@code message} to all online players as a yell.
     * @param author author yelling.
     * @param message the message being yelled.
     * @param rights the rights of the author.
     */
    public void yell(String author, String message, Rights rights) {
        Player p;
        Iterator<Player> it = world.getPlayers().iterator();
        YellPacket yellPacket = new YellPacket(author, message, rights);
        while((p = it.next()) != null) {
            p.send(yellPacket);
        }
    }

    public void messageIf(String message, Predicate<? super Player> filter) {
        world.getPlayers().stream().filter(Objects::nonNull).filter(filter).forEach(p -> p.message(message));
    }
    public void broadcastIf(Predicate<? super Player> filter, int time, String message, boolean countdown) {
        BroadcastPacket broadcastPacket = new BroadcastPacket(countdown ? 0 : 1, time, message);
        world.getPlayers().stream().filter(Objects::nonNull).filter(filter).forEach(p -> p.send(broadcastPacket));
    }

    /**
     * Retrieves and returns the local {@link Mob}s for {@code character}. The
     * specific mobs returned is completely dependent on the character given in
     * the argument.
     * @param character the character that it will be returned for.
     * @return the local mobs.
     */
    public Iterator<Mob> getLocalMobs(Actor character) {
        if(character.isPlayer())
            return character.toPlayer().getLocalMobs().iterator();
        return world.getMobRepository().iterator();
    }

    /**
     * Returns a player within an optional whose name is equal to
     * {@code username}.
     * @param username the name to check the collection of players for.
     * @return the player within an optional if found, or an empty optional if
     * not found.
     */
    public Optional<Player> getPlayer(String username) {
        return getPlayer(TextUtils.nameToHash(username));
    }

    /**
     * Retrieves and returns the local {@link Player}s for {@code character}.
     * The specific players returned is completely dependent on the character
     * given in the argument.
     * @param character the character that it will be returned for.
     * @return the local players.
     */
    public Iterator<Player> getLocalPlayers(Actor character) {
        if(character.isPlayer())
            return character.toPlayer().getLocalPlayers().iterator();
        return world.getPlayers().iterator();
    }

    /**
     * Returns a player within an optional whose name hash is equal to
     * {@code username}.
     * @param username the name hash to check the collection of players for.
     * @return the player within an optional if found, or an empty optional if
     * not found.
     */
    public Optional<Player> getPlayer(long username) {
        return Optional.ofNullable(world.playerByNames.get(username));
    }

    public boolean isPlayerOnline(long username) {
        return world.playerByNames.containsKey(username);
    }

    /**
     * Checks if one player is the same person or is logged in from the same machine/host as
     * the other player.
     * @param p1 The first player.
     * @param p2 The second player.
     * @return Whether or not they are connected physically.
     */
    public boolean samePerson(Player p1, Player p2) {
        return p1.credentials.getHostAddress().equals(p2.credentials.getHostAddress()) ||
                p1.credentials.getMacAddress().equals(p2.credentials.getMacAddress());
    }

    /**
     * Get's a players alt accounts.
     */
    public ObjectArrayList<Player> getAlts(Player player) {
        ObjectArrayList<Player> alts = new ObjectArrayList<>();

        for(Player p : world.getPlayers()) {
            if(samePerson(player, p))
                alts.add(p);
        }
        return alts;
    }


    /**
     * Creates a set of every single actor in the player and npc character
     * lists, with {@link Mob} actors first and {@link Player} actors second.
     * @return a set containing every single character.
     */
    public Set<Actor> getActors() {
        Set<Actor> actors = new HashSet<>();
        world.getMobRepository().forEach(actors::add);
        world.getPlayers().forEach(actors::add);
        return actors;
    }
}
