package com.rageps.command.impl;

import com.rageps.RagePS;
import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.net.refactor.packet.out.model.BroadcastPacket;
import com.rageps.net.refactor.packet.out.model.LogoutPacket;
import com.rageps.task.Task;
import com.rageps.world.World;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

import java.util.Iterator;

@CommandSignature(alias = {"hit"}, rights = {Rights.ADMINISTRATOR}, syntax = "Updates the server, ::update seconds")
public final class HitCommand implements Command {
	
	/**
	 * The updating count in progress flag.
	 */
	public static int inProgess = 0;
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.writeDamage(new Hit(45));
	}
	
}
