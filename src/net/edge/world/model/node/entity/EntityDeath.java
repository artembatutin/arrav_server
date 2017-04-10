package net.edge.world.model.node.entity;

import net.edge.task.Task;
import net.edge.world.World;

/**
 * The parent class that handles the death process for all characters.
 * @param <T> the type of character the death process is being executed for.
 * @author lare96 <http://github.com/lare96>
 */
public abstract class EntityDeath<T extends EntityNode> extends Task {

	/**
	 * The character who has died and needs the death process.
	 */
	private final T character;

	/**
	 * The counter that will determine which part of the death process we are
	 * on.
	 */
	private int counter;

	/**
	 * Creates a new {@link EntityDeath}.
	 * @param character the character who has died and needs the death process.
	 */
	public EntityDeath(T character) {
		super(1, true);
		this.character = character;
	}

	/**
	 * The part of the death process where the character is prepared for the
	 * rest of the death process.
	 */
	public abstract void preDeath();

	/**
	 * The main part of the death process where the killer is found for the
	 * character.
	 */
	public abstract void death();

	/**
	 * The last part of the death process where the character is reset.
	 */
	public abstract void postDeath();

	@Override
	public final void execute() {
		switch(counter++) {
		case 0:
			getCharacter().setDead(true);
			getCharacter().getPoisonDamage().set(0);
			getCharacter().getMovementQueue().reset();
			getCharacter().unfreeze();
			getCharacter().unStun();
			if(getCharacter().isPlayer()) {
				getCharacter().toPlayer().getMessages().sendConfig(174, 0);
			}
			break;
		case 1:
			preDeath();
			break;
		case 5:
			death();
			break;
		case 6:
			postDeath();
			if(getCharacter().isPlayer()) {
				getCharacter().setDead(false);
			}
			this.cancel();
			break;
		}
	}

	@Override
	public final void onException(Exception e) {
		e.printStackTrace();
		if(getCharacter().isPlayer()) {
			World.queueLogout(getCharacter().toPlayer());
		} else if(getCharacter().isNpc()) {
			World.getNpcs().remove(getCharacter().toNpc());
		}
	}
	
	public T getCharacter() {
		return character;
	}
	
	@Override
	public int getCounter() {
		return counter;
	}
	
	public void setCounter(int counter) {
		this.counter = counter;
	}
}