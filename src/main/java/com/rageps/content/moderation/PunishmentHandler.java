package com.rageps.content.moderation;


import com.rageps.net.sql.punishments.UpdatePunishmentTransaction;
import com.rageps.util.Utility;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.ArrayList;

public class PunishmentHandler {

    public static ArrayList<Punishment> punishments = new ArrayList<>();


    public static void punishPlayer(Player victim, Player agent, PunishmentType type, PunishmentPolicy policy, String reason, long time) {
        try {
            long expireDate = System.currentTimeMillis() + time;
            String duration = Utility.convertTime(time);
            Punishment punishment = new Punishment(victim.getFormatUsername(), agent.getFormatUsername(), victim.getSession().getSessionId(), expireDate, duration, policy, reason, type, victim.getSession().getMacAddress(), victim.getSession().getHost(), victim.getSession().getUid());
            World.get().getDatabaseWorker().submit(new UpdatePunishmentTransaction(punishment));
            switch (type) {
                case BAN:
                    if (policy.equals(PunishmentPolicy.HOST)) {
                        ObjectArrayList<Player> alts = World.get().getAlts(victim);
                        for(Player p : alts) {
                            if (p == null)
                                continue;
                            if (World.get().isAlt(victim, p)) {
                                victim.muted = true;
                                victim.message("You have been host muted for " + duration + " by " + agent.getFormatUsername());
                            }
                        }
                    }
                    World.get().queueLogout(victim);
                    break;
                case JAIL:

                    break;

                case MUTE:
                    if (policy.equals(PunishmentPolicy.HOST)) {
                        ObjectArrayList<Player> alts = World.get().getAlts(victim);
                        for(Player p : alts) {
                            if (p == null)
                                continue;
                            if (World.get().isAlt(victim, p)) {
                                victim.muted = true;
                                victim.message("You have been host muted for " + duration + " by " + agent.getFormatUsername());
                            }
                        }
                    }
                    victim.message("You have been" + ((policy.equals(PunishmentPolicy.HOST) ? " host " : " ") + "muted for " + duration + " by " + agent.getFormatUsername()));
                    victim.muted = true;
                    break;
            }
            agent.message("Successfully punished player.");
        } catch (Exception e) {
            agent.message("Error punishing player.");
            e.printStackTrace();
        }
    }
}
