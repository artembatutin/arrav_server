package com.rageps.command.impl;

import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.world.model.Direction;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"spawn", "setnpc", "setrad", "setface"}, rights = {Rights.ADMINISTRATOR}, syntax = "Spawns npcs permanantly")
public class NpcSpawnToolCommands implements Command {

    @Override
    public void execute(Player player, String[] cmd, String command) throws Exception {
        switch (cmd[0]) {
            case "spawn": {
                player.getMobSpawner().printSpawn();
            }
            break;

            case "setnpc": {
                try {
                int npc = Integer.parseInt(cmd[1]);
                player.getMobSpawner().setNpcID(npc);
                player.message("Set npc id to @red@"+npc);
                } catch(Exception e) {
                    player.message("@red@Error setting that npc id, please check it was a real number.");
                }
            }
            break;

            case "setrad": {
                try {
                    int rad = Integer.parseInt(cmd[1]);
                    player.getMobSpawner().setRadius(rad);
                    player.message("Set radius to @red@"+rad);
                } catch(Exception e) {
                    player.message("@red@Error setting that radius, please check it was a real number.");
                }
            }
            break;


            case "setface": {
                try {
                    Direction face = Direction.valueOf(cmd[1].toUpperCase());
                    player.message("Direction set to @red@" + face.name());
                    player.getMobSpawner().setDirection(face);
                }catch (Exception e) {
                    player.message(cmd[1]+" Isn't a valid direction.");
                }
            }
            break;

        }
    }

}