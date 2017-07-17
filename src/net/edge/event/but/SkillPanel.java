package net.edge.event.but;

import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.skill.Skills;
import net.edge.content.teleport.impl.DefaultTeleportSpell;
import net.edge.event.EventInitializer;
import net.edge.event.impl.ButtonEvent;
import net.edge.locale.Position;
import net.edge.world.node.actor.player.Player;

public class SkillPanel extends EventInitializer {
	
	@Override
	public void init() {
		//Skills 0-24
		ButtonEvent e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3093, 3479, 1), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(Skills.THIEVING);
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3147, 3451), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(Skills.COOKING);
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2605, 4774), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(Skills.WOODCUTTING);
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2605, 3412), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(Skills.FISHING);
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3039, 4834), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(Skills.RUNECRAFTING);
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3002, 9799), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(Skills.FLETCHING);
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2998, 9829), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(Skills.MINING);
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2998, 9826), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(Skills.SMITHING);
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2610, 3095), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(Skills.FIREMAKING);
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3350, 3334), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(Skills.CRAFTING);
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2898, 3430), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(Skills.HERBLORE);
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.getDialogueBuilder().append(new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
						player.teleport(new Position(2475, 3439), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
					} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
						player.teleport(new Position(2551, 3556), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
					} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
						player.teleport(new Position(2998, 3915), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
					}
				}, "Gnome", "Barbarian", "@red@Wilderness"));
				return true;
			}
		};
		e.register(Skills.AGILITY);
		e = new ButtonEvent() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2375, 3618), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(Skills.CONSTRUCTION);

	}
	
}
