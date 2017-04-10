package net.edge.world.content.commands.impl;

import net.edge.Server;
import net.edge.world.World;
import net.edge.world.content.commands.Command;
import net.edge.world.content.commands.CommandSignature;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.entity.player.assets.Rights;
import net.edge.task.Task;

@CommandSignature(alias = {"update"}, rights = {Rights.DEVELOPER}, syntax = "Use this command as ::update seconds")
public final class UpdateCommand implements Command {
	
	/**
	 * The updating count in progress flag.
	 */
	public static boolean inProgess = false;
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		inProgess = true;
		int timer = Integer.parseInt(cmd[1]);
		World.getPlayers().forEach(p -> p.getMessages().sendSystemUpdate(timer * 50 / 30));
		Server.UPDATING = timer;
		World.getTaskManager().submit(new Task(1, true) {
			@Override
			protected void execute() {
				Server.UPDATING -= 0.6;
				System.out.println("Update count: " + Server.UPDATING);
				if(Server.UPDATING <= 0) {
					System.out.println("Setting player into updating mode.");
					System.out.println("Logging players out...");
					World.getPlayers().forEach(World::handleLogout);
					System.out.println("Waiting for shutdown.");
					World.submit(new Task(1, false) {
						@Override
						protected void execute() {
							if(World.getPlayers().size() == 0) {
								this.cancel();
								System.out.println("Terminating server instance.");
								System.exit(0);//it calls the {@link ServerHook} after.
							}
						}
					});
					this.cancel();
				}
			}
		});
	}
	
}
