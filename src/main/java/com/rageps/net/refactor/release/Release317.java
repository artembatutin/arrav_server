package com.rageps.net.refactor.release;

import com.rageps.action.ActionInitializer;
import com.rageps.net.refactor.meta.PacketMetaDataGroup;
import com.rageps.net.refactor.packet.Packet;
import com.rageps.net.refactor.packet.in.decoder.IdleStatePacketPacketDecoder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.encoder.*;
import com.rageps.net.refactor.packet.out.encoder.update.NpcSynchronizationMessageEncoder;
import com.rageps.net.refactor.packet.out.encoder.update.PlayerSynchronizationMessageEncoder;
import com.rageps.net.refactor.packet.out.model.*;
import com.rageps.net.refactor.packet.out.model.update.NpcSynchronizationPacket;
import com.rageps.net.refactor.packet.out.model.update.PlayerSynchronizationPacket;
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
	public static final int[] PACKET_LENGTHS = {
			0, 0, 0, 1, -1, 0, 0, 0, 0, 0, // 0
			0, 0, 0, 0, 8, 0, 6, 2, 2, 0, // 10
			0, 2, 0, 6, 0, 12, 0, 0, 0, 0, // 20
			0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30
			2, 6, 0, 6, 0, -1, 0, 0, 0, 0, // 40
			0, 0, 0, 12, 0, 0, 0, 8, 8, 0, // 50
			0, 8, 0, 0, 0, 0, 0, 0, 0, 0, // 60
			6, 0, 2, 2, 8, 6, 0, -1, 0, 6, // 70
			0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80
			0, 0, 0, 0, 0, 3, 0, 0, -1, 0, // 90
			0, 13, 0, -1, 0, 0, 0, 0, 0, 0, // 100
			0, 0, 0, 0, 0, 0, 0, 6, 0, 0, // 110
			1, 0, 6, 0, 0, 0, -1, 0, 2, 6, // 120
			0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130
			0, 0, 0, 0, 0, 6, 0, 0, 0, 0, // 140
			0, 0, 1, 2, 0, 2, 6, 0, 0, 0, // 150
			0, 0, 0, 0, -1, -1, 0, 0, 0, 0, // 160
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170
			0, 8, 0, 3, 0, 2, 0, 0, 8, 1, // 180
			0, 0, 12, 0, 0, 0, 0, 0, 0, 0, // 190
			2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200
			4, 0, 0, 0, 7, 8, 0, 0, 10, 0, // 210
			0, 0, 0, 0, 0, 0, -1, 0, 6, 0, // 220
			1, 0, 0, 0, 6, 0, 6, 8, 1, 0, // 230
			0, 4, 0, 0, 0, 0, -1, 0, -1, 4, // 240
			0, 0, 6, 6, 0, 0, // 250
	};

	/**
	 * Creates and initialises this release.
	 */
	public Release317() {
		super(317, PacketMetaDataGroup.createFromArray(PACKET_LENGTHS));
		init();
	}

	/**
	 * Initialises this release by registering encoders and decoders.
	 */
	private void init() {
		// register decoders

		register(0, new IdleStatePacketPacketDecoder());
		/*WalkMessageDecoder walkMessageDecoder = new WalkMessageDecoder();
		register(248, walkMessageDecoder);
		register(164, walkMessageDecoder);
		register(98, walkMessageDecoder);

		register(0, new KeepAliveMessageDecoder());
		register(101, new PlayerDesignMessageDecoder());
		register(4, new PublicChatMessageDecoder());
		register(103, new CommandMessageDecoder());
		register(214, new SwitchItemMessageDecoder());

		register(132, new FirstObjectActionMessageDecoder());
		register(252, new SecondObjectActionMessageDecoder());
		register(70, new ThirdObjectActionMessageDecoder());

		register(122, new FirstItemOptionMessageDecoder());
		register(41, new SecondItemOptionMessageDecoder());
		register(16, new ThirdItemOptionMessageDecoder());
		register(75, new FourthItemOptionMessageDecoder());
		register(87, new FifthItemOptionMessageDecoder());

		register(145, new FirstItemActionMessageDecoder());
		register(117, new SecondItemActionMessageDecoder());
		register(43, new ThirdItemActionMessageDecoder());
		register(129, new FourthItemActionMessageDecoder());
		register(135, new FifthItemActionMessageDecoder());

		register(185, new ButtonMessageDecoder());
		register(130, new ClosedInterfaceMessageDecoder());
		register(208, new EnteredAmountMessageDecoder());
		register(40, new DialogueContinueMessageDecoder());
		register(120, new FlashingTabClickedMessageDecoder());

		register(53, new ItemOnItemMessageDecoder());
		register(57, new ItemOnNpcMessageDecoder());
		register(237, new MagicOnItemMessageDecoder());
		register(249, new MagicOnPlayerMessageDecoder());
		register(131, new MagicOnNpcMessageDecoder());

		register(3, new FocusUpdateMessageDecoder());
		register(45, new FlaggedMouseEventMessageDecoder());
		register(241, new MouseClickedMessageDecoder());
		register(86, new ArrowKeyMessageDecoder());
		register(95, new PrivacyOptionMessageDecoder());

		SpamPacketMessageDecoder spamMessageDecoder = new SpamPacketMessageDecoder();
		register(77, spamMessageDecoder);
		register(78, spamMessageDecoder);
		register(165, spamMessageDecoder);
		register(189, spamMessageDecoder);
		register(210, spamMessageDecoder);
		register(226, spamMessageDecoder);
		register(121, spamMessageDecoder);

		register(155, new FirstNpcActionMessageDecoder());
		register(72, new SecondNpcActionMessageDecoder());
		register(17, new ThirdNpcActionMessageDecoder());
		register(21, new FourthNpcActionMessageDecoder());
		register(18, new FifthNpcActionMessageDecoder());

		register(236, new TakeTileItemMessageDecoder());
		register(192, new ItemOnObjectMessageDecoder());

		register(128, new FirstPlayerActionMessageDecoder());
		register(153, new SecondPlayerActionMessageDecoder());
		register(73, new ThirdPlayerActionMessageDecoder());
		register(139, new FourthPlayerActionMessageDecoder());
		register(39, new FifthPlayerActionMessageDecoder());

		register(188, new AddFriendMessageDecoder());
		register(133, new AddIgnoreMessageDecoder());
		register(215, new RemoveFriendMessageDecoder());
		register(74, new RemoveIgnoreMessageDecoder());
		register(126, new PrivateChatMessageDecoder());

		register(218, new ReportAbuseMessageDecoder());*/

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
	}
}