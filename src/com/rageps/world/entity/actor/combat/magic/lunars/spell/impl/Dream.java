package com.rageps.world.entity.actor.combat.magic.lunars.spell.impl;

import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.task.LinkedTaskSequence;
import com.rageps.task.Task;
import com.rageps.world.Animation;
import com.rageps.world.Graphic;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.magic.MagicRune;
import com.rageps.world.entity.actor.combat.magic.RequiredRune;
import com.rageps.world.entity.actor.combat.magic.lunars.spell.LunarButtonSpell;

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
		super("Dream", 117226, 79, 82, new RequiredRune(MagicRune.ASTRAL_RUNE, 2), new RequiredRune(MagicRune.COSMIC_RUNE, 1), new RequiredRune(MagicRune.BODY_RUNE, 5));
	}
	
	@Override
	public void effect(Actor caster, Optional<Actor> victim) {
		super.effect(caster, victim);
		
		caster.toPlayer().getActivityManager().disable();
		World.get().submit(new DreamTask(caster.toPlayer()));
	}
	
	@Override
	public boolean canCast(Actor caster, Optional<Actor> victim) {
		if(!super.canCast(caster, victim)) {
			return false;
		}
		if(caster.getAttr().get("lunar_dream").getBoolean()) {
			caster.toPlayer().message("You are already dreaming...");
			return false;
		}
		
		if(caster.getCurrentHealth() >= caster.toPlayer().getMaximumHealth()) {
			caster.toPlayer().message("You have no need to cast this spell since your life points are already full.");
			return false;
		}
		return true;
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
