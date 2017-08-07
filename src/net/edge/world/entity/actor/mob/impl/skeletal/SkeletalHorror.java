package net.edge.world.entity.actor.mob.impl.skeletal;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.action.impl.MobAction;
import net.edge.action.impl.ObjectAction;
import net.edge.content.skill.Skills;
import net.edge.net.packet.out.SendFade;
import net.edge.net.packet.out.SendGraphic;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Animation;
import net.edge.world.Hit;
import net.edge.world.World;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.mob.impl.DefaultMob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.GroundItem;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.region.Region;
import net.edge.world.locale.Position;
import net.edge.world.object.GameObject;

import static net.edge.world.entity.actor.mob.impl.skeletal.SkeletalHorrorStage.*;

/**
 * The class which represents the skeletal horror.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class SkeletalHorror extends Mob {
	
	/**
	 * If the horror is currently alive.
	 */
	private static boolean horrorAlive = false;
	
	/**
	 * A skeletal horror instance.
	 */
	public static SkeletalHorror horror;
	
	/**
	 * Skeleton minions.
	 */
	private final ObjectList<Mob> minions = new ObjectArrayList<>();
	
	/**
	 * The ground bones.
	 */
	private final ObjectList<GroundItem> bones = new ObjectArrayList<>();
	
	/**
	 * The players in the ring.
	 */
	private final ObjectList<Player> players = new ObjectArrayList<>();
	
	/**
	 * The stage of the skeletal horror.
	 */
	private SkeletalHorrorStage stage;
	
	/**
	 * The ring region.
	 */
	private final Region reg;
	
	/*
	 * Location constants.
	 */
	private final int x = 3382;
	private final int y = 3511;
	private final int size = 10;
	
	/**
	 * Constructs a new {@link SkeletalHorror}.
	 */
	public SkeletalHorror() {
		super(9177, new Position(3386, 3517));
		stage = MAIN;
		reg = getRegion();
		horrorAlive = true;
		if(horror == null)
			horror = this;
	}
	
	@Override
	public Mob create() {
		return new SkeletalHorror();
	}
	
	@Override
	public void setDead(boolean dead) {
		super.setDead(dead);
		horrorAlive = !dead;
	}
	
	@Override
	public Hit decrementHealth(Hit hit) {
		if(hit.getDamage() > getCurrentHealth()) {
			hit.setDamage(getCurrentHealth());
			if(hit.getType() == Hit.HitType.CRITICAL) {
				hit.setType(Hit.HitType.NORMAL);
			}
		}
		setCurrentHealth(getCurrentHealth() - hit.getDamage());
		if(getCurrentHealth() <= 0) {
			setCurrentHealth(0);
			for(Mob m : minions) {
				World.get().getMobs().remove(m);
			}
			minions.clear();
			for(GroundItem b : bones) {
				reg.unregister(b);
			}
			bones.clear();
			for(Player p : players) {
				p.out(new SendFade(20, 100, 160));
				p.task(2, pl -> pl.move(new Position(3367, 3513)));
				p.getSkills()[Skills.PRAYER].increaseExperience(400);
			}
			players.clear();
		}
		if(stage == MAIN && getCurrentHealth() < 3000) {
			stage = ARM_CUT;
			transform(ARM_CUT.npc);
			message("The skeletal horror lost his arm!");
			drop(9181);
		} else if(stage == ARM_CUT && getCurrentHealth() < 2000) {
			stage = NO_ARMS;
			transform(NO_ARMS.npc);
			message("The skeletal horror lost his other arm!");
			drop(9182);
		} else if(stage == NO_ARMS && getCurrentHealth() < 1000) {
			stage = NO_TAIL;
			transform(NO_TAIL.npc);
			message("The skeletal horror lost his tail!");
			drop(9183);
		}
		return hit;
	}
	
	public void message(String message) {
		for(Player p : players) {
			p.message(message);
		}
	}
	
	public void bones() {
		for(int i = 0; i < RandomUtils.inclusive(20); i++) {
			Position pos = new Position(x + RandomUtils.inclusive(size), y + RandomUtils.inclusive(y));
			GroundItem item = new GroundItem(new Item(526), pos, null);
			for(Player p : players) {
				p.out(new SendGraphic(520, pos, 0));
				if(p.getPosition().same(pos)) {
					p.damage(new Hit(100, Hit.HitType.DISEASE, Hit.HitIcon.MAGIC));
				}
			}
			reg.register(item);
			bones.add(item);
		}
	}
	
	public void minion() {
		Mob mob = new DefaultMob(RandomUtils.inclusive(10629, 10641), new Position(x + RandomUtils.inclusive(size), y + RandomUtils.inclusive(y)));
		World.get().getMobs().add(mob);
		minions.add(mob);
		mob.animation(new Animation(9598));
	}
	
	public void drop(int part) {
		Mob mob = new DefaultMob(part, new Position(x + RandomUtils.inclusive(size), y + RandomUtils.inclusive(y)));
		World.get().getMobs().add(mob);
		minions.add(mob);
	}
	
	private void join(Player p) {
		if(!horrorAlive) {
			p.message("The skeletal horror is reassembling itself...");
			return;
		}
		players.add(p);
		p.out(new SendFade(20, 100, 160));
		p.task(2, pl -> pl.move(new Position(3380, 3513)));
	}
	
	public static void quit(Player p) {
		if(horror == null)
			return;
		if(!horror.players.contains(p))
			return;
		horror.players.remove(p);
		p.out(new SendFade(20, 100, 160));
		p.task(2, pl -> pl.move(new Position(3367, 3513)));
	}
	
	public static void action() {
		MobAction part = new MobAction() {
			@Override
			public boolean click(Player player, Mob mob, int click) {
				player.getSkills()[Skills.PRAYER].increaseExperience(300);
				World.get().getMobs().remove(mob);
				return true;
			}
		};
		part.registerFirst(9181);
		part.registerFirst(9182);
		part.registerFirst(9183);
		
		ObjectAction join = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				System.out.println(horror == null);
				if(horror == null)
					return true;
				horror.join(player);
				return true;
			}
		};
		join.registerFirst(86381);
		
		ObjectAction quit = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				quit(player);
				return true;
			}
		};
		quit.registerFirst(86382);
	}
	
}
