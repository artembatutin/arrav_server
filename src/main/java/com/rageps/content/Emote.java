package com.rageps.content;

import com.rageps.action.impl.ButtonAction;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.model.Animation;
import com.rageps.world.model.Graphic;

/**
 * An enumeration of emotes.
 * @author Artem Batutin
 */
public enum Emote {
	
	YES(168, 855, -1),
	NO(169, 856, -1),
	BOW(164, 858, -1),
	ANGRY(167, 864, -1),
	THINK(162, 857, -1),
	WAVE(163, 863, -1),
	SHRUG(13370, 2113, -1),
	CHEER(171, 862, -1),
	BECKON(165, 859, -1),
	LAUGH(170, 861, -1),
	JUMP_FOR_JOY(13366, 2109, -1),
	YAWN(13368, 2111, -1),
	DANCE(166, 866, -1),
	JIG(13363, 2106, -1),
	TWIRL(13364, 2107, -1),
	HEADBANG(13364, 2108, -1),
	CRY(161, 860, -1),
	BLOW_KISS(11100, 1374, 574),
	PANIC(13362, 2105, -1),
	RASBERRY(13367, 2110, -1),
	CLAP(172, 865, -1),
	SAUTE(13367, 2112, -1),
	GOBLIN_BOW(13383, 2127, -1),
	GOBLIN_SALUTE(13384, 2128, -1),
	GLASS_BOX(667, 1131, -1),
	CLIMB_ROPE(6503, 1130, -1),
	LEAN(6506, 1129, -1),
	GLASS_WALL(666, 1128, -1),
	IDEA(22588, 4276, 712),
	STOMP(22589, 4278, -1),
	FLAP(22590, 4280, -1),
	SLAP_HEAD(22591, 4275, -1),
	ZOMBIE_WALK(18464, 3544, -1),
	ZOMBIE_DANCE(18465, 3543, -1),
	ZOMBIE_HAND(22593, 7272, 1244),
	SCARED(15166, 2836, -1),
	BUNY_HOP(18686, 6111, -1),
	//SKILLCAPE(18686, 1, 1),
	SNOW_MAN(22832, 7531, -1),
	AIR_GUITAR(22833, 2414, 1537),
	SAFETY_FIRST(22834, 8770, 1553);
	
	/**
	 * The initialization button id of the emote.
	 */
	private final int button;
	
	/**
	 * The animation id of the emote.
	 */
	private final int animation;
	
	/**
	 * The graphic id of the emote.
	 */
	private final int graphic;
	
	Emote(int button, int animation, int graphic) {
		this.button = button;
		this.animation = animation;
		this.graphic = graphic;
	}
	
	public static void action() {
		for(Emote emote : Emote.values()) {
			new ButtonAction() {
				@Override
				public boolean click(Player player, int button) {
					if(emote.animation != -1)
						player.animation(new Animation(emote.animation));
					if(emote.graphic != -1)
						player.graphic(new Graphic(emote.graphic));
					return true;
				}
			}.register(emote.button);
		}
	}
	
}
