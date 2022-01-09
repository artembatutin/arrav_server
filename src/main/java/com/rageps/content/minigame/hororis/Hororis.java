package com.rageps.content.minigame.hororis;

import com.rageps.content.achievements.Achievement;
import com.rageps.content.minigame.Minigame;
import com.rageps.content.skill.Skills;
import com.rageps.net.refactor.packet.out.model.FadePacket;
import com.rageps.net.refactor.packet.out.model.GraphicPacket;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.World;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.combat.hit.HitIcon;
import com.rageps.world.entity.actor.combat.hit.Hitsplat;
import com.rageps.world.entity.actor.mob.DefaultMob;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.mob.MobAttributes;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.GroundItem;
import com.rageps.world.entity.item.GroundItemPolicy;
import com.rageps.world.entity.item.GroundItemStatic;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.object.GameObject;
import com.rageps.world.locale.Position;
import com.rageps.world.model.Animation;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.util.Optional;

/**
 * A custom "Horroris" minigame.
 * @author Artem Batutin
 */
public class Hororis extends Minigame {
	
	//	private final SkeletalHorror horror;
	
	/**
	 * Skeleton minions.
	 */
	private static final ObjectList<Mob> minions = new ObjectArrayList<>();
	
	/**
	 * The ground bones.
	 */
	private static final ObjectList<GroundItem> bones = new ObjectArrayList<>();
	
	/**
	 * The players in the ring.
	 */
	private static final ObjectList<Player> players = new ObjectArrayList<>();
	
	public Hororis() {
		super("Hororis", MinigameSafety.SAFE, MinigameType.NORMAL);
	}
	
	@Override
	public void onLogin(Player player) {

	}
	
	@Override
	public void onLogout(Player player) {
		players.remove(player);
		player.getAttributeMap().set(MobAttributes.IGNORED_AGGRESSION_LEVEL, false);
		player.send(new FadePacket(20, 100, 160));
		player.task(2, pl -> pl.move(new Position(3367, 3513)));
		player.setMinigame(Optional.empty());
	}
	
	@Override
	public void onEnter(Player player) {
		players.add(player);
		player.getAttributeMap().set(MobAttributes.IGNORED_AGGRESSION_LEVEL, true);
		player.send(new FadePacket(20, 100, 160));
		player.task(2, pl -> pl.move(new Position(3380, 3513)));
		player.setMinigame(this);
	}
	
	@Override
	public boolean onFirstClickNpc(Player player, Mob mob) {
		if(mob.getId() == 9181 || mob.getId() == 9182 || mob.getId() == 9183) {
			if(!player.playerData.lockedXP) {
				player.getSkills()[Skills.PRAYER].increaseExperience(300);
			}
			World.get().getMobRepository().remove(mob);
			return true;
		}
		player.message("You cannot interact with this mob in here!");
		return false;
	}
	
	@Override
	public boolean onFirstClickObject(Player player, GameObject object) {
		if(object.getId() == 86382) {
			onLogout(player);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean contains(Player player) {
		return players.contains(player);
	}
	
	@Override
	public Position deathPosition(Player player) {
		return new Position(3380, 3513);
	}
	
	public void over() {
		for(Mob m : minions) {
			World.get().getMobRepository().remove(m);
		}
		minions.clear();
		//		horror.getRegion().ifPresent(reg -> {
		//			for(GroundItem b : bones) {
		//				reg.unregister(b);
		//			}
		//		});
		bones.clear();
		for(Player p : players) {
			p.send(new FadePacket(20, 100, 160));
			p.task(2, pl -> pl.move(new Position(3367, 3513)));
			if(!p.playerData.lockedXP) {
				p.getSkills()[Skills.PRAYER].increaseExperience(400);
			}
			p.getAttributeMap().set(MobAttributes.IGNORED_AGGRESSION_LEVEL, false);
			p.message("The skeletal horror didn't stand a chance! Good job.");
			Achievement.HORRORIFIC.inc(p);
		}
		players.clear();
	}
	
	public void drop(int part) {
		Mob mob = new DefaultMob(part, getRandom());
		World.get().getMobRepository().add(mob);
		minions.add(mob);
		if(players.size() > 0) {
			Player p = RandomUtils.random(players);
			if(p != null)
				mob.getCombat().attack(p);
		}
	}
	
	public void bones() {
		for(int i = 0; i < 5; i++) {
			Position pos = getRandom();
			GroundItemStatic item = new GroundItemStatic(new Item(526), pos, GroundItemPolicy.TIMEOUT);
			for(Player p : players) {
				p.send(new GraphicPacket(p, 520, pos, 0));
				if(p.getPosition().same(pos)) {
					p.damage(new Hit(RandomUtils.inclusive(20, 100), Hitsplat.DISEASE, HitIcon.MAGIC));
				}
			}
			//			horror.getRegion().ifPresent(r -> r.register(item));
			bones.add(item);
		}
	}
	
	public void minion() {
		Mob mob = new DefaultMob(RandomUtils.inclusive(10629, 10641), getRandom());
		World.get().getMobRepository().add(mob);
		minions.add(mob);
		mob.animation(new Animation(9598));
	}
	
	private Position getRandom() {
		return new Position(3382 + RandomUtils.inclusive(10), 3511 + RandomUtils.inclusive(10));
	}
	
	public void message(String message) {
		for(Player p : players) {
			p.message(message);
		}
	}
	
}
