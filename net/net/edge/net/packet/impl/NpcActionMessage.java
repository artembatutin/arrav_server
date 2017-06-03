package net.edge.net.packet.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.database.connection.use.Donating;
import net.edge.net.packet.PacketReader;
import net.edge.util.Utility;
import net.edge.world.Graphic;
import net.edge.content.combat.magic.CombatSpells;
import net.edge.content.dialogue.Expression;
import net.edge.content.dialogue.convo.CulinaromancerConversation;
import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.dialogue.impl.PlayerDialogue;
import net.edge.content.dialogue.impl.StatementDialogue;
import net.edge.content.dialogue.test.DialogueAppender;
import net.edge.content.item.Skillcape;
import net.edge.content.market.MarketCounter;
import net.edge.content.minigame.MinigameHandler;
import net.edge.content.pets.Pet;
import net.edge.content.shootingstar.StarSprite;
import net.edge.content.skill.Skills;
import net.edge.content.skill.crafting.Tanning;
import net.edge.content.skill.fishing.Fishing;
import net.edge.content.skill.fishing.Tool;
import net.edge.content.skill.hunter.butterfly.ButterflyCatching;
import net.edge.content.skill.slayer.Slayer;
import net.edge.content.skill.summoning.Summoning;
import net.edge.content.skill.thieving.impl.Pickpocketing;
import net.edge.content.teleport.impl.AuburyTeleport;
import net.edge.locale.Boundary;
import net.edge.locale.loc.Location;
import net.edge.locale.Position;
import net.edge.world.Animation;
import net.edge.world.World;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.npc.NpcDefinition;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;
import net.edge.world.node.entity.player.assets.activity.ActivityManager;

import java.util.Optional;

/**
 * The message sent from the client when a player attacks or clicks on an NPC.
 * @author lare96 <http://github.com/lare96>
 * @author Artem Batutin <artembatutin@gmail.com
 */
public final class NpcActionMessage implements PacketReader {
	
	@Override
	public void handleMessage(Player player, int opcode, int size, ByteMessage payload) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.NPC_ACTION))
			return;
		switch(opcode) {
			case 72:
				attackOther(player, payload);
				break;
			case 131:
				attackMagic(player, payload);
				break;
			case 155:
				firstClick(player, payload);
				break;
			case 17:
				secondClick(player, payload);
				break;
			case 21:
				thirdClick(player, payload);
				break;
			case 18:
				fourthClick(player, payload);
				break;
		}
		player.getActivityManager().execute(ActivityManager.ActivityType.NPC_ACTION);
	}
	
	/**
	 * Handles the melee and ranged attacks on an NPC.
	 * @param player  the player this will be handled for.
	 * @param payload the payload that will read the sent data.
	 */
	private void attackOther(Player player, ByteMessage payload) {
		int index = payload.getShort(false, ByteTransform.A);
		Npc npc = World.get().getNpcs().get(index - 1);
		if(npc == null || !checkAttack(player, npc))
			return;
		player.getTolerance().reset();
		player.getCombatBuilder().attack(npc);
	}
	
	/**
	 * Handles the magic attacks on an NPC.
	 * @param player  the player this will be handled for.
	 * @param payload the payload that will read the sent data.
	 */
	private void attackMagic(Player player, ByteMessage payload) {
		int index = payload.getShort(true, ByteTransform.A, ByteOrder.LITTLE);
		int spellId = payload.getShort(true, ByteTransform.A);
		Npc npc = World.get().getNpcs().get(index - 1);
		Optional<CombatSpells> spell = CombatSpells.getSpell(spellId);
		if(npc == null || !spell.isPresent() || !checkAttack(player, npc))
			return;
		player.setCastSpell(spell.get().getSpell());
		player.getTolerance().reset();
		player.getCombatBuilder().attack(npc);
	}
	
	/**
	 * Handles the first click NPC slot.
	 * @param player  the player this will be handled for.
	 * @param payload the payload that will read the sent data.
	 */
	private void firstClick(Player player, ByteMessage payload) {
		int index = payload.getShort(true, ByteOrder.LITTLE);
		Npc npc = World.get().getNpcs().get(index - 1);
		if(npc == null)
			return;
		final int id = npc.getId();
		Position position = npc.getPosition().copy();
		player.getMovementListener().append(() -> {
			if(new Boundary(position, npc.size()).within(player.getPosition(), player.size(), 1)) {
				player.facePosition(npc.getPosition());
				npc.facePosition(player.getPosition());
				if(!MinigameHandler.execute(player, m -> m.onFirstClickNpc(player, npc))) {
					return;
				}
				if(World.getScoreboardManager().claimPlayerScoreboardRewards(player, npc)) {
					return;
				}
				if(Summoning.interact(player, npc, 1)) {
					return;
				}
				if(Pet.pickup(player, npc)) {
					return;
				}
				if(ButterflyCatching.catchButterfly(player, npc)) {
					return;
				}
				if(Slayer.append(player, npc.getId())) {
					return;
				}
				if(StarSprite.interact(player, npc.getId(), 1)) {
					return;
				}
				switch(id) {
					case 6892:
						DialogueAppender pets = new DialogueAppender(player);
						pets.chain(new NpcDialogue(6892, "Hello " + player.getFormatUsername() + ", lovely day isn't it?"));
						pets.chain(new OptionDialogue(t -> {
							if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
								player.getMessages().sendCloseWindows();
								MarketCounter.getShops().get(25).openShop(player);
							} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
								pets.getBuilder().advance();
							} else {
								pets.getBuilder().last();
							}
						}, "Yes, can i buy a pet?", "You don't have what I'm looking for", "Nevermind"));
						pets.chain(new PlayerDialogue(Expression.QUESTIONING, "You don't have much choices", "in your shop do you?"));
						pets.chain(new NpcDialogue(6892, "Suggest other pets on the forums", "if you wish!"));
						pets.chain(new PlayerDialogue("Nevermind..."));
						pets.start();
						break;
					case 3705:
						DialogueAppender night = new DialogueAppender(player);
						night.chain(new NpcDialogue(3705, "Evening " + player.getFormatUsername() + ", what do you want?"));
						night.chain(new OptionDialogue(t -> {
							if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
								night.getBuilder().skip();
							} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
								night.getBuilder().advance();
							} else {
								night.getBuilder().last();
							}
						}, "Who are you?", "I'd like to see your shop", "Nevermind"));
						night.chain(new PlayerDialogue("I'd like to see your shop.").attachAfter(() -> {
							player.getMessages().sendCloseWindows();
							MarketCounter.getShops().get(24).openShop(player);
						}));
						night.chain(new PlayerDialogue("Who are you?"));
						night.chain(new NpcDialogue(3705, "I am the Night's watch captain, it is thanks to me", "that the nightmare mode is available for players like you.", "Oh and I also sell interesting items in my shop..."));
						night.chain(new NpcDialogue(3705, "Would you perhaps want to take a look?"));
						night.chain(new OptionDialogue(t -> {
							if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
								night.getBuilder().skip();
							} else {
								night.getBuilder().advance();
							}
						}, "Sure", "No"));
						night.chain(new PlayerDialogue("No thank you...").attachAfter(() -> player.getMessages().sendCloseWindows()));
						night.chain(new PlayerDialogue("Yeah sure...").attachAfter(() -> {
							player.getMessages().sendCloseWindows();
							MarketCounter.getShops().get(24).openShop(player);
						}));
						night.chain(new PlayerDialogue("Nevermind..."));
						night.start();
						break;
					case 669:
						DialogueAppender a = new DialogueAppender(player);
						a.chain(new NpcDialogue(669, "Hey " + player.getFormatUsername() + ", what do you need?"));
						a.chain(new OptionDialogue(t -> {
							if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
								a.getBuilder().skip();
							} else {
								a.getBuilder().advance();
							}
						}, "Open shop", "Nevermind"));
						a.chain(new PlayerDialogue("Nevermind, I forgot what I wanted to ask.").attachAfter(() -> player.getMessages().sendCloseWindows()));
						a.chain(new PlayerDialogue("Can I see your shop?"));
						a.chain(new NpcDialogue(669, "Ofcourse!").attachAfter(() -> MarketCounter.getShops().get(0).openShop(player)));
						a.start();
						break;
					case 604:
						player.getDialogueBuilder().append(new NpcDialogue(604, "Beautiful day in this dark cave, isn't it?"));
						break;
					case 4946:
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
						}, "What is this place?", active ? "Howlong till the event ends?" : "Howmany logs do the fire pits have?", "Do you sell anything?", "Nevermind"));
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
						break;
					case 5913:
						player.getDialogueBuilder().append(new NpcDialogue(5913, "Hello " + player.getFormatUsername() + ", I am Aubury."), new OptionDialogue(t -> {
							if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
								player.getDialogueBuilder().advance();
							} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
								player.getDialogueBuilder().go(3);
							} else {
								player.getDialogueBuilder().last();
							}
						}, "What do you do?", "Teleport me to the Essence Mine.", "Nevermind."), new PlayerDialogue("What do you do?"), new NpcDialogue(5913, "What I do you ask? I can do anything, but my powers are", "limited for Avarrockians, for you all I can do is teleport", "you to the Essence mine or sell you the mastery skillcape", "of Runecrafting.").attachAfter(() -> player.getMessages().sendCloseWindows()), new PlayerDialogue("Can you teleport me to the essence mine?"), new NpcDialogue(5913, "Ofcourse I can!").attachAfter(() -> AuburyTeleport.move(player, npc)), new PlayerDialogue("Nevermind."));
						break;
					case 5113:
						player.getDialogueBuilder().append(new NpcDialogue(5113, "Hey there, welcome to my hunting area."), new OptionDialogue(t -> {
							if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
								player.getDialogueBuilder().advance();
							} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
								player.getDialogueBuilder().go(6);
							} else {
								player.getDialogueBuilder().last();
							}
						}, "What is this place?", "Can I see your shop?", "Nevermind."), new PlayerDialogue("What is this place?"), new NpcDialogue(5113, "This is the hunting area, my hunting area to be specific.", "Here you can try to hunt for all sorts of animals."), new PlayerDialogue("And you won't kill me for catching your animals?"), new NpcDialogue(5113, "Nope, I won't kill you for catching my animals, I have", "put them here so people can train off of them."), new PlayerDialogue("Alright, I better get starting!").attachAfter(() -> player.getMessages().sendCloseWindows()), new PlayerDialogue("Can I see your shop?"), new NpcDialogue(5113, "Ofcourse you can!").attachAfter(() -> {
							player.getMessages().sendCloseWindows();
							MarketCounter.getShops().get(4).openShop(player);
						}), new PlayerDialogue("Nevermind."));
						break;
					case 3400:
						player.getDialogueBuilder().send(new CulinaromancerConversation());
						break;
					case 659://party guy
						player.getDialogueBuilder().append(new NpcDialogue(659, Expression.HAPPY, "Hello Adventurer, what do you seek from me", "today?"), new OptionDialogue(t -> {
							if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
								player.getDialogueBuilder().advance();
							} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
								player.getMessages().sendLink("pay");
								player.getDialogueBuilder().last();
							} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
								new Donating(player, World.getDonation());
								player.getMessages().sendCloseWindows();
							} else {
								player.getMessages().sendCloseWindows();
							}
						}, "I'd like to see the donator shop.", "I would like to buy some edge tokens", "I wan't to claim my edge tokens", "Nevermind."), new PlayerDialogue("I'd like to see the donator shop.").attachAfter(() -> {
							player.getMessages().sendCloseWindows();
							MarketCounter.getShops().get(1).openShop(player);
						}), new NpcDialogue(659, "You can buy credits from here.", "Come back to me to claim the credits once", "payment is done. Thank you."));
						break;
					case 2270:
						MarketCounter.getShops().get(26).openShop(player);
						break;
					case 4657:
						MarketCounter.getShops().get(23).openShop(player);
						break;
					case 847:
						MarketCounter.getShops().get(6).openShop(player);
						break;
					case 805:
						MarketCounter.getShops().get(7).openShop(player);
						break;
					case 3295:
						MarketCounter.getShops().get(11).openShop(player);
						break;
					case 4901:
						MarketCounter.getShops().get(18).openShop(player);
						break;
					case 4906:
						MarketCounter.getShops().get(21).openShop(player);
						break;
					case 6180:
						MarketCounter.getShops().get(8).openShop(player);
						break;
					case 587:
						MarketCounter.getShops().get(9).openShop(player);
						break;
					case 455:
						MarketCounter.getShops().get(10).openShop(player);
						break;
					case 1281:
						MarketCounter.getShops().get(20).openShop(player);
						break;
					case 551:
					case 552:
						MarketCounter.getShops().get(12).openShop(player);
						break;
					case 494:
					case 7605:
						player.getBank().open();
						break;
					case 960://Healers
					case 959:
					case 962:
						player.heal();
						break;
					case 599://makeover mage
						player.getMessages().sendInterface(3559);
						break;
					case 233:
					case 234:
					case 235:
					case 236:
						Fishing fishing = new Fishing(player, Tool.FISHING_ROD, position);
						fishing.start();
						break;
					case 309:
					case 310:
					case 311:
					case 314:
					case 315:
					case 317:
					case 318:
						Fishing fly_fishing = new Fishing(player, Tool.FLY_FISHING_ROD, position);
						fly_fishing.start();
						break;
					case 312:
						Fishing lobster_pot = new Fishing(player, Tool.LOBSTER_POT, position);
						lobster_pot.start();
						break;
					case 313:
						Fishing big_net = new Fishing(player, Tool.BIG_NET, position);
						big_net.start();
						break;
					case 316:
					case 319:
						Fishing net = new Fishing(player, Tool.NET, position);
						net.start();
						break;
					case 13926://SPECIALIST MASTER
						break;
					default:
						if(player.getRights().greater(Rights.ADMINISTRATOR)) {
							player.message("[Action 1]Npc Id = " + id);
						}
						break;
				}
			}
		});
	}
	
	/**
	 * Handles the second click NPC slot.
	 * @param player  the player this will be handled for.
	 * @param payload the payload that will read the sent data.
	 */
	private void secondClick(Player player, ByteMessage payload) {
		int index = payload.getShort(false, ByteTransform.A, ByteOrder.LITTLE);
		Npc npc = World.get().getNpcs().get(index - 1);
		if(npc == null)
			return;
		final int id = npc.getId();
		Position position = npc.getPosition().copy();
		player.getMovementListener().append(() -> {
			if(new Boundary(position, npc.size()).within(player.getPosition(), player.size(), 1)) {
				player.facePosition(npc.getPosition());
				
				if(!MinigameHandler.execute(player, m -> m.onSecondClickNpc(player, npc))) {
					return;
				}
				
				/** don't move this code, because npcs dont face back if the player successfully pickpockets. */
				if(Pickpocketing.steal(player, npc)) {
					return;
				}
				npc.facePosition(player.getPosition());
				if(Skillcape.reward(player, npc.getId(), 2)) {
					return;
				}
				if(Summoning.interact(player, npc, 2)) {
					return;
				}
				
				if(StarSprite.interact(player, npc.getId(), 2)) {
					return;
				}
				
				switch(id) {
					case 6892:
						MarketCounter.getShops().get(25).openShop(player);
						break;
					case 3705:
						MarketCounter.getShops().get(24).openShop(player);
						break;
					case 7605:
						MarketCounter.getShops().get(22).openShop(player);
						break;
					
					case 669://hazelmere
						MarketCounter.getShops().get(0).openShop(player);
						break;
					case 659://party guy
						MarketCounter.getShops().get(1).openShop(player);
						break;
					case 8462://slayer
						Slayer.openPanel(player);
						break;
					case 3400:
						MarketCounter.getShops().get(3).openShop(player);
						break;
					case 551:
					case 552:
						MarketCounter.getShops().get(12).openShop(player);
						break;
					case 587:
						MarketCounter.getShops().get(9).openShop(player);
						break;
					case 494:
					case 2380:
					case 375:
						player.getBank().open();
						break;
					case 960://Healers
					case 959:
					case 962:
						player.heal();
						break;
					case 309:
					case 310:
					case 311:
					case 314:
					case 315:
					case 317:
					case 316:
					case 318:
					case 319:
						Fishing fishing_rod = new Fishing(player, Tool.FISHING_ROD, position);
						fishing_rod.start();
						break;
					case 312:
						Fishing harpoon = new Fishing(player, Tool.HARPOON, position);
						harpoon.start();
						break;
					case 313:
						Fishing shark_harpoon = new Fishing(player, Tool.SHARK_HARPOON, position);
						shark_harpoon.start();
						break;
					default:
						if(player.getRights().greater(Rights.ADMINISTRATOR)) {
							player.message("[Action 2]Npc Id = " + id);
						}
						break;
				}
			}
		});
	}
	
	/**
	 * Handles the third click NPC slot.
	 * @param player  the player this will be handled for.
	 * @param payload the payload that will read the sent data.
	 */
	private void thirdClick(Player player, ByteMessage payload) {
		int index = payload.getShort(true);
		Npc npc = World.get().getNpcs().get(index - 1);
		if(npc == null)
			return;
		final int id = npc.getId();
		Position position = npc.getPosition().copy();
		player.getMovementListener().append(() -> {
			if(new Boundary(position, npc.size()).within(player.getPosition(), player.size(), 1)) {
				player.facePosition(npc.getPosition());
				npc.facePosition(player.getPosition());
				if(Summoning.interact(player, npc, 3)) {
					return;
				}
				if(Skillcape.reward(player, npc.getId(), 3)) {
					return;
				}
				switch(id) {
					case 494:
						player.getBank().open();
						break;
					default:
						if(player.getRights().greater(Rights.ADMINISTRATOR)) {
							player.message("[Action 3]Npc Id = " + id);
						}
						break;
				}
			}
		});
	}
	
	/**
	 * Handles the fourth click NPC slot.
	 * @param player  the player this will be handled for.
	 * @param payload the payload that will read the sent data.
	 */
	private void fourthClick(Player player, ByteMessage payload) {
		int index = payload.getShort(true, ByteOrder.LITTLE);
		Npc npc = World.get().getNpcs().get(index - 1);
		if(npc == null)
			return;
		final int id = npc.getId();
		Position position = npc.getPosition();
		player.getMovementListener().append(() -> {
			if(new Boundary(position, npc.size()).within(player.getPosition(), player.size(), 1)) {
				player.facePosition(npc.getPosition());
				npc.facePosition(player.getPosition());
				
				if(Summoning.interact(player, npc, 4)) {
					return;
				}
				
				switch(id) {
					case 3705:
						DialogueAppender ap = new DialogueAppender(player);
						boolean maxed = Skills.maxed(player);
						String[] message = maxed ? new String[]{"Very well warrior, I see that you've accomplished maxing", "out all of your skills. Would you like to prestige to remove",  "your restrictions?"} : new String[]{"You have not yet maxed all of your skills warrior."};
						ap.chain(new NpcDialogue(3705, message).attachAfter(() -> {
							if(!maxed) {
								player.getMessages().sendCloseWindows();
							}
						}));
						ap.chain(new OptionDialogue(t -> {
							if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
								ap.getBuilder().skip();
							} else {
								ap.getBuilder().advance();
							}
						}, "Yes please", "No thanks"));
						ap.chain(new PlayerDialogue("No thanks...").attachAfter(() -> player.getMessages().sendCloseWindows()));
						ap.chain(new PlayerDialogue("Yes, please..."));
						ap.chain(new NpcDialogue(3705, "Very well, you've been prestiged and your restrictions have", "been removed...").attach(() -> {
							player.graphic(new Graphic(2189));
							player.setNight(2);
						}));
						ap.start();
						break;
					case 805:
						Tanning.openInterface(player);
						break;
					case 604:
						MarketCounter.getShops().get(19).openShop(player);
						break;
					case 8462:
						MarketCounter.getShops().get(2).openShop(player);
						break;
					case 5113:
						MarketCounter.getShops().get(4).openShop(player);
						break;
					case 961:
						player.heal();
						break;
					case 5913:
						AuburyTeleport.move(player, npc);
						break;
					case 5917:
						player.animation(new Animation(2110));
						player.forceChat("Thbbbbt!");
						npc.forceChat("Whine!");
						Optional<Position> move = World.getTraversalMap().getRandomTraversableTile(npc.getPosition(), npc.size(), player.getPosition());
						move.ifPresent(position1 -> npc.getMovementQueue().walk(position1));
						break;
					default:
						if(player.getRights().greater(Rights.ADMINISTRATOR)) {
							player.message("[Action 4]Npc Id = " + id);
						}
						break;
				}
			}
		});
	}
	
	/**
	 * Determines if {@code player} can make an attack on {@code npc}.
	 * @param player the player attempting to make an attack.
	 * @param npc    the npc being attacked.
	 * @return {@code true} if the player can make an attack, {@code false}
	 * otherwise.
	 */
	private boolean checkAttack(Player player, Npc npc) {
		if(!NpcDefinition.DEFINITIONS[npc.getId()].isAttackable())
			return false;
		if(!Location.inMultiCombat(player) && player.getCombatBuilder().isBeingAttacked() && !npc.equals(player.getCombatBuilder().getAggressor())) {
			player.message("You are already under attack!");
			return false;
		}
		if(!Slayer.canAttack(player, npc)) {
			return false;
		}
		return true;
	}
}
