package net.arrav.content.commands.impl;

import net.arrav.Arrav;
import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandDispatcher;
import net.arrav.content.commands.CommandSignature;
import net.arrav.content.market.MarketCounter;
import net.arrav.net.host.HostListType;
import net.arrav.net.host.HostManager;
import net.arrav.task.Task;
import net.arrav.util.json.impl.*;
import net.arrav.world.World;
import net.arrav.world.entity.actor.combat.attack.listener.CombatListenerDispatcher;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;
import net.arrav.world.locale.area.AreaManager;

@CommandSignature(alias = {"reload"}, rights = {Rights.ADMINISTRATOR}, syntax = "Use this command as just ::reload")
public final class ReloadCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		switch(cmd[1].toLowerCase()) {
			case "actions":
				Arrav.loadEvents();
				player.message("Reloaded action events.");
				break;
			case "drops":
				new MobDropTableLoader().load();
				player.message("Reloaded mob drops.");
				break;
			case "market":
				new MarketValueLoader().load();
				player.message("Reloaded market prices.");
				break;
			case "shops":
				new ShopLoader().load();
				player.message("Reloaded shops.");
				break;
			case "commands":
				CommandDispatcher.reload();
				player.message("Reloaded commands.");
				break;
			case "combat":
				new CombatProjectileLoader().load();
				new CombatRangedBowLoader().load();
				CombatListenerDispatcher.load();
				player.message("Reloaded combat.");
				break;
			case "ips":
				HostManager.deserialize(HostListType.BANNED_MAC);
				HostManager.deserialize(HostListType.BANNED_IP);
				HostManager.deserialize(HostListType.MUTED_IP);
				HostManager.deserialize(HostListType.STARTER_RECEIVED);
				player.message("Reloaded IP hosts.");
				break;
			default:
				World.get().getTask().submit(new Task(3, false) {
					boolean removed;
					
					@Override
					protected void execute() {
						if(!removed) {
							//Removing npcs.
							World.get().getMobs().clear();
							for(Player p : World.get().getPlayers()) {
								if(p == null)
									continue;
								p.getLocalMobs().clear();
								p.getMobs().clear();
							}
							//clearing areas
							AreaManager.get().getAreas().clear();
							//Deleting shops
							MarketCounter.getShops().clear();
							player.message("Successfully removed the world nodes [Objects, Mobs, Shops, Areas].");
							removed = true;
						} else {
							new ItemDefinitionLoader().load();
							new MobDefinitionLoader().load();
							new MobNodeLoader().load();
							new ShopLoader().load();
							new AreaLoader().load();
							this.cancel();
							player.message("Successfully reloaded the world [Objects, Mobs, Shops, Defs, Areas].");
						}
					}
				});
				break;
		}
	}
}
