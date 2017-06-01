package net.edge.world.content.skill.smithing;

import com.google.common.collect.ImmutableMap;
import net.edge.task.LinkedTaskSequence;
import net.edge.task.Task;
import net.edge.utils.TextUtils;
import net.edge.world.content.dialogue.impl.PlayerDialogue;
import net.edge.world.content.dialogue.impl.StatementDialogue;
import net.edge.world.content.skill.SkillData;
import net.edge.world.content.skill.Skills;
import net.edge.world.content.skill.action.impl.ProducingSkillAction;
import net.edge.world.locale.Position;
import net.edge.world.Animation;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;
import net.edge.world.object.ObjectNode;

import java.util.Optional;

/**
 * Holds functionality for creating items on an anvil.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Smithing extends ProducingSkillAction {
	
	/**
	 * The definition for this table.
	 */
	private final SmithingTable definition;
	
	/**
	 * The amount being created.
	 */
	private int amount;
	
	/**
	 * Constructs a new {@link Smithing} skill.
	 * @param player     {@link #getPlayer()}.
	 * @param definition {@link #definition}.
	 * @param amount     {@link #amount}.
	 */
	public Smithing(Player player, SmithingTable definition, int amount) {
		super(player, Optional.empty());
		this.definition = definition;
		this.amount = amount;
	}
	
	/**
	 * Attempts to forge the item clicked for the specified {@code player}.
	 * @param player      {@link #getPlayer()}.
	 * @param interfaceId the interface id clicked.
	 * @param slot        the slot.
	 * @param amount      the amount that should be produced.
	 * @return <true> if the skill action was started, <false> otherwise.
	 */
	public static boolean forge(Player player, int interfaceId, int slot, int amount) {
		if(player.getAttr().get("smithing_equipment").get() == null) {
			return false;
		}
		SmithingTable[] values = TABLE.get(((Item) player.getAttr().get("smithing_equipment").get()).getId());
		
		if(values == null) {
			return false;
		}
		
		SmithingTable table = null;
		
		for(int i = 0; i < FRAME_DATA.length; i++) {
			if(FRAME_DATA[i][3] == interfaceId && slot == FRAME_DATA[i][2]) {
				if(i >= values.length)
					break;
				table = values[i];
				break;
			}
		}
		
		if(table == null) {
			return false;
		}
		
		Smithing smithing = new Smithing(player, table, amount);
		smithing.start();
		return true;
	}
	
	/**
	 * Opens the smithing interface and sets all the values for
	 * the specified {code player}.
	 * @param player {@link #getPlayer()}
	 * @param item   the item that was used on the object.
	 * @param object the object the item was used on.
	 * @return <true> if the interface opened, <false> otherwise.
	 */
	public static boolean openInterface(Player player, Item item, ObjectNode object) {
		SmithingTable[] values = TABLE.get(item.getId());
		
		if(!object.getDefinition().getName().equalsIgnoreCase("anvil")) {
			return false;
		}
		
		if(item.getId() == 11286) {
			if(!player.getInventory().containsAny(2347, 2949)) {
				player.message("You need a hammer to forge items.");
				return false;
			}
			
			if(!player.getSkills()[Skills.SMITHING].reqLevel(90)) {
				player.message("You need a smithing level of 70 to create a dragonfire shield.");
				return false;
			}
			
			if(!player.getInventory().contains(11286)) {
				player.message("You need a draconic visage to create a dragonfire shield.");
				return false;
			}
			
			if(!player.getInventory().contains(1540)) {
				player.message("You need an anti-dragon shield to create a dragonfire shield.");
				return false;
			}
			
			player.getDialogueBuilder().append(
					
					new StatementDialogue("You set to work, trying to attach the ancient draconic visage to your", "anti-dragonbreath shield. It's not easy to work with the ancient artifact", "and it takes all of your skill as a master smith."), new PlayerDialogue("Here goes nothing...").attach(() -> {
						player.getActivityManager().disable();
						
						player.getMessages().sendCloseWindows();
						
						player.animation(new Animation(898));
						player.getMessages().sendLocalGraphic(2123, object.getGlobalPos().copy(), 50);
						LinkedTaskSequence seq = new LinkedTaskSequence();
						seq.connect(3, () -> {
							
							player.getInventory().removeAll(new Item(1540), new Item(11286));
							
							player.getInventory().add(new Item(11283));
							
							Skills.experience(player, 2_000, Skills.SMITHING);
							
							player.getActivityManager().enable();
							
							player.getDialogueBuilder().append(
									
									new StatementDialogue("Even for an expert armourer it is not an easy task, but eventually it", "is ready. You have crafted the draconic visage and anti-dragonbreath", "shield into a dragonfire shield.")
							
							);
						});
						seq.start();
					})
			
			);
			return true;
		}
		//Godsword blade creation.
		if(item.getId() == 11710 || item.getId() == 11712 || item.getId() == 11714) {
			
			if(!player.getInventory().containsAny(2347, 2949)) {
				player.message("You need a hammer to forge items.");
				return false;
			}
			
			if(!player.getInventory().containsAll(11710, 11712, 11714)) {
				player.message("You do not have all the shards.");
				return false;
			}
			if(!player.getSkills()[Skills.SMITHING].reqLevel(80)) {
				player.message("You need 80 smithing to do this.");
				return false;
			}
			player.animation(new Animation(898));
			player.getMessages().sendLocalGraphic(2123, object.getGlobalPos().copy(), 50);
			player.getInventory().removeAll(new Item(11710), new Item(11712), new Item(11714));
			player.getInventory().add(new Item(11690));
			return true;
		}
		
		if(values == null) {
			return false;
		}
		
		if(!player.getInventory().containsAny(2347, 2949)) {
			player.message("You need a hammer to forge items.");
			return false;
		}
		
		SmithingTable table = null;
		
		for(int i = 0; i < FRAME_DATA.length; i++) {
			if(i >= values.length) {
				player.getMessages().sendString("", FRAME_DATA[i][0]);
				player.getMessages().sendString("", FRAME_DATA[i][1]);
				player.getMessages().sendItemOnInterfaceSlot(FRAME_DATA[i][3], new Item(-1, 0), FRAME_DATA[i][2]);
				continue;
			}
			
			table = values[i];
			
			if(table == null || table.getBar() == null) {
				return false;
			}
			
			final boolean has_bar = player.getInventory().computeAmountForId(table.getBar().getId()) >= table.getBarsRequired();
			final String bar_color = !has_bar ? "@red@" : "@gre@";
			final String name_color = player.getSkills()[Skills.SMITHING].getRealLevel() >= table.getLevelRequirement() && has_bar ? "@whi@" : "@bla@";
			
			player.getMessages().sendString(bar_color + table.getBarsRequired() + " Bar" + (table.getBarsRequired() != 1 ? "s" : ""), FRAME_DATA[i][0]);
			player.getMessages().sendString(name_color + TextUtils.capitalize(table.getName().toLowerCase()), FRAME_DATA[i][1]);
			player.getMessages().sendItemOnInterfaceSlot(FRAME_DATA[i][3], table.getProduced(), FRAME_DATA[i][2]);
		}
		if(table == null || table.getBar() == null) {
			return false;
		}
		player.getAttr().get("smithing_equipment").set(table.getBar());
		player.getAttr().get("smithing_position").set(object.getGlobalPos());
		player.getMessages().sendInterface(994);
		return true;
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			player.animation(new Animation(898));
			player.getMessages().sendLocalGraphic(2123, (Position) player.getAttr().get("smithing_position").get(), 50);
			amount--;
			if(amount < 1)
				t.cancel();
		}
	}
	
	@Override
	public void onStop() {
		player.getAttr().get("smithing_equipment").set(null);
		player.getAttr().get("smithing_position").set(null);
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{new Item(definition.getBar().getId(), definition.getBarsRequired())});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{definition.getProduced()});
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
		player.getMessages().sendCloseWindows();
		return canExecute();
	}
	
	@Override
	public boolean canExecute() {
		if(!player.getInventory().containsAny(2347, 2949)) {
			player.message("You need a hammer to forge items.");
			return false;
		}
		if(!player.getSkills()[Skills.SMITHING].reqLevel(definition.getLevelRequirement())) {
			player.message("You need a smithing level of " + definition.getLevelRequirement() + " to smith " + TextUtils.appendIndefiniteArticle(definition.getName()));
			return false;
		}
		return true;
	}
	
	@Override
	public double experience() {
		return definition.getExperience();
	}
	
	@Override
	public SkillData skill() {
		return SkillData.SMITHING;
	}
	
	/**
	 * The mapviewer which contains all the bar ids with their respective tables.
	 */
	private static final ImmutableMap<Integer, SmithingTable[]> TABLE = ImmutableMap.<Integer, SmithingTable[]>builder().put(2349, SmithingTable.BronzeTable.values()).put(2351, SmithingTable.IronTable.values()).put(2353, SmithingTable.SteelTable.values()).put(2359, SmithingTable.MithrilTable.values()).put(2361, SmithingTable.AdamantTable.values()).put(2363, SmithingTable.RuniteTable.values()).build();
	
	/**
	 * The array containing all of the frames data.
	 */
	private static final int[][] FRAME_DATA = {{1125, 1094, 0, 1119}, {1126, 1091, 0, 1120}, {1109, 1098, 0, 1121}, {1127, 1102, 0, 1122}, {1128, 1107, 0, 1123}, {1124, 1085, 1, 1119}, {1129, 1093, 1, 1120}, {1110, 1099, 1, 1121}, {1113, 1103, 1, 1122}, {1130, 1108, 1, 1123}, {1116, 1087, 2, 1119}, {1118, 1083, 2, 1120}, {1111, 1100, 2, 1121}, {1114, 1104, 2, 1122}, {1131, 1106, 2, 1123}, {1089, 1086, 3, 1119}, {1095, 1092, 3, 1120}, {1112, 1101, 3, 1121}, {1115, 1105, 3, 1122}, {1132, 1096, 3, 1123}, {1090, 1088, 4, 1119}, {8428, 8429, 4, 1120}, {11459, 11461, 4, 1121}, {13357, 13358, 4, 1122}, {1135, 1134, 4, 1123},};
}
