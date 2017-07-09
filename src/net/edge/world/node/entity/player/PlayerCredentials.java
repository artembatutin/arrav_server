package net.edge.world.node.entity.player;

import com.google.gson.JsonObject;
import net.edge.util.TextUtils;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 8-7-2017.
 */
public final class PlayerCredentials {
    private String username;

    private String password;

    public PlayerCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public long getUsernameHash() {
        return TextUtils.nameToHash(username);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
   
}
