package net.arrav.content.commands.impl;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"bug", "report", "error"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR, Rights.MODERATOR, Rights.GOLDEN_DONATOR, Rights.EXTREME_DONATOR, Rights.SUPER_DONATOR, Rights.DONATOR, Rights.IRON_MAN, Rights.DESIGNER, Rights.YOUTUBER, Rights.HELPER, Rights.PLAYER}, syntax = "Reports a bug, ::bug explain the bug here")
public final class BugCommand implements Command {
	
	public static final ObjectArrayList<String> REPORT_LINES = new ObjectArrayList<>();
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(player.getFormatUsername());
		sb.append(":");
		for(String s : cmd) {
			sb.append(s);
			sb.append(" ");
		}
		REPORT_LINES.add(sb.toString());
		player.message("Your bug has been reported, thank you!");
	}
	
}
