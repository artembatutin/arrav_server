package com.rageps.world.entity.actor.combat.magic.lunars.spell.impl;

import com.rageps.content.dialogue.impl.OptionDialogue;
import com.rageps.content.skill.magic.Spellbook;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.task.LinkedTaskSequence;
import com.rageps.task.Task;
import com.rageps.world.Animation;
import com.rageps.world.Graphic;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.magic.MagicRune;
import com.rageps.world.entity.actor.combat.magic.RequiredRune;
import com.rageps.world.entity.actor.combat.magic.lunars.LunarSpell;
import com.rageps.world.entity.actor.player.PlayerAttributes;

import java.util.Optional;

/**
 * Holds functionality for the spellbook swap lunar spell.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class SpellbookSwap extends LunarSpell {
	
	/**
	 * The option type the player selected;
	 */
	private final OptionDialogue.OptionType type;
	
	/**
	 * Constructs a new {@link SpellbookSwap}.
	 * @param type the type the player clicked.
	 */
	public SpellbookSwap(OptionDialogue.OptionType type) {
		super("Spellbook Swap", 96, 130, new RequiredRune(MagicRune.LAW_RUNE, 1), new RequiredRune(MagicRune.COSMIC_RUNE, 2), new RequiredRune(MagicRune.ASTRAL_RUNE, 3));
		this.type = type;
		
	}
	
	@Override
	public void effect(Actor caster, Optional<Actor> victim) {
		super.effect(caster, victim);
		caster.toPlayer().getActivityManager().disable();
		LinkedTaskSequence seq = new LinkedTaskSequence();
		seq.connect(11, () -> {
			Spellbook.convert(caster.toPlayer(), type.equals(OptionDialogue.OptionType.FIRST_OPTION) ? Spellbook.NORMAL : Spellbook.ANCIENT);
			caster.getAttributeMap().set(PlayerAttributes.LUNAR_SPELLBOOK_SWAP, true);
			World.get().submit(new SpellbookSwapTask(caster.toPlayer(), type.equals(OptionDialogue.OptionType.FIRST_OPTION) ? Spellbook.NORMAL : Spellbook.ANCIENT));
			caster.toPlayer().getActivityManager().enable();
		});
		seq.start();
		
	}
	
	@Override
	public boolean canCast(Actor caster, Optional<Actor> victim) {
		if(!super.canCast(caster, victim)) {
			return false;
		}
		
		if(type.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
			return false;
		}
		return true;
	}
	
	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(new Animation(6299));
	}
	
	@Override
	public Optional<Graphic> startGraphic() {
		return Optional.of(new Graphic(1062));
	}
	
	/**
	 * Holds functionality for the timers when the spell
	 * is casted.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private static final class SpellbookSwapTask extends Task {
		
		/**
		 * The player whom has casted this spell.
		 */
		private final Player player;
		
		/**
		 * The spellbook this player has selected.
		 */
		private final Spellbook spellbook;
		
		/**
		 * The timer which counts howmany cycles this task has been running for.
		 */
		private int timer;
		
		/**
		 * Constructs a new {@link SpellbookSwapTask}.
		 * @param player {@link #player}.
		 * @param spellbook {@link #spellbook}.
		 */
		public SpellbookSwapTask(Player player, Spellbook spellbook) {
			super(1, false);
			this.player = player;
			this.spellbook = spellbook;
			this.attach(player);
		}
		
		@Override
		public void execute() {
			if(!player.getAttributeMap().getBoolean(PlayerAttributes.LUNAR_SPELLBOOK_SWAP)) {
				this.cancel();
				return;
			}
			if(++timer >= 200) {
				this.cancel();
			}
		}
		
		@Override
		public void onCancel() {
			if(timer >= 200) {
				player.message("The timer ran out and your spellbook has been restored.");
			}
			if(player.getSpellbook().equals(spellbook)) {
				Spellbook.convert(player, Spellbook.LUNAR);
			}
		}
	}
}
