package com.rageps.content.scene.impl;

import com.rageps.content.clanchannel.channel.ClanChannelHandler;
import com.rageps.content.dialogue.Dialogue;
import com.rageps.content.dialogue.impl.GiveItemDialogue;
import com.rageps.content.dialogue.impl.OptionDialogue;
import com.rageps.content.dialogue.impl.StatementDialogue;
import com.rageps.content.scene.Cutscene;
import com.rageps.net.packet.out.*;
import com.rageps.content.TabInterface;
import com.rageps.net.host.HostListType;
import com.rageps.net.host.HostManager;
import com.rageps.net.refactor.packet.out.model.*;
import com.rageps.task.Task;
import com.rageps.world.model.Animation;
import com.rageps.world.model.Graphic;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerAttributes;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager.ActivityType;
import com.rageps.world.entity.item.Item;
import com.rageps.world.locale.Position;

import java.util.Optional;

/**
 * The introduction cutscene for the player when he logs into the game for the first time.
 * @author Artem Batutin
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class IntroductionCutscene extends Cutscene {
	
	/**
	 * The player this introduction is for.
	 */
	private final Player player;
	
	/**
	 * Constructs a new {@link IntroductionCutscene}.
	 * @param player {@link #player}.
	 */
	public IntroductionCutscene(Player player) {
		this.player = player;
		this.player.firstLogin = !HostManager.contains(player.credentials.getHostAddress(), HostListType.STARTER_RECEIVED);
	}
	
	@Override
	public void onSubmit() {
		player.move(new Position(3088, 3509));
		player.widget(3559);
	}
	
	@Override
	public void execute(Task t) {
		player.setPosition(new Position(3088, 3509));
		if(player.getPosition().same(new Position(3088, 3509))) {
			if(player.getAttributeMap().getInt(PlayerAttributes.INTRODUCTION_STAGE) == 1) {
				player.getDialogueBuilder().append(new StatementDialogue("Welcome to the World of "+ World.get().getEnvironment().getName()+"!").attach(() -> {
					player.getActivityManager().set(ActivityType.CLICK_BUTTON);
					player.send(new CameraMovementPacket(new Position(3083, 3510), 420, 2, 10, player.getPosition()));
					player.send(new CameraAnglePacket(new Position(3091, 3510), 270, 2, 10, player.getPosition()));
				}), new StatementDialogue("Here is the @red@home@bla@ area.", "We've opened a bank account for you.", "So you can use the bank booths to store your goods."), new StatementDialogue("By the way, this is you.", "But lets proceed...").attach(() -> {
					player.facePosition(new Position(3086, 3510));
					player.animation(new Animation(863));
				}), new StatementDialogue("This is the famous @red@Market place.", "You can search any type of item you want here.").attach(() -> {
					player.send(new CameraAnglePacket(new Position(3079, 3509), 240, 4, 10, player.getPosition()));
				}), new GiveItemDialogue(new Item(995, 200000), "There some coins to get you started...", Optional.empty()).attach(() -> {
				}), new StatementDialogue("This is the @red@Scoreboard.", "It tracks the top 20 players with the highest killstreak.", "Dying will reset your killstreak.").attach(() -> {
				}), new StatementDialogue("Those @red@3@bla@ with the highest streak at the @red@end of the week@bla@", "will gain a reward.", " Those @red@3@bla@ with the most recent @red@on-going@bla@ streaks", "will also gain rewards!").attach(() -> {
					TabInterface.QUEST.sendInterface(player, 638);
					player.send(new ForceTabPacket(TabInterface.QUEST));
				}), new GiveItemDialogue(new Item(19000, 200), "Each player kill will give you blood money.", Optional.empty()).attach(() -> {
				}), new StatementDialogue("You can view your player killing statistics by", "clicking the quest tab which is just next to your skill tab.").attach(() -> {
				}), new StatementDialogue("This shiny portal allows you to", "get anywhere: @red@skills, minigames, bosses.").attach(() -> {
				}), new StatementDialogue("Spria is the slayer master, ask her for any tasks."), new StatementDialogue("This is @red@Party Pete", "He changes tokens into precious goods.").attach(() -> {
				}), new GiveItemDialogue(new Item(7478, 20), "You can get tokens by donating on our website.", Optional.empty()).attach(() -> {
				}), new StatementDialogue("As you may noticed, this is the @red@Garden.", "You can plant trees here!").attach(() -> {
				}), new StatementDialogue("There is the @red@Fire pit.", "Firing it up will enable double xp."), new StatementDialogue("You will discover these two buildings on your own.", "Let's move on...").attach(() -> {
				}), new StatementDialogue("This is the @red@Construction Site.", "You can train few skills here.").attach(() -> {
				}), new StatementDialogue("A good start would be thieving on the second floor", "However be aware of guards!").attach(() -> {
				}), new StatementDialogue("This is the @red@Iron man house.", "It's exclusive to iron man members.").attach(() -> {
				}), new StatementDialogue("@red@Iron man mode:", "Levels decrease on death, can't search global market,", "can't trade nor drop and tougher monsters.").attach(() -> {
				}), new StatementDialogue("@red@Benefits when maxed out:", "Restrictions removed, 10% bonus monsters drops,", "unlimited run outside of wilderness", "and exclusive iron man items.").attach(() -> {
				}), new StatementDialogue("Another place on the list is the @red@Camp.", "Most of holidays activities will be held here.").attach(() -> {
				}), new StatementDialogue("We hope you'll enjoy your stay at @red@"+World.get().getEnvironment().getName()+".", "Don't forget to register on our forums.", "Report bugs also!").attach(() -> {
					player.setVisible(true);
					player.setInstance(0);
				}), complete());
				this.destruct();
			}
		} else
			player.move(new Position(3088, 3509));
	}
	
	@Override
	public void onCancel() {
	
	}
	
	private Dialogue complete() {
		return new StatementDialogue("You're on your own now. Goodluck!").attach(() -> {
			player.setInstance(0);
			player.send(new FadePacket(10, 100, 120));
			player.facePosition(new Position(3221, 3432));
			player.send(new CameraResetPacket());

			player.setVisible(true);
			player.getAttributeMap().set(PlayerAttributes.INTRODUCTION_STAGE, 2);
			player.graphic(new Graphic(2189));
			ClanChannelHandler.connect(player, "help", false);
		}).attachAfter(() -> {
			player.move(new Position(3088, 3509));
			player.widget(-5);
		});
		
	}
	
	public void prerequisites() {
		player.resetSidebars();
		player.getActivityManager().setAllExcept(ActivityType.WALKING, ActivityType.CLICK_BUTTON, ActivityType.LOG_OUT, ActivityType.CHARACTER_SELECTION, ActivityType.DIALOGUE_INTERACTION, ActivityType.FACE_POSITION);
		if(player.getAttributeMap().getInt(PlayerAttributes.INTRODUCTION_STAGE) < 2) {
			player.setVisible(false);
			if(player.firstLogin) {
				submit();
				return;
			}
			player.getDialogueBuilder().append(new OptionDialogue(t -> {
				if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
					submit();
				} else {
					player.getDialogueBuilder().advance();
					player.getAttributeMap().set(PlayerAttributes.INTRODUCTION_STAGE, 3);
				}
			}, "I want a quick tour.", "Skip the introduction."), complete());
		}
		if(player.getAttributeMap().getInt(PlayerAttributes.INTRODUCTION_STAGE) == 2) {
			player.widget(-5);
		}
	}
	
}
