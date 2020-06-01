package com.rageps.content.skill.fishing;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import com.rageps.content.skill.Skill;
import com.rageps.content.skill.Skills;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;

public enum Tool {
	NET(303, 1, -1, 0.35, 621, new Catchable[]{Catchable.SHRIMP, Catchable.ANCHOVY, Catchable.MONKFISH}) {
		@Override
		public Catchable catchable() {
			return Catchable.SHRIMP;
		}
	},
	NET_MONKFISH(303, 62, -1, 0.25, 621, new Catchable[]{Catchable.MONKFISH}),
	BIG_NET(305, 16, -1, 0.50, 620, new Catchable[]{Catchable.MACKEREL, Catchable.COD, Catchable.BASS, Catchable.CASKET, Catchable.LEATHER_BOOTS, Catchable.LEATHER_GLOVES, Catchable.OYSTER, Catchable.SEAWEED, Catchable.ROCKTAIL}) {
		@Override
		public Item[] onCatch(Player player) {
			int amount = RandomUtils.inclusive(1, 3);
			int slots = player.getInventory().remaining();
			if(amount > slots)
				amount = slots;
			return calculate(player, amount).toArray(new Item[amount]);
		}

		@Override
		public Catchable catchable() {
			return Catchable.MACKEREL;
		}
	},
	FISHING_ROD(307, 5, 313, 0.45, 622, new Catchable[]{Catchable.SARDINE, Catchable.HERRING, Catchable.PIKE, Catchable.SLIMY_EEL, Catchable.CAVE_EEL, Catchable.LAVA_EEL}) {
		@Override
		public Catchable catchable() {
			return Catchable.SARDINE;
		}
	},
	FLY_FISHING_ROD(309, 20, 314, 0.50, 622, new Catchable[]{Catchable.TROUT, Catchable.SALMON}) {
		@Override
		public Catchable catchable() {
			return Catchable.TROUT;
		}
	},
	HARPOON(311, 35, -1, 0.20, 618, new Catchable[]{Catchable.TUNA, Catchable.SWORDFISH}) {
		@Override
		public Catchable catchable() {
			return Catchable.TUNA;
		}
	},
	SHARK_HARPOON(311, 76, -1, 0.15, 618, new Catchable[]{Catchable.SHARK, Catchable.MANTAS}),
	LOBSTER_POT(301, 40, -1, 0.25, 619, new Catchable[]{Catchable.LOBSTER});

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
		return catchables[0];
	}

	ObjectList<Item> calculate(Player player, int cap) {
		ObjectList<Item> success = new ObjectArrayList<>();
		Skill skill = player.getSkills()[Skills.FISHING];
		int index = cap;
		for(Catchable c : catchables) {
			if(!skill.reqLevel(c.getLevel()))
				continue;
			if(!c.catchable(player))
				continue;
			if(!RandomUtils.success(c.getChance()))
				continue;
			success.add(new Item(c.getId()));
			index--;
			if(index == 0)
				break;
		}
		return success;
	}

	public Item[] onCatch(Player player) {
		return calculate(player, 1).toArray(new Item[1]);
	}
}
