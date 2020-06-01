package com.rageps.world.locale.instance;


/**
 * All handling of instances.
 * @author Tamatea <tamateea@gmail.com>
 */
//public class InstanceHandler {

	/**
	 * Singleton
	 */
	/*private static InstanceHandler instance;
	private InstanceHandler() { }

	public static InstanceHandler getInstance() {
		if (instance == null) {
			instance = new InstanceHandler();
		}
		return instance;
	}*/

	/**
	 * A {@link CopyOnWriteArraySet} of all of the active {@link Instance}.
	 */
	//private CopyOnWriteArraySet<Instance> instances = new CopyOnWriteArraySet<>();

	/**
	 * Get's all active instances.
	 * @return the instances.
	 */
	/*public Set<Instance> getInstances() {
		return instances;
	}*/

	/**
	 * A player to join an instance. if this instance isn't already active, the instance
	 * will be created.
	 * @param player - The player joining the instance.
	 * @param instance - The instance that the player is joining.
	 */
	/*public void createOrJoin(Player player, Instance instance) {
		Optional<Instance> existingInstance = findInstance(instance.getHash());
		if (existingInstance.isPresent()) {
			join(player, existingInstance.get());
		} else {
			initialize(player, instance);
		}
	}*/

	/**
	 * Retreives an instance by it's hash, empty {@link Optional} if it's not found.
	 * @param hash - The instances hash.
	 * @return - The instance.
	 */
	/*private Optional<Instance> findInstance(int hash) {
		for(Instance $it : instances) {
			if($it.getHash() == hash)
				return Optional.of($it);
		}
		return Optional.empty();
	}*/

	/**
	 * Initiates an {@link Instance}.
	 * @param player - The player initiaiting the instance.
	 * @param instance - The instance being initiated.
	 */
	/*private void initialize(Player player, Instance instance) {
		// if (player.getInstance() != Instance.NORMAL) {
		// System.err.println("[Debug]: This player is already in an
		// instance.");
		// return;
		// }
		instance.onCreate(player);
		join(player, instance);
		instances.add(instance);
	}

	public void join(Player player, Instance instance) {
		player.setInstance(instance);
		player.getUpdateFlag().flag(Flag.APPEARANCE);
		player.getMovementQueue().reset();
		player.setNeedsPlacement(true);
	}

	public void removePlayer(Player player) {
		join(player, Instance.NORMAL);
	}

	public Instance getInstanceOf(Player player) {
		return player.getInstance();
	}

	public void process() {

		instances.forEach(instance -> {
			if (instance == Instance.NORMAL)
				return;

			if (!instance.isInvalidated())
				return;

			if (instance.isDisposable()) {
				dispose(instance);
			} else {
				instance.validate();
			}
		});
	}

	public void disposeNPCs(Instance instance) {
		getNpcsOn(instance).forEach(Mob::dispose);
	}

	private void dispose(Instance instance) {

		CustomObjects.CUSTOM_OBJECTS.forEach(gameObject -> {
			if(gameObject.getInstance() == instance)
				CustomObjects.deleteGlobalObject(gameObject);
		});


		getPlayersOn(instance).forEach(player -> {
			if (instance.getTeleportOnDispose() != null) {
				player.move(instance.getTeleportOnDispose());
			}
			player.restoreInstance();
		});

		disposeNPCs(instance);
		getItemsOn(instance).forEach(GroundItem::dispose);

		instances.remove(instance);
	}

	private ObjectArrayList<GroundItem> getItemsOn(Instance instance) {
        return World.getRegions().getRegion(instance.getInstancedArea().getRegion()).getItemsInstance(instance);

    }

	private Set<Mob> getNpcsOn(Instance instance) {
		return World.getRegions().getRegion(instance.getInstancedArea().getRegion()).getMobs();
	}

	private Set<Player> getPlayersOn(Instance instance) {
		return World.getRegions().getRegion(instance.getInstancedArea().getRegion()).getPlayers();
	}

}
*/