package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;
import net.arrav.world.locale.Position;

@CommandSignature(alias = {"regtele", "regiontele", "rtp"}, rights = {Rights.ADMINISTRATOR}, syntax = "Teleports to a region, ::regtele regionid")
public class RegTeleCommand implements Command {
    @Override
    public void execute(Player player, String[] cmd, String command) throws Exception {
        int regionId = Integer.parseInt(cmd[1]);
        int x = ((regionId >> 8) << 6) + 32;
        int y = ((regionId & 0xFF) << 6) + 32;
        int z = player.getPosition().getZ();
        if (cmd.length > 2) {
            z = Integer.parseInt(cmd[2]);
        }
        Position position = new Position(x, y, z);
        player.move(position);
    }
}
