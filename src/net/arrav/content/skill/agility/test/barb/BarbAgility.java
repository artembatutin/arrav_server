package net.arrav.content.skill.agility.test.barb;

import net.arrav.action.impl.ObjectAction;
import net.arrav.content.skill.agility.obstacle.ObstacleType;
import net.arrav.content.skill.agility.test.Agility;
import net.arrav.content.skill.agility.test.obstacle.Obstacle;
import net.arrav.content.skill.agility.test.obstacle.impl.ClimbableObstacle;
import net.arrav.content.skill.agility.test.obstacle.impl.FMObstacle;
import net.arrav.content.skill.agility.test.obstacle.impl.WalkableObstacle;
import net.arrav.net.packet.out.SendObjectAnimation;
import net.arrav.task.LinkedTaskSequence;
import net.arrav.world.Animation;
import net.arrav.world.entity.actor.move.ForcedMovement;
import net.arrav.world.entity.actor.move.ForcedMovementManager;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;
import net.arrav.world.entity.object.GameObject;
import net.arrav.world.entity.object.ObjectDirection;
import net.arrav.world.entity.object.ObjectType;

import java.util.OptionalInt;
import java.util.function.Function;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 4-8-2017.
 */
public final class BarbAgility extends Agility {
	
	/**
	 * Constructs a new {@link Agility}.
	 * @param player {@link #player}.
	 * @param object {@link #object}.
	 * @param crossing {@link #crossing}.
	 */
	private BarbAgility(Player player, GameObject object, Obstacle crossing) {
		super(player, object, crossing);
	}
	
	public static void action() {
		for(Obstacles obstacleFunction : Obstacles.values()) {
			ObjectAction action = new ObjectAction() {
				
				@Override
				public boolean click(Player player, GameObject object, int click) {
					Obstacle obstacle = obstacleFunction.obstacles.apply(player);
					
					if(!obstacle.findProperPosition(player)) {
						new BarbAgility(player, object, obstacle).start();
						return true;
					}
					
					Position walk = null;
					Position[] targets = obstacle.start;
					if(targets.length == 0) {
						walk = targets[0];
					} else {
						double dist = -50;
						for(Position p : targets) {
							double d = player.getPosition().getDistance(p);
							if(dist == -50 || dist > d) {
								walk = p;
								dist = d;
							}
						}
					}
					final Position dest = walk;
					player.getMovementQueue().smartWalk(walk);
					player.getMovementListener().append(() -> {
						if(player.getPosition().same(dest)) {
							new BarbAgility(player, object, obstacleFunction.obstacles.apply(player)).start();
						}
					});
					
					return true;
				}
			};
			for(int objId : obstacleFunction.ids) {
				action.registerFirst(objId);
				action.registerFirst(objId + 42003);
			}
		}
	}
	
	@Override
	public double experience() {
		return 39;
	}
	
	public enum Obstacles {
		ROPE_SWING(43526, p -> new FMObstacle(90, OptionalInt.of(3), new Position[]{new Position(2551, 3554), new Position(2552, 3554)}, new Position(p.getPosition().getX(), 3549), 751, 35, 22) {
			@Override
			public boolean findProperPosition(Player player) {
				return false;
			}
			
			@Override
			public boolean crossable(Player player) {
				if(player.getPosition().same(new Position(2552, 3554, 0)) || player.getPosition().same(new Position(2551, 3554, 0))) {
					return true;
				}
				player.message("You must be standing infront one of the ropes.");
				return false;
			}
			
			@Override
			public void initialize(Player player) {
				super.initialize(player);
				player.out(new SendObjectAnimation(new Position(player.getPosition().getX(), 3550), 497, ObjectType.GENERAL_PROP, ObjectDirection.WEST));
			}
		}),
		LOG_BALANCE(43595, p -> new WalkableObstacle(new Position(2551, 3546, 0), new Position(2541, 3546, 0), 762, 35, 13.7) {
			@Override
			public boolean findProperPosition(Player player) {
				return true;
			}
		}),
		OBSTACLE_NET(20211, p -> new ClimbableObstacle(new Position[]{new Position(2539, 3546), new Position(2539, 3545)}, new Position(2537, p.getPosition().getY(), 1), 828, 35, 8.2)),
		BALANCING_LEDGE(2302, p -> new WalkableObstacle(new Position(2536, 3547, 1), new Position(2532, 3547, 1), 756, 35, 22)),
		LADDER_DOWN(3205, p -> new ClimbableObstacle(new Position(2532, 3546, 1), new Position(2532, 3546, 0), 828, 30, 0)),
		CRUMBLING_WALL(1948, p -> {
			boolean startPoint = p.getPosition().getDistance(new Position(2538, 3553)) < p.getPosition().getDistance(new Position(2543, 3553));
			Position destination = startPoint ? new Position(2538, 3553) : new Position(2543, 3553);
			return new FMObstacle(57, OptionalInt.of(2), new Position[]{new Position(2536, 3553), new Position(2541, 3553)}, destination, 839, 35, 13.7) {
				@Override
				public boolean crossable(Player player) {
					if(player.getPosition().same(new Position(2536, 3553, 0)) || player.getPosition().same(new Position(2541, 3553, 0))) {
						return true;
					}
					
					player.message("You can't cross this obstacle from this side.");
					return false;
				}
			};
		}),
		//ADVANCED
		RUN_UP_WALL(43533, p -> new FMObstacle(50, OptionalInt.of(1), new Position(2538, 3542, 0), new Position(2538, 3545, 2), 10439, 90, 15) {
			@Override
			public void initialize(Player player) {
				player.facePosition(new Position(2538, 3543, 0));
				LinkedTaskSequence sequence = new LinkedTaskSequence();
				sequence.connect(1, () -> player.animation(new Animation(10492)));
				sequence.connect(7, () -> {
					player.move(new Position(2538, 3543, 2));
					ForcedMovement movement = new ForcedMovement(player);
					movement.setFirst(player.getPosition());
					movement.setSecond(new Position(2538, 3545, 2));
					movement.setSecondSpeed(20);
					movement.setAnimation(10493);
					ForcedMovementManager.submit(player, movement);
				});
				sequence.start();
			}
		}),
		CLIMB_UP_WALL(43597, p -> new ClimbableObstacle(new Position(2537, 3546, 2), new Position(2536, 3546, 3), ObstacleType.CLIMB_UP_WALL.getAnimation(), 90, 15) {
			@Override
			public void initialize(Player player) {
				player.facePosition(new Position(2536, 3546));
				
				super.initialize(player);
			}
			
			@Override
			public void onStop(Player player) {
				player.animation(new Animation(11794));
			}
		}),
		SPRING_DEVICE(43587, p -> new FMObstacle(80, OptionalInt.of(4), new Position(2533, 3547, 3), new Position(2532, 3553, 3), 4189, 90, 15) {
			@Override
			public void initialize(Player player) {
				player.facePosition(new Position(player.getPosition().getX(), player.getPosition().getY() + 1));
				player.out(new SendObjectAnimation(new Position(2532, 3544, 3), 11819, ObjectType.GENERAL_PROP, ObjectDirection.EAST));
				
				super.initialize(player);
			}
		}),
		BALANCE_BEAM(43527, P -> new FMObstacle(-1, OptionalInt.empty(), new Position(2533, 3553, 3), new Position(2536, 3553, 3), 16079, 90, 15) {
			@Override
			public void initialize(Player player) {
				ForcedMovement movement = new ForcedMovement(player);
				movement.setFirst(player.getPosition());
				movement.setSecond(new Position(2535, 3553, 3));
				movement.setFirstSpeed(10);
				movement.setSecondSpeed(65);
				movement.setAnimation(animation);
				ForcedMovementManager.submit(player, movement);
			}
		}),
		JUMP_GAP(85534, p -> new FMObstacle(35, OptionalInt.of(1), new Position(2535, 3553, 3), new Position(2538, 3553, 2), 2586, 90, 15) {
			@Override
			public void onStop(Player player) {
				player.animation(new Animation(2588));
			}
		}),
		SLIDE_DOWN_ROOF(43532, p -> new FMObstacle(-1, OptionalInt.empty(), new Position(2539, p.getPosition().getY(), 2), new Position(2543, p.getPosition().getY(), 0), ObstacleType.SLIDE_ROOF.getAnimation(), 90, 615) {
			@Override
			public void initialize(Player player) {
				final ForcedMovement[] movements = new ForcedMovement[]{new ForcedMovement(player), // first slide
						new ForcedMovement(player), // second slide
						new ForcedMovement(player), // jump down
				};
				
				LinkedTaskSequence seq = new LinkedTaskSequence();
				seq.connect(2, () -> {
					player.animation(new Animation(2588, 20));
					movements[0].setFirst(player.getPosition());
					movements[0].setSecond(new Position(2540, player.getPosition().getY(), 1));
					movements[0].setSecondSpeed(60);
					movements[0].setTimer(2);
					movements[0].setAnimation(11792);
					ForcedMovementManager.submit(player, movements[0]);
				});
				seq.connect(2, () -> {
					movements[1].setFirst(new Position(2540, player.getPosition().getY(), 1));
					movements[1].setSecond(new Position(2542, player.getPosition().getY(), 1));
					movements[1].setSecondSpeed(20);
					movements[1].setAnimation(11790);
					movements[1].setTimer(2);
					ForcedMovementManager.submit(player, movements[1]);
				});
				seq.connect(2, () -> {
					movements[2].setFirst(new Position(2542, player.getPosition().getY(), 1));
					movements[2].setSecond(end);
					movements[2].setSecondSpeed(20);
					movements[2].setAnimation(11791);
					movements[2].setTimer(0);
					ForcedMovementManager.submit(player, movements[2]);
				});
				seq.start();
			}
		});
		
		public final int[] ids;
		
		Function<Player, Obstacle> obstacles;
		
		Obstacles(int[] ids, Function<Player, Obstacle> obstacles) {
			this.obstacles = obstacles;
			this.ids = ids;
		}
		
		Obstacles(int id, Function<Player, Obstacle> obstacles) {
			this.obstacles = obstacles;
			this.ids = new int[]{id};
		}
	}
}
