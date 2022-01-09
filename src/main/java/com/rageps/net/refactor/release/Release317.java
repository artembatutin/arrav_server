package com.rageps.net.refactor.release;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rageps.action.ActionInitializer;
import com.rageps.net.refactor.meta.PacketMetaDataGroup;
import com.rageps.net.refactor.packet.Packet;
import com.rageps.net.refactor.packet.in.decoder.*;
import com.rageps.net.refactor.packet.in.decoder.interface_actions.*;
import com.rageps.net.refactor.packet.in.decoder.mob_actions.*;
import com.rageps.net.refactor.packet.in.decoder.object_actions.*;
import com.rageps.net.refactor.packet.in.decoder.social.*;
import com.rageps.net.refactor.packet.in.handler.InterfaceClickPacketPacketHandler;
import com.rageps.net.refactor.packet.in.model.InterfaceActionPacketPacket;
import com.rageps.net.refactor.packet.in.model.InterfaceClickPacketPacket;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.encoder.*;
import com.rageps.net.refactor.packet.out.encoder.update.NpcSynchronizationMessageEncoder;
import com.rageps.net.refactor.packet.out.encoder.update.PlayerSynchronizationMessageEncoder;
import com.rageps.net.refactor.packet.out.model.*;
import com.rageps.net.refactor.packet.out.model.update.NpcSynchronizationPacket;
import com.rageps.net.refactor.packet.out.model.update.PlayerSynchronizationPacket;
import com.rageps.util.json.JsonLoader;
import org.apache.tools.ant.taskdefs.Move;
import org.reflections.Reflections;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Set;

/**
 * A {@link Release} implementation for the 317 protocol.
 *
 * @author Graham
 */
public final class Release317 extends Release {

	/**
	 * The incoming packet lengths array.
	 */
	//public static final int[] PACKET_LENGTHS = {
	//		0, 0, 0, 1, -1, 0, 0, 0, 0, 0, // 0
	//		0, 0, 0, 0, 8, 0, 6, 2, 2, 0, // 10
	//		0, 2, 0, 6, 0, 12, 0, 0, 0, 0, // 20
	//		0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30
	//		2, 6, 0, 6, 0, -1, 0, 0, 0, 0, // 40
	//		0, 0, 0, 12, 0, 0, 0, 8, 8, 0, // 50
	//		0, 8, 0, 0, 0, 0, 0, 0, 0, 0, // 60
	//		6, 0, 2, 2, 8, 6, 0, -1, 0, 6, // 70
	//		0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80
	//		0, 0, 0, 0, 0, 3, 0, 0, -1, 0, // 90
	//		0, 13, 0, -1, 0, 0, 0, 0, 0, 0, // 100
	//		0, 0, 0, 0, 0, 0, 0, 6, 0, 0, // 110
	//		1, 0, 6, 0, 0, 0, -1, 0, 2, 6, // 120
	//		0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130
	//		0, 0, 0, 0, 0, 6, 0, 0, 0, 0, // 140
	//		0, 0, 1, 2, 0, 2, 6, 0, 0, 0, // 150
	//		0, 0, 0, 0, -1, -1, 0, 0, 0, 0, // 160
	//		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170
	//		0, 8, 0, 3, 0, 2, 0, 0, 8, 1, // 180
	//		0, 0, 12, 0, 0, 0, 0, 0, 0, 0, // 190
	//		2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200
	//		4, 0, 0, 0, 7, 8, 0, 0, 10, 0, // 210
	//		0, 0, 0, 0, 0, 0, -1, 0, 6, 0, // 220
	//		1, 0, 0, 0, 6, 0, 6, 8, 1, 0, // 230
	//		0, 4, 0, 0, 0, 0, -1, 0, -1, 4, // 240
	//		0, 0, 6, 6, 0, 0, // 250
	//};
	public static int[] PACKET_LENGTHS  = new int[256];

	/**
	 * Creates and initialises this release.
	 */
	public Release317() {
		super(317);
		init();
		setIncomingPacketMetaData(PacketMetaDataGroup.createFromArray(PACKET_LENGTHS));
	}

	/**
	 * Initialises this release by registering encoders and decoders.
	 */
	private void init() {
		// register decoders

		//register(0, new IdleStatePacketPacketDecoder());



		register(103, new CommandPacketPacketDecoder());

		register(213, new InterfaceActionPacketPacketDecoder());
		register(181, new MagicOnGroundItemPacketPacketDecoder());
		register(232, new OperateEquipmentPacketPacketDecoder());
		register(237, new MagicOnItemPacketPacketDecoder());
		register(10, new ConstructionPacketPacketDecoder());
		register(135, new InputXOptionPacketPacketDecoder());
		register(208, new EnterAmountPacketPacketDecoder());
		register(60, new EnterSyntaxPacketDecoder());
		register(57, new ItemOnMobPacketPacketDecoder());
		register(202, new IdleStatePacketPacketDecoder());
		register(153, new AttackPlayerPacketPacketDecoder());
		register(249, new MagicOnPlayerPacketDecoder());
		register(101, new CharacterSelectionPacketPacketDecoder());
		register(186, new SummoningCreationPacketPacketDecoder());
		register(3, new FocusChangePacketPacketDecoder());
		register(4, new ChatPacketPacketDecoder());
		register(185, new ClickButtonPacketPacketDecoder());
		register(105, new MarketPacketPacketDecoder());

		DefaultPacketPacketDecoder defaultDecoder = new DefaultPacketPacketDecoder();
		register(0, defaultDecoder);
		register(95, defaultDecoder);
		register(241, defaultDecoder);
		register(86, defaultDecoder);

		register(40, new AdvanceDialoguePacketPacketDecoder());
		register(87, new DropItemPacketPacketDecoder());
		register(73, new FollowPlayerPacketPacketDecoder());
		register(130, new InterfaceClickPacketDecoder());





		register(122, new FirstItemPacketDecoder());
		register(145, new FirstItemInterfacePacketDecoder());
		register(117, new SecondItemInterfacePacketDecoder());
		register(43, new ThirdItemInterfacePacketDecoder());
		register(129, new FourthItemInterfacePacketDecoder());
		register(41, new EquipItemPacketDecoder());
		register(214, new SwapSlotPacketDecoder());
		register(216, new BankTabPacketDecoder());
		register(53, new ItemOnItemPacketPacketDecoder());
		register(192, new ItemOnObjectPacketPacketDecoder());
		register(14, new ItemOnPlayerPacketPacketDecoder());



		register(139, new TradeRequestPacketPacketDecoder());
		register(128, new DuelRequestPacketPacketDecoder());

		MovementQueuePacketPacketDecoder movementPacket = new MovementQueuePacketPacketDecoder();
		register(248, movementPacket);
		register(164, movementPacket);
		register(98, movementPacket);

		register(72, new AttackMobPacketDecoder());
		register(131, new AttackMobMagicPacketDecoder());
		register(155, new FirstActionMobPacketDecoder());
		register(17, new SecondActionMobPacketDecoder());
		register(21, new ThirdActionMobPacketDecoder());
		register(18, new FourthActionMobPacketDecoder());


		register(132, new FirstActionObjectPacketDecoder());
		register(252, new SecondActionObjectPacketDecoder());
		register(70, new ThirdActionObjectPacketDecoder());
		register(234, new FourthActionObjectPacketDecoder());
		register(228, new FifthActionObjectPacketDecoder());
		register(35, new SpellOnObjectPacketDecoder());

		//object actions

		register(253, new ItemOnItemNodePacketPacketDecoder());
		register(236, new PickupItemPacketPacketDecoder());

		register(188, new AddFriendPacketDecoder());
		register(215, new RemoveFriendPacketDecoder());
		register(133, new AddIgnorePacketDecoder());
		register(74, new RemoveIgnorePacketDecoder());
		register(126, new SendPMPacketDecoder());

		register(121, new UpdateRegionPacketPacketDecoder());
		register(20, new ShopAccessPacketPacketDecoder());



		// register encoders
		register(InterfacePlayerModelPacket.class, new InterfacePlayerModelPacketEncoder());
		register(MobUpdatePacket.class, new MobUpdatePacketEncoder());
		register(ArrowPositionPacket.class, new ArrowPositionPacketEncoder());
		register(EnterNamePacket.class, new EnterNamePacketEncoder());
		register(ShopPricePacket.class, new ShopPricePacketEncoder());
		register(MapRegionPacket.class, new MapRegionPacketEncoder());
		register(InterfacePacket.class, new InterfacePacketEncoder());
		register(ClearTextPacket.class, new ClearTextPacketEncoder());
		register(ItemNodeRemovalPacket.class, new ItemNodeRemovalPacketEncoder());
		register(CameraShakePacket.class, new CameraShakePacketEncoder());
		register(CloseInterfacePacket.class, new CloseInterfacePacketEncoder());
		register(AddIgnorePacket.class, new AddIgnorePacketEncoder());
		register(MoveComponentPacket.class, new MoveComponentPacketEncoder());
		register(EnterAmountPacket.class, new EnterAmountPacketEncoder());
		register(MobDropPacket.class, new MobDropPacketEncoder());
		register(InventoryInterfacePacket.class, new InventoryInterfacePacketEncoder());
		register(InterfaceColorPacket.class, new InterfaceColorPacketEncoder());
		register(ShopPacket.class, new ShopPacketEncoder());
		register(TabPacket.class, new TabPacketEncoder());
		register(InterfaceStringPacket.class, new InterfaceStringPacketEncoder());
		register(ShopStockPacket.class, new ShopStockPacketEncoder());
		register(LogoutPacket.class, new LogoutPacketEncoder());
		register(YellPacket.class, new YellPacketEncoder());
		register(ClanDetailsPacket.class, new ClanDetailsPacketEncoder());
		register(UpdateSpecialPacket.class, new UpdateSpecialPacketEncoder());
		register(PrivateMessagePacket.class, new PrivateMessagePacketEncoder());
		register(CoordinatesPacket.class, new CoordinatesPacketEncoder());
		register(PaletteMapPacket.class, new PaletteMapPacketEncoder());
		register(ChatInterfacePacket.class, new ChatInterfacePacketEncoder());
		register(InterfaceLayerPacket.class, new InterfaceLayerPacketEncoder());
		register(LinkPacket.class, new LinkPacketEncoder());
		register(AddFriendPacket.class, new AddFriendPacketEncoder());
		register(CombatTargetPacket.class, new CombatTargetPacketEncoder());
		register(ItemsOnInterfacePacket.class, new ItemsOnInterfacePacketEncoder());
		register(RemoveObjectsPacket.class, new RemoveObjectsPacketEncoder());
		register(PlayerUpdatePacket.class, new PlayerUpdatePacketEncoder());
		register(InterfaceAnimationPacket.class, new InterfaceAnimationPacketEncoder());
		register(ObjectsConstructionPacket.class, new ObjectsConstructionPacketEncoder());
		register(ConfigPacket.class, new ConfigPacketEncoder());
		register(ScrollBarPacket.class, new ScrollBarPacketEncoder());
		register(ObjectAnimationPacket.class, new ObjectAnimationPacketEncoder());
		register(SlotPacket.class, new SlotPacketEncoder());
		register(ArrowEntityPacket.class, new ArrowEntityPacketEncoder());
		register(ItemNodePacket.class, new ItemNodePacketEncoder());
		register(TooltipPacket.class, new TooltipPacketEncoder());
		register(InterfaceItemPacket.class, new InterfaceItemPacketEncoder());
		register(FeedMessagePacket.class, new FeedMessagePacketEncoder());
		register(WildernessActivityPacket.class, new WildernessActivityPacketEncoder());
		register(ItemOnInterfaceSlotPacket.class, new ItemOnInterfaceSlotPacketEncoder());
		register(EnergyPacket.class, new EnergyPacketEncoder());
		register(FadePacket.class, new FadePacketEncoder());
		register(ClanBannedPacket.class, new ClanBannedPacketEncoder());
		register(CameraResetPacket.class, new CameraResetPacketEncoder());
		register(PrivateMessageStatusPacket.class, new PrivateMessageStatusPacketEncoder());
		register(ContextMenuPacket.class, new ContextMenuPacketEncoder());
		register(SkillGoalPacket.class, new SkillGoalPacketEncoder());
		register(FlashTabPacket.class, new FlashTabPacketEncoder());
		register(MinimapStatePacket.class, new MinimapStatePacketEncoder());
		register(MessagePacket.class, new MessagePacketEncoder());
		register(TaskPacket.class, new TaskPacketEncoder());
		register(ObjectRemovalPacket.class, new ObjectRemovalPacketEncoder());
		register(WalkableInterfacePacket.class, new WalkableInterfacePacketEncoder());
		register(ChatOptionPacket.class, new ChatOptionPacketEncoder());
		register(CameraAnglePacket.class, new CameraAnglePacketEncoder());
		register(SkillPacket.class, new SkillPacketEncoder());
		register(CameraMovementPacket.class, new CameraMovementPacketEncoder());
		register(MultiIconPacket.class, new MultiIconPacketEncoder());
		register(ScorePacket.class, new ScorePacketEncoder());
		register(ItemModelInterfacePacket.class, new ItemModelInterfacePacketEncoder());
		register(PrivateFriendUpdatePacket.class, new PrivateFriendUpdatePacketEncoder());
		register(ClearContainerPacket.class, new ClearContainerPacketEncoder());
		register(ObjectPacket.class, new ObjectPacketEncoder());
		register(ClanMessagePacket.class, new ClanMessagePacketEncoder());
		register(InterfaceNpcModelPacket.class, new InterfaceNpcModelPacketEncoder());
		register(BroadcastPacket.class, new BroadcastPacketEncoder());
		register(ProjectilePacket.class, new ProjectilePacketEncoder());
		register(GraphicPacket.class, new GraphicPacketEncoder());
		register(ForceTabPacket.class, new ForceTabPacketEncoder());

		register(PlayerSynchronizationPacket.class, new PlayerSynchronizationMessageEncoder());
		register(NpcSynchronizationPacket.class, new NpcSynchronizationMessageEncoder());


		new JsonLoader("./data/def/packet/packet_sizes.json") {
			@Override
			public void load(JsonObject reader, Gson builder) {
				int opcode = reader.get("opcode").getAsInt();
				int size = reader.get("size").getAsInt();
				PACKET_LENGTHS[opcode] = size;
			}
		}.load();


	}
}