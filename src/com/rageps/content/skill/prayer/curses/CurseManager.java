package com.rageps.content.skill.prayer.curses;

import com.rageps.content.skill.prayer.Prayer;
import com.rageps.util.rand.RandomUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the effects of the Curse prayer book.
 * @author Michael | Chex
 */
public class CurseManager {
	private final Map<Prayer, Counter> counters = new HashMap<>();
	
	public int modifyOutgoingLevel(int level, int base, int limit, Prayer prayer) {
		Counter counter = counters.computeIfAbsent(prayer, p -> new Counter());
		
		if(!counter.isActivated() && RandomUtils.success(0.25)) {
			counter.increase();
			counter.activate();
		}
		
		return level + counter.modify(level, base, limit);
	}
	
	public int modifyIncomingLevel(int level, int base, int limit, Prayer prayer) {
		Counter counter = counters.computeIfAbsent(prayer, p -> new Counter());
		
		if(!counter.isActivated() && RandomUtils.success(0.25)) {
			counter.increase();
			counter.activate();
		}
		
		return level - counter.modify(level, base, limit);
	}
	
	public void reset(Prayer prayer) {
		counters.remove(prayer);
	}
	
	public void deactivate(Prayer prayer) {
		counters.getOrDefault(prayer, new Counter()).deactivate();
	}
	
	public boolean isActivated(Prayer prayer) {
		return counters.getOrDefault(prayer, new Counter()).isActivated();
	}
	
}

final class Counter {
	private int counter;
	private boolean activated;
	
	void increase() {
		counter++;
	}
	
	void activate() {
		activated = true;
	}
	
	public void deactivate() {
		activated = false;
	}
	
	public boolean isActivated() {
		return activated;
	}
	
	int modify(int level, int base, int limit) {
		int modifier = counter + base;
		if(modifier > limit)
			modifier = limit;
		return level * modifier / 100;
	}
	
}
