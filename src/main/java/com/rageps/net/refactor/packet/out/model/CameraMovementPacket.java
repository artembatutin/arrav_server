package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.locale.Position;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class CameraMovementPacket extends Packet {

    private final int height, movementSpeed, rotationSpeed;
    private final Position position, base;

    public CameraMovementPacket(Position position, int height, int movementSpeed, int rotationSpeed, Position base) {
        this.height = height;
        this.movementSpeed = movementSpeed;
        this.rotationSpeed = rotationSpeed;
        this.position = position;
        this.base = base;
    }

    public int getRotationSpeed() {
        return rotationSpeed;
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public int getHeight() {
        return height;
    }

    public Position getPosition() {
        return position;
    }

    public Position getBase() {
        return base;
    }
}