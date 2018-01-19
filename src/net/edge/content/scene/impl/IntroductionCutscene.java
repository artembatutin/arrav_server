package net.edge.content.scene.impl;

import net.edge.content.TabInterface;
import net.edge.content.clanchat.ClanManager;
import net.edge.content.dialogue.Dialogue;
import net.edge.content.dialogue.impl.GiveItemDialogue;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.dialogue.impl.OptionDialogue.OptionType;
import net.edge.content.dialogue.impl.StatementDialogue;
import net.edge.content.scene.Cutscene;
import net.edge.net.host.HostListType;
import net.edge.net.host.HostManager;
import net.edge.net.packet.out.*;
import net.edge.task.Task;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.activity.ActivityManager.ActivityType;
import net.edge.world.entity.item.Item;
import net.edge.world.locale.Position;

import java.util.Optional;

/**
 * The introduction cutscene for the player when he logs into the game for the first time.
 * @author Artem Batutin <artembatutin@gmail.com>
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
		this.player.firstLogin = !HostManager.contains(player.getSession().getHost(), HostListType.STARTER_RECEIVED);
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
			if(player.getAttr().get("introduction_stage").getInt() == 1) {
				player.getDialogueBuilder().append(new StatementDialogue("Welcome to the World of @blu@Arrav!").attach(() -> {
					player.getActivityManager().set(ActivityType.CLICK_BUTTON);
					player.out(new SendCameraMovement(new Position(3085, 3510), 310, 2, 10));
					player.out(new SendCameraAngle(new Position(3093, 3509), 300, 2, 10));
				}), new StatementDialogue("Here is the @red@home@bla@ area.", "You can use the bank booths to store your goods.", "There are the stores also at the back."), new StatementDialogue("By the way, this is you.", "But lets proceed...").attach(() -> {
					player.facePosition(new Position(3084, 3511));
					player.animation(new Animation(863));
				}), new StatementDialogue("This is the famous @red@Market place.", "You can search any type of item you want here.").attach(() -> {
					player.out(new SendCameraAngle(new Position(3081, 3508), 240, 4, 10));
				}), new GiveItemDialogue(new Item(995, 200000), "There some coins to get you started...", Optional.empty()).attach(() -> {
				}), new StatementDialogue("This is the @red@Scoreboard.", "It tracks the top 20 players with the highest killstreak.", "Dying will reset your killstreak.").attach(() -> {
					player.out(new SendCameraMovement(new Position(3087, 3516), 340, 4, 10));
					player.out(new SendCameraAngle(new Position(3089, 3514), 300, 6, 10));
				}), new StatementDialogue("Those @red@3@bla@ with the highest streak at the @red@end of the week@bla@", "will gain a reward.", " Those @red@3@bla@ with the most recent @red@on-going@bla@ streaks", "will also gain rewards!").attach(() -> {
					TabInterface.QUEST.sendInterface(player, 638);
					player.out(new SendForceTab(TabInterface.QUEST));
				}), new GiveItemDialogue(new Item(19000, 200), "Each player kill will give you blood money.", Optional.empty()).attach(() -> {
				}), new StatementDialogue("You can view your player killing statistics by", "clicking the quest tab which is just next to your skill tab.").attach(() -> {
				}), new StatementDialogue("This shiny portal allows you to", "get anywhere: @red@skills, minigames, bosses.").attach(() -> {
					player.out(new SendCameraMovement(new Position(3087, 3514), 340, 2, 10));
					player.out(new SendCameraAngle(new Position(3083, 3514), 320, 2, 5));
				}), new StatementDialogue("There is one slayer master in Arrav, ask her for any tasks.").attach(() -> {
					player.out(new SendCameraMovement(new Position(3087, 3501), 280, 2, 10));
					player.out(new SendCameraAngle(new Position(3085, 3502), 240, 2, 10));
				}), new StatementDialogue("This is the @red@Fire pit.", "Firing it up will enable double xp.").attach(() -> {
					player.out(new SendCameraAngle(new Position(3083, 3497), 250, 2, 5));
				}), new StatementDialogue("This is @red@Party Pete", "He changes edge tokens into precious goods.").attach(() -> {
					player.out(new SendCameraAngle(new Position(3089, 3502), 250, 4, 10));
				}), new GiveItemDialogue(new Item(7478, 20), "You can get edge tokens by donating on our website.", Optional.empty()).attach(() -> {
				}), new StatementDialogue("As you may noticed, this is the @red@Garden.", "You can plant trees here!").attach(() -> {
					player.out(new SendCameraMovement(new Position(3087, 3502), 1800, 2, 10));
					player.out(new SendCameraAngle(new Position(3087, 3496), 2000, 2, 10));
				}), new StatementDialogue("You will discover these two buildings on your own.", "Let's move on...").attach(() -> {
					player.out(new SendCameraMovement(new Position(3087, 3492), 1800, 2, 10));
					player.out(new SendCameraAngle(new Position(3092, 3477), 2000, 2, 10));
				}), new StatementDialogue("This is the @red@Construction Site.", "You can train few skills here.").attach(() -> {
					player.out(new SendCameraMovement(new Position(3088, 3484), 1800, 2, 10));
					player.out(new SendCameraAngle(new Position(3093, 3475), 2000, 2, 10));
				}), new StatementDialogue("A good start would be thieving on the second floor", "However be aware of guards!").attach(() -> {
					player.out(new SendCameraMovement(new Position(3088, 3482), 1200, 2, 10));
					player.out(new SendCameraAngle(new Position(3094, 3476), 1400, 2, 10));
				}), new StatementDialogue("This is the @red@Iron man house.", "It's exclusive to iron man members.").attach(() -> {
					player.out(new SendCameraMovement(new Position(3099, 3496), 1200, 2, 10));
					player.out(new SendCameraAngle(new Position(3102, 3499), 1400, 2, 10));
				}), new StatementDialogue("@red@Iron man mode:", "Levels decrease on death, can't search global market,", "can't trade nor drop and tougher monsters.").attach(() -> {
				}), new StatementDialogue("@red@Benefits when maxed out:", "Restrictions removed, 10% bonus monsters drops,", "unlimited run outside of wilderness", "and exclusive iron man items.").attach(() -> {
				}), new StatementDialogue("Another place on the list is the @red@Camp.", "Most of holidays activities will be held here.").attach(() -> {
					player.out(new SendCameraMovement(new Position(3106, 3504), 1900, 2, 10));
					player.out(new SendCameraAngle(new Position(3107, 3512), 2100, 2, 10));
				}), new StatementDialogue("We hope you'll enjoy your stay at @red@Arrav.", "Don't forget to register on our forums.", "Report bugs also!").attach(() -> {
					player.out(new SendCameraMovement(new Position(3096, 3519), 1900, 2, 10));
					player.out(new SendCameraAngle(new Position(3087, 3510), 2100, 2, 10));
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
			player.out(new SendFade(10, 100, 120));
			player.facePosition(new Position(3221, 3432));
			player.out(new SendCameraReset());
			player.setVisible(true);
			player.getAttr().get("introduction_stage").set(2);
			player.graphic(new Graphic(2189));
			ClanManager.get().join(player, "avro");
		}).attachAfter(() -> {
			player.move(new Position(3088, 3509));
			player.widget(-5);
		});
		
	}
	
	public void prerequisites() {
		player.resetSidebars();
		player.getActivityManager().setAllExcept(ActivityType.CLICK_BUTTON, ActivityType.LOG_OUT, ActivityType.CHARACTER_SELECTION, ActivityType.DIALOGUE_INTERACTION, ActivityType.FACE_POSITION);
		if(player.getAttr().get("introduction_stage").getInt() < 2) {
			player.setVisible(false);
			if(player.firstLogin) {
				submit();
				return;
			}
			player.getDialogueBuilder().append(new OptionDialogue(t -> {
				if(t.equals(OptionType.FIRST_OPTION)) {
					submit();
				} else {
					player.getDialogueBuilder().advance();
					player.getAttr().get("introduction_stage").set(3);
				}
			}, "I want a quick tour.", "Skip the introduction."), complete());
		}
		if(player.getAttr().get("introduction_stage").getInt() == 2) {
			player.widget(-5);
		}
	}
	
}
