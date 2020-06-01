package com.rageps.content.minigame.nexchamber;

import com.rageps.net.packet.out.SendFade;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import com.rageps.action.impl.ObjectAction;
import com.rageps.content.minigame.Minigame;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.CombatType;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.object.GameObject;
import com.rageps.world.locale.Position;

import java.util.Optional;

/**
 * The nex chamber acting as a {@link Minigame}.
 * @author Artem Batutin
 */
public class NexMinigame extends Minigame {
	
	/**
	 * The saved static nex game.
	 */
	public static NexMinigame game;
	
	//	private final Nex nex;
	
	private static final ObjectList<Player> players = new ObjectArrayList<>();
	
	public NexMinigame() {
		super("Nex", MinigameSafety.DEFAULT, MinigameType.NORMAL);
	}
	
	/**
	 * The players in the chamber.
	 */
	public static ObjectList<Player> getPlayers() {
		return players;
	}
	
	@Override
	public void onLogin(Player player) {
		onLogout(player);
	}
	
	@Override
	public void onLogout(Player player) {
		getPlayers().remove(player);
		player.out(new SendFade(20, 100, 160));
		player.task(2, pl -> pl.move(new Position(2907, 5204)));
		player.setMinigame(Optional.empty());
	}
	
	@Override
	public void onEnter(Player player) {
		getPlayers().add(player);
		player.out(new SendFade(20, 100, 160));
		player.task(2, pl -> pl.move(new Position(2911, 5204)));
		player.setMinigame(this);
	}
	
	@Override
	public boolean canHit(Player player, Actor other, CombatType type) {
		if(other.isPlayer())
			return false;
		Mob m = other.toMob();
		//		if(nex.minionStage != m.getId() - 13450 && m.getId() != 13447) {
		//			player.message("The avatar is not weak enough to damage this minion.");
		//			return false;
		//		}
		return true;
	}
	
	@Override
	public boolean onFirstClickObject(Player player, GameObject object) {
		if(object.getId() == 99228) {
			onLogout(player);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean contains(Player player) {
		return getPlayers().contains(player);
	}
	
	@Override
	public Position deathPosition(Player player) {
		return new Position(2911, 5204);
	}
	
	public static void action() {
		ObjectAction enter = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(true) {
					player.message("In construction.");
					return true;
				}
				if(game == null)
					return true;
				if(game.contains(player)) {
					game.onLogout(player);
					return true;
				}
				game.onEnter(player);
				return true;
			}
		};
		enter.registerFirst(99228);
	}
	
	@Override
	public boolean canLogout(Player player) {
		return true;
	}
	
	/**
	 * Gets a random spot in the nex chamber, excluding the four black empty spots.
	 * @return random spot, to handle the smoke hits mostly.
	 */
	private Position getRandom() {
		Position p = new Position(RandomUtils.inclusive(2937, 2913), RandomUtils.inclusive(5191, 5216));
		if(p.getX() <= 2923 && p.getX() >= 2916) {//west two empty spaces
			if(p.getY() >= 5205 && p.getY() <= 5212) {
				//top west
				return getRandom();
			} else if(p.getY() <= 5201 && p.getY() >= 5194) {
				//bottom west
				return getRandom();
			}
		} else if(p.getX() >= 2927 && p.getX() <= 2934) {
			if(p.getY() >= 5205 && p.getY() <= 5212) {
				//top east
				return getRandom();
			} else if(p.getY() <= 5201 && p.getY() >= 5194) {
				//bottom east
				return getRandom();
			}
		}
		return p;
	}
	
}
