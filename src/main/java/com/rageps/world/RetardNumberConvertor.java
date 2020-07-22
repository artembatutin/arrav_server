package com.rageps.world;

import java.nio.ByteBuffer;

public class RetardNumberConvertor {


    public static void main(String[] args) {
        int[] convert = new int[]{33206};

        for (int  button : convert) {
            System.out.println("Clicked button " + button + ", old way:"+hexToInt(ByteBuffer.allocate(2).putShort((short) button).array())+", old way:"+(button - 23808));
        }
    }

    private static int hexToInt(byte[] data) {
        int value = 0;
        int n = 1000;
        for(byte aData : data) {
            int num = (-aData & 0xFF) * n;
            value += num;
            if(n > 1) {
                n = n / 1000;
            }
        }
        return value;
    }

}
