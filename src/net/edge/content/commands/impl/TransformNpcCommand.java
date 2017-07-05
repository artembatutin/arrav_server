package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;
import net.edge.world.node.entity.update.UpdateFlag;

@CommandSignature(alias = {"pnpc", "transformnpc"}, rights = {Rights.ADMINISTRATOR}, syntax = "Use this command as ::pnpc or ::transformnpc npcId")
public final class TransformNpcCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		int id = Integer.parseInt(cmd[1]);
		player.setPlayerNpc(id);
		player.getFlags().flag(UpdateFlag.APPEARANCE);
	}
	
}
