package net.edge.event.but;

import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.teleport.impl.DefaultTeleportSpell;
import net.edge.content.wilderness.WildernessActivity;
import net.edge.event.EventInitializer;
import net.edge.event.impl.ButtonEvent;
import net.edge.locale.Position;
import net.edge.world.node.entity.player.Player;

import java.util.concurrent.TimeUnit;

public class Spellbook extends EventInitializer {
	
	@Override
	public void init() {
		//home
		ButtonEvent e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3085, 3508));
				return true;
			}
		};
		e.register(84237);
		e.register(75010);
		e.register(75008);
		e.register(117048);
		
		//skills
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.getMessages().sendInterface(-4);
				return true;
			}
		};
		e.register(6004);
		e.register(51023);
		e.register(117131);

		//monsters
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.getMessages().sendInterface(-9);
				return true;
			}
		};
		e.register(4146);
		e.register(50253);
		e.register(117112);

		//minigames
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.getMessages().sendInterface(-6);
				return true;
			}
		};
		e.register(6005);
		e.register(51031);
		e.register(117154);
		
		//bosses
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.getMessages().sendInterface(-7);
				return true;
			}
		};
		e.register(4150);
		e.register(51013);
		e.register(117123);
		
		//pvp
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.getMessages().sendInterface(-15);
				if(player.getWildernessActivity().elapsed(5, TimeUnit.MINUTES)) {
					player.message("Wilderness map has been updated! Next update in 5 minutes.");
					player.getMessages().sendWildernessActivity(WildernessActivity.getPlayers());
					player.getWildernessActivity().reset();
				}
				return true;
			}
		};
		e.register(4140);
		e.register(50235);
		e.register(117162);
		
		//locations
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.getDialogueBuilder().append(new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
						player.teleport(new Position(3223, 3218), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
					} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
						player.teleport(new Position(2964, 3378), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
					} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
						player.teleport(new Position(3093, 3244), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
					} else if(t.equals(OptionDialogue.OptionType.FOURTH_OPTION)) {
						player.teleport(new Position(2815, 3447), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
					} else {
						player.getDialogueBuilder().advance();
					}
					if(!t.equals(OptionDialogue.OptionType.FIFTH_OPTION)) {
						player.getMessages().sendCloseWindows();
					}
				}, "Lumbridge", "Falador", "Draynor", "Catherby", "@red@Next page"), new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
						player.getDialogueBuilder().previous();
					} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
						player.teleport(new Position(2529, 3307), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
					} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
						player.teleport(new Position(2662, 3305), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
					}
					
					if(!t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
						player.getMessages().sendCloseWindows();
					}
				}, "@red@Previous page", "West Ardougne", "East Ardougne", "Nevermind"));
				return true;
			}
		};
		e.register(4143);
		e.register(50245);
		e.register(117210);

	}
	
}
