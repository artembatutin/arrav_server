package com.rageps.content.object.cannon;

import com.rageps.action.impl.ItemAction;
import com.rageps.action.impl.ItemOnObjectAction;
import com.rageps.action.impl.ObjectAction;
import com.rageps.task.Task;
import com.rageps.world.model.Animation;
import com.rageps.world.model.Direction;
import com.rageps.world.entity.EntityState;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.container.impl.Inventory;
import com.rageps.world.entity.object.DynamicObject;
import com.rageps.world.entity.object.GameObject;
import com.rageps.world.entity.object.ObjectDirection;
import com.rageps.world.entity.object.ObjectType;
import com.rageps.world.entity.region.Region;
import com.rageps.world.entity.region.TraversalConstants;

import java.util.Optional;

/**
 * Dwarf multicannon handler
 * @author Artem Batutin
 */
public class Multicannon extends DynamicObject {
	
	/**
	 * The player who placed the cannon.
	 */
	protected final Player player;
	
	/**
	 * The facing direction of the cannon.
	 */
	Direction facing;
	
	/**
	 * The state if the cannon is firing.
	 */
	boolean firing;
	
	private Multicannon(Player player) {
		super(7, player.getPosition(), ObjectDirection.SOUTH, ObjectType.GENERAL_PROP, false, 0, player.getInstance());
		this.player = player;
	}
	
	/**
	 * Picking the cannon.
	 */
	public void pickup(boolean logout) {
		Inventory inv = player.getInventory();
		if(logout) {
			if(getElements() > 0) {
				inv.addOrBank(new Item(2, getElements()));
			}
			inv.addOrBank(new Item(6), new Item(8), new Item(10), new Item(12));
			remove();
			return;
		}
		int free = inv.remaining();
		if(free == (getElements() > 0 ? 4 : 3)) {
			player.message("You don't have enough inventory space to pickup your cannon.");
			return;
		}
		if(getElements() > 0) {
			inv.add(new Item(2, getElements()));
		}
		inv.addAll(new Item(6), new Item(8), new Item(10), new Item(12));
		firing = false;
		remove();
	}
	
	/**
	 * Attempting to fire with the cannon.
	 */
	public void fire() {
		if(getElements() == 0) {
			if(player.getInventory().contains(2)) {
				int amount = player.getInventory().computeAmountForId(2) > 30 ? 30 : player.getInventory().computeAmountForId(2);
				player.getInventory().remove(new Item(2, amount));
				this.setElements(this.getElements() + amount);
				player.message("You charge the cannon with " + amount + " cannonballs.");
				return;
			}
			player.message("There is no cannon balls to fire with.");
			return;
		}
		if(!firing) {
			firing = true;
			new MulticannonTask(this).submit();
		}
	}
	
	/**
	 * Initializes all of the cannon manipulation actions.
	 */
	public static void action() {
		//setup action
		ItemAction setup = new ItemAction() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				if(player.getMinigame().isPresent()) {
					if(!player.getMinigame().get().cannonSetup()) {
						player.message("You can't setup the cannon here.");
						return true;
					}
				}
				if(player.cannon.isPresent()) {
					player.message("You can't setup a second cannon.");
					return true;
				}
				//inventory and walk reset
				Inventory inv = player.getInventory();
				if(!inv.containsAll(6, 8, 10, 12)) {
					player.message("You do not have all of the cannon parts.");
					return true;
				}
				Region reg = player.getRegion();
				if(reg == null)
					return true;
				player.getMovementQueue().reset();
				
				//clip & location
				boolean clip = true;
				for(int x = 0; x < 2; x++) {
					if(!clip)
						break;
					for(int y = 0; y < 2; y++) {
						if(reg.getTile(player.getPosition().getZ(), (player.getPosition().getX() & 0x3F) + x, (player.getPosition().getY() & 0x3F) + y).getFlags() != TraversalConstants.NONE) {
							player.message("tile: X:" + ((player.getPosition().getX() & 0x3F) + x) + ", Y: " + ((player.getPosition().getY() & 0x3F) + y) + "," + reg.getTile(0, (player.getPosition().getX() & 0x3F) + x, (player.getPosition().getY() & 0x3F) + y).getFlags());
							clip = false;
							break;
						}
						if(reg.getObjects(player.getPosition().move(x, y)).hasInteractive()) {
							clip = false;
							break;
						}
					}
				}
				if(!clip || player.getLocation().isCannonAllowed()) {
					player.message("You can't set the cannon here.");
					return true;
				}
				
				//base
				Multicannon cannon = new Multicannon(player);
				player.cannon = Optional.of(cannon);
				player.animation(new Animation(827));
				player.facePosition(player.getPosition().move(1, 1));
				inv.remove(new Item(6));
				cannon.publish();
				
				//stand setup
				(new Task(5) {
					@Override
					protected void execute() {
						cancel();
						if(player.getState() != EntityState.ACTIVE || !inv.contains(8)) {
							inv.add(new Item(6));
							cannon.remove();
							return;
						}
						cannon.setId(8);
						player.animation(new Animation(827));
						inv.remove(new Item(8));
						cannon.publish();
						
						//barrel setup
						(new Task(5) {
							@Override
							protected void execute() {
								cancel();
								if(player.getState() != EntityState.ACTIVE || !inv.contains(10)) {
									inv.add(new Item(6));
									inv.add(new Item(8));
									cannon.remove();
									return;
								}
								cannon.setId(9);
								player.animation(new Animation(827));
								inv.remove(new Item(10));
								cannon.publish();
								
								//furnace setup
								(new Task(5) {
									@Override
									protected void execute() {
										cancel();
										if(player.getState() != EntityState.ACTIVE || !inv.contains(12)) {
											inv.add(new Item(6));
											inv.add(new Item(8));
											inv.add(new Item(10));
											cannon.remove();
											return;
										}
										cannon.setId(6);
										player.animation(new Animation(827));
										inv.remove(new Item(12));
										cannon.publish();
									}
								}).submit();
							}
						}).submit();
					}
				}).submit();
				return true;
			}
		};
		setup.register(6);
		
		//fire action
		ObjectAction fire = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(!player.cannon.isPresent())
					return true;
				Multicannon cannon = player.cannon.get();
				if(!cannon.validate(player, object))
					return true;
				cannon.fire();
				return true;
			}
		};
		fire.registerFirst(6);
		
		//pickup action
		ObjectAction pickup = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(!player.cannon.isPresent())
					return true;
				Multicannon cannon = player.cannon.get();
				if(!cannon.validate(player, object))
					return true;
				cannon.pickup(false);
				return true;
			}
		};
		pickup.registerSecond(6);
		
		//filling cannon balls
		ItemOnObjectAction fill = new ItemOnObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, Item item, int container, int slot) {
				if(!player.cannon.isPresent())
					return true;
				Multicannon cannon = player.cannon.get();
				if(!cannon.validate(player, object))
					return true;
				Inventory inv = player.getInventory();
				int am = inv.computeAmountForId(2);
				if(am > 30)
					am = 30;
				if(am == 0) {
					player.message("You do not have any cannonballs.");
					return true;
				}
				inv.remove(new Item(2, am));
				cannon.setElements(cannon.getElements() + am);
				player.message("You charge the cannon with " + am + " cannonballs.");
				return true;
			}
		};
		fill.registerObj(6);
		fill.registerItem(2);
	}
	
	/**
	 * Determines if the player manipulating is the owner of the cannon.
	 */
	private boolean validate(Player player, GameObject object) {
		return this.player.same(player) && hashCode() == object.hashCode();
	}
	
	public synchronized void publish() {
		Region r = getRegion();
		if(r != null) {
			r.addObj(this);
			clip(r);
		}
		player.cannon = Optional.of(this);
		setDisabled(false);
	}
	
	@Override
	public synchronized void remove() {
		Region r = getRegion();
		if(r != null) {
			r.removeObj(this);
			unclip(r);
		}
		setDisabled(true);
		player.cannon = Optional.empty();
	}
}