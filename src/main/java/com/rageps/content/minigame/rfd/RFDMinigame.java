package com.rageps.content.minigame.rfd;

import com.rageps.content.achievements.Achievement;
import com.rageps.world.World;
import com.rageps.GameConstants;
import com.rageps.content.dialogue.Expression;
import com.rageps.content.dialogue.impl.NpcDialogue;
import com.rageps.content.item.FoodConsumable;
import com.rageps.content.item.PotionConsumable;
import com.rageps.content.minigame.SequencedMinigame;
import com.rageps.content.skill.prayer.Prayer;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.attribute.AttributeValue;
import com.rageps.world.entity.actor.mob.DefaultMob;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.GroundItem;
import com.rageps.world.entity.object.GameObject;
import com.rageps.world.locale.InstanceManager;
import com.rageps.world.locale.Position;

import java.util.Optional;

/**
 * Holds functionality for the recipe for disaster minigame.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class RFDMinigame extends SequencedMinigame {
	
	/**
	 * The instance of the current rfd minigame.
	 */
	private final int instance = InstanceManager.get().closeNext();
	
	/**
	 * The current npc spawned for this wave.
	 */
	private Optional<Mob> currentNpc = Optional.empty();
	
	/**
	 * The first wave this minigame will start at.
	 */
	private RFDData wave = RFDData.WAVE_ONE;
	
	/**
	 * The delay between rounds in ticks.
	 */
	private static final int DELAY_BETWEEN_ROUNDS = 10;
	
	/**
	 * Constructs a new {@link RFDMinigame}.
	 */
	public RFDMinigame() {
		super("RFD", MinigameSafety.SAFE);
	}
	
	public static void enterRFD(Player player) {
		RFDMinigame rfd = new RFDMinigame();
		player.setMinigame(rfd);
		rfd.onEnter(player);
	}
	
	private int timer;
	
	@Override
	public void onSequence() {
		for(Player player : getPlayers()) {//there is one player.
			if(player.getMinigame().isPresent() && !((RFDMinigame) (player.getMinigame().get())).currentNpc.isPresent() && ++timer >= DELAY_BETWEEN_ROUNDS) {
				RFDData data = ((RFDMinigame) player.getMinigame().get()).wave;
				this.currentNpc = Optional.of(new DefaultMob(data.getNpcId(), new Position(1900, 5354, 2)));
				Mob mob = this.currentNpc.get();
				InstanceManager.get().isolate(mob, instance);
				World.get().getMobs().add(mob);
				mob.getCombat().attack(player);
				timer = 0;
			}
		}
	}
	
	@Override
	public void enter(Player player) {
		InstanceManager.get().isolate(player, instance);
		player.move(new Position(1899, 5366, 2));
		player.message("The next wave will start in 6 seconds...");
	}
	
	@Override
	public int delay() {
		return 1;
	}
	
	@Override
	public void login(Player player) {

	}
	
	@Override
	public boolean canLogout(Player player) {
		return true;
	}
	
	@Override
	public void logout(Player player) {
		if(currentNpc.isPresent() && !currentNpc.get().isDead()) {
			World.get().getMobs().remove(currentNpc.get());
		}
		leave(player);
	}
	
	@Override
	public void onDeath(Player player) {
		logout(player);
	}
	
	@Override
	public void onKill(Player player, Actor other) {
		if(!other.isMob()) {
			return;
		}
		
		Mob mob = other.toMob();
		
		if(!RFDData.isValidNpc(mob.getId())) {
			return;
		}
		
		AttributeValue<RFDData> key = player.getAttr().get("rfd_wave");
		
		if(((RFDMinigame) player.getMinigame().get()).wave.getNextOrLast().equals(RFDData.WAVE_SIX)) {
			key.set(RFDData.WAVE_SIX);
			player.message("You have completed the rfd minigame.");
			leave(player);
			Achievement.DISASTER.inc(player);
			return;
		}
		
		((RFDMinigame) (player.getMinigame().get())).currentNpc = Optional.empty();
		
		if(((RFDMinigame) player.getMinigame().get()).wave.getIndex() > key.get().getIndex()) {
			key.set(key.get().getNextOrLast());
		}
		
		((RFDMinigame) player.getMinigame().get()).wave = ((RFDMinigame) player.getMinigame().get()).wave.getNextOrLast();
		
		player.message("The next wave will start in 6 seconds...");
	}
	
	@Override
	public boolean canPot(Player player, PotionConsumable potion) {
		return true;
	}
	
	@Override
	public boolean canEat(Player player, FoodConsumable food) {
		return true;
	}
	
	public boolean canPickup(Player player, GroundItem node) {
		return true;
	}
	
	@Override
	public boolean canPray(Player player, Prayer prayer) {
		player.getDialogueBuilder().append(new NpcDialogue(3400, Expression.MAD, "You cannot use prayers in here!"));
		return false;
	}
	
	@Override
	public boolean onFirstClickObject(Player player, GameObject object) {
		switch(object.getId()) {
			case 12356:
				if(currentNpc.isPresent() && !currentNpc.get().isDead()) {
					World.get().getMobs().remove(currentNpc.get());
				}
				player.message("You leave the minigame...");
				leave(player);
				return true;
			default:
				player.message("You can't interact with this object...");
				return false;
		}
	}
	
	@Override
	public boolean contains(Player player) {
		return player.getPosition().within(1888, 5367, 1911, 5344);
	}
	
	@Override
	public boolean canTeleport(Player player, Position position) {
		return player.getInstance() == 0;
	}
	
	private void leave(Player player) {
		player.setMinigame(Optional.empty());
		player.setInstance(0);
		InstanceManager.get().open(instance);
		player.teleport(GameConstants.STARTING_POSITION);
		this.destruct();
	}
}
