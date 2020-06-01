package com.rageps.action.but;

import com.rageps.action.impl.ButtonAction;
import com.rageps.action.ActionInitializer;
import com.rageps.world.entity.actor.combat.attack.FightType;
import com.rageps.world.entity.actor.combat.weapon.WeaponInterface;
import com.rageps.world.entity.actor.player.Player;

public class CombatFightButton extends ActionInitializer {
	
	@Override
	public void init() {
		//FIGHT TYPES
		register(1080, FightType.STAFF_BASH);
		register(1079, FightType.STAFF_POUND);
		register(1078, FightType.STAFF_FOCUS);
		register(1177, FightType.WARHAMMER_POUND);
		register(1176, FightType.WARHAMMER_PUMMEL);
		register(1175, FightType.WARHAMMER_BLOCK);
		register(3014, FightType.SCYTHE_REAP);
		register(3017, FightType.SCYTHE_CHOP);
		register(3016, FightType.SCYTHE_JAB);
		register(3015, FightType.SCYTHE_BLOCK);
		register(6168, FightType.BATTLEAXE_CHOP);
		register(6171, FightType.BATTLEAXE_HACK);
		register(6170, FightType.BATTLEAXE_SMASH);
		register(6169, FightType.BATTLEAXE_BLOCK);
		register(14218, FightType.MACE_POUND);
		register(14221, FightType.MACE_PUMMEL);
		register(14220, FightType.MACE_SPIKE);
		register(14219, FightType.MACE_BLOCK);
		register(18077, FightType.SPEAR_LUNGE);
		register(18080, FightType.SPEAR_SWIPE);
		register(18079, FightType.SPEAR_POUND);
		register(18078, FightType.SPEAR_BLOCK);
		register(18106, FightType.TWOHANDEDSWORD_SLASH);
		register(18105, FightType.TWOHANDEDSWORD_SMASH);
		register(18104, FightType.TWOHANDEDSWORD_BLOCK);
		register(18103, FightType.TWOHANDEDSWORD_CHOP);
		register(15106, FightType.TWOHANDEDSWORD_SLASH);
		register(21200, FightType.PICKAXE_SPIKE);
		register(21203, FightType.PICKAXE_IMPALE);
		register(21202, FightType.PICKAXE_SMASH);
		register(21201, FightType.PICKAXE_BLOCK);
		register(30088, FightType.CLAWS_CHOP);
		register(30091, FightType.CLAWS_SLASH);
		register(30090, FightType.CLAWS_LUNGE);
		register(30089, FightType.CLAWS_BLOCK);
		register(33018, FightType.HALBERD_JAB);
		register(33020, FightType.HALBERD_SWIPE);
		register(33016, FightType.HALBERD_FEND);
		register(22228, FightType.UNARMED_PUNCH);
		register(22230, FightType.UNARMED_KICK);
		register(22229, FightType.UNARMED_BLOCK);
		register(48010, FightType.WHIP_FLICK);
		register(48009, FightType.WHIP_LASH);
		register(48008, FightType.WHIP_DEFLECT);
		register(94014, FightType.SCORCH);
		register(94015, FightType.FLARE);
		register(94016, FightType.BLAZE);
		ButtonAction e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(player.getWeapon().equals(WeaponInterface.CHINCHOMPA)) {
					player.getCombat().setFightType(FightType.SHORT_FUSE);
				}
				return true;
			}
		};
		e.register(93251);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(player.getWeapon().equals(WeaponInterface.CHINCHOMPA)) {
					player.getCombat().setFightType(FightType.MEDIUM_FUSE);
				}
				return true;
			}
		};
		e.register(93252);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(player.getWeapon().equals(WeaponInterface.CHINCHOMPA)) {
					player.getCombat().setFightType(FightType.LONG_FUSE);
				}
				return true;
			}
		};
		e.register(93253);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(player.getWeapon() == WeaponInterface.KNIFE) {
					player.getCombat().setFightType(FightType.KNIFE_ACCURATE);
				} else if(player.getWeapon() == WeaponInterface.THROWNAXE) {
					player.getCombat().setFightType(FightType.THROWNAXE_ACCURATE);
				} else if(player.getWeapon() == WeaponInterface.DART) {
					player.getCombat().setFightType(FightType.DART_ACCURATE);
				} else if(player.getWeapon() == WeaponInterface.JAVELIN) {
					player.getCombat().setFightType(FightType.JAVELIN_ACCURATE);
				}
				return true;
			}
		};
		e.register(17102); // knife, thrownaxe, dart & javelin
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(player.getWeapon() == WeaponInterface.KNIFE) {
					player.getCombat().setFightType(FightType.KNIFE_RAPID);
				} else if(player.getWeapon() == WeaponInterface.THROWNAXE) {
					player.getCombat().setFightType(FightType.THROWNAXE_RAPID);
				} else if(player.getWeapon() == WeaponInterface.DART) {
					player.getCombat().setFightType(FightType.DART_RAPID);
				} else if(player.getWeapon() == WeaponInterface.JAVELIN) {
					player.getCombat().setFightType(FightType.JAVELIN_RAPID);
				}
				return true;
			}
		};
		e.register(17101);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(player.getWeapon() == WeaponInterface.KNIFE) {
					player.getCombat().setFightType(FightType.KNIFE_LONGRANGE);
				} else if(player.getWeapon() == WeaponInterface.THROWNAXE) {
					player.getCombat().setFightType(FightType.THROWNAXE_LONGRANGE);
				} else if(player.getWeapon() == WeaponInterface.DART) {
					player.getCombat().setFightType(FightType.DART_LONGRANGE);
				} else if(player.getWeapon() == WeaponInterface.JAVELIN) {
					player.getCombat().setFightType(FightType.JAVELIN_LONGRANGE);
				}
				return true;
			}
		};
		e.register(17100);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(player.getWeapon() == WeaponInterface.SHORTBOW) {
					player.getCombat().setFightType(FightType.SHORTBOW_ACCURATE);
				} else if(player.getWeapon() == WeaponInterface.LONGBOW) {
					player.getCombat().setFightType(FightType.LONGBOW_ACCURATE);
				} else if(player.getWeapon() == WeaponInterface.CROSSBOW) {
					player.getCombat().setFightType(FightType.CROSSBOW_ACCURATE);
				} else if(player.getWeapon() == WeaponInterface.COMPOSITE_BOW) {
					player.getCombat().setFightType(FightType.LONGBOW_ACCURATE);
				}
				return true;
			}
		};
		e.register(6236); // shortbow & longbow & crossbow
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(player.getWeapon() == WeaponInterface.SHORTBOW) {
					player.getCombat().setFightType(FightType.SHORTBOW_RAPID);
				} else if(player.getWeapon() == WeaponInterface.LONGBOW) {
					player.getCombat().setFightType(FightType.LONGBOW_RAPID);
				} else if(player.getWeapon() == WeaponInterface.CROSSBOW) {
					player.getCombat().setFightType(FightType.CROSSBOW_RAPID);
				} else if(player.getWeapon() == WeaponInterface.COMPOSITE_BOW) {
					player.getCombat().setFightType(FightType.LONGBOW_RAPID);
				}
				return true;
			}
		};
		e.register(6235);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(player.getWeapon() == WeaponInterface.SHORTBOW) {
					player.getCombat().setFightType(FightType.SHORTBOW_LONGRANGE);
				} else if(player.getWeapon() == WeaponInterface.LONGBOW) {
					player.getCombat().setFightType(FightType.LONGBOW_LONGRANGE);
				} else if(player.getWeapon() == WeaponInterface.CROSSBOW) {
					player.getCombat().setFightType(FightType.CROSSBOW_LONGRANGE);
				} else if(player.getWeapon() == WeaponInterface.COMPOSITE_BOW) {
					player.getCombat().setFightType(FightType.LONGBOW_LONGRANGE);
				}
				return true;
			}
		};
		e.register(6234);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(player.getWeapon() == WeaponInterface.DAGGER) {
					player.getCombat().setFightType(FightType.DAGGER_STAB);
				} else if(player.getWeapon() == WeaponInterface.SWORD) {
					player.getCombat().setFightType(FightType.SWORD_STAB);
				}
				return true;
			}
		};
		e.register(8234); // dagger & sword
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(player.getWeapon() == WeaponInterface.DAGGER) {
					player.getCombat().setFightType(FightType.DAGGER_LUNGE);
				} else if(player.getWeapon() == WeaponInterface.SWORD) {
					player.getCombat().setFightType(FightType.SWORD_LUNGE);
				}
				return true;
			}
		};
		e.register(8237);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(player.getWeapon() == WeaponInterface.DAGGER) {
					player.getCombat().setFightType(FightType.DAGGER_SLASH);
				} else if(player.getWeapon() == WeaponInterface.SWORD) {
					player.getCombat().setFightType(FightType.SWORD_SLASH);
				}
				return true;
			}
		};
		e.register(8236);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(player.getWeapon() == WeaponInterface.DAGGER) {
					player.getCombat().setFightType(FightType.DAGGER_BLOCK);
				} else if(player.getWeapon() == WeaponInterface.SWORD) {
					player.getCombat().setFightType(FightType.SWORD_BLOCK);
				}
				return true;
			}
		};
		e.register(8235);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(player.getWeapon() == WeaponInterface.SCIMITAR) {
					player.getCombat().setFightType(FightType.SCIMITAR_CHOP);
				} else if(player.getWeapon() == WeaponInterface.LONGSWORD) {
					player.getCombat().setFightType(FightType.LONGSWORD_CHOP);
				}
				return true;
			}
		};
		e.register(9125); // scimitar & longsword
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(player.getWeapon() == WeaponInterface.SCIMITAR) {
					player.getCombat().setFightType(FightType.SCIMITAR_SLASH);
				} else if(player.getWeapon() == WeaponInterface.LONGSWORD) {
					player.getCombat().setFightType(FightType.LONGSWORD_SLASH);
				}
				return true;
			}
		};
		e.register(9128);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(player.getWeapon() == WeaponInterface.SCIMITAR) {
					player.getCombat().setFightType(FightType.SCIMITAR_LUNGE);
				} else if(player.getWeapon() == WeaponInterface.LONGSWORD) {
					player.getCombat().setFightType(FightType.LONGSWORD_LUNGE);
				}
				return true;
			}
		};
		e.register(9127);
		e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				if(player.getWeapon() == WeaponInterface.SCIMITAR) {
					player.getCombat().setFightType(FightType.SCIMITAR_BLOCK);
				} else if(player.getWeapon() == WeaponInterface.LONGSWORD) {
					player.getCombat().setFightType(FightType.LONGSWORD_BLOCK);
				}
				return true;
			}
		};
		e.register(9126);
	}
	
	public void register(int button, FightType type) {
		ButtonAction e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.getCombat().setFightType(type);
				return true;
			}
		};
		e.register(button);
	}
	
}
