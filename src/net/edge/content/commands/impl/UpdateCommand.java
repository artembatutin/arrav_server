package net.edge.content.commands.impl;

import net.edge.Server;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.net.packet.out.SendLogout;
import net.edge.net.packet.out.SendUpdateTimer;
import net.edge.task.Task;
import net.edge.world.World;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.actor.player.assets.Rights;

import java.util.Iterator;

@CommandSignature(alias = {"update"}, rights = {Rights.ADMINISTRATOR}, syntax = "Use this command as ::update seconds")
public final class UpdateCommand implements Command {
	
	/**
	 * The updating count in progress flag.
	 */
	public static int inProgess = 0;
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		inProgess = 1;
		int timer = Integer.parseInt(cmd[1]);
		Player other;
		Iterator<Player> it = World.get().getPlayers().iterator();
		while((other = it.next()) != null) {
			other.out(new SendUpdateTimer(timer * 50 / 30));
		}
		Server.UPDATING = timer;
		World.get().getTask().submit(new Task(1, true) {
			@Override
			protected void execute() {
				Server.UPDATING -= 0.6;
				System.out.println("Update count: " + Server.UPDATING);
				if(Server.UPDATING <= 0) {
					inProgess = 2;
					System.out.println("Setting player into updating mode.");
					System.out.println("Logging players out... - Players online: " + World.get().getPlayers().size());
					Player other;
					Iterator<Player> it = World.get().getPlayers().iterator();
					while((other = it.next()) != null) {
						other.out(new SendLogout());
					}
					System.out.println("Waiting for shutdown.");
					World.get().getTask().submit(new Task(10, false) {
						@Override
						protected void execute() {
							System.out.println("Terminating server instance - Players online: " + World.get().getPlayers().size());
							World.get().shutdown();
							System.exit(0);
							this.cancel();
						}
					});
				}
			}
		});
	}
	
}
