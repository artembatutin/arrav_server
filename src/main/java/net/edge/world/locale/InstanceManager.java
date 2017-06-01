package net.edge.world.locale;

import net.edge.world.node.Node;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 * The instance manager for this world.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class InstanceManager {
	
	/**
	 * The array of instances that exist on the world.
	 */
	private static final boolean[] INSTANCES = new boolean[1000];
	
	/**
	 * Gets the next opened instance.
	 * @return the next opened instance.
	 */
	public int getOpenInstance() {
		OptionalInt openSlots = IntStream.rangeClosed(0, INSTANCES.length).filter(def -> !INSTANCES[def]).findAny();
		if(!openSlots.isPresent()) {
			throw new IllegalStateException("Maximum amount of instances have been created.");
		}
		return openSlots.getAsInt();
	}
	
	/**
	 * Isolates a single {@link Node} in a specific {@code instance}.
	 * @param node     the node to be isolated.
	 * @param instance the selected instance.
	 */
	public void isolate(Node node, int instance) {
		node.setInstance(instance);
		if(!INSTANCES[instance]) {
			INSTANCES[instance] = true;
		}
	}
	
	/**
	 * Isolates a single {@link Node}
	 * @param node the single node to isolate.
	 */
	public void isolate(Node node) {
		if(node.getInstance() != 0) {
			throw new IllegalStateException(node.toString() + " is already in an instance.");
		}
		isolate(node, getOpenInstance());
	}
	
	/**
	 * Isolates a list of nodes.
	 * @param nodes the nodes to isolate.
	 */
	public void isolate(List<Node> nodes) {
		int instance = getOpenInstance();
		nodes.forEach(i -> isolate(i, instance));
	}
	
	/**
	 * Isolates an array of nodes.
	 * @param nodes the nodes to isolate.
	 */
	public void isolate(Node... nodes) {
		int instance = getOpenInstance();
		Arrays.asList(nodes).forEach(i -> isolate(i, instance));
	}
	
	/**
	 * Opens a single instance number from the array.
	 * @param instance the instance to open.
	 */
	public void open(int instance) {
		INSTANCES[instance] = false;
	}
	
	/**
	 * Closes a single instance number from the array.
	 * @param instance the instance to close.
	 */
	public void close(int instance) {
		INSTANCES[instance] = true;
	}
	
	/**
	 * Closes the next opened instance from the array.
	 */
	public int closeNext() {
		int i = getOpenInstance();
		INSTANCES[i] = true;
		return i;
	}
	
}
