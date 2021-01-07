package com.rageps.util.noise;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class Testing {

    public static void main(String[] args) {

        int width = 10, height = 10;
        int maxHeight = 100, minHeight = 0;

        int[][][] terrain = new int[width][height][maxHeight];
        for(int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
        for (int z = 0; z < width; z++) {
                   System.out.print(ImprovedNoise.noise(x,y,z)+" ");
                //terrain[x][y][(int) h] = x;
            }
        }
        System.out.println("");
        }
    }


}
