package net.edge.world.content.skill.firemaking;

import net.edge.task.Task;
import net.edge.utils.TextUtils;
import net.edge.world.World;
import net.edge.world.content.skill.SkillData;
import net.edge.world.content.skill.action.impl.DestructionSkillAction;
import net.edge.world.content.skill.firemaking.pits.FirepitData;
import net.edge.world.content.skill.firemaking.pits.FirepitObject;
import net.edge.world.model.node.entity.model.Animation;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;
import net.edge.world.model.node.object.ObjectNode;

import java.util.Optional;

public final class Bonfire extends DestructionSkillAction {
	
	private final LogType log;
	
	private final ObjectNode object;
	
	private final FirepitObject pit;
	
	private int amount;
	
	public Bonfire(Player player, ObjectNode object, LogType log, FirepitObject pit) {
		super(player, Optional.of(object.getPosition()));
		this.object = object;
		this.log = log;
		this.pit = pit;
		this.amount = player.getInventory().computeAmountForId(log.getLog().getId());
	}
	
	public static boolean addLogs(Player player, Item item, ObjectNode object, boolean click) {
		FirepitData data = FirepitData.VALUES.stream().filter(d -> d.getObjectId() == object.getId()).findAny().orElse(null);
		
		if(data == null) {
			return false;
		}
		
		LogType log = click ? LogType.getDefinition(player).orElse(null) : LogType.getDefinition(item.getId()).orElse(null);
		
		if(log == null) {
			return false;
		}
		
		FirepitObject pit = World.getFirepitEvent().getFirepit();
		boolean logs = FireLighter.VALUES.stream().anyMatch(def -> def.getObjectId() == object.getId());
		
		if(pit == null && !logs) {
			player.message("You can't add logs to " + TextUtils.appendIndefiniteArticle(object.getDefinition().getName()) + ".");
			return false;
		}
		
		Bonfire boneFire = new Bonfire(player, object, log, pit);
		boneFire.start();
		return true;
	}
	
	@Override
	public void onDestruct(Task t, boolean success) {
		if(success) {
			pit.increment();
			if(amount-- <= 0) {
				t.cancel();
			}
		}
	}
	
	@Override
	public Optional<Animation> animation() {
		return Optional.of(new Animation(827));
	}
	
	@Override
	public Item destructItem() {
		return log.getLog();
	}
	
	@Override
	public int delay() {
		return 4;
	}
	
	@Override
	public boolean instant() {
		return true;
	}
	
	@Override
	public boolean init() {
		return checkFiremaking();
	}
	
	@Override
	public boolean canExecute() {
		return checkFiremaking();
	}
	
	@Override
	public double experience() {
		return pit != null && pit.isActive() ? log.getExperience() * 1.2 : log.getExperience();
	}
	
	@Override
	public SkillData skill() {
		return SkillData.FIREMAKING;
	}
	
	@Override
	public void onStop() {
		player.animation(null);
	}
	
	private boolean checkFiremaking() {
		if(!World.getRegions().getRegion(object.getPosition()).getObject(object.getId(), object.getPosition()).isPresent()) {
			return false;
		}
		
		if(pit != null && !pit.isPermissable(player, log.getLog().getId())) {
			return false;
		}
		return true;
	}
	
}
