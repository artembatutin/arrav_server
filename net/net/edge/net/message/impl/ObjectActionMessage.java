package net.edge.net.message.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.net.message.InputMessageListener;
import net.edge.task.LinkedTaskSequence;
import net.edge.GameConstants;
import net.edge.World;
import net.edge.content.ViewingOrb;
import net.edge.content.WebSlashing;
import net.edge.content.combat.weapon.WeaponInterface;
import net.edge.content.dialogue.Dialogue;
import net.edge.content.dialogue.Expression;
import net.edge.content.dialogue.impl.*;
import net.edge.content.dialogue.test.DialogueAppender;
import net.edge.content.door.DoorHandler;
import net.edge.content.market.MarketCounter;
import net.edge.content.minigame.MinigameHandler;
import net.edge.content.minigame.fightcaves.FightcavesMinigame;
import net.edge.content.minigame.warriorsguild.WarriorsGuild;
import net.edge.content.skill.Skill;
import net.edge.content.skill.Skills;
import net.edge.content.skill.firemaking.Bonfire;
import net.edge.content.teleport.impl.DefaultTeleportSpell;
import net.edge.content.wilderness.Obelisk;
import net.edge.locale.Boundary;
import net.edge.locale.Position;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.node.entity.move.ForcedMovement;
import net.edge.world.node.entity.move.ForcedMovementDirection;
import net.edge.world.node.entity.move.ForcedMovementManager;
import net.edge.world.node.entity.npc.impl.gwd.GodwarsFaction;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.PrayerBook;
import net.edge.world.node.entity.player.assets.Rights;
import net.edge.world.node.entity.player.assets.Spellbook;
import net.edge.world.node.entity.player.assets.activity.ActivityManager;
import net.edge.world.node.item.Item;
import net.edge.world.object.ObjectNode;

import java.util.Optional;

import static net.edge.content.teleport.impl.DefaultTeleportSpell.TeleportType.*;

/**
 * The message sent from the client when a player clicks an object.
 * @author lare96 <http://github.com/lare96>
 */
public final class ObjectActionMessage implements InputMessageListener {
	
	@Override
	public void handleMessage(Player player, int opcode, int size, ByteMessage payload) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.OBJECT_ACTION))
			return;
		
		switch(opcode) {
			case 132:
				firstClick(player, payload);
				break;
			case 252:
				secondClick(player, payload);
				break;
			case 70:
				thirdClick(player, payload);
				break;
			case 234:
				fourthClick(player, payload);
				break;
			case 228:
				fifthClick(player, payload);
				break;
			case 35:
				spellObject(player, payload);
				break;
		}
		player.getActivityManager().execute(ActivityManager.ActivityType.OBJECT_ACTION);
	}
	
	/**
	 * Handles the first slot object click for the {@code player}.
	 * @param player  the player to handle this for.
	 * @param payload the payload for reading the sent data.
	 */
	private void firstClick(Player player, ByteMessage payload) {
		//Getting data.
		int objectId = payload.getMedium();
		int objectX = payload.getShort(false);
		int objectY = payload.getShort(false);
		int objectZ = player.getPosition().getZ();
		//Validating data.
		Position position = new Position(objectX, objectY, player.getPosition().getZ());
		if(objectId < 0 || objectX < 0 || objectY < 0)
			return;
		Optional<ObjectNode> o = World.getRegions().getRegion(position).getObject(objectId, position.toLocalPacked());
		if(!o.isPresent())
			return;
		player.facePosition(position);
		final ObjectNode object = o.get();
		if(player.getRights().greater(Rights.ADMINISTRATOR))
			player.message("[OBJ-1]:" + o.get().toString());
		player.getMovementListener().append(() -> {
			if(objectId == 85584 || objectId == 85532 || objectId == 85534 ||//agility
					new Boundary(position, object.getDefinition().getSize()).within(player.getPosition(), player.size(), 1)) {
				if(!MinigameHandler.execute(player, m -> m.onFirstClickObject(player, object))) {
					return;
				}
				if(WebSlashing.slash(player, object)) {
					return;
				}
				if(WarriorsGuild.enterCyclopsRoom(player, object)) {
					return;
				}
				if(World.getShootingStarEvent().mine(player, objectId)) {
					return;
				}
				if(WarriorsGuild.enterAnimationRoom(player, object)) {
					return;
				}
				if(Skills.executeObjectantSkills(player, object, 1)) {
					return;
				}
				if(GodwarsFaction.enterChamber(player, objectId)) {
					return;
				}
				if(DoorHandler.interact(player, object)) {
					return;
				}
				if(FightcavesMinigame.enter(player, object)) {
					return;
				}
				if(Obelisk.activate(player, object)) {
					return;
				}
				if(Bonfire.addLogs(player, new Item(-100), object, true)) {
					return;
				}
				switch(objectId) {
					case 44566:
						DialogueAppender app = new DialogueAppender(player);
						app.chain(new StatementDialogue("You investigate the mysterious cape that is hanging on the rack..."));
						boolean maxed = !Skills.maxed(player);
						Dialogue dialogue = maxed ? new OptionDialogue(t -> {
							if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
								app.getBuilder().advance();
							} else {
								player.getMessages().sendCloseWindows();
							}
						}, "Claim max cape for 100,000 coins", "Nevermind") : new PlayerDialogue(Expression.CONFUSED, "A strange feeling is telling me I do not meet", "the requirements for this cape.");
						app.chain(dialogue);
						app.chain(new RequestItemDialogue(new Item(995, 100_000), Optional.of(new Item(20769)), "You offer your coins up and receive the cape in return.", Optional.empty(), true));
						app.start();
						break;
					case 29947:
						player.message("Summoning will be released soon.");
						break;
					case 24124:
						player.getMessages().sendInterface(-13);
						break;
					case 38811:
						if(player.getPosition().getX() == 2970) {//entering
							player.move(new Position(2974, player.getPosition().getY(), 2));
						} else {
							player.move(new Position(2970, player.getPosition().getY(), 2));
						}
						break;
					case 6839:
						MarketCounter.getShops().get(5).openShop(player);
						break;
					case 52232:
						player.move(new Position(1910, 4367));
						break;
					case 10230:
						player.animation(new Animation(827));
						LinkedTaskSequence seq2 = new LinkedTaskSequence();
						seq2.connect(2, () -> player.move(new Position(2899, 4449)));
						seq2.start();
						break;
					case 30205:
						World.getScoreboardManager().sendPlayerScoreboardStatistics(player);
						break;
					case 2522:
						player.getDialogueBuilder().append(new OptionDialogue(t -> {
							if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
								player.getMessages().sendInterface(-9);
							} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
								player.getMessages().sendInterface(-7);
							} else {
								player.getMessages().sendCloseWindows();
							}
						}, "@red@This will be clan bossing hub soon!", "Monsters", "Bosses"));
						break;
					case 28139:
						player.getDialogueBuilder().append(new OptionDialogue(t -> {
							if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
								player.getMessages().sendInterface(-4);
							} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
								player.getMessages().sendInterface(-6);
							} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
								player.getMessages().sendInterface(-9);
							} else if(t.equals(OptionDialogue.OptionType.FOURTH_OPTION)) {
								player.getMessages().sendInterface(-7);
							}
						}, "Skills", "Minigames", "Monsters", "Bosses"));
						break;
					case 26439:
						player.getActivityManager().disable();
						boolean north = player.getPosition().getY() < 5334;
						LinkedTaskSequence seq = new LinkedTaskSequence();
						seq.connect(2, () -> player.getMovementQueue().walk(new Position(2885, north ? 5333 : 5344, 2)));
						seq.connect(2, () -> player.getMessages().sendFade(130, 80, 120));
						seq.connect(1, () -> {
							ForcedMovement movement = new ForcedMovement(player);
							movement.setSecond(new Position(2885, north ? 5334 : 5343, 2));
							movement.setSecondSpeed(50);
							movement.setAnimation(3067);
							movement.submit();
						});
						seq.connect(2, () -> {
							player.animation(new Animation(772));
							player.graphic(new Graphic(68));
						});
						seq.connect(4, () -> player.move(new Position(2885, north ? 5345 : 5332, 2)));
						seq.connect(1, () -> {
							player.getActivityManager().enable();
							player.getSkills()[Skills.PRAYER].setLevel(0, false);
							Skills.refresh(player, Skills.PRAYER);
						});
						seq.start();
						break;
					case 68306:
						if(!player.getEquipment().contains(9419)) {
							player.message("You need to wield a mithril grapple to cross this.");
							return;
						}
						if(!player.getWeapon().equals(WeaponInterface.CROSSBOW)) {
							player.message("You need to wield a crossbow to fire a mithril grapple.");
							return;
						}
						ForcedMovement movement = new ForcedMovement(player);
						movement.setFirstSpeed(95);
						movement.setAnimation(7081);
						movement.setSecondSpeed(120);
						if(player.getPosition().getY() < object.getY()) {
							movement.setDirection(ForcedMovementDirection.NORTH);
							movement.setFirst(new Position(2872, 5269, 2));
							movement.setSecond(new Position(2872, 5279, 2));
						} else {
							movement.setDirection(ForcedMovementDirection.SOUTH);
							movement.setFirst(new Position(2872, 5279, 2));
							movement.setSecond(new Position(2872, 5269, 2));
						}
						ForcedMovementManager.submit(player, movement);
						break;
					case 2273:
						if(objectX == 3005 && objectY == 3963 && objectZ == 0)//wilderness agility
							player.teleport(GameConstants.STARTING_POSITION, DefaultTeleportSpell.TeleportType.OBELISK);
						if(objectX == 2996 && objectY == 9823 && objectZ == 0)//to rune essence
							player.teleport(new Position(2922, 4819, 0), DefaultTeleportSpell.TeleportType.FREEZE);
						if(objectX == 2922 && objectY == 4819 && objectZ == 0)//from rune essence
							player.teleport(new Position(2996, 9823, 0), DefaultTeleportSpell.TeleportType.FREEZE);
						break;
					case 23271://Edgeville ditch.
						ForcedMovement jump;
						int wild = 3;
						if(player.getPosition().getX() < 3000 && player.getPosition().getX() > 2994) {
							if(player.getPosition().getX() > 2997)
								wild = -3;
							jump = ForcedMovement.create(player, player.getPosition().move(wild, 0), new Animation(6132));
						} else {
							if(player.getPosition().getY() > 3520)
								wild = -3;
							jump = ForcedMovement.create(player, player.getPosition().move(0, wild), new Animation(6132));
						}
						jump.setSecondSpeed(50);
						ForcedMovementManager.submit(player, jump);
						break;
					case 9391:
						player.setViewingOrb(new ViewingOrb(player, new Position(2398, 5150), new Position(2384, 5157), new Position(2409, 5158), new Position(2388, 5138), new Position(2411, 5137)));
						player.getViewingOrb().open();
						break;
					case 3193:
					case 2213:
					case 34752:
					case 26972:
					case 30928:
					case 19230:
					case 11402:
					case 72931:
					case 35647:
					case 44216:
						player.getBank().open();
						break;
					case 409:
						int level = player.getSkills()[Skills.PRAYER].getRealLevel();
						if(player.getSkills()[Skills.PRAYER].getLevel() < level) {
							player.animation(new Animation(645));
							player.getSkills()[Skills.PRAYER].setLevel(level, true);
							player.message("You recharge your prayer points.");
							Skills.refresh(player, Skills.PRAYER);
						} else {
							player.message("You already have full prayer points.");
						}
						break;
					case 6552:
						player.getDialogueBuilder().append(new OptionDialogue(t -> {
							if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
								if(player.getPrayerBook().equals(PrayerBook.CURSES)) {
									PrayerBook.convert(player, PrayerBook.NORMAL);
									player.getPrayerActive().forEach(p -> {
										if(p.getType() == PrayerBook.CURSES) {
											p.deactivate(player);
										}
									});
								} else if(player.getPrayerBook().equals(PrayerBook.NORMAL)) {
									PrayerBook.convert(player, PrayerBook.CURSES);
									player.getPrayerActive().forEach(p -> {
										if(p.getType() == PrayerBook.NORMAL) {
											p.deactivate(player);
										}
									});
								}
								player.getMessages().sendCloseWindows();
							} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
								player.getDialogueBuilder().advance();
							}
						}, "Prayer switch", "Spell book switch"), new OptionDialogue(t -> {
							if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
								Spellbook.convert(player, Spellbook.NORMAL);
							} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
								Spellbook.convert(player, Spellbook.ANCIENT);
							} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
								Spellbook.convert(player, Spellbook.LUNAR);
							}
							player.getMessages().sendCloseWindows();
						}, "Normal", "Ancient", "Lunar"));
						break;
					//nightmare ladder
					case 34548:
							player.teleport(new Position(3099, 3497, 1), LADDER);
						break;
					case 34550:
						player.teleport(new Position(3102, 3497, 0), LADDER);
						break;
					//home staires
					case 2711:
						if(objectX == 3082 && objectY == 3510 && objectZ == 0) {
							if(!player.getRights().isStaff() && !player.getRights().isDonator()) {
								int total = 0;
								for(Skill s : player.getSkills())
									total += s.getRealLevel();
								if(total < 2000) {
									player.message("You need total level greater than 2000 to go up, or be a donator.");
									return;
								}
							}
							player.teleport(new Position(3081, 3510, 1), LADDER);
						}
						break;
					case 24354:
						player.teleport(new Position(3091, 3478, 1), LADDER);
						break;
					case 24362:
						player.teleport(new Position(3091, 3480, 0), LADDER);
						break;
					case 34499:
						player.teleport(new Position(3084, 3510, 0), LADDER);
						break;
					//home mining ladder
					case 24363:
						if(objectX == 3088 && objectY == 3481 && objectZ == 0)
							player.teleport(new Position(2995, 9826, 0), LADDER);
						break;
					//Brimhaven stairs
					case 5094:
						if(objectX == 2648 && objectY == 9592 && objectZ == 0)
							player.teleport(new Position(2642, 9595, 2), LADDER);
						break;
					case 5096:
						if(objectX == 2644 && objectY == 9593 && objectZ == 2)
							player.teleport(new Position(2649, 9591, 0), LADDER);
						break;
					case 5097:
						if(objectX == 2635 && objectY == 9514 && objectZ == 0)
							player.teleport(new Position(2637, 9510, 2), LADDER);
						break;
					case 5098:
						if(objectX == 2635 && objectY == 9511 && objectZ == 2)
							player.teleport(new Position(2637, 9517, 0), LADDER);
						break;
					
					//ancient cavern dungeon
					case 67342:
						if(objectX == 1778 && objectY == 5344 && objectZ == 0)
							player.move(new Position(1778, 5343, 1));
						break;
					case 67343:
						if(objectX == 1778 && objectY == 5344 && objectZ == 1)
							player.move(new Position(1778, 5346, 0));
						break;
					case 81471:
						if(objectX == 1744 && objectY == 5322 && objectZ == 1)
							player.move(new Position(1745, 5325, 0));
						break;
					case 67340:
						if(objectX == 1744 && objectY == 5323 && objectZ == 0)
							player.move(new Position(1744, 5321, 1));
						break;
					
					case 8972://Woodcutting portal
						if(objectX == 2611 && objectY == 4776 && objectZ == 0)
							player.teleport(new Position(3220, 3433, 0), TRAINING_PORTAL);
						break;
					
					case 1817://king black dragon lever
						if(objectX == 2273 && objectY == 4680 && objectZ == 0)
							player.teleport(new Position(3220, 3430, 0), BOSS_PORTAL);
						break;
					
					//Abyssal rift
					case 7133:
						if(objectX == 3035 && objectY == 4842 && objectZ == 0) {
							player.teleport(new Position(2400, 4850, 0), TRAINING_PORTAL);
						}
						break;
					case 7132:
						if(objectX == 3028 && objectY == 4837 && objectZ == 0) {
							player.teleport(new Position(2142, 4836, 0), TRAINING_PORTAL);
						}
						break;
					case 7141:
						if(objectX == 3027 && objectY == 4834 && objectZ == 0) {
							player.teleport(new Position(2466, 4888, 1));
						}
						break;
					case 7129:
						if(objectX == 3029 && objectY == 4830 && objectZ == 0) {
							player.teleport(new Position(2580, 4843, 0));
						}
						break;
					case 7130:
						if(objectX == 3031 && objectY == 4825 && objectZ == 0) {
							player.teleport(new Position(2659, 4839, 0));
						}
						break;
					case 7131:
						if(objectX == 3039 && objectY == 4821 && objectZ == 0) {
							player.teleport(new Position(2522, 4842, 0));
						}
						break;
					case 7140:
						if(objectX == 3044 && objectY == 4822 && objectZ == 0) {
							player.teleport(new Position(2788, 4839, 0));
						}
						break;
					case 7139:
						if(objectX == 3047 && objectY == 4825 && objectZ == 0) {
							player.teleport(new Position(2846, 4836, 0));
						}
						break;
					case 7138:
						if(objectX == 3050 && objectY == 4829 && objectZ == 0) {
							player.message("This altar hasn't been added yet.");
						}
						break;
					case 7137:
						if(objectX == 3051 && objectY == 4833 && objectZ == 0) {
							player.teleport(new Position(3486, 4836, 0));
						}
						break;
					case 7136:
						if(objectX == 3050 && objectY == 4837 && objectZ == 0) {
							player.teleport(new Position(2203, 4836, 0));
						}
						break;
					case 7135:
						if(objectX == 3049 && objectY == 4839 && objectZ == 0) {
							player.teleport(new Position(2464, 4829, 0));
						}
						break;
					case 7134:
						if(objectX == 3044 && objectY == 4842 && objectZ == 0) {
							player.teleport(new Position(2271, 4840, 0));
						}
						break;
					//Agility stairs
					case 3205:
						if(objectX == 2532 && objectY == 3545)
							player.teleport(new Position(2532, 3546), LADDER);
						break;
					
					//chaos dwarf
					case 87012:
						if(objectX == 1487 && objectY == 4704)
							player.teleport(new Position(3085, 3513, 0), TRAINING_PORTAL);
						break;
					
					case 4493:
						player.teleport(new Position(3432, 3537, 1), LADDER);
						break;
					case 4494:
						player.teleport(new Position(3438, 3537, 0), LADDER);
						break;
					case 4495:
						player.teleport(new Position(3417, 3540, 2), LADDER);
						break;
					case 4496:
						player.teleport(new Position(3412, 3540, 1), LADDER);
						break;
				}
			}
		});
	}
	
	/**
	 * Handles the second slot object click for the {@code player}.
	 * @param player  the player to handle this for.
	 * @param payload the payload for reading the sent data.
	 */
	private void secondClick(Player player, ByteMessage payload) {
		//Getting data.
		int objectId = payload.getMedium();
		int objectX = payload.getShort(false);
		int objectY = payload.getShort(false);
		//Validating data.
		Position position = new Position(objectX, objectY, player.getPosition().getZ());
		if(objectId < 0 || objectX < 0 || objectY < 0)
			return;
		Optional<ObjectNode> o = World.getRegions().getRegion(position).getObject(objectId, position.toLocalPacked());
		if(!o.isPresent())
			return;
		//Controlling data.
		if(player.getRights().greater(Rights.ADMINISTRATOR))
			player.message("[OBJ-2]:" + o.get().toString());
		player.facePosition(position);
		final ObjectNode object = o.get();
		player.getMovementListener().append(() -> {
			if(new Boundary(position, object.getDefinition().getSize()).within(player.getPosition(), player.size(), 1)) {
				if(!MinigameHandler.execute(player, m -> m.onSecondClickObject(player, object))) {
					return;
				}
				
				if(Skills.executeObjectantSkills(player, object, 2)) {
					return;
				}
				if(World.getShootingStarEvent().getShootingStar().prospect(player, objectId)) {
					return;
				}
				switch(objectId) {
					case 3193:
					case 2213:
					case 34752:
					case 26972:
					case 30928:
					case 19230:
					case 11402:
					case 72931:
					case 35647:
					case 44216:
						player.getBank().open();
						break;
				}
			}
		});
	}
	
	/**
	 * Handles the third slot object click for the {@code player}.
	 * @param player  the player to handle this for.
	 * @param payload the payload for reading the sent data.
	 */
	private void thirdClick(Player player, ByteMessage payload) {
		//Getting data.
		int objectId = payload.getMedium();
		int objectX = payload.getShort(false);
		int objectY = payload.getShort(false);
		//Validating data.
		Position position = new Position(objectX, objectY, player.getPosition().getZ());
		if(objectId < 0 || objectX < 0 || objectY < 0)
			return;
		Optional<ObjectNode> o = World.getRegions().getRegion(position).getObject(objectId, position.toLocalPacked());
		if(!o.isPresent())
			return;
		if(player.getRights().greater(Rights.ADMINISTRATOR))
			player.message("[OBJ-3]:" + o.get().toString());
		//Controlling data.
		player.facePosition(position);
		final ObjectNode object = o.get();
		player.getMovementListener().append(() -> {
			if(new Boundary(position, object.getDefinition().getSize()).within(player.getPosition(), player.size(), 1)) {
				if(!MinigameHandler.execute(player, m -> m.onThirdClickObject(player, object))) {
					return;
				}
				switch(objectId) {
					
				}
			}
		});
	}
	
	/**
	 * Handles the fourth slot object click for the {@code player}.
	 * @param player  the player to handle this for.
	 * @param payload the payload for reading the sent data.
	 */
	private void fourthClick(Player player, ByteMessage payload) {
		//Getting data.
		int objectId = payload.getMedium();
		int objectX = payload.getShort(false);
		int objectY = payload.getShort(false);
		//Validating data.
		Position position = new Position(objectX, objectY, player.getPosition().getZ());
		if(objectId < 0 || objectX < 0 || objectY < 0)
			return;
		Optional<ObjectNode> o = World.getRegions().getRegion(position).getObject(objectId, position.toLocalPacked());
		if(!o.isPresent())
			return;
		if(player.getRights().greater(Rights.ADMINISTRATOR))
			player.message("[OBJ-4]:" + o.get().toString());
		//Controlling data.
		player.facePosition(position);
		final ObjectNode object = o.get();
		player.getMovementListener().append(() -> {
			if(new Boundary(position, object.getDefinition().getSize()).within(player.getPosition(), player.size(), 1)) {
				if(!MinigameHandler.execute(player, m -> m.onFourthClickObject(player, object))) {
					return;
				}
				switch(objectId) {
					
				}
			}
		});
	}
	
	/**
	 * Handles the fifth slot object click for the {@code player}.
	 * @param player  the player to handle this for.
	 * @param payload the payload for reading the sent data.
	 */
	private void fifthClick(Player player, ByteMessage payload) {
		//Getting data.
		int objectId = payload.getMedium();
		int objectX = payload.getShort(false);
		int objectY = payload.getShort(false);
		//Validating data.
		Position position = new Position(objectX, objectY, player.getPosition().getZ());
		if(objectId < 0 || objectX < 0 || objectY < 0)
			return;
		Optional<ObjectNode> o = World.getRegions().getRegion(position).getObject(objectId, position.toLocalPacked());
		if(!o.isPresent())
			return;
		if(player.getRights().greater(Rights.ADMINISTRATOR))
			player.message("[OBJ-5]:" + o.get().toString());
		//Controlling data.
		player.facePosition(position);
		final ObjectNode object = o.get();
		player.getMovementListener().append(() -> {
			if(new Boundary(position, object.getDefinition().getSize()).within(player.getPosition(), player.size(), 1)) {
				if(!MinigameHandler.execute(player, m -> m.onFourthClickObject(player, object))) {
					return;
				}
				switch(objectId) {
					
				}
			}
		});
	}
	
	/**
	 * Handles the spell on object for the {@code player}.
	 * @param player  the player to handle this for.
	 * @param payload the payload for reading the sent data.
	 */
	private void spellObject(Player player, ByteMessage payload) {
		//Getting data.
		int objectId = payload.getMedium();
		int objectX = payload.getShort(false);
		int objectY = payload.getShort(false);
		int spell = payload.getShort(false);
		//Validating data.
		Position position = new Position(objectX, objectY, player.getPosition().getZ());
		if(spell < 0 || objectId < 0 || objectX < 0 || objectY < 0)
			return;
		Optional<ObjectNode> o = World.getRegions().getRegion(position).getObject(objectId, position.toLocalPacked());
		if(!o.isPresent())
			return;
		//Controlling data.
		player.facePosition(position);
		final ObjectNode object = o.get();
		player.getMovementListener().append(() -> {
			if(new Boundary(position, object.getDefinition().getSize()).within(player.getPosition(), player.size(), 1)) {
				switch(objectId) {
					
				}
			}
		});
	}
	
}
