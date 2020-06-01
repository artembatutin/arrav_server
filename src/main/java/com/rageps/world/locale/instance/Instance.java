package com.rageps.world.locale.instance;

/*
import com.rageps.world.locale.instance.impl.DefaultInstance;
import Area;
import Entity;
import Mob;
import Player;
import Position;

import java.util.function.Predicate;*/

/**
 * Represents an entity instance, identififed by a signature and a hashcode
 *
 */
/*public abstract class Instance {

	public static final Instance NORMAL = new DefaultInstance();

	private static final long INVALIDATE_DELAY = 5_000;

	private final int hash;
	private final String signature;
	private final InstancePolicy policy;
	private long instanceLifeSpan;
	private long lastInvalidation;
	private boolean resetOnAreaExit;
	private boolean hasPlayersUpdate;
	private boolean multi;
	private Area instancedArea;
	private Position teleportOnDispose;
	private Predicate<Instance> disposePolicy = InstanceDisposePolicy.DISPOSE_ON_NO_NPCS;

	private boolean keepItemsOnDeath;

	protected Instance() {
		this.signature = null;
		this.policy = InstancePolicy.DISPLAY_INSTANCED_ENTITIES_ONLY;
		this.hash = policy.hashCode();
	};

	public Instance(String signature) {
		this(signature, InstancePolicy.DISPLAY_INSTANCED_ENTITIES_ONLY);
	}

	public Instance(String signature, InstancePolicy policy) {
		this.signature = signature;
		this.policy = policy;
		this.hash = signature.hashCode() + policy.hashCode();
	}

	@Override
	public int hashCode() {
		return hash;
	}

	public int getHash() {
		return hash;
	}

	public String getSignature() {
		return signature;
	}

	public Instance setDisposePolicy(Predicate<Instance> disposePolicy) {
		this.disposePolicy = disposePolicy;
		return this;
	}

	public Predicate<Instance> getDisposePolicy() {
		return disposePolicy;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		return (this == obj) || obj.hashCode() == this.hashCode();
	}

	@Override
	public String toString() {
		return "Instance [hash=" + hash + ", signature=" + signature + "] (" + super.toString() + ")";
	}

	public Position getTeleportOnDispose() {
		return teleportOnDispose;
	}

	public Instance setTeleportOnDispose(Position teleportOnDispose) {
		this.teleportOnDispose = teleportOnDispose;
		return this;
	}

	public abstract void onCreate(Player creator);
	public abstract void onJoin(Player player);
	public abstract void onExit(Player player);
	public abstract void onDeath(Entity entity);
	public abstract void onRegionChange(Player entity);

	public boolean isInArea(Player player) {
		if (instancedArea != null) {
			return player.getPosition().inside(instancedArea);
		}
		return true;
	}

	public Area getInstancedArea() {
		return instancedArea;
	}

	public boolean isResetOnAreaExit() {
		return resetOnAreaExit;
	}

	protected void setInstancedArea(Area area, boolean resetOnAreaExit) {
		this.instancedArea = area;
		this.resetOnAreaExit = resetOnAreaExit;
	}

	public boolean isDisposable() {
		return hasExpired() || disposePolicy.test(this);
	}

	public Instance setLifeSpan(long lifeSpan) {
		this.instanceLifeSpan = System.currentTimeMillis() + lifeSpan;
		return this;
	}

	public boolean hasExpired() {
		return this.instanceLifeSpan > 0 && System.currentTimeMillis() > instanceLifeSpan;
	}

	public boolean isInvalidated() {
		return System.currentTimeMillis() - lastInvalidation > INVALIDATE_DELAY;
	}

	public void validate() {
		if (!isInvalidated()) {
			this.lastInvalidation = System.currentTimeMillis();
		}
	}

	public void onDispose() {
	}

	public InstancePolicy getPolicy() {
		return policy;
	}

	public boolean hasPlayersUpdate() {
		return hasPlayersUpdate;
	}

	public void setHasPlayersUpdate(boolean hasPlayersUpdate) {
		this.hasPlayersUpdate = hasPlayersUpdate;
	}

	public void regionChange(Player entity) {
		this.hasPlayersUpdate = true;
		onRegionChange(entity);
	}


	public void exit(Player entity) {
		setHasPlayersUpdate(true);
		onExit(entity);
	}

	public void death(Mob mob) {
		this.hasPlayersUpdate = true;
		onDeath(mob);
	}


	public boolean isKeepItemsOnDeath() {
		return keepItemsOnDeath;
	}

	public Instance setKeepItemsOnDeath(boolean keepItemsOnDeath) {
		this.keepItemsOnDeath = keepItemsOnDeath;
		return this;
	}

	public void setMulti(boolean multi) {
		this.multi = multi;
	}

	public boolean isMulti() {
		return multi;
	}
}
*/