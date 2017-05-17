package net.edge.world.content.commands.impl;

import net.edge.world.content.commands.Command;
import net.edge.world.content.commands.CommandSignature;
import net.edge.world.content.dialogue.impl.OptionDialogue;
import net.edge.world.content.dialogue.impl.StatementDialogue;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.entity.player.assets.Rights;

@CommandSignature(alias = {"emptybank"}, rights = {Rights.DEVELOPER}, syntax = "Use this command as just ::emptybank")
public final class EmptyBankCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.getDialogueBuilder().append(new StatementDialogue("Are you sure you want to empty your bank?"), new OptionDialogue(t -> {
			if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
				player.getBank().clear();
				player.getBank().refreshAll();
				player.getMessages().sendCloseWindows();
			} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION) || t.equals(OptionDialogue.OptionType.FOURTH_OPTION)) {
				player.getMessages().sendCloseWindows();
			} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
				player.getDialogueBuilder().previous();
			}
		}, "Yes", "No", "What was the question again?", "Nevermind"));
	}
	
}
