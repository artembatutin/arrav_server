package net.edge.content.commands.impl;

import net.edge.Server;
import net.edge.task.Task;
import net.edge.world.World;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

import java.util.concurrent.TimeUnit;

@CommandSignature(alias = {"update"}, rights = {Rights.DEVELOPER}, syntax = "Use this command as ::update seconds")
public final class UpdateCommand implements Command {
	
	/**
	 * The updating count in progress flag.
	 */
	public static int inProgess = 0;
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		inProgess = 1;
		int timer = Integer.parseInt(cmd[1]);
		World.get().getPlayers().forEach(p -> p.getMessages().sendSystemUpdate(timer * 50 / 30));
		Server.UPDATING = timer;
		World.get().getTask().submit(new Task(1, true) {
			@Override
			protected void execute() {
				Server.UPDATING -= 0.6;
				System.out.println("Update count: " + Server.UPDATING);
				if(Server.UPDATING <= 0) {
					inProgess = 2;
					System.out.println("Setting player into updating mode.");
					System.out.println("Logging players out...");
					World.get().getPlayers().forEach(p -> World.get().logout(p, true));
					System.out.println("Waiting for shutdown.");
					World.get().submit(new Task(3, false) {
						@Override
						protected void execute() {
							try {
								System.out.println("Awaiting terminal - Players online: " + World.get().getPlayers().size());
								if(World.get().getPlayers().isEmpty()) {
									System.out.println("Terminating server instance.");
									//Have to wait for saves.
									World.get().getExecutor().get().shutdown();
									World.get().getExecutor().get().awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
									World.get().shutdown();
									System.exit(0);
								} else {
									World.get().getPlayers().forEach(p -> World.get().logout(p, true));
								}
							} catch(InterruptedException e) {
								e.printStackTrace();
							}
						}
					});
					this.cancel();
				}
			}
		});
	}
	
}
