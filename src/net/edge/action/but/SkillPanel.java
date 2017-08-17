package net.edge.action.but;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.ButtonAction;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.skill.Skills;
import net.edge.world.entity.actor.player.Player;

public class SkillPanel extends ActionInitializer {

	@Override
	public void init() {
		//Skills 0-24
		ButtonAction e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
//				player.teleport(new Position(3366, 3509), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL); TODO: add teleports
				return true;
			}
		};
		e.register(Skills.PRAYER);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
//				player.teleport(new Position(3093, 3479, 1), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(Skills.THIEVING);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
//				player.teleport(new Position(3147, 3451), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(Skills.COOKING);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
//				player.teleport(new Position(2605, 4774), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(Skills.WOODCUTTING);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
//				player.teleport(new Position(2605, 3412), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(Skills.FISHING);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
//				player.teleport(new Position(3039, 4834), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(Skills.RUNECRAFTING);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
//				player.teleport(new Position(3002, 9799), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(Skills.FLETCHING);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
//				player.teleport(new Position(2998, 9829), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(Skills.MINING);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
//				player.teleport(new Position(2998, 9826), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(Skills.SMITHING);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
//				player.teleport(new Position(2610, 3095), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(Skills.FIREMAKING);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
//				player.teleport(new Position(3350, 3334), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(Skills.CRAFTING);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
//				player.teleport(new Position(2898, 3430), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(Skills.HERBLORE);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
//				player.teleport(new Position(2807, 3464), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(Skills.FARMING);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
//				player.teleport(new Position(3077, 3518), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(Skills.SUMMONING);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.getDialogueBuilder().append(new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
		//				player.teleport(new Position(2475, 3439), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
					} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
		//				player.teleport(new Position(2551, 3556), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
					} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
		//				player.teleport(new Position(2998, 3915), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
					}
				}, "Gnome", "Barbarian", "@red@Wilderness"));
				return true;
			}
		};
		e.register(Skills.AGILITY);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
//				player.teleport(new Position(2375, 3618), DefaultTeleportSpell.TeleportType.TRAINING_PORTAL);
				return true;
			}
		};
		e.register(Skills.CONSTRUCTION);

	}

}
