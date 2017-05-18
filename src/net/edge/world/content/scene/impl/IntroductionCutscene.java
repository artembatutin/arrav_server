package net.edge.world.content.scene.impl;

import net.edge.net.PunishmentHandler;
import net.edge.task.Task;
import net.edge.world.GameConstants;
import net.edge.world.World;
import net.edge.world.content.TabInterface;
import net.edge.world.content.container.impl.Inventory;
import net.edge.world.content.dialogue.Dialogue;
import net.edge.world.content.dialogue.impl.OptionDialogue;
import net.edge.world.content.dialogue.impl.OptionDialogue.OptionType;
import net.edge.world.content.dialogue.impl.StatementDialogue;
import net.edge.world.content.scene.Cutscene;
import net.edge.world.locale.Position;
import net.edge.world.node.entity.model.Animation;
import net.edge.world.node.entity.model.Graphic;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.activity.ActivityManager.ActivityType;

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
	 * The player logs in for the first time.
	 */
	private final boolean firstLogin;

	/**
	 * Constructs a new {@link IntroductionCutscene}.
	 * @param player {@link #player}.
	 */
	public IntroductionCutscene(Player player) {
		this.player = player;
		firstLogin = !PunishmentHandler.recievedStarter(player.getSession().getHost());
	}

	@Override
	public void onSubmit() {
		player.setPosition(new Position(3088, 3509));
		player.getMessages().sendInterface(3559);
	}

	@Override
	public void execute(Task t) {
		player.setPosition(new Position(3088, 3509));
		if(player.getPosition().same(new Position(3088, 3509))) {
			if((int) player.getAttr().get("introduction_stage").get() == 1) {
				player.getDialogueBuilder().append(new StatementDialogue("Welcome to the World of @blu@Main!").attach(() -> {
					player.getMessages().sendCameraMovement(new Position(3083, 3506), 400, 2, 20);
					player.getMessages().sendCameraAngle(new Position(3089, 3508), 360, 2, 10);
				}), new StatementDialogue("Here is the @red@home@bla@ area.", "You can use the bank booths to store your goods.", "There are the stores also at the back."), new StatementDialogue("By the way, this is you.", "But lets proceed...").attach(() -> {
					player.facePosition(new Position(3083, 3506));
					player.animation(new Animation(863));
				}), new StatementDialogue("This is our @red@Scoreboard.", "It tracks the top 20 players with the highest killstreak.", "Dying in Main will reset your killstreak.").attach(() -> {
					player.getMessages().sendCameraMovement(new Position(3082, 3510), 340, 2, 10);
					player.getMessages().sendCameraAngle(new Position(3081, 3513), 300, 2, 5);
				}), new StatementDialogue("Those @red@3@bla@ with the longest streak at the @red@end of the week@bla@", "will gain a reward.", " Those @red@3@bla@ with the most recent @red@on-going@bla@ streaks", "will also gain rewards!").attach(() -> {
					TabInterface.QUEST.sendInterface(player, 638);
					player.getMessages().sendForceTab(2);
				}), new StatementDialogue("You can view your personal individual statistics by", "clicking the quest tab which is just next to your skill tab.").attach(() -> {
					player.getMessages().sendCameraMovement(new Position(3082, 3510), 340, 2, 10);
					player.getMessages().sendCameraAngle(new Position(3081, 3513), 300, 2, 5);
				}), new StatementDialogue("This shiny portal is the @red@traning portal.", "You can train all sort of skills.").attach(() -> {
					player.getMessages().sendCameraMovement(new Position(3080, 3510), 280, 2, 10);
					player.getMessages().sendCameraAngle(new Position(3076, 3510), 240, 2, 5);
				}), new StatementDialogue("This purple portal is the @red@Minigame/Boss portal.", "A wonderful journey awaits you here.").attach(() -> {
					player.getMessages().sendCameraMovement(new Position(3080, 3508), 280, 2, 10);
					player.getMessages().sendCameraAngle(new Position(3080, 3506), 240, 2, 5);
				}), new StatementDialogue("This is the @red@General Store.", "You can sell or purchase items here.").attach(() -> {
					player.getMessages().sendCameraMovement(new Position(3081, 3498), 280, 5, 20);
					player.getMessages().sendCameraAngle(new Position(3076, 3495), 240, 2, 5);
				}), new StatementDialogue("This is the @red@Garden.", "One day, you might be able to farm here.").attach(() -> {
					player.getMessages().sendCameraMovement(new Position(3084, 3496), 1800, 2, 10);
					player.getMessages().sendCameraAngle(new Position(3091, 3497), 2000, 2, 5);
				}), new StatementDialogue("This is the @red@Construction Site.", "You can train few skills here.").attach(() -> {
					player.getMessages().sendCameraMovement(new Position(3088, 3482), 1800, 2, 10);
					player.getMessages().sendCameraAngle(new Position(3092, 3471), 2000, 2, 5);
				}), new StatementDialogue("We hope you'll enjory your stay at @red@Main.", "Don't forget to register on our forums!").attach(() -> {
					player.getMessages().sendCameraMovement(new Position(3087, 3521), 1800, 5, 20);
					player.getMessages().sendCameraAngle(new Position(3089, 3511), 2000, 2, 5);
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
			player.getMessages().sendMinimapState(0);
			player.getMessages().sendFade(10, 100, 120);
			player.move(new Position(3088, 3509));
			player.facePosition(new Position(3221, 3432));
			player.getMessages().sendResetCameraPosition();
			player.getActivityManager().enable();
			player.setVisible(true);
			player.getAttr().get("introduction_stage").set(2);
			player.graphic(new Graphic(2189));
			player.sendDefaultSidebars();
			player.getInventory().refresh(player, Inventory.INVENTORY_DISPLAY_ID);
			if(firstLogin) {
				player.getInventory().setItems(GameConstants.STARTER_PACKAGE);
				PunishmentHandler.addStarter(player.getSession().getHost());
				player.getInventory().refresh(player, Inventory.INVENTORY_DISPLAY_ID);
			} else {
				player.message("You already received a starter package before.");
			}
			World.getClanManager().join(player, "avro");
		});
	}
	
	public void prerequisites() {
		player.setVisible(false);
		player.getMessages().sendMinimapState(2);
		player.getActivityManager().setAllExcept(ActivityType.CLICK_BUTTON, ActivityType.LOG_OUT, ActivityType.CHARACTER_SELECTION, ActivityType.DIALOGUE_INTERACTION, ActivityType.FACE_POSITION);
		if(firstLogin) {
			new IntroductionCutscene(player).submit();
			return;
		}
		player.getDialogueBuilder().append(new OptionDialogue(t -> {
			if(t.equals(OptionType.FIRST_OPTION)) {
				new IntroductionCutscene(player).submit();
			} else {
				player.getDialogueBuilder().advance();
				player.getMessages().sendInterface(3559);
			}
		}, "I want a quick tour.", "Got no time for that."), complete());
	}

}
