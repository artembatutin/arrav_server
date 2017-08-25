package net.edge.content.combat.content.lunars.impl.spells;

import net.edge.task.LinkedTaskSequence;
import net.edge.task.Task;
import net.edge.content.combat.magic.lunars.impl.LunarButtonSpell;
import net.edge.world.World;
import net.edge.world.entity.actor.Actor;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.Optional;

/**
 * Holds functionality for the dream spell.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Dream extends LunarButtonSpell {

	/**
	 * Constructs a new {@link Dream}.
	 */
	public Dream() {
		super(117226);
	}

	@Override
	public void effect(Player caster, Actor victim) {
		caster.getActivityManager().disable();
		World.get().submit(new DreamTask(caster));
	}

	@Override
	public boolean prerequisites(Player caster, Actor victim) {
		if(caster.getAttr().get("lunar_dream").getBoolean()) {
			caster.message("You are already dreaming...");
			return false;
		}

		if(caster.getCurrentHealth() >= caster.getMaximumHealth()) {
			caster.message("You have no need to cast this spell since your life points are already full.");
			return false;
		}
		return true;
	}

	@Override
	public String name() {
		return "Dream";
	}

	@Override
	public int levelRequired() {
		return 79;
	}

	@Override
	public double baseExperience() {
		return 82;
	}

	@Override
	public Optional<Item[]> itemsRequired(Player player) {
		return Optional.of(new Item[]{new Item(9075, 2), new Item(564, 1), new Item(559, 5)});
	}

	/**
	 * The dream task responsible for playing the animations and resetting the dream spell
	 * if the player is no longer dreaming.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private static final class DreamTask extends Task {

		/**
		 * The player this task is dependent of.
		 */
		private final Player player;

		/**
		 * Constructs a new {@link DreamTask}.
		 * @param player {@link #player}.
		 */
		public DreamTask(Player player) {
			super(5);
			this.player = player;
		}

		@Override
		protected void onSubmit() {
			player.animation(new Animation(6295));
			player.getAttr().get("lunar_dream").set(true);
		}

		private boolean replenished;

		@Override
		protected void execute() {
			if(player.getCurrentHealth() >= player.getMaximumHealth()) {
				player.message("You feel fully replenished...");
				player.getAttr().get("lunar_dream").set(false);
				replenished = true;
				this.cancel();
				return;
			}
			if(!player.getAttr().get("lunar_dream").getBoolean()) {
				this.cancel();
				return;
			}

			player.animation(new Animation(6296));
			player.graphic(new Graphic(1056));

		}

		@Override
		protected void onCancel() {
			if(replenished) {
				LinkedTaskSequence seq = new LinkedTaskSequence();
				seq.connect(2, () -> player.getActivityManager().enable());
				seq.start();
			}
			player.animation(new Animation(6297));
			player.graphic(null);
		}

	}

}
