package com.rageps.net.refactor.packet.in.model;

import com.google.common.base.Preconditions;
import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.locale.Position;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class MovementQueuePacketPacket extends Packet {

    /**
     * The running flag.
     */
    private final boolean run;

    private final int opcode;

    /**
     * The steps.
     */
    private final Position[] steps;

    /**
     * Creates the message.
     *
     * @param steps The steps array.
     * @param run The run flag.
     */
    public MovementQueuePacketPacket(Position[] steps, boolean run, int opcode) {
        Preconditions.checkArgument(steps.length >= 0, "Number of steps cannot be negative.");
        this.steps = steps;
        this.run = run;
        this.opcode = opcode;
    }

    public int getOpcode() {
        return opcode;
    }

    /**
     * Gets the steps array.
     *
     * @return An array of steps.
     */
    public Position[] getSteps() {
        return steps;
    }

    /**
     * Checks if the steps should be ran (ctrl+click).
     *
     * @return {@code true} if so, {@code false} otherwise.
     */
    public boolean isRunning() {
        return run;
    }
}