package net.edge.content.commands.impl;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.content.skill.Skill;
import net.edge.content.skill.Skills;
import net.edge.game.GameConstants;
import net.edge.locale.Position;
import net.edge.net.session.BotSession;
import net.edge.task.Task;
import net.edge.util.TextUtils;
import net.edge.util.rand.RandomUtils;
import net.edge.world.World;
import net.edge.world.node.entity.npc.NpcAggression;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

import java.util.Optional;

@CommandSignature(alias = {"stress"}, rights = {Rights.DEVELOPER}, syntax = "Use this command as ::stress")
public final class StressCommand implements Command {

	private static int id = 0;
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Task process = new Task(1, false) {
			@Override
			protected void execute() {
				for(int i = 0; i < 40; i++) {
					int x = 3072 + RandomUtils.inclusive(20);
					int y = 3539 + RandomUtils.inclusive(20);
					id++;Player p1 = createBot(id, new Position(x, y));
					p1.setAutoRetaliate(true);
					id++;Player p2 = createBot(id, new Position(x, y));
					p2.setAutoRetaliate(true);
					World.get().queueLogin(p1);
					World.get().queueLogin(p2);
					Task fight = new Task(4, false) {
						@Override
						protected void execute() {
							p1.getCombatBuilder().attack(p2);
							p2.getCombatBuilder().attack(p1);
							cancel();
						}
					};
					fight.submit();
					if(id > 300)
						cancel();
				}
			}
		};
		process.submit();
		
		
		// WALKING STRESS
		/*int spawns = 1400;
		while(spawns > 0) {
			int x = 3077 + RandomUtils.inclusive(22);
			int y = 3484 + RandomUtils.inclusive(40);
			Optional<Position> pos = World.getTraversalMap().getRandomTraversableTile(new Position(x, y), 1);
			if(!pos.isPresent())
				continue;
			bots.add(createBot(spawns, pos.get()));
			spawns--;
		}
		bots.forEach(b -> World.get().queueLogin(b));
		
		Task process = new Task(40, false) {
			@Override
			protected void execute() {
				System.out.println("RANDOMIZED CLIPPED A* PATHFINDER WALKING INITIATED");
				bots.forEach(b -> {
					Task walk = new Task(2, false) {//each 2 ticks check!
						@Override
						protected void execute() {
							if(b.getMovementQueue().isMovementDone()) {
								boolean xNeg = RandomUtils.nextBoolean();
								boolean yNeg = RandomUtils.nextBoolean();
								int x = RandomUtils.inclusive(5);
								int y = RandomUtils.inclusive(5);
								b.getMovementQueue().smartWalk(new Position(b.getPosition().getX() + (xNeg ? -x : x), b.getPosition().getY() + (yNeg ? -y : y)));
							}
						}
					};
					walk.submit();
				});
				cancel();
			}
		};
		process.submit();*/

		Task ticks = new Task(2, false) {
			@Override
			protected void execute() {
				player.message("Took " + World.millis + "ms on sync.");
			}
		};
		ticks.submit();
	}
	
	public Player createBot(int id, Position pos) {
		String name = "bot" + id;
		Player bot = new Player(TextUtils.nameToHash(name), false);
		bot.setUsername(name);
		bot.setPassword("123");
		bot.setRights(Rights.PLAYER);
		Skills.create(bot);
		for(Skill s : bot.getSkills()) {
			s.setRealLevel(99);
			s.setLevel(99, true);
		}
		bot.healEntity(90);
		bot.setSession(new BotSession(bot));
		bot.setVisible(true);
		bot.setPosition(pos);
		return bot;
	}

}
