package net.edge.event.npc;

import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.dialogue.impl.PlayerDialogue;
import net.edge.content.dialogue.impl.StatementDialogue;
import net.edge.content.dialogue.test.DialogueAppender;
import net.edge.event.EventInitializer;
import net.edge.event.impl.NpcEvent;
import net.edge.util.Utility;
import net.edge.world.World;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;

public class Ignatius extends EventInitializer {
	@Override
	public void init() {
		NpcEvent e = new NpcEvent() {
			@Override
			public boolean click(Player player, Npc npc, int click) {
				DialogueAppender ap = new DialogueAppender(player);
				boolean active = World.getFirepitEvent().getFirepit().isActive();
				ap.chain(new NpcDialogue(4946, "Hey, " + player.getFormatUsername() + ", what can I help you with?"));
				ap.chain(new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
						player.getDialogueBuilder().advance();
					} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
						player.getDialogueBuilder().go(8);
					} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
						player.getDialogueBuilder().go(10);
					} else {
						player.getMessages().sendCloseWindows();
					}
				}, "What is this place?", active ? "How long till the event ends?" : "Howmany logs do the fire pits have?", "Do you sell anything?", "Nevermind"));
				ap.chain(new PlayerDialogue("What is this place?"));
				ap.chain(new NpcDialogue(4946, "You don't know... ? ", "This is the fire pit area, you can add logs to ", "the fire pit, and once it has reached an amount", "of 1,000 logs, there will be a global event for all players."));
				ap.chain(new NpcDialogue(4946, "Once the fire pit has reached an amount of 1,000 logs,", "you'll be able to use a fire lighter on it. The player who", "successfully ignites the fire pit will receive a huge", "firemaking experience bonus!"));
				ap.chain(new NpcDialogue(4946, "The fire pit, once ignited, is responsible for", "a double experience event, the event lasts for a hour."));
				ap.chain(new PlayerDialogue("Ah, yeah I think I understand the concept now."));
				ap.chain(new NpcDialogue(4946, "Aha, if you think you have any ideas to improve the concept", "feel free to make a suggestion on the forums!"));
				ap.chain(new PlayerDialogue("Will do!").attachAfter(() -> player.getMessages().sendCloseWindows()));
				ap.chain(new PlayerDialogue(active ? "Howlong till the event ends?" : "Howmany logs does the fire pit have?"));
				String[] messages = active ? new String[]{"The event is active for another:", Utility.convertTime(World.getFirepitEvent().getFirepit().getTime())} : new String[]{"Fire pit: " + World.getFirepitEvent().getFirepit().getElements() + "/1000 logs.", "The minimum log that's sacrificable: " + World.getFirepitEvent().getFirepit().getLogRequirement()};
				ap.chain(new StatementDialogue(messages).attachAfter(() -> player.getMessages().sendCloseWindows()));
				ap.chain(new PlayerDialogue("Do you sell anything?"));
				ap.chain(new NpcDialogue(4946, "Sadly, I don't have anything for sale, I used all my logs", "to get 99 firemaking years ago..."));
				ap.start();
				return true;
			}
		};
		e.registerFirst(4946);
	}
}