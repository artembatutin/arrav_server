package com.rageps.command.impl;

import com.rageps.RagePS;
import com.rageps.net.refactor.packet.out.model.BroadcastPacket;
import com.rageps.net.refactor.packet.out.model.LogoutPacket;
import com.rageps.world.World;
import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.task.Task;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.actor.player.persist.PlayerPersistenceManager;

import java.util.Iterator;

@CommandSignature(alias = {"update"}, rights = {Rights.ADMINISTRATOR}, syntax = "Updates the server, ::update seconds")
public final class UpdateCommand implements Command {
	
	/**
	 * The updating count in progress flag.
	 */
	public static int inProgess = 0;
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		inProgess = 1;
		int timer = (Integer.parseInt(cmd[1]));
		Player other;
		Iterator<Player> it = World.get().getPlayers().iterator();
		while((other = it.next()) != null) {
			other.send(new BroadcastPacket(0, timer, "System update"));
		}
		RagePS.UPDATING = timer * 0.6;
		World.get().getTask().submit(new Task(1, true) {
			@Override
			protected void execute() {
				RagePS.UPDATING -= 0.6;
				System.out.println("Update count: " + RagePS.UPDATING);
				if(RagePS.UPDATING <= 0) {
					inProgess = 2;
					System.out.println("Setting player into updating mode.");
					System.out.println("Logging players out... - Players online: " + World.get().getPlayers().size());
					for(Player p : World.get().getPlayers()) {
						if(p == null)
							continue;
						World.get().getLoginService().submitSaveRequest(p.getSession(), p);
						p.send(new LogoutPacket(p));
					}
					System.out.println("Waiting for shutdown.");
					World.get().getTask().submit(new Task(10, false) {
						@Override
						protected void execute() {
							System.out.println("Terminating server instance - Players online: " + World.get().getPlayers().size());

							World.get().getGameService().shutdown(true);
							//World.get().stopAsync().awaitRunning();
							System.exit(0);
							this.cancel();
						}
					});
				}
			}
		});
	}
	
}
