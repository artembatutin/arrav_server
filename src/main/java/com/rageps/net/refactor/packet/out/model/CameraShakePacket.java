package com.rageps.net.refactor.packet.out.model;

import com.google.common.base.Preconditions;
import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class CameraShakePacket extends Packet {

    private final int parameter, jitter, amplitude, frequency;

    public CameraShakePacket(int parameter, int jitter, int amplitude, int frequency) {
        Preconditions.checkArgument(parameter <= 4);
        this.parameter = parameter;
        this.jitter = jitter;
        this.amplitude = amplitude;
        this.frequency = frequency;
    }

    public int getAmplitude() {
        return amplitude;
    }

    public int getFrequency() {
        return frequency;
    }

    public int getJitter() {
        return jitter;
    }

    public int getParameter() {
        return parameter;
    }
}