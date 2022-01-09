package com.rageps.content.item;

import com.rageps.action.ActionInitializer;
import com.rageps.action.impl.ButtonAction;
import com.rageps.action.impl.ItemAction;
import com.rageps.content.skill.Skills;
import com.rageps.net.refactor.packet.out.model.CloseInterfacePacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;

/**
 * Created by Dave/Ophion
 * Date: 12/08/2017
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 */
public class ExperienceLamp extends ActionInitializer {
	
	private int skill;
	private static final int INTERFACE = 2808;
	
	@Override
	public void init() {
		ButtonAction e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(skill > 24 && skill < 0) {
					player.message("You must choose a skill you wish to add experience to.");
					player.send(new CloseInterfacePacket(player.getDialogueBuilder()));
					return false;
				}
				if(!player.getInventory().contains(4447)) {
					player.message("You need an antique lamp to do this.");
					return false;
				}
				player.getInventory().remove(new Item(4447, 1));
				player.getSkills()[getSkill()].increaseExperience(800000.0);
				player.message("You gained some experience.");
				player.send(new CloseInterfacePacket(player.getDialogueBuilder()));
				setSkill(-1);
				return true;
			}
		};
		e.register(11015);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				setSkill(Skills.ATTACK);
				return true;
			}
		};
		e.register(10252);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				setSkill(Skills.STRENGTH);
				return true;
			}
		};
		e.register(10253);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				setSkill(Skills.RANGED);
				return true;
			}
		};
		e.register(10254);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				setSkill(Skills.MAGIC);
				return true;
			}
		};
		e.register(10255);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				setSkill(Skills.DEFENCE);
				return true;
			}
		};
		e.register(11000);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				setSkill(Skills.HITPOINTS);
				return true;
			}
		};
		e.register(11001);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				setSkill(Skills.PRAYER);
				return true;
			}
		};
		e.register(11002);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				setSkill(Skills.AGILITY);
				return true;
			}
		};
		e.register(11003);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				setSkill(Skills.HERBLORE);
				return true;
			}
		};
		e.register(11004);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				setSkill(Skills.THIEVING);
				return true;
			}
		};
		e.register(11005);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				setSkill(Skills.CRAFTING);
				return true;
			}
		};
		e.register(11006);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				setSkill(Skills.RUNECRAFTING);
				return true;
			}
		};
		e.register(11007);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				setSkill(Skills.SLAYER);
				return true;
			}
		};
		e.register(47002);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				setSkill(Skills.FARMING);
				return true;
			}
		};
		e.register(54090);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				setSkill(Skills.MINING);
				return true;
			}
		};
		e.register(11008);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				setSkill(Skills.SMITHING);
				return true;
			}
		};
		e.register(11009);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				setSkill(Skills.FISHING);
				return true;
			}
		};
		e.register(11010);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				setSkill(Skills.COOKING);
				return true;
			}
		};
		e.register(11011);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				setSkill(Skills.FIREMAKING);
				return true;
			}
		};
		e.register(11012);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				setSkill(Skills.WOODCUTTING);
				return true;
			}
		};
		e.register(11013);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				setSkill(Skills.FLETCHING);
				return true;
			}
		};
		e.register(11014);
	}
	
	public int getSkill() {
		return skill;
	}
	
	public void setSkill(int skillId) {
		this.skill = skillId;
	}
	
	public static void handleItem() {
		ItemAction e = new ItemAction() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				player.getInterfaceManager().open(ExperienceLamp.INTERFACE, true);
				return true;
			}
		};
		e.register(4447);
	}
	
}
