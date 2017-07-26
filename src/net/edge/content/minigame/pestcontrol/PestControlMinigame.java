package net.edge.content.minigame.pestcontrol;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.content.combat.special.CombatSpecial;
import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.content.door.DoorHandler;
import net.edge.content.item.FoodConsumable;
import net.edge.content.item.PotionConsumable;
import net.edge.content.market.currency.Currency;
import net.edge.content.minigame.SequencedMinigame;
import net.edge.content.minigame.pestcontrol.defence.PestGate;
import net.edge.content.minigame.pestcontrol.pest.Pest;
import net.edge.content.skill.Skills;
import net.edge.world.locale.Position;
import net.edge.net.packet.out.SendConfig;
import net.edge.net.packet.out.SendWalkable;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Hit;
import net.edge.world.World;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;
import net.edge.world.entity.item.GroundItem;
import net.edge.world.entity.item.Item;
import net.edge.world.object.GameObject;

import java.util.Optional;

import static net.edge.content.teleport.impl.DefaultTeleportSpell.TeleportType.LADDER;

public final class PestControlMinigame extends SequencedMinigame {
	
	/**
	 * All the pest gates, there is 3 of them.
	 */
	private static ObjectList<PestGate> gates = new ObjectArrayList<>();
	
	/**
	 * The strings that the knight yells out.
	 */
	private static final String[] YELLS = {
			"We must not fail!",
			"Take down the portals",
			"The Void Knights will not fall!",
			"Hail the Void Knights!",
			"We are beating these scums!"
	};
	
	/**
	 * The middle void knight.
	 */
	private final VoidKnight voidKnight;
	
	/**
	 * All of the portals.
	 * 0 purple
	 * 1 red
	 * 2 yellow
	 * 3 gray
	 */
	private final PestPortal[] portals;
	
	/**
	 * The pests in the minigame.
	 */
	private ObjectList<Pest> pests = new ObjectArrayList<>();
	
	/**
	 * 10 is 1 minute. so 20 minutes. 10 calls per 1 minute meaning each 6 seconds.
	 */
	private int time = 200;
	
	PestControlMinigame(String minigame, MinigameSafety safety) {
		super(minigame, safety);
		voidKnight = new VoidKnight();
		portals = new PestPortal[] {
				new PestPortal(6142, new Position(2628, 2591), new Position(2632, 2594), 21111, voidKnight),
				new PestPortal(6145, new Position(2645, 2569), new Position(2647, 2573), 21114, voidKnight),
				new PestPortal(6144, new Position(2669, 2570), new Position(2671, 2574), 21113, voidKnight),
				new PestPortal(6143, new Position(2680, 2588), new Position(2679, 2589), 21112, voidKnight)
		};
	}

	@Override
	public void onSequence() {
		if(time == 200)
			start();
		time(--time);
		for(PestPortal portal : portals) {
			if(RandomUtils.inclusive(3) == 1)
				portal.spawn(pests);
		}
		for(Pest pest : pests) {
			pest.sequence(voidKnight);
		}
	}

	@Override
	public void enter(Player player) {
	
	}

	@Override
	public int delay() {
		return 10;//6 seconds.
	}

	@Override
	public void login(Player player) {
		logout(player);
	}

	@Override
	public void logout(Player player) {
		player.move(new Position(2657, 2638 + RandomUtils.inclusive(5)));
		getPlayers().remove(player);
		player.setMinigame(Optional.empty());
		player.setInstance(0);
		player.out(new SendWalkable((-1)));
		if(player.isPoisoned()) {
			player.getPoisonDamage().set(0);
			player.out(new SendConfig(174, 0));
		}
		player.getAttr().get("participation").set(0);
		player.getAttr().get("master_archery").set(false);
		CombatSpecial.restore(player, 100);
		Skills.restoreAll(player);
		player.getCombatBuilder().reset();
		player.getInventory().remove(new Item(1511, 28));//removing logs.
	}

	@Override
	public boolean contains(Player player) {
		return getPlayers().contains(player);
	}
	
	@Override
	public void onTeleportBefore(Player player, Position position) {
		//nothing.
	}
	
	@Override
	public boolean canTeleport(Player player, Position position) {
		//ladders.
		return position.same(new Position(2645, 2601)) || position.same(new Position(2643, 2601)) || position.same(new Position(2668, 2601)) || position
				.same(new Position(2670, 2601)) || position.same(new Position(2647, 2585)) || position.same(new Position(2647, 2587)) || position
				.same(new Position(2666, 2585)) || position.same(new Position(2666, 2587));
	}
	
	@Override
	public boolean onFirstClickObject(Player player, GameObject object) {
		Position pos = object.getGlobalPos();
		//west north ladder.
		if(object.getId() == 14296 && pos.getX() == 2644) {
			player.teleport(new Position(player.getPosition().getX() <= 2643 ? 2645 : 2643, 2601), LADDER);
			player.getAttr().get("master_archery").set(player.getPosition().getX() <= 2643);
			return false;
		}
		//east north ladder.
		if(object.getId() == 14296 && pos.getX() == 2669) {
			player.teleport(new Position(player.getPosition().getX() >= 2670 ? 2668 : 2670, 2601), LADDER);
			player.getAttr().get("master_archery").set(player.getPosition().getX() < 2670);
			return false;
		}
		//west south ladder
		if(object.getId() == 14296 && pos.getX() == 2647) {
			player.teleport(new Position(2647, player.getPosition().getY() <= 2585 ? 2587 : 2585), LADDER);
			player.getAttr().get("master_archery").set(!(player.getPosition().getY() <= 2585));
			return false;
		}
		//east south ladder
		if(object.getId() == 14296 && pos.getX() == 2666) {
			player.teleport(new Position(2666, player.getPosition().getY() <= 2585 ? 2587 : 2585), LADDER);
			player.getAttr().get("master_archery").set(!(player.getPosition().getY() <= 2585));
			return false;
		}
		for(PestGate gate : gates) {
			if(gate.clicked(object.getGlobalPos())) {
				gate.click(player);
				return false;
			}
		}
		if(DoorHandler.isDoor(object.getDefinition())) {
			PestGate gate = new PestGate(object);
			gates.add(gate);
			gate.click(player);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean onThirdClickObject(Player player, GameObject object) {
		for(PestGate gate : gates) {
			if(gate.clicked(object.getGlobalPos())) {
				gate.repair(player);
				return true;
			}
		}
		if(DoorHandler.isDoor(object.getDefinition())) {
			PestGate gate = new PestGate(object);
			gates.add(gate);
			gate.repair(player);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean aggression() {
		for(Pest pest : pests) {
			if(pest.isDead())
				continue;
			if(!pest.aggressive())
				continue;
			if(!pest.getCombatBuilder().isAttacking()) {
				if(pest.getPosition().withinDistance(voidKnight.getPosition(), pest.ranged() ? 15 : 5)) {
					pest.getCombatBuilder().attack(voidKnight);
					continue;
				}
				for(Player p : pest.getRegion().getPlayers()) {
					if(p.getPosition().withinDistance(pest.getPosition(), pest.ranged() ? 10 : 5)) {
						pest.getCombatBuilder().attack(p);
						break;
					}
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean canPickup(Player player, GroundItem node) {
		return true;
	}
	
	@Override
	public boolean canPot(Player player, PotionConsumable potion) {
		return true;
	}
	
	@Override
	public boolean canEat(Player player, FoodConsumable food) {
		return true;
	}
	
	@Override
	public Position deathPosition(Player player) {
		return new Position(2656 + RandomUtils.inclusive(3), 2609 + RandomUtils.inclusive(4));
	}
	
	@Override
	public void postDeath(Player player) {
		player.getMovementQueue().reset();
		player.out(new SendWalkable((21100)));
	}
	
	@Override
	public void onInflictDamage(Player player, Actor other, Hit[] inflicted) {
		int add = 0;
		for(Hit hit : inflicted) {
			add += hit.getDamage();
		}
		if(add != 0) {
			player.getAttr().get("participation").set(player.getAttr().get("participation").getInt() + (add / 10));
			player.text(21116, "" + player.getAttr().get("participation").getInt());
		}
	}
	
	void end(boolean won) {
		for(Player p : getPlayers()) {
			logout(p);
			if(won) {
				p.getDialogueBuilder().append(new NpcDialogue(3784, "Congratulations " + p.getFormatUsername() +"!",  "You won the pest control match", "You been awarded, well done."));
				Rights right = p.getRights();
				int donatorBonus = right.equals(Rights.EXTREME_DONATOR) ? 3 : right.equals(Rights.SUPER_DONATOR) ? 2 : right.equals(Rights.DONATOR) ? 1 : 0;
				Currency.PEST_POINTS.getCurrency().recieveCurrency(p, (p.getAttr().get("participation").getInt() / 300) + donatorBonus);
			} else if(voidKnight.getCurrentHealth() > 0) {
				p.getDialogueBuilder().append(new NpcDialogue(3784, p.getFormatUsername() +" you have Failed.", "You did participate enough to take down", "the portals. ", "Try Harder next time."));
			} else {
				p.getDialogueBuilder().append(new NpcDialogue(3784, "All is Lost!", "You could not take down the portals in time.", "Try Harder next time."));
			}
		}
		World.get().getMobs().remove(voidKnight);
		for(PestPortal portal : portals) {
			World.get().getMobs().remove(portal);
		}
		for(Pest pest : pests) {
			World.get().getMobs().remove(pest);
		}
		for(PestGate gate : gates) {
			gate.reset();
		}
		PestControlWaitingLobby.PEST_LOBBY.pestGameOn = false;
		destruct();
	}
	
	boolean portalsAlive() {
		for(PestPortal portal : portals) {
			if(portal.getCurrentHealth() > 0)
				return true;
		}
		return false;
	}
	
	private void start() {
		voidKnight.setGame(this);
		World.get().getMobs().add(voidKnight);
		for(Player p : getPlayers()) {
			spawn(p);
		}
		for(PestPortal portal : portals) {
			portal.setGame(this);
			World.get().getMobs().add(portal);
			portal.spawn(pests);
		}
	}
	
	private void spawn(Player p) {
		p.move(new Position(2656 + RandomUtils.inclusive(3), 2609 + RandomUtils.inclusive(4)));
		p.text(21116, "" + p.getAttr().get("participation").getInt());
		p.out(new SendWalkable((21100)));
		p.getMovementQueue().reset();
	}
	
	private void time(int time) {
		this.time = time;
		for(Player p : getPlayers()) {
			p.text(21117, (time * 6) + " seconds");
		}
		if(time == 0) {
			//kept void alive, lost.
			end(false);
		}
		if(time % 3 == 0) {
			voidKnight.forceChat(RandomUtils.random(YELLS));
		}
	}
	
	public static PestGate getNearestGate(Position position) {
		double distance = 0;
		PestGate selected = null;
		for(PestGate gate : gates) {
			double dis = gate.getPos().getDistance(position);
			if(distance == 0 || dis < distance) {
				selected = gate;
				distance = dis;
			}
		}
		return selected;
	}

}
