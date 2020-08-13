package com.rageps.content.skill.summoning;

import com.rageps.content.skill.SkillData;
import com.rageps.content.skill.action.impl.ProducingSkillAction;
import com.rageps.task.Task;
import com.rageps.world.model.Animation;
import com.rageps.world.model.Graphic;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;

import java.util.Optional;

/**
 * Holds functionality for creating pouch items..
 * @author Artem Batutin
 */
public final class PouchCreation extends ProducingSkillAction {
	
	/**
	 * The summoning data clicked.
	 */
	private final SummoningData data;
	
	/**
	 * Constructs a new {@link PouchCreation} skill action.
	 * @param player {@link #getPlayer()}.
	 * @param data {@link #data}.
	 */
	public PouchCreation(Player player, SummoningData data) {
		super(player, Optional.empty());
		this.data = data;
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			t.cancel();
			getPlayer().animation(new Animation(9068));
			getPlayer().graphic(new Graphic(1207));
		}
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(data.getItems());
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{new Item(data.getPouchId())});
	}
	
	@Override
	public int delay() {
		return 0;
	}
	
	@Override
	public boolean instant() {
		return true;
	}
	
	@Override
	public boolean init() {
		player.closeWidget();
		return true;
	}
	
	@Override
	public boolean canExecute() {
		return true;
	}
	
	@Override
	public double experience() {
		return data.getCreateExperience();
	}
	
	@Override
	public SkillData skill() {
		return SkillData.SUMMONING;
	}
	
}
