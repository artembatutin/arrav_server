package com.rageps.world.entity.actor.player.persist;


import com.rageps.net.codec.login.LoginCode;
import com.rageps.world.entity.actor.player.Player;

public interface PlayerPersistable {

	void save(Player player);

	LoginCode load(Player player);

}
