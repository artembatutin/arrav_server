package com.rageps.world.entity.actor.mob;

import com.rageps.world.Direction;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class MobSpawner {

    private Player player;

    private int npcSpawning = -1;

    private Direction face = Direction.NORTH;

    private int radius = -1;




    public MobSpawner(Player player) {
        this.player = player;
    }

    public void setDirection(Direction direction) {
        this.face = direction;
    }


    public void setNpcID(int id) {
        this.npcSpawning = id;
    }

    public void setRadius(int rad) {
        this.radius = rad;
    }

    public void printSpawn() {
       System.out.println("\t{\"id\": "+npcSpawning+",\"position\": {\"x\": "+player.getPosition().getX()+",\"y\": "+player.getPosition().getY()+",\"z\": "+player.getPosition().getZ()+"},\"random-walk\": "+(radius != -1)+",\"walk-radius\": "+radius+"},");
    }



}
