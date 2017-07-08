package net.edge.content.skill.summoning;

import net.edge.content.skill.SkillData;
import net.edge.content.skill.action.impl.ProducingSkillAction;
import net.edge.task.Task;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.Optional;

/**
 * Holds functionality for creating pouch items..
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class PouchCreation extends ProducingSkillAction {
	
	/**
	 * The summoning data clicked.
	 */
	private final SummoningData data;
	
	/**
	 * Constructs a new {@link PouchCreation} skill action.
	 * @param player {@link #getPlayer()}.
	 * @param data   {@link #data}.
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
