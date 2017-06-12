package net.edge.world.node.entity.move;

import net.edge.task.Task;
import net.edge.locale.Position;
import net.edge.locale.loc.Location;
import net.edge.world.World;
import net.edge.world.node.NodeType;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.Direction;
import net.edge.world.node.entity.move.path.Path;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;

/**
 * The movement queue sequencer that handles the entire movement process for
 * characters.
 * @author lare96 <http://github.com/lare96>
 */
public final class MovementQueue {
	
	/**
	 * The character this movement queue is for.
	 */
	private final EntityNode character;
	
	/**
	 * A double ended queue of waypoints in this movement queue.
	 */
	private Deque<Point> waypoints = new LinkedList<>();
	
	/**
	 * The task ran when following another character.
	 */
	private Optional<Task> followTask = Optional.empty();
	
	/**
	 * The flag that determines if the run button is toggled.
	 */
	private boolean running = false;
	
	/**
	 * The flag that determines if the current path is a run path.
	 */
	private boolean runPath = false;
	
	/**
	 * The flag that determines if movement is locked.
	 */
	private boolean lockMovement;
	
	/**
	 * Creates a new {@link MovementQueue}.
	 * @param character the character this movement queue is for.
	 */
	public MovementQueue(EntityNode character) {
		this.character = character;
	}
	
	/**
	 * Executes movement processing which primarily consists of polling
	 * waypoints, and updating the map region.
	 * @throws Exception if any errors occur while sequencing movement.
	 */
	public void sequence() {
		Point walkPoint;
		Point runPoint = null;
		
		walkPoint = waypoints.poll();
		
		if(running) {
			runPoint = waypoints.poll();
		}
		
		runPath = runPoint != null;
		
		if(walkPoint != null && walkPoint.getDirection() != Direction.NONE) {
			int x = walkPoint.getDirection().getX();
			int y = walkPoint.getDirection().getY();
			//boolean traversable = World.getTraversalMap().isTraversable(character.getPosition(), walkPoint.getDirection(), character.size());
			//if(!traversable) {
			//	reset();
			//	return;
			//}
			if(character.isFollowing() && character.getFollowEntity() != null) {
				if(character.getPosition().move(x, y).same(character.getFollowEntity().getPosition())) {
					return;
				}
			}
			
			character.setPosition(character.getPosition().move(x, y));
			character.setPrimaryDirection(walkPoint.getDirection());
			character.setLastDirection(walkPoint.getDirection());
			
			if(character.isPlayer()) {
				character.toPlayer().sendInterfaces();
			}
		}
		
		if(runPoint != null && runPoint.getDirection() != Direction.NONE) {
			int x = runPoint.getDirection().getX();
			int y = runPoint.getDirection().getY();
			//boolean traversable = World.getTraversalMap().isTraversable(character.getPosition(), runPoint.getDirection(), character.size());
			//if(!traversable) {
			//	reset();
			//	return;
			//}
			if(character.isFollowing() && character.getFollowEntity() != null) {
				if(character.getPosition().move(x, y).same(character.getFollowEntity().getPosition())) {
					return;
				}
			}
			
			if(character.isPlayer()) {
				Player player = character.toPlayer();
				if(player.getRights().less(Rights.ADMINISTRATOR)) {
					if(player.getRunEnergy() > 0 && !(player.isNightMaxed() && !Location.inWilderness(player))) {
						double drainRate = 0.7D;
						double weight = player.getWeight();
						double weightFactor = (weight > 1.371D ? (int) weight * 0.00729166D : 0.0D);
						weightFactor = Math.round(weightFactor * 100.0D) / 100.0D;
						player.setRunEnergy(player.getRunEnergy() - (drainRate + weightFactor));
						player.sendInterfaces();
						player.getMessages().sendRunEnergy();
					} else {
						running = false;
						player.getMessages().sendConfig(173, 0);
					}
				}
			}
			
			character.setPosition(character.getPosition().move(x, y));
			character.setSecondaryDirection(runPoint.getDirection());
			character.setLastDirection(runPoint.getDirection());
		}
		
		if(character.isPlayer()) {
			int deltaX = character.getPosition().getX() - character.getLastRegion().getRegionX() * 8;
			int deltaY = character.getPosition().getY() - character.getLastRegion().getRegionY() * 8;
			
			if(deltaX < 16 || deltaX >= 88 || deltaY < 16 || deltaY > 88 || character.isNeedsRegionUpdate()) {
				character.toPlayer().getMessages().sendMapRegion();
			}
		}
	}
	
	/**
	 * Forces the character to walk to a certain position point relevant to its
	 * current position.
	 * @param addX the amount of spaces to walk to the {@code X}.
	 * @param addY the amount of spaces to walk to the {@code Y}.
	 */
	public void walk(int addX, int addY) {
		walk(new Position(character.getPosition().getX() + addX, character.getPosition().getY() + addY));
	}
	
	/**
	 * Forces the character to walk to a certain position point not relevant to
	 * its current position.
	 * @param position the position the character is moving too.
	 */
	public void walk(Position position) {
		reset();
		addToPath(position);
		finish();
	}
	
	/**
	 * Forces the character to walk to a certain position with the path specified.
	 * @param path the path to be taken.
	 */
	public void walk(Deque<Position> path) {
		reset();
		path.forEach(this::addToPath);
		finish();
	}
	
	/**
	 * Forces the character to walk to a certain position point not relevant to
	 * its current position with the {@code AStarPathfinder} algorithm.
	 * @param position the position the character is moving too.
	 */
	public void smartWalk(Position position) {
		Path path = World.getAStarPathFinder().find(character, position);
		if(path != null && path.isPossible()) {
			walk(path.getMoves());
		} else {
			reset();
		}
	}
	
	/**
	 * Resets the walking queue for this character.
	 */
	public void reset() {
		runPath = false;
		waypoints.clear();
		Position p = character.getPosition();
		waypoints.add(new Point(p.getX(), p.getY(), Direction.NONE));
	}
	
	/**
	 * Finishes the current path for this character.
	 */
	public void finish() {
		waypoints.removeFirst();
	}
	
	/**
	 * Determines if this walking queue is finished or not.
	 * @return {@code true} if this walking queue is finished, {@code false}
	 * otherwise.
	 */
	public boolean isMovementDone() {
		return waypoints.size() == 0;
	}
	
	/**
	 * Adds a new position to the walking queue.
	 * @param position the position to add.
	 */
	public void addToPath(Position position) {
		if(lockMovement || character.isFrozen() || character.isStunned()) {
			if(character.isPlayer()) {
				Player player = (Player) character;
				if(player.isFrozen())
					player.message("You're currently frozen and you cannot move.");
				if(player.isStunned())
					player.message("You're currently stunned and you cannot move.");
			}
			return;
		}
		if(waypoints.size() == 0) {
			reset();
		}
		Point last = waypoints.peekLast();
		int deltaX = position.getX() - last.getX();
		int deltaY = position.getY() - last.getY();
		int max = Math.max(Math.abs(deltaX), Math.abs(deltaY));
		for(int i = 0; i < max; i++) {
			if(deltaX < 0) {
				deltaX++;
			} else if(deltaX > 0) {
				deltaX--;
			}
			if(deltaY < 0) {
				deltaY++;
			} else if(deltaY > 0) {
				deltaY--;
			}
			addStep(position.getX() - deltaX, position.getY() - deltaY);
		}
	}
	
	/**
	 * Adds a step to the walking queue.
	 * @param x the {@code X} coordinate of the step.
	 * @param y the {@code Y} coordinate of the step.
	 */
	private void addStep(int x, int y) {
		if(waypoints.size() >= 100) {
			return;
		}
		Point last = waypoints.peekLast();
		int deltaX = x - last.getX();
		int deltaY = y - last.getY();
		Direction dir = Direction.fromDeltas(deltaX, deltaY);
		if(dir.getId() > -1) {
			waypoints.add(new Point(x, y, dir));
		}
	}
	
	/**
	 * Prompts the controller of this movement queue to follow {@code leader}.
	 * @param leader the character being followed.
	 */
	public void follow(EntityNode leader) {
		if(character.getFollowEntity() != null && character.getFollowEntity().same(leader)) {
			return;
		}
		if(character.isFollowing() && !leader.same(character.getFollowEntity())) {
			character.faceEntity(null);
			character.setFollowing(false);
			character.setFollowEntity(null);
		}
		if(!character.isFollowing()) {
			followTask.ifPresent(Task::cancel);
		}
		if(!followTask.isPresent()) {
			character.setFollowing(true);
			character.setFollowEntity(leader);
			followTask = Optional.of(new CharacterFollowTask(character, leader));
			World.get().submit(followTask.get());
		}
	}
	
	/**
	 * Determines if the run button is toggled.
	 * @return {@code true} if the run button is toggled, {@code false}
	 * otherwise.
	 */
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * Sets the value for {@link MovementQueue#running}.
	 * @param runToggled the new value to set.
	 */
	public void setRunning(boolean runToggled) {
		if(character.getType().equals(NodeType.PLAYER)) {
			Player player = (Player) character;
			player.getMessages().sendConfig(173, runToggled ? 0 : 1);
		}
		this.running = runToggled;
	}
	
	/**
	 * Determines if the current path is a run path.
	 * @return {@code true} if the current path is a run path, {@code false}
	 * otherwise.
	 */
	public boolean isRunPath() {
		return runPath;
	}
	
	/**
	 * Sets the value for {@link MovementQueue#runPath}.
	 * @param runPath the new value to set.
	 */
	public void setRunPath(boolean runPath) {
		this.runPath = runPath;
	}
	
	/**
	 * Determines if the movement queue is locked.
	 * @return {@code true} if the movement queue is locked, {@code false}
	 * otherwise.
	 */
	public boolean isLockMovement() {
		return lockMovement;
	}
	
	/**
	 * Sets the value for {@link MovementQueue#lockMovement}.
	 * @param lockMovement the new value to set.
	 */
	public void setLockMovement(boolean lockMovement) {
		this.lockMovement = lockMovement;
	}
	
	/**
	 * Sets the value for {@link MovementQueue#followTask}.
	 * @param followTask the new value to set.
	 */
	public void setFollowTask(Optional<Task> followTask) {
		this.followTask = followTask;
	}
	
	/**
	 * The internal position type class with support for direction.
	 * @author blakeman8192
	 */
	private final class Point extends Position {
		
		/**
		 * The direction to this point.
		 */
		private final Direction direction;
		
		/**
		 * Creates a new {@link Point}.
		 * @param x         the {@code X} coordinate.
		 * @param y         the {@code Y} coordinate.
		 * @param direction the direction to this point.
		 */
		public Point(int x, int y, Direction direction) {
			super(x, y);
			this.direction = direction;
		}
		
		/**
		 * Gets the direction to this point.
		 * @return the direction.
		 */
		public Direction getDirection() {
			return direction;
		}
	}
	
}