package com.rageps.action.but;

import com.rageps.action.impl.ButtonAction;
import com.rageps.content.TabInterface;
import com.rageps.content.skill.magic.Spellbook;
import com.rageps.action.ActionInitializer;
import com.rageps.net.refactor.packet.out.model.ConfigPacket;
import com.rageps.world.entity.actor.combat.magic.CombatSpell;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.container.impl.Equipment;

public class CombatAutocastButton extends ActionInitializer {
	
	@Override
	public void init() {
		//AUTOCASTING
		register(CombatSpell.SMOKE_RUSH, 51133, 50139);
		register(CombatSpell.SHADOW_RUSH, 51185, 50187);
		register(CombatSpell.BLOOD_RUSH, 51091, 50101);
		register(CombatSpell.ICE_RUSH, 24018, 50061);
		register(CombatSpell.SMOKE_BURST, 51159, 50163);
		register(CombatSpell.SHADOW_BURST, 51211, 50211);
		register(CombatSpell.BLOOD_BURST, 51111, 50119);
		register(CombatSpell.ICE_BURST, 51069, 50081);
		register(CombatSpell.SMOKE_BLITZ, 51146, 50151);
		register(CombatSpell.SHADOW_BLITZ, 51198, 50199);
		register(CombatSpell.BLOOD_BLITZ, 51102, 50111);
		register(CombatSpell.ICE_BLITZ, 51058, 50071);
		register(CombatSpell.SMOKE_BARRAGE, 51172, 50175);
		register(CombatSpell.SHADOW_BARRAGE, 51224, 50223);
		register(CombatSpell.BLOOD_BARRAGE, 51122, 50129);
		register(CombatSpell.ICE_BARRAGE, 51080, 50091);
		register(CombatSpell.WIND_STRIKE, 7038, 4128);
		register(CombatSpell.WATER_STRIKE, 7039, 4130);
		register(CombatSpell.EARTH_STRIKE, 7040, 4132);
		register(CombatSpell.FIRE_STRIKE, 7041, 4134);
		register(CombatSpell.WIND_BOLT, 7042, 4136);
		register(CombatSpell.WATER_BOLT, 7043, 4139);
		register(CombatSpell.EARTH_BOLT, 7044, 4142);
		register(CombatSpell.FIRE_BOLT, 7045, 4145);
		register(CombatSpell.WIND_BLAST, 7046, 4148);
		register(CombatSpell.WATER_BLAST, 7047, 4151);
		register(CombatSpell.EARTH_BLAST, 7048, 4153);
		register(CombatSpell.FIRE_BLAST, 7049, 4157);
		register(CombatSpell.WIND_WAVE, 7050, 4159);
		register(CombatSpell.WATER_WAVE, 7051, 4161);
		register(CombatSpell.EARTH_WAVE, 7052, 4164);
		register(CombatSpell.FIRE_WAVE, 7053, 4165);
		register(CombatSpell.CONFUSE, 4129);
		register(CombatSpell.WEAKEN, 4133);
		register(CombatSpell.CURSE, 4137);
		register(CombatSpell.BIND, 6036);
		register(CombatSpell.IBAN_BLAST, 6003);
		register(CombatSpell.MAGIC_DART, 47005);
		register(CombatSpell.SARADOMIN_STRIKE, 4166);
		register(CombatSpell.CLAWS_OF_GUTHIX, 4167);
		register(CombatSpell.FLAMES_OF_ZAMORAK, 4168);
		register(CombatSpell.VULNERABILITY, 6006);
		register(CombatSpell.ENFEEBLE, 6007);
		register(CombatSpell.ENTANGLE, 6056);
		register(CombatSpell.STUN, 6026);
		
		ButtonAction e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.message("Cannot autocast this.");
				return true;
			}
		};
		e.register(48147);
		e.register(48157);
		e.register(48167);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.setAutocastSpell(null);
				player.send(new ConfigPacket(108, 0));
				return true;
			}
		};
		e.register(26010);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.message("@red@No support for training defense with magic yet.");
				return true;
			}
		};
		e.register(94047);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(player.autocasting) {
					player.setAutocastSpell(null);
					player.send(new ConfigPacket(108, 0));
				} else {
					Item staff = player.getEquipment().get(Equipment.WEAPON_SLOT);
					if(staff != null && staff.getId() == 4675) {
						if(!player.getSpellbook().equals(Spellbook.ANCIENT)) {
							player.message("You can only autocast ancient magics with this staff.");
							return true;
						}
						
						TabInterface.ATTACK.sendInterface(player, 1689);
					} else {
						if(!player.getSpellbook().equals(Spellbook.NORMAL)) {
							player.message("You can only autocast standard magics with this staff.");
							return true;
						}
						
						TabInterface.ATTACK.sendInterface(player, 1829);
					}
				}
				return true;
			}
		};
		e.register(1093);
		e.register(1094);
		e.register(1097);
	}
	
	public void register(CombatSpell spell, int... buttons) {
		ButtonAction e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.setAutocastSpell(spell);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.send(new ConfigPacket(108, 3));
				return true;
			}
		};
		for(int b : buttons) {
			e.register(b);
		}
	}
	
}
