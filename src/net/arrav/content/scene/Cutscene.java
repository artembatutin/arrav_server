package net.arrav.content.scene;

import net.arrav.task.Task;
import net.arrav.world.World;

import java.util.Optional;

/**
 * Holds functionality for cutscenes.
 * @author Artem Batutin <artembatutin@gmail.com>
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class Cutscene {

	/**
	 * The stage this cutscene is at.
	 */
	private int stage;

	/**
	 * The backing task running for this {@link Cutscene}.
	 */
	private Optional<CutsceneTask> task = Optional.empty();

	/**
	 * Constructs a new {@link Cutscene}.
	 * @param stage the stage this cutscene is at.
	 */
	public Cutscene(int stage) {
		this.stage = stage;
	}

	/**
	 * Constructs a new {@link Cutscene} with the stage set at 1.
	 */
	public Cutscene() {
		this.stage = 1;
	}

	/**
	 * The method which will submit this cutscene to the world.
	 */
	public final void submit() {
		if(task.isPresent()) {
			return;
		}

		task = Optional.of(new CutsceneTask(this));
		World.get().submit(task.get());
	}

	/**
	 * The method which will destruct this cutscene from the world.
	 */
	public final void destruct() {
		if(!task.isPresent()) {
			return;
		}
		task.get().cancel();
	}

	/**
	 * Any functionality that should be executed on submit.
	 */
	public abstract void onSubmit();

	/**
	 * Any functionality that should be executed while the task is running.
	 * @param t the backing task running for this cutscene.
	 */
	public abstract void execute(Task t);

	/**
	 * Any functionality tha should be executed on stop.
	 */
	public abstract void onCancel();

	/**
	 * @return the stage
	 */
	public int getStage() {
		return stage;
	}

	/**
	 * @param stage the stage to set
	 */
	public void setStage(int stage) {
		this.stage = stage;
	}

	/**
	 * Holds functionality for the backing cutscene.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private static final class CutsceneTask extends Task {

		/**
		 * The cutscene this task is running for.
		 */
		private final Cutscene scene;

		/**
		 * Constructs a new {@link CutsceneTask}.
		 * @param scene {@link #scene}.
		 */
		public CutsceneTask(Cutscene scene) {
			super(1, false);
			this.scene = scene;
		}

		@Override
		public void onSubmit() {
			scene.onSubmit();
		}

		@Override
		public void execute() {
			scene.execute(this);
		}

		@Override
		public void onCancel() {
			scene.onCancel();
			scene.task = Optional.empty();
			scene.setStage(0);
		}

	}
}
