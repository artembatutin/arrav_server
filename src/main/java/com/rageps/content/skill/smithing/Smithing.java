package com.rageps.content.skill.smithing;

import com.google.common.collect.ImmutableMap;
import com.rageps.content.achievements.Achievement;
import com.rageps.content.dialogue.impl.PlayerDialogue;
import com.rageps.content.dialogue.impl.StatementDialogue;
import com.rageps.world.entity.actor.player.PlayerAttributes;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import com.rageps.content.skill.SkillData;
import com.rageps.content.skill.Skills;
import com.rageps.content.skill.action.impl.ProducingSkillAction;
import com.rageps.net.packet.out.SendGraphic;
import com.rageps.net.packet.out.SendItemOnInterfaceSlot;
import com.rageps.task.LinkedTaskSequence;
import com.rageps.task.Task;
import com.rageps.util.TextUtils;
import com.rageps.world.model.Animation;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.object.GameObject;
import com.rageps.world.locale.Position;

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
	 * @param player {@link #getPlayer()}.
	 * @param definition {@link #definition}.
	 * @param amount {@link #amount}.
	 */
	public Smithing(Player player, SmithingTable definition, int amount) {
		super(player, Optional.empty());
		this.definition = definition;
		this.amount = amount;
	}
	
	/**
	 * Attempts to forge the item clicked for the specified {@code player}.
	 * @param player {@link #getPlayer()}.
	 * @param interfaceId the interface id clicked.
	 * @param slot the slot.
	 * @param amount the amount that should be produced.
	 * @return <true> if the skill action was started, <false> otherwise.
	 */
	public static boolean forge(Player player, int interfaceId, int slot, int amount) {
		if(player.getAttributeMap().get(PlayerAttributes.SMITHING_EQUIPMENT) == null) {
			return false;
		}
		SmithingTable[] values = TABLE.get(((Item) player.getAttributeMap().getObject(PlayerAttributes.SMITHING_EQUIPMENT)).getId());
		
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
	 * @param item the item that was used on the object.
	 * @param object the object the item was used on.
	 * @return <true> if the interface opened, <false> otherwise.
	 */
	public static boolean openInterface(Player player, Item item, GameObject object) {
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
						
						player.closeWidget();
						
						player.animation(new Animation(898));
						SendGraphic.local(player, 2123, object.getPosition().copy(), 50);
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
			SendGraphic.local(player, 2123, object.getPosition().copy(), 50);
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
				player.interfaceText(FRAME_DATA[i][0], "");
				player.interfaceText(FRAME_DATA[i][1], "");
				player.send(new ItemOnInterfaceSlotPacket(FRAME_DATA[i][3], new Item(-1, 0), FRAME_DATA[i][2]));
				continue;
			}
			
			table = values[i];
			
			if(table == null || table.getBar() == null) {
				return false;
			}
			
			final boolean has_bar = player.getInventory().computeAmountForId(table.getBar().getId()) >= table.getBarsRequired();
			final String bar_color = !has_bar ? "@red@" : "@gre@";
			final String name_color = player.getSkills()[Skills.SMITHING].getRealLevel() >= table.getLevelRequirement() && has_bar ? "@whi@" : "@bla@";
			
			player.interfaceText(FRAME_DATA[i][0], bar_color + table.getBarsRequired() + " Bar" + (table.getBarsRequired() != 1 ? "s" : ""));
			player.interfaceText(FRAME_DATA[i][1], name_color + TextUtils.capitalize(table.getName().toLowerCase()));
			player.send(new ItemOnInterfaceSlotPacket(FRAME_DATA[i][3], table.getProduced(), FRAME_DATA[i][2]));
		}
		if(table == null || table.getBar() == null) {
			return false;
		}
		player.getAttributeMap().set(PlayerAttributes.SMITHING_EQUIPMENT, table.getBar());
		player.getAttributeMap().set(PlayerAttributes.SMITHING_POSITION, object.getPosition());

		player.widget(994);
		return true;
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			player.animation(new Animation(898));
			SendGraphic.local(player, 2123, (Position) player.getAttributeMap().getObject(PlayerAttributes.SMITHING_POSITION), 50);
			amount--;
			if(amount < 1)
				t.cancel();
			Achievement.BLACKSMITH.inc(player);
		}
	}
	
	@Override
	public void onStop() {
		player.getAttributeMap().set(PlayerAttributes.SMITHING_EQUIPMENT, null);
		player.getAttributeMap().set(PlayerAttributes.SMITHING_POSITION, null);
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
		player.closeWidget();
		return canExecute();
	}
	
	@Override
	public boolean canExecute() {
		if(!player.getInventory().containsAny(2347, 2949)) {
			player.message("You need a hammer to forge items.");
			return false;
		}
		if(!player.getSkills()[Skills.SMITHING].reqLevel(definition.getLevelRequirement())) {
			player.message("You need a smithing level of " + definition.getLevelRequirement() + " to smith " + TextUtils.appendIndefiniteArticle(definition.getName().toLowerCase()) + ".");
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
	 * The map which contains all the bar ids with their respective tables.
	 */
	private static final Int2ObjectArrayMap<SmithingTable[]> TABLE = new Int2ObjectArrayMap<>(ImmutableMap.<Integer, SmithingTable[]>builder().put(2349, SmithingTable.BronzeTable.values()).put(2351, SmithingTable.IronTable.values()).put(2353, SmithingTable.SteelTable.values()).put(2359, SmithingTable.MithrilTable.values()).put(2361, SmithingTable.AdamantTable.values()).put(2363, SmithingTable.RuniteTable.values()).build());
	
	/**
	 * The array containing all of the frames data.
	 */
	private static final int[][] FRAME_DATA = {{1125, 1094, 0, 1119}, {1126, 1091, 0, 1120}, {1109, 1098, 0, 1121}, {1127, 1102, 0, 1122}, {1128, 1107, 0, 1123}, {1124, 1085, 1, 1119}, {1129, 1093, 1, 1120}, {1110, 1099, 1, 1121}, {1113, 1103, 1, 1122}, {1130, 1108, 1, 1123}, {1116, 1087, 2, 1119}, {1118, 1083, 2, 1120}, {1111, 1100, 2, 1121}, {1114, 1104, 2, 1122}, {1131, 1106, 2, 1123}, {1089, 1086, 3, 1119}, {1095, 1092, 3, 1120}, {1112, 1101, 3, 1121}, {1115, 1105, 3, 1122}, {1132, 1096, 3, 1123}, {1090, 1088, 4, 1119}, {8428, 8429, 4, 1120}, {11459, 11461, 4, 1121}, {13357, 13358, 4, 1122}, {1135, 1134, 4, 1123},};
}
