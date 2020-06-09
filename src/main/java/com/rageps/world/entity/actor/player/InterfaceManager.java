package com.rageps.world.entity.actor.player;


import com.rageps.content.TabInterface;
import com.rageps.content.market.MarketShop;
import com.rageps.net.packet.out.*;
import com.rageps.world.entity.item.container.session.ExchangeSessionManager;

/**
 * Contains information about the state of interfaces enter in the client.
 *
 * @author Tamatea <tamateea@gmail.com>
 */
public class InterfaceManager {

	/** The player instance. */
	private Player player;

	/** The current main interface. */
	private int main = -1;

	/** The current overlay interface. */
	private int overlay = -1;

	/** The current walkable-interface. */
	private int walkable = -1;

	/** The current dialogue. */
	private int dialogue = -1;

	private int[] sidebars = new int[15];

	/** Creates a new <code>InterfaceManager<code>. */
	InterfaceManager(Player player) {
		this.player = player;
	}

	/** Opens an interface for the player. */
	public void open(int identification) {
		open(identification, true);
	}
	

	/** Opens an interface for the player. */
	public void openNonDuplicate(int identification) {
		if (!isInterfaceOpen(identification)) {
			open(identification, true);
		}
	}
	
	


	/** Opens an itemcontainer for the player. */
	public void open(int identification, boolean secure) {
		if (secure) {

			if (main == identification) {
				return;
			}

			if (player.getCombat().inCombat()) {
				player.message("You can't do this right now!");
				return;
			}

			//if (player.dialogueFactory.isActive() || player.dialogueFactory.isOption()) {
			//	player.dialogueFactory.clear();
			//}
		}
		main = identification;
		player.getMovementQueue().reset();
		player.out(new SendInterface(identification));
		//player.send(new SendInterface(identification));
		//player.send(new SendString("[CLOSE_MENU]", 0));
	}

	/** Opens a walkable-itemcontainer for the player. */
	public void openWalkable(int identification) {
		if (walkable == identification) {
			return;
		}
		setWalkable(identification);
		player.out(new SendWalkable(identification));
	}
	/** close a walkable-itemcontainer for the player. */
	public void closeWalkable() {
		openWalkable(-1);
	}

	/** Opens an inventory interface for the player. */
	public void openInventory(int identification, int overlay) {
		if (main == identification && this.overlay == overlay) {
			return;
		}

		main = identification;
		this.overlay = overlay;
		player.getMovementQueue().reset();
		player.out(new SendInventoryInterface(identification, overlay));
	}

	/** Clears the player's screen. */
	public void close() {
		close(true);
	}

	/** Handles clearing the screen. */
	public void close(boolean walkable) {

		if(player.getMarketShop() != null) {
			MarketShop.clearFromShop(player);
			player.getAttributeMap().reset(PlayerAttributes.SHOP_ITEM);
			player.getAttributeMap().reset(PlayerAttributes.BUYING_SHOP_ITEM);
		}

		ExchangeSessionManager.get().reset(player);

		player.getAttributeMap().reset(PlayerAttributes.BANKING);
		player.getAttributeMap().reset(PlayerAttributes.BOB);

		clean(walkable);
		player.getDialogueBuilder().getChain().clear();
		//player.dialogueFactory.clear();
		player.out(new SendCloseInterface());
		if(walkable)
			player.out(new SendWalkable(-1));
	}

	public void setSidebar(TabInterface tab, int id) {
		if (sidebars[tab.ordinal()] == id && id != -1) {
			return;
		}
		sidebars[tab.ordinal()] = id;
		player.out(new SendTab(id, tab));

	}

	/** Cleans the interfaces. */
	public void clean(boolean walkableFlag) {
		main = -1;
		dialogue = -1;
		if (walkableFlag) {
			walkable = -1;
		}
	}

	/** Checks if a certain interface is enter. */
	public boolean isInterfaceOpen(int id) {
		return main == id;
	}

	/** Checks if the player's screen is clear. */
	public boolean isClear() {
		return main == -1 && dialogue == -1 && walkable == -1;
	}

	/** Checks if the main interface is clear. */
	public boolean isMainClear() {
		return main == -1;
	}

	/** Checks if the dialogue interface is clear. */
	public boolean isDialogueClear() {
		return dialogue == -1;
	}

	/** Sets the current interface. */
	public void setMain(int currentInterface) {
		this.main = currentInterface;
	}

	/** gets the current main interface. */
	public int getMain() {
		return main;
	}

	/** Gets the dialogue interface. */
	public int getDialogue() {
		return dialogue;
	}

	/** Sets the dialogue interface. */
	public void setDialogue(int dialogueInterface) {
		this.dialogue = dialogueInterface;
	}

	/** Gets the walkable interface. */
	public int getWalkable() {
		return walkable;
	}

	/** Sets the walkable interface. */
	public void setWalkable(int walkableInterface) {
		this.walkable = walkableInterface;
	}

	public int getSidebar(TabInterface tab) {
		if (tab.ordinal() > sidebars.length) {
			return -1;
		}
		return sidebars[tab.ordinal()];
	}

	public boolean isSidebar(TabInterface tab, int id) {
		return tab.ordinal() <= sidebars.length && sidebars[tab.ordinal()] == id;
	}

	public boolean hasSidebar(int id) {
		for (int sidebar : sidebars) {
			if (sidebar == id) {
				return true;
			}
		}
		return false;
	}
}