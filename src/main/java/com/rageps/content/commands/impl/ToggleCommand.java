package com.rageps.content.commands.impl;

import com.rageps.world.World;
import com.rageps.GameConstants;
import com.rageps.content.commands.Command;
import com.rageps.content.commands.CommandSignature;
import com.rageps.content.object.pit.FirepitData;
import com.rageps.content.object.pit.FirepitManager;
import com.rageps.content.object.pit.PitFiring;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

/**
 * Created by Dave/Ophion
 * Date: 12/08/2017
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 */
@CommandSignature(alias = {"disable", "enable"}, rights = {Rights.ADMINISTRATOR}, syntax = "Use this command as just ::disable/enable [trade/duel/drop/doublexp]")
public class ToggleCommand implements Command {
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		switch(cmd[1].toLowerCase()) {
			case "pit":
				if(command.equals("disable")) {
					if(PitFiring.burning != null) {
						PitFiring.burning.setDelay(20);
					}
				}
				break;
			case "doublexp":
				if(command.equals("enable")) {
					if(FirepitManager.get().getFirepit() != null) {
						FirepitManager.get().getFirepit().setElements(999);
						FirepitManager.get().getFirepit().data = FirepitData.PHASE_FIVE;
						FirepitManager.get().getFirepit().setId(38821);
						FirepitManager.get().getFirepit().publish();
						World.get().message("Fire pit has been filled up, you can light it up.");
					}
				} else if(command.equals("disable")) {
					if(FirepitManager.get().getFirepit() != null) {
						FirepitManager.get().getFirepit().getTask().ifPresent(t -> {
							t.setDelay(5);
						});
					}
				}
				break;
			case "trade":
				if(command.equals("enable")) {
					GameConstants.TRADE_DISABLED = false;
					player.message(cmd[1] + " has been enabled");
				} else if(command.equals("disable")) {
					GameConstants.TRADE_DISABLED = true;
					player.message(cmd[1] + " has been disabled");
				}
				break;
			case "duel":
				if(command.equals("enable")) {
					GameConstants.DUEL_DISABLED = false;
					player.message(cmd[1] + " has been enabled");
				} else if(command.equals("disable")) {
					GameConstants.DUEL_DISABLED = true;
					player.message(cmd[1] + " has been disabled");
				}
				break;
			case "drop":
				if(command.equals("enable")) {
					GameConstants.DROP_DISABLED = false;
					player.message(cmd[1] + " has been enabled");
				} else if(command.equals("disable")) {
					GameConstants.DROP_DISABLED = true;
					player.message(cmd[1] + " has been disabled");
				}
				break;
		}
		
	}
}
