package net.edge.net.packet.in;

import net.edge.Application;
import net.edge.action.ActionContainer;
import net.edge.action.impl.ButtonAction;
import net.edge.content.Emote;
import net.edge.content.TabInterface;
import net.edge.content.clanchat.ClanManager;
import net.edge.content.combat.weapon.WeaponInterface;
import net.edge.content.dialogue.Dialogues;
import net.edge.content.item.Skillcape;
import net.edge.content.market.MarketShop;
import net.edge.content.minigame.MinigameHandler;
import net.edge.content.combat.content.MagicSpell;
import net.edge.content.combat.strategy.player.PlayerMagicStrategy;
import net.edge.content.skill.SkillData;
import net.edge.content.skill.cooking.Cooking;
import net.edge.content.skill.cooking.CookingData;
import net.edge.content.skill.cooking.DoughCreation;
import net.edge.content.skill.crafting.*;
import net.edge.content.skill.fletching.BowCarving;
import net.edge.content.skill.magic.EnchantCrossbowBolts;
import net.edge.content.skill.prayer.Prayer;
import net.edge.content.skill.slayer.Slayer;
import net.edge.content.skill.smithing.Smelting;
import net.edge.content.skill.summoning.Summoning;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.packet.IncomingPacket;
import net.edge.net.packet.out.SendConfig;
import net.edge.net.packet.out.SendEnterName;
import net.edge.net.packet.out.SendLogout;
import net.edge.net.packet.out.SendMessage;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;
import net.edge.world.entity.actor.player.assets.Spellbook;
import net.edge.world.entity.actor.player.assets.activity.ActivityManager;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.container.impl.Equipment;
import net.edge.world.entity.item.container.session.ExchangeSessionManager;
import net.edge.world.object.GameObject;

import java.util.concurrent.TimeUnit;

/**
 * The message sent from the client when the player clicks some sort of button or
 * module.
 * @author lare96 <http://github.com/lare96>
 */
public final class ClickButtonPacket implements IncomingPacket {
	
	public static final ActionContainer<ButtonAction> BUTTONS = new ActionContainer<>();
	
	/**
	 * The flag that determines if this message should be read properly.
	 */
	private static final boolean PROPER_READ = false;
	
	private static int hexToInt(byte[] data) {
		int value = 0;
		int n = 1000;
		for(byte aData : data) {
			int num = (aData & 0xFF) * n;
			value += num;
			if(n > 1) {
				n = n / 1000;
			}
		}
		return value;
	}
	
	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
		int button = PROPER_READ ? payload.getShort() : hexToInt(payload.getBytes(2));
		if(Application.DEBUG && player.getRights().equals(Rights.ADMINISTRATOR)) {
			player.message("Clicked button " + button + ".");
		}
		
		if(button != 9154 && button != 200 && button != 201 && player.getActivityManager().contains(ActivityManager.ActivityType.CLICK_BUTTON)) {
			return;
		}
		if(button == 123) {
			player.closeWidget();
			if(player.getMarketShop() != null) {
				MarketShop.clearFromShop(player);
			}
		}
		ButtonAction e = BUTTONS.get(button);
		if(e != null) {
			if(e.click(player, button)) {
				return;
			}
		}
		if(Prayer.activate(player, true, button)) {
			return;
		}
		if(Prayer.activateQuickPrayer(player, button)) {
			return;
		}
		if(Prayer.toggleQuickPrayer(player, button)) {
			return;
		}
		if(EnchantCrossbowBolts.openInterface(player, button) || EnchantCrossbowBolts.enchant(player, button)) {
			return;
		}
		if(Emote.handle(player, button)) {
			return;
		}
		if(Skillcape.handle(player, button)) {
			return;
		}
		if(BowCarving.fletch(player, button)) {
			return;
		}
		if(Summoning.withdrawAll(player, button)) {
			return;
		}
		if(Smelting.smelt(player, button)) {
			return;
		}
		if(Glassblowing.blow(player, button)) {
			return;
		}
		if(Tanning.create(player, button)) {
			return;
		}
		if(DoughCreation.create(player, button)) {
			return;
		}
		if(Spinning.create(player, button)) {
			return;
		}
		if(PotClaying.create(player, button)) {
			return;
		}
		if(HideWorking.create(player, button)) {
			return;
		}
		if(LeatherWorking.create(player, button)) {
			return;
		}
		if(ExchangeSessionManager.get().buttonClickAction(player, button)) {
			return;
		}
		if(Dialogues.executeOptionListeners(player, button)) {
			return;
		}
		if(SkillData.sendEnterGoalLevel(player, button)) {
			return;
		}
		if(Slayer.clickButton(player, button)) {
			return;
		}
		//Bank 100-109
		if(button >= 100 && button <= 109) {
			player.getBank().setTab(button - 100);
		}
		switch(button) {
			case 55095:
				Item item = player.getInventory().get(player.getAttr().get("destroy_item_slot").getInt());
				player.getInventory().remove(item);
				
				player.getAttr().get("destroy_item_slot").set(-1);
				player.closeWidget();
				break;
			case 55096:
				player.closeWidget();
				break;
			case 195212:
				if(player.getClan().isPresent())
					if(player.getClan().get().getRank().getValue() >= player.getClan().get().getClan().getLowest().getValue())
						player.widget(-3);
					else
						player.getClan().get().sendMessage("You don't have the requirements to do that.");
				else
					player.out(new SendEnterName("Your clan chat name:", t -> () -> ClanManager.get().create(player, t)));
				break;
			case 83093:
				player.widget(15106);
				break;
			case 195209:
				if(player.getClan().isPresent())
					ClanManager.get().exit(player);
				else
					player.out(new SendEnterName("Enter the name of the chat you wish to join.", s -> () -> ClanManager.get().join(player, s)));
				break;
			
			case 59135:
				if(player.getViewingOrb() != null)
					player.getViewingOrb().move("Centre", 15239, player.getViewingOrb().getCentre());
				break;
			case 59136:
				if(player.getViewingOrb() != null)
					player.getViewingOrb().move("North-West", 15240, player.getViewingOrb().getNorthWest());
				break;
			case 59137:
				if(player.getViewingOrb() != null)
					player.getViewingOrb().move("North-East", 15241, player.getViewingOrb().getNorthEast());
				break;
			case 59138:
				if(player.getViewingOrb() != null)
					player.getViewingOrb().move("South-East", 15242, player.getViewingOrb().getSouthEast());
				break;
			case 59139:
				if(player.getViewingOrb() != null)
					player.getViewingOrb().move("South-West", 15243, player.getViewingOrb().getSouthWest());
				break;
			case 17111:
				if(player.getViewingOrb() != null) {
					player.getViewingOrb().close();
					player.setViewingOrb(null);
				}
				break;
			case 53152:
				CookingData cookingData = (CookingData) player.getAttr().get("cooking_data").get();
				if(cookingData != null) {
					Cooking cooking = new Cooking(player, (GameObject) player.getAttr().get("cooking_object").get(), cookingData, (Boolean) player.getAttr().get("cooking_usingStove").get(), 1);
					cooking.start();
				}
				break;
			case 53151:
				CookingData cookingData1 = (CookingData) player.getAttr().get("cooking_data").get();
				if(cookingData1 != null) {
					Cooking cooking = new Cooking(player, (GameObject) player.getAttr().get("cooking_object").get(), cookingData1, (Boolean) player.getAttr().get("cooking_usingStove").get(), 5);
					cooking.start();
				}
				break;
			case 53149:
				CookingData cookingData2 = (CookingData) player.getAttr().get("cooking_data").get();
				if(cookingData2 != null) {
					int amount = player.getInventory().computeAmountForId(cookingData2.getRawId());
					Cooking cooking = new Cooking(player, (GameObject) player.getAttr().get("cooking_object").get(), cookingData2, (Boolean) player.getAttr().get("cooking_usingStove").get(), amount);
					cooking.start();
				}
				break;
			case 100237:
			case 48176:
				Boolean acceptAid = (Boolean) player.getAttr().get("accept_aid").get();
				if(!acceptAid) {
					player.message("Accept aid has been turned on.");
					player.getAttr().get("accept_aid").set(true);
				} else {
					player.message("Accept aid has been turned off.");
					player.getAttr().get("accept_aid").set(false);
				}
				player.out(new SendConfig(427, !acceptAid ? 0 : 1));
				break;
			case 89061:
			case 93202:
				if(!player.isAutoRetaliate()) {
					player.setAutoRetaliate(true);
					player.message("Auto retaliate has been turned on!");
				} else {
					player.setAutoRetaliate(false);
					player.message("Auto retaliate has been turned off!");
				}
				break;
			case 9154:
				if(!MinigameHandler.execute(player, t -> t.canLogout(player)))
					break;
				if(!player.getLastCombat().elapsed(10, TimeUnit.SECONDS)) {
					player.message("You must wait " + (TimeUnit.MILLISECONDS.toSeconds(10_000 - player.getLastCombat().elapsedTime())) + " seconds after combat before logging out.");
					break;
				}
				if(player.getActivityManager().contains(ActivityManager.ActivityType.LOG_OUT)) {
					player.message("You can't log out right now.");
					break;
				}
				player.out(new SendLogout());
				break;
			case 153:
			case 152:
			case 74214:
				if(player.getMovementQueue().isRunning()) {
					player.getMovementQueue().setRunning(false);
				} else {
					if(player.getRunEnergy() <= 0) {
						break;
					}
					player.getMovementQueue().setRunning(true);
				}
				break;
			case 82018:
				player.getBank().depositeInventory();
				break;
			case 231047:
				player.getBank().depositeEquipment();
				break;
			case 231043:
				player.getBank().depositeFamiliar();
				break;
			case 231041:
				player.getAttr().get("withdraw_as_note").set(!(player.getAttr().get("withdraw_as_note").getBoolean()));
				player.out(new SendConfig(115, player.getAttr().get("withdraw_as_note").getBoolean() ? 1 : 0));
				break;
			case 231037:
				//player.getAttr().get("insert_item").set(!(player.getAttr().get("insert_item").getBoolean()));
				player.message("Temporary disabled feature.");
				//player.out(new SendConfig(116, player.getAttr().get("insert_item").getBoolean() ? 1 : 0));
				break;
			case 24017:
			case 7212:
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				break;
			//AUTOCASTING
			case 51133:
			case 50139:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.SMOKE_RUSH, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 51185:
			case 50187:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.SHADOW_RUSH, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 51091:
			case 50101:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.BLOOD_RUSH, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 24018:
			case 50061:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.ICE_RUSH, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 51159:
			case 50163:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.SMOKE_BURST, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 51211:
			case 50211:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.SHADOW_BURST, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 51111:
			case 50119:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.BLOOD_BURST, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 51069:
			case 50081:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.ICE_BURST, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 51146:
			case 50151:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.SMOKE_BLITZ, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 51198:
			case 50199:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.SHADOW_BLITZ, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 51102:
			case 50111:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.BLOOD_BLITZ, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 51058:
			case 50071:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.ICE_BLITZ, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 51172:
			case 50175:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.SMOKE_BARRAGE, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 51224:
			case 50223:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.SHADOW_BARRAGE, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 51122:
			case 50129:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.BLOOD_BARRAGE, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 51080:
			case 50091:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.ICE_BARRAGE, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 7038:
			case 4128:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.WIND_STRIKE, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 7039:
			case 4130:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.WATER_STRIKE, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 7040:
			case 4132:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.EARTH_STRIKE, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 7041:
			case 4134:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.FIRE_STRIKE, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 7042:
			case 4136:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.WIND_BOLT, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 7043:
			case 4139:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.WATER_BOLT, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 7044:
			case 4142:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.EARTH_BOLT, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 7045:
			case 4145:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.FIRE_BOLT, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 7046:
			case 4148:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.WIND_BLAST, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 7047:
			case 4151:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.WATER_BLAST, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 7048:
			case 4153:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.EARTH_BLAST, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 7049:
			case 4157:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.FIRE_BLAST, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 7050:
			case 4159:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.WIND_WAVE, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 7051:
			case 4161:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.WATER_WAVE, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 7052:
			case 4164:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.EARTH_WAVE, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 7053:
			case 4165:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.FIRE_WAVE, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 4129:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.CONFUSE, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 4133:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.WEAKEN, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 4137:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.CURSE, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 6036:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.BIND, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 6003:
//				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.IBAN_BLAST, false));
//				player.setAutocast(true);
//				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
//				player.out(new SendConfig(108, 3));
				player.out(new SendMessage("NEED IBAN BLAST")); // TODO: Iban blast
				break;
			case 47005:
//				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.MAGIC_DART, false));
//				player.setAutocast(true);
//				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
//				player.out(new SendConfig(108, 3));
				player.out(new SendMessage("NEED MAGIC DART")); // TODO: Magic dart
				break;
			case 4166:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.SARADOMIN_STRIKE, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 4167:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.CLAWS_OF_GUTHIX, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 4168:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.FLAMES_OF_ZAMORAK, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 6006:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.VULNERABILITY, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 6007:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.ENFEEBLE, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 6056:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.ENTANGLE, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 6026:
				player.getCombat().setStrategy(new PlayerMagicStrategy(MagicSpell.STUN, false));
				player.setAutocast(true);
				TabInterface.ATTACK.sendInterface(player, player.getWeapon().getId());
				player.out(new SendConfig(108, 3));
				break;
			case 48147:
			case 48157:
			case 48167:
				player.message("Cannot autocast this.");
				break;
			case 94047:
				player.message("No support for training defense with magic yet.");
				break;
			case 26010:
				player.setAutocast(false);
				player.out(new SendConfig(108, 0));
				break;
			case 1093:
			case 1094:
			case 1097:
				if(player.isAutocast()) {
					player.setAutocast(false);
					player.out(new SendConfig(108, 0));
				} else if(!player.isAutocast()) {
					Item staff = player.getEquipment().get(Equipment.WEAPON_SLOT);
					if(staff != null && staff.getId() == 4675) {
						if(!player.getSpellbook().equals(Spellbook.ANCIENT)) {
							player.message("You can only autocast ancient magics with this staff.");
							break;
						}
						
						TabInterface.ATTACK.sendInterface(player, 1689);
					} else {
						if(!player.getSpellbook().equals(Spellbook.NORMAL)) {
							player.message("You can only autocast standard magics with this staff.");
							break;
						}
						
						TabInterface.ATTACK.sendInterface(player, 1829);
					}
				}
				break;
			//SPECIALS
			case 29038:
			case 29063:
			case 29113:
			case 29188:
			case 29213:
			case 48023:
			case 7462:
			case 7512:
			case 12311:
			case 7562:
			case 7537:
			case 7788:
			case 7498:
			case 8481:
			case 7662:
			case 7667:
			case 7687:
			case 7587:
			case 7612:
			case 7623:
			case 7473:
			case 12322:
			case 29138:
			case 29163:
			case 29199:
			case 29074:
			case 33033:
			case 29238:
			case 30007:
			case 30108:
			case 48034:
			case 29049:
			case 30043:
			case 29124:
				if(player.getCombatSpecial() == null) {
					break;
				}
				
				if(!MinigameHandler.execute(player, m -> m.canUseSpecialAttacks(player, player.getCombatSpecial()))) {
					break;
				}

				if(player.isSpecialActivated()) {
					player.out(new SendConfig(301, 0));
					player.setSpecialActivated(false);
					WeaponInterface.setStrategy(player);
				} else {
					if (player.getSpecialPercentage().intValue() < player.getCombatSpecial().getAmount()) {
						player.message("You do not have enough special energy left!");
						break;
					}
					player.setSpecialActivated(true);
					player.out(new SendConfig(301, 1));
					player.getCombatSpecial().enable(player);
				}
				break;
		}
		player.getActivityManager().execute(ActivityManager.ActivityType.CLICK_BUTTON);
	}
}
