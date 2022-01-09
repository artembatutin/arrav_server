package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.locale.Position;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class CameraAnglePacket extends Packet {


    private final int height, movementSpeed, rotationSpeed;
    private final Position position, base;

    public CameraAnglePacket(Position position, int height, int movementSpeed, int rotationSpeed, Position base) {
        this.height = height;
        this.movementSpeed = movementSpeed;
        this.rotationSpeed = rotationSpeed;
        this.position = position;
        this.base = base;
    }

    public Position getPosition() {
        return position;
    }

    public int getHeight() {
        return height;
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public int getRotationSpeed() {
        return rotationSpeed;
    }

    public Position getBase() {
        return base;
    }
}