package com.rageps.content.skill.firemaking;

import com.rageps.content.achievements.Achievement;
import com.rageps.content.object.pit.FirepitData;
import com.rageps.content.object.pit.FirepitManager;
import com.rageps.content.object.pit.FirepitObject;
import com.rageps.action.impl.ItemOnObjectAction;
import com.rageps.action.impl.ObjectAction;
import com.rageps.content.skill.SkillData;
import com.rageps.content.skill.action.impl.DestructionSkillAction;
import com.rageps.task.Task;
import com.rageps.util.TextUtils;
import com.rageps.world.model.Animation;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.object.DynamicObject;
import com.rageps.world.entity.object.GameObject;

import java.util.Optional;

public final class Bonfire extends DestructionSkillAction {
	
	private final LogType log;
	
	public final DynamicObject object;
	
	private final FirepitObject pit;
	
	private int amount;
	
	public Bonfire(Player player, GameObject object, LogType log, FirepitObject pit) {
		super(player, Optional.of(object.getPosition()));
		this.object = object.toDynamic();
		this.log = log;
		this.pit = pit;
		this.amount = player.getInventory().computeAmountForId(log.getLog().getId());
	}
	
	public static void action() {
		for(FirepitData data : FirepitData.values()) {
			ItemOnObjectAction a = new ItemOnObjectAction() {
				@Override
				public boolean click(Player player, GameObject object, Item item, int container, int slot) {
					return addLogs(player, item, object);
				}
			};
			ObjectAction a2 = new ObjectAction() {
				@Override
				public boolean click(Player player, GameObject object, int click) {
					return addLogs(player, new Item(-100), object);
				}
			};
			a.registerObj(data.getObjectId());
			a2.registerFirst(data.getObjectId());
		}
	}
	
	public static boolean addLogs(Player player, Item item, GameObject object) {
		LogType log = item.getId() == -100 ? LogType.getDefinition(player).orElse(null) : LogType.getDefinition(item.getId()).orElse(null);
		if(log == null) {
			return false;
		}
		FirepitObject pit = FirepitManager.get().getFirepit();
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
			Achievement.BONE_FIRE.inc(player);
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
		if(object.isDisabled()) {
			return false;
		}
		if(pit != null && !pit.isPermissable(player, log.getLog().getId())) {
			return false;
		}
		return true;
	}
	
}
