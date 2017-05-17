package net.edge.world.content.skill.fishing;

import net.edge.utils.rand.RandomUtils;
import net.edge.world.content.skill.Skill;
import net.edge.world.content.skill.Skills;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Tool {
	NET(303, 1, -1, 0.30, 621, new Catchable[]{Catchable.SHRIMP, Catchable.ANCHOVY, Catchable.MONKFISH}) {
		@Override
		public Catchable catchable() {
			return Catchable.SHRIMP;
		}
	},
	BIG_NET(305, 16, -1, 0.25, 620, new Catchable[]{Catchable.MACKEREL, Catchable.COD, Catchable.BASS, Catchable.CASKET, Catchable.LEATHER_BOOTS, Catchable.LEATHER_GLOVES, Catchable.OYSTER, Catchable.SEAWEED}) {
		@Override
		public Item[] onCatch(Player player) {
			int amount = RandomUtils.inclusive(1, 3);
			int slots = player.getInventory().remaining();
			int counter = 0;
			Item[] items = new Item[player.getInventory().remaining() < amount ? player.getInventory().remaining() : amount];
			if(amount > slots)
				amount = slots;
			for(int i = 0; i < amount; i++) {
				items[counter++] = new Item(calculate(player).getId());
			}
			return items;
		}
		
		@Override
		public Catchable catchable() {
			return Catchable.MACKEREL;
		}
	},
	FISHING_ROD(307, 5, 313, 0.40, 622, new Catchable[]{Catchable.SARDINE, Catchable.HERRING, Catchable.PIKE, Catchable.SLIMY_EEL, Catchable.CAVE_EEL, Catchable.LAVA_EEL}) {
		@Override
		public Catchable catchable() {
			return Catchable.SARDINE;
		}
	},
	FLY_FISHING_ROD(309, 20, 314, 0.45, 622, new Catchable[]{Catchable.TROUT, Catchable.SALMON}) {
		@Override
		public Catchable catchable() {
			return Catchable.TROUT;
		}
	},
	HARPOON(311, 35, -1, 0.15, 618, new Catchable[]{Catchable.TUNA, Catchable.SWORDFISH}) {
		@Override
		public Catchable catchable() {
			return Catchable.TUNA;
		}
	},
	SHARK_HARPOON(311, 76, -1, 0.05, 618, new Catchable[]{Catchable.SHARK}) {
		@Override
		public Catchable catchable() {
			return Catchable.SHARK;
		}
	},
	LOBSTER_POT(301, 40, -1, 0.20, 619, new Catchable[]{Catchable.LOBSTER}) {
		@Override
		public Catchable catchable() {
			return Catchable.LOBSTER;
		}
	};
	
	final int id;
	final int level;
	final int needed;
	final double success;
	final int animation;
	final Catchable[] catchables;
	
	Tool(int id, int level, int needed, double success, int animation, Catchable[] catchables) {
		this.id = id;
		this.level = level;
		this.needed = needed;
		this.success = success;
		this.animation = animation;
		this.catchables = catchables;
	}
	
	@Override
	public String toString() {
		return name().toLowerCase().replaceAll("_", " ");
	}
	
	public Catchable catchable() {
		return null;
	}
	
	Catchable calculate(Player player) {
		List<Catchable> success = new ArrayList<>(catchables.length);
		Skill skill = player.getSkills()[Skills.FISHING];
		Arrays.stream(catchables).filter(def -> skill.reqLevel(def.getLevel()) && def.catchable(player)).forEach(success::add);
		//Collections.shuffle(success, random);
		
		return success.stream().anyMatch(def -> RandomUtils.success(def.getChance())) ? RandomUtils.random(success) : catchable();
	}
	
	public Item[] onCatch(Player player) {
		return new Item[]{new Item(calculate(player).getId())};
	}
}
