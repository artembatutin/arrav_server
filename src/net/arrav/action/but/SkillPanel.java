package net.arrav.action.but;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ButtonAction;
import net.arrav.content.dialogue.impl.OptionDialogue;
import net.arrav.content.skill.Skills;
import net.arrav.content.teleport.TeleportType;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;

public class SkillPanel extends ActionInitializer {
	
	@Override
	public void init() {
		//Skills 0-24
		ButtonAction e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3366, 3509), TeleportType.NORMAL);
				return true;
			}
		};
		e.register(Skills.PRAYER);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3093, 3479, 1), TeleportType.NORMAL);
				return true;
			}
		};
		e.register(Skills.THIEVING);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3147, 3451), TeleportType.NORMAL);
				return true;
			}
		};
		e.register(Skills.COOKING);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2605, 4774), TeleportType.NORMAL);
				return true;
			}
		};
		e.register(Skills.WOODCUTTING);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2605, 3412), TeleportType.NORMAL);
				return true;
			}
		};
		e.register(Skills.FISHING);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3039, 4834), TeleportType.NORMAL);
				return true;
			}
		};
		e.register(Skills.RUNECRAFTING);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3002, 9799), TeleportType.NORMAL);
				return true;
			}
		};
		e.register(Skills.FLETCHING);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2998, 9829), TeleportType.NORMAL);
				return true;
			}
		};
		e.register(Skills.MINING);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2998, 9826), TeleportType.NORMAL);
				return true;
			}
		};
		e.register(Skills.SMITHING);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2610, 3095), TeleportType.NORMAL);
				return true;
			}
		};
		e.register(Skills.FIREMAKING);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3350, 3334), TeleportType.NORMAL);
				return true;
			}
		};
		e.register(Skills.CRAFTING);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2898, 3430), TeleportType.NORMAL);
				return true;
			}
		};
		e.register(Skills.HERBLORE);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2807, 3464), TeleportType.NORMAL);
				return true;
			}
		};
		e.register(Skills.FARMING);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(3077, 3518), TeleportType.NORMAL);
				return true;
			}
		};
		e.register(Skills.SUMMONING);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.getDialogueBuilder().append(new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
						player.teleport(new Position(2475, 3439), TeleportType.NORMAL);
					} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
						player.teleport(new Position(2551, 3556), TeleportType.NORMAL);
					} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
						player.teleport(new Position(2998, 3915), TeleportType.NORMAL);
					}
				}, "Gnome", "Barbarian", "@red@Wilderness"));
				return true;
			}
		};
		e.register(Skills.AGILITY);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.teleport(new Position(2375, 3618), TeleportType.NORMAL);
				return true;
			}
		};
		e.register(Skills.CONSTRUCTION);
		
	}
	
}
