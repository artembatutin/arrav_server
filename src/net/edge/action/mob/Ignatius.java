package net.edge.action.mob;

import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.dialogue.impl.PlayerDialogue;
import net.edge.content.dialogue.impl.StatementDialogue;
import net.edge.content.dialogue.test.DialogueAppender;
import net.edge.action.ActionInitializer;
import net.edge.action.impl.MobAction;
import net.edge.content.skill.firemaking.pits.FirepitManager;
import net.edge.util.Utility;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

public class Ignatius extends ActionInitializer {
	@Override
	public void init() {
		MobAction e = new MobAction() {
			@Override
			public boolean click(Player player, Mob npc, int click) {
				DialogueAppender ap = new DialogueAppender(player);
				boolean active = FirepitManager.get().getFirepit().isActive();
				ap.chain(new NpcDialogue(4946, "Hey, " + player.getFormatUsername() + ", what can I help you with?"));
				ap.chain(new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
						player.getDialogueBuilder().advance();
					} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
						player.getDialogueBuilder().go(8);
					} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
						player.getDialogueBuilder().go(10);
					} else {
						player.closeWidget();
					}
				}, "What is this place?", active ? "How long till the event ends?" : "Howmany logs do the fire pits have?", "Do you sell anything?", "Nevermind"));
				ap.chain(new PlayerDialogue("What is this place?"));
				ap.chain(new NpcDialogue(4946, "You don't know... ? ", "This is the fire pit area, you can add logs to ", "the fire pit, and once it has reached an amount", "of 1,000 logs, there will be a global event for all players."));
				ap.chain(new NpcDialogue(4946, "Once the fire pit has reached an amount of 1,000 logs,", "you'll be able to use a fire lighter on it. The player who", "successfully ignites the fire pit will receive a huge", "firemaking experience bonus!"));
				ap.chain(new NpcDialogue(4946, "The fire pit, once ignited, is responsible for", "a double experience event, the event lasts for a hour."));
				ap.chain(new PlayerDialogue("Ah, yeah I think I understand the concept now."));
				ap.chain(new NpcDialogue(4946, "Aha, if you think you have any ideas to improve the concept", "feel free to make a suggestion on the forums!"));
				ap.chain(new PlayerDialogue("Will do!").attachAfter(() -> player.closeWidget()));
				ap.chain(new PlayerDialogue(active ? "Howlong till the event ends?" : "Howmany logs does the fire pit have?"));
				String[] messages = active ? new String[]{"The event is active for another:", Utility.convertTime(FirepitManager.get().getFirepit().getTime())} : new String[]{"Fire pit: " + FirepitManager.get().getFirepit().getElements() + "/1000 logs.", "The minimum log that's sacrificable: " + FirepitManager.get().getFirepit().getLogRequirement()};
				ap.chain(new StatementDialogue(messages).attachAfter(() -> player.closeWidget()));
				ap.chain(new PlayerDialogue("Do you sell anything?"));
				ap.chain(new NpcDialogue(4946, "Sadly, I don't have anything for sale, I used all my logs", "to get 99 firemaking years ago..."));
				ap.start();
				return true;
			}
		};
		e.registerFirst(4946);
	}
}
