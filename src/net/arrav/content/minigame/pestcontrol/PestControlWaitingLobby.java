package net.arrav.content.minigame.pestcontrol;

import net.arrav.action.impl.ButtonAction;
import net.arrav.action.impl.MobAction;
import net.arrav.action.impl.ObjectAction;
import net.arrav.content.market.currency.Currency;
import net.arrav.content.minigame.MinigameLobby;
import net.arrav.content.skill.Skills;
import net.arrav.net.packet.out.SendWalkable;
import net.arrav.task.Task;
import net.arrav.util.rand.RandomUtils;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.mob.drop.Drop;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;
import net.arrav.world.entity.item.ItemCache;
import net.arrav.world.entity.object.GameObject;
import net.arrav.world.locale.Position;

import static net.arrav.content.minigame.Minigame.MinigameSafety.SAFE;
import static net.arrav.world.entity.item.ItemCache.*;

public final class PestControlWaitingLobby extends MinigameLobby {
	
	/**
	 * The pest control lobby.
	 */
	public static final PestControlWaitingLobby PEST_LOBBY = new PestControlWaitingLobby();
	
	/**
	 * Condition if the pest control game is currently on.
	 */
	boolean pestGameOn = false;
	
	private int count;
	private int timer;
	
	public PestControlWaitingLobby() {
		super(540); // 5 minutes
	}
	
	@Override
	public void onCountdown(int current, Task t) {
		timer = current;
		if(timer % 10 == 0) {
			for(Player p : getPlayers()) {
				p.text(21120, "@whi@Next Departure: " + seconds() + " seconds.");
			}
		}
	}
	
	@Override
	public void onEnter(Player player) {
		count++;
		getPlayers().add(player);
		player.out(new SendWalkable((21119)));
		player.text(21120, "@whi@Next Departure: " + seconds() + " seconds");
		player.text(21123, "@cya@Pest Points: " + player.getPest());
		updateCounts();
		player.move(new Position(2661, 2639));
	}
	
	@Override
	public void onLeave(Player player) {
		if(!getPlayers().contains(player))
			return;
		count--;
		getPlayers().remove(player);
		player.out(new SendWalkable((-1)));
		updateCounts();
		player.move(new Position(2657, 2639));
	}
	
	@Override
	public void onStart() {
		PestControlMinigame game = new PestControlMinigame("Pest control", SAFE);
		for(Player p : getPlayers()) {
			game.onEnter(p);
		}
		pestGameOn = true;
		getPlayers().clear();
		count = 0;
	}
	
	@Override
	public boolean canEnter(Player player) {
		if(player.getFamiliar().isPresent()) {
			player.message("You can't enter with a familiar aboard.");
			return false;
		}
		if(player.getPetManager().getPet().isPresent()) {
			player.message("You can't enter with a pet aboard.");
			return false;
		}
		if(player.getInventory().contains(1511)) {
			player.message("You cannot bring any logs in the boat.");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canStart() {
		//if enough players are in the room.
		return getPlayers().size() >= 5 && !pestGameOn;
	}
	
	@Override
	public int restartTimer() {
		return 540;
	}
	
	@Override
	public void onDestruct() {

	}
	
	public int seconds() {
		return (timer * 600) / 1000;
	}
	
	public void updateCounts() {
		for(Player p : getPlayers()) {
			p.text(21121, "@gre@Players Ready: " + count);
		}
	}
	
	public static void event() {
		//boat entering/exiting.
		ObjectAction plank = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				PEST_LOBBY.submit(player);
				return true;
			}
		};
		plank.registerFirst(14315);
		ObjectAction ladder = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				PEST_LOBBY.onLeave(player);
				return true;
			}
		};
		ladder.registerFirst(14314);
		
		//accessing shop.
		MobAction shop = new MobAction() {
			@Override
			public boolean click(Player player, Mob npc, int click) {
				player.widget(37000);
				player.text(37007, player.getPest() + " points");
				return true;
			}
		};
		shop.registerFirst(3786);
		shop.registerSecond(3786);
		
		//skill rewards
		int[] skills = {Skills.ATTACK, Skills.DEFENCE, Skills.MAGIC, Skills.PRAYER, Skills.STRENGTH, Skills.RANGED, Skills.HITPOINTS};
		int button = 144150;
		for(int skill : skills) {
			for(int i = 0; i < 3; i++) {
				final int cost = i == 0 ? 1 : i == 1 ? 10 : 100;
				ButtonAction attack = new ButtonAction() {
					@Override
					public boolean click(Player player, int button) {
						if(Currency.PEST_POINTS.getCurrency().takeCurrency(player, cost)) {
							player.getSkills()[skill].increaseExperience(cost * 4);//not exactly right.
						}
						return true;
					}
				};
				attack.register(button);
				button++;
			}
			button += 3;
		}
		
		//pack rewards
		button = 144189;
		ItemCache[] packs = {HIGH_HERBS, MED_MINERALS, HERB_SEEDS};
		for(int i = 0; i < 3; i++) {
			int index = i;
			ButtonAction attack = new ButtonAction() {
				@Override
				public boolean click(Player player, int button) {
					if(player.getInventory().remaining() < 1) {
						player.message("You do not have space in your inventory.");
						return true;
					}
					if(Currency.PEST_POINTS.getCurrency().takeCurrency(player, (index == 0 ? 30 : 15))) {
						//three items.
						Drop pack = RandomUtils.random(COMMON.get(packs[index]));
						player.getInventory().add(new Item(pack.getId(), RandomUtils.inclusive(pack.getMinimum(), pack.getMaximum())));
					}
					return true;
				}
			};
			attack.register(button);
			button += 3;
		}
		
		//void rewards
		final int[] items = {8841, 8840, 11663, 11665, 8839, 8842, 11664};
		final int[] costs = {250, 250, 200, 200, 200, 250, 150, 200};
		button = 144198;
		for(int i = 0; i < items.length; i++) {
			int index = i;
			ButtonAction attack = new ButtonAction() {
				@Override
				public boolean click(Player player, int button) {
					if(player.getInventory().remaining() < 1) {
						player.message("You do not have space in your inventory.");
						return true;
					}
					if(Currency.PEST_POINTS.getCurrency().takeCurrency(player, costs[index])) {
						player.getInventory().add(new Item(items[index]));
					}
					return true;
				}
			};
			attack.register(button);
			button += 3;
		}
	}
	
}
