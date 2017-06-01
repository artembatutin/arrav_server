package net.edge.content.combat.magic.lunars.impl.spells;

import net.edge.task.LinkedTaskSequence;
import net.edge.task.Task;
import net.edge.World;
import net.edge.content.combat.magic.lunars.LunarSpell;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Spellbook;
import net.edge.world.node.item.Item;

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
		this.type = type;
	}
	
	@Override
	public void effect(Player caster, EntityNode victim) {
		caster.getActivityManager().disable();
		LinkedTaskSequence seq = new LinkedTaskSequence();
		seq.connect(11, () -> {
			Spellbook.convert(caster, type.equals(OptionDialogue.OptionType.FIRST_OPTION) ? Spellbook.NORMAL : Spellbook.ANCIENT);
			caster.getAttr().get("lunar_spellbook_swap").set(true);
			World.submit(new SpellbookSwapTask(caster, type.equals(OptionDialogue.OptionType.FIRST_OPTION) ? Spellbook.NORMAL : Spellbook.ANCIENT));
			caster.getActivityManager().enable();
		});
		seq.start();
		
	}
	
	@Override
	public boolean prerequisites(Player caster, EntityNode victim) {
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
	
	@Override
	public String name() {
		return "Spellbook Swap";
	}
	
	@Override
	public int levelRequired() {
		return 96;
	}
	
	@Override
	public double baseExperience() {
		return 130;
	}
	
	@Override
	public Optional<Item[]> itemsRequired(Player player) {
		return Optional.of(new Item[]{new Item(563, 1), new Item(564, 2), new Item(9075, 3)});
	}
	
	/**
	 * Holds functionality for the timers when the {@link LunarSpells#SPELLBOOK_SWAP}
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
		 * @param player    {@link #player}.
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
			if(!player.getAttr().get("lunar_spellbook_swap").getBoolean()) {
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
