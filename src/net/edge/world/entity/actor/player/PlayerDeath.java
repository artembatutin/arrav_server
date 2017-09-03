package net.edge.world.entity.actor.player;

import com.google.common.collect.Ordering;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.GameConstants;
import net.edge.content.PlayerPanel;
import net.edge.content.minigame.Minigame;
import net.edge.content.minigame.MinigameHandler;
import net.edge.content.scoreboard.PlayerScoreboardStatistic;
import net.edge.content.scoreboard.ScoreboardManager;
import net.edge.content.skill.Skill;
import net.edge.content.skill.Skills;
import net.edge.content.skill.prayer.Prayer;
import net.edge.net.host.HostManager;
import net.edge.net.packet.out.SendConfig;
import net.edge.net.packet.out.SendGraphic;
import net.edge.net.packet.out.SendWalkable;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.ActorDeath;
import net.edge.world.entity.actor.combat.CombatConstants;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.combat.hit.HitIcon;
import net.edge.world.entity.actor.combat.hit.Hitsplat;
import net.edge.world.entity.actor.combat.weapon.WeaponInterface;
import net.edge.world.entity.actor.player.assets.Rights;
import net.edge.world.entity.actor.update.UpdateFlag;
import net.edge.world.entity.item.GroundItem;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.container.impl.Equipment;
import net.edge.world.entity.item.container.session.ExchangeSessionManager;
import net.edge.world.entity.region.Region;
import net.edge.world.locale.Position;
import net.edge.world.locale.loc.Location;

import java.util.Iterator;
import java.util.Optional;

import static net.edge.content.achievements.Achievement.KILLER;
import static net.edge.content.minigame.Minigame.MinigameSafety.SAFE;

/**
 * The {@link ActorDeath} implementation that is dedicated to managing the
 * death process for all {@link Player}s.
 * @author lare96 <http://github.com/lare96>
 */
public final class PlayerDeath extends ActorDeath<Player> {
	
	/**
	 * Determines if the death message is send.
	 */
	private boolean deathMessage;
	
	/**
	 * Creates a new {@link PlayerDeath}.
	 * @param player the player who has died and needs the death process.
	 */
	public PlayerDeath(Player player) {
		super(player);
	}
	
	@Override
	public void preDeath() {
		getActor().getActivityManager().disable();
		getActor().animation(new Animation(0x900, Animation.AnimationPriority.HIGH));
		getActor().setSkillAction(Optional.empty());
		ExchangeSessionManager.get().reset(getActor());
		
		if(!MinigameHandler.getMinigame(getActor()).isPresent()) {
			deathMessage = true;
		}
		
		if(Prayer.isActivated(getActor(), Prayer.RETRIBUTION)) {
			getActor().graphic(new Graphic(437));
			final int hit = RandomUtils.inclusive(CombatConstants.MAXIMUM_RETRIBUTION_DAMAGE);
			if(getActor().inMulti()) {
				getActor().getLocalMobs().stream().filter(n -> n.getPosition().withinDistance(getActor().getPosition(), 2)).forEach(h -> h.damage(new Hit(hit, Hitsplat.NORMAL, HitIcon.NONE)));
				if(getActor().inWilderness()) {
					getActor().getLocalPlayers().stream().filter(p -> p.getPosition().withinDistance(getActor().getPosition(), 2)).forEach(h -> h.damage(new Hit(hit, Hitsplat.NORMAL, HitIcon.NONE)));
				}
			} else {
				Actor victim = getActor().getCombat().getLastDefender();
				if(victim != null && victim.getPosition().withinDistance(getActor().getPosition(), 2)) {
					victim.damage(new Hit(RandomUtils.inclusive(hit), Hitsplat.NORMAL, HitIcon.NONE));
				}
			}
		}
		
		if(Prayer.isActivated(getActor(), Prayer.WRATH)) {
			getActor().graphic(new Graphic(2259));
			int x = getActor().getPosition().getX() - 3;
			int y = getActor().getPosition().getY() - 2;
			for(int i = 0; i < 25; i++) {
				x++;
				if(i == 5 || i == 10 || i == 15 || i == 20) {
					x -= 5;
					y++;
				}
				if(i % 2 == 1)
					continue;
				SendGraphic.local(getActor(), 2260, new Position(x, y, getActor().getPosition().getZ()), 25);
			}
			int maxHit = (int) ((getActor().getSkills()[Skills.PRAYER].getLevel() / 100.D) * 25);
			if(getActor().inMulti()) {
				getActor().getLocalMobs().stream().filter(n -> n.getPosition().withinDistance(getActor().getPosition(), 3)).forEach(h -> h.damage(new Hit(RandomUtils.inclusive(maxHit), Hitsplat.NORMAL, HitIcon.NONE)));
				if(getActor().inWilderness()) {
					getActor().getLocalPlayers().stream().filter(p -> p.getPosition().withinDistance(getActor().getPosition(), 3)).forEach(h -> h.damage(new Hit(RandomUtils.inclusive(maxHit), Hitsplat.NORMAL, HitIcon.NONE)));
				}
			} else {
				Actor victim = getActor().getCombat().getLastDefender();
				if(victim != null && victim.getPosition().withinDistance(getActor().getPosition(), 3)) {
					victim.damage(new Hit(RandomUtils.inclusive(maxHit), Hitsplat.NORMAL, HitIcon.NONE));
				}
			}
		}
		
		if(Location.inFunPvP(getActor())) {
			getActor().move(new Position(3076, 3503, 1));
			setCounter(6);
		}
	}
	
	@Override
	public void death() {
		Optional<Player> killer = getActor().getCombat().getDamageCache().getPlayerKiller();
		Optional<Minigame> optional = MinigameHandler.getMinigame(getActor());
		if(optional.isPresent()) {
			optional.get().onDeath(getActor());
			if(optional.get().getSafety().equals(Minigame.MinigameSafety.DEFAULT)) {
				if(getActor().getRights().less(Rights.ADMINISTRATOR)) {
					dropItems(getActor(), killer, false);
				}
			} else if(optional.get().getSafety().equals(Minigame.MinigameSafety.DANGEROUS)) {
				if(getActor().getRights().less(Rights.ADMINISTRATOR)) {
					dropItems(getActor(), killer, true);
				}
			}
			getActor().move(optional.get().deathPosition(getActor()));
			killer.ifPresent(k -> optional.get().onKill(k, getActor()));
			return;
		}
		if(getActor().getRights().less(Rights.ADMINISTRATOR)) {
			dropItems(getActor(), killer, false);
			getActor().move(new Position(3084, 3514));
		}
		
		killer.ifPresent(k -> {
			if(HostManager.same(getActor(), k)) {
				k.message("You don't receive any points because you and " + getActor().getFormatUsername() + " are connected from the same network.");
				return;
			}
			if(getActor().lastKiller != null && getActor().lastKiller.equalsIgnoreCase(k.getFormatUsername())) {
				k.message("You don't receive any points because you have killed " + getActor().getFormatUsername() + " twice in a row.");
				return;
			}
			KILLER.inc(k);
			
			//deaths
			PlayerScoreboardStatistic characterStatistic = ScoreboardManager.get().getPlayerScoreboard().putIfAbsent(getActor().getFormatUsername(), new PlayerScoreboardStatistic(getActor().getFormatUsername()));
			if(characterStatistic == null) {
				characterStatistic = new PlayerScoreboardStatistic(getActor().getFormatUsername());
			}
			PlayerPanel.PVP_DEATHS.refresh(getActor(), "@or2@ - Current Player deaths: @yel@" + characterStatistic.getDeaths().incrementAndGet());
			PlayerPanel.TOTAL_PLAYER_DEATHS.refresh(getActor(), "@or2@ - Killed by players: @yel@" + getActor().getDeathsByPlayer().incrementAndGet());
			
			//kills
			PlayerScoreboardStatistic killerStatistic = ScoreboardManager.get().getPlayerScoreboard().putIfAbsent(k.getFormatUsername(), new PlayerScoreboardStatistic(k.getFormatUsername()));
			if(killerStatistic == null) {
				killerStatistic = new PlayerScoreboardStatistic(k.getFormatUsername());
			}
			
			PlayerPanel.PVP_KILLS.refresh(k, "@or2@ - Death: @yel@" + killerStatistic.getKills().incrementAndGet());
			PlayerPanel.TOTAL_PLAYER_KILLS.refresh(k, "@or2@ - Killed players: @yel@" + k.getPlayerKills().incrementAndGet());
			
			//killstreak
			if(k.getCurrentKillstreak().incrementAndGet() > k.getHighestKillstreak().get()) {
				k.getHighestKillstreak().set(k.getCurrentKillstreak().get());
				PlayerPanel.HIGHEST_KILLSTREAK.refresh(k, "@or2@ - Highest killstreak: @yel@" + k.getHighestKillstreak());
			}
			
			PlayerPanel.CURRENT_KILLSTREAK.refresh(k, "@or2@ - Current killstreak: @yel@" + k.getCurrentKillstreak().get());
			
			if(killerStatistic.getCurrentKillstreak().incrementAndGet() > killerStatistic.getHighestKillstreak().get()) {
				killerStatistic.getHighestKillstreak().set(killerStatistic.getCurrentKillstreak().get());
				PlayerPanel.PVP_HIGHEST_KILLSTREAKS.refresh(k, "@or2@ - Highest killstreak: @yel@" + killerStatistic.getHighestKillstreak().get());
			}
			PlayerPanel.PVP_CURRENT_KILLSTREAKS.refresh(k, "@or2@ - Current killstreak: @yel@" + killerStatistic.getCurrentKillstreak().get());
			
			k.message(RandomUtils.random(GameConstants.DEATH_MESSAGES).replaceAll("-victim-", getActor().getFormatUsername()).replaceAll("-killer-", k.getFormatUsername()));
		});
		
		getActor().getCombat().getDamageCache().calculateProperKiller().ifPresent(e -> {
			if(e.isMob()) {
				getActor().getDeathsByNpc().incrementAndGet();
				PlayerPanel.TOTAL_NPC_DEATHS.refresh(getActor(), "@or2@ - Killed by mobs: @yel@" + getActor().getDeathsByNpc().get());
			}
		});
	}
	
	@Override
	public void postDeath() {
		getActor().closeWidget();
		getActor().getCombat().reset();
		getActor().getCombat().getDamageCache().clear();
		getActor().getTolerance().reset();
		getActor().getSpecialPercentage().set(100);
		getActor().out(new SendConfig(301, 0));
		getActor().setSpecialActivated(false);
		getActor().getSkullTimer().set(0);
		getActor().setRunEnergy(100);
		getActor().setAntifireDetail(Optional.empty());
		getActor().getTeleblockTimer().set(0);
		getActor().animation(new Animation(65535));
		WeaponInterface.execute(getActor(), getActor().getEquipment().get(Equipment.WEAPON_SLOT));
		if(deathMessage) {
			getActor().message(getActor().getRights().less(Rights.ADMINISTRATOR) ? "Oh dear, you're dead!" : "You are unaffected by death because of your rank.");
		}
		getActor().out(new SendWalkable(-1));
		Prayer.deactivateAll(getActor());
		
		Optional<Minigame> minigame = MinigameHandler.getMinigame(getActor());
		if(!minigame.isPresent() || minigame.get().getSafety() != SAFE) {
			if(getActor().isIronMan() && !getActor().isIronMaxed()) {
				for(int index = 0; index < getActor().getSkills().length; index++) {
					Skill skill = getActor().getSkills()[index];
					int experience = (int) (skill.getExperience() * 0.75);
					int newLevel = Skills.getLevelForExperience(experience);
					if(index == Skills.HITPOINTS && newLevel < 10) {
						newLevel = 10;
					} else if(newLevel < 1) {
						newLevel = 1;
					}
					skill.setRealLevel(newLevel);
				}
			}
		}
		
		Skills.restoreAll(getActor());
		getActor().getActivityManager().enable();
		getActor().getFlags().flag(UpdateFlag.APPEARANCE);
		minigame.ifPresent(m -> m.postDeath(getActor()));
	}
	
	/**
	 * Calculates and drops all of the items from {@code player} for
	 * {@code killer}.
	 * @param player  the player whose items are being dropped.
	 * @param killer  the killer who the items are being dropped for.
	 * @param dropAll indicates no-items will be kept except the untradables.
	 */
	private void dropItems(Player player, Optional<Player> killer, boolean dropAll) {
		ObjectList<Item> keep = new ObjectArrayList<>();
		ObjectList<Item> drop = new ObjectArrayList<>();
		killer.ifPresent(p -> {
			if(getActor().lastKiller != null && !getActor().lastKiller.equalsIgnoreCase(p.getFormatUsername())) {
				if(!HostManager.same(player, p)) {
					Rights right = p.getRights();
					int baseAmount = RandomUtils.inclusive(10, 50) * (GameConstants.DOUBLE_BLOOD_MONEY_EVENT ? 2 : 1);
					int amount = right.equals(Rights.EXTREME_DONATOR) ? ((int) (baseAmount * 1.30)) : right.equals(Rights.SUPER_DONATOR) ? ((int) (baseAmount * 1.20)) : right.equals(Rights.DONATOR) ? ((int) (baseAmount * 1.10)) : baseAmount;
					drop.add(new Item(19000, amount));
				}
			}
			getActor().lastKiller = player.getFormatUsername();
		});
		for(Item i : player.getEquipment().getItems()) {
			if(i == null)
				continue;
			if(i.getDefinition().isTradable()) {
				drop.add(i);
			} else {
				keep.add(i);
			}
		}
		for(Item i : player.getInventory().getItems()) {
			if(i == null)
				continue;
			if(i.getDefinition().isTradable()) {
				drop.add(i);
			} else {
				keep.add(i);
			}
		}
		Player viewer = killer.orElse(player);
		Region reg = player.getRegion().orElse(null);
		if(drop.size() > 0) {
			player.getEquipment().clear();
			player.getInventory().clear();
			int amount = dropAll ? 0 : player.getSkullTimer().get() > 0 ? 0 : 3;
			if(Prayer.isActivated(player, Prayer.PROTECT_ITEM) || Prayer.isActivated(player, Prayer.CURSES_PROTECT_ITEM)) {
				if(!dropAll) {
					amount++;
				}
			}
			if(amount > 0) {
				drop.sort(new Ordering<Item>() {
					@Override
					public int compare(Item left, Item right) {
						return Integer.compare(left.getValue().getValue(), right.getValue().getValue());
					}
				}.reverse());
				for(Iterator<Item> it = drop.iterator(); it.hasNext(); ) {
					Item next = it.next();
					if(amount == 0) {
						break;
					}
					player.getInventory().add(new Item(next.getId()));
					if(next.getDefinition().isStackable() && next.getAmount() > 1) {
						next.decrementAmountBy(1);
					} else {
						it.remove();
					}
					amount--;
				}
			}
			player.getEquipment().updateBulk();
			player.getInventory().updateBulk();
			if(reg != null) {
				reg.register(new GroundItem(new Item(526), player.getPosition(), viewer));
				for(Item droppedItem : drop) {
					if(droppedItem == null)
						continue;
					reg.register(new GroundItem(droppedItem, player.getPosition(), viewer));
				}
			}
			player.getInventory().addAll(keep.toArray(new Item[keep.size()]));
		} else {
			if(reg != null) {
				reg.register(new GroundItem(new Item(526), player.getPosition(), viewer));
			}
		}
	}
}
