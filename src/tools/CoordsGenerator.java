package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CoordsGenerator {
	public static void main(String[] args) throws IOException {
		Scanner input = new Scanner(System.in);
		BufferedWriter writer;
		File file = new File("Coords.txt");
		writer = new BufferedWriter(new FileWriter(file));
		System.out.println("Please enter the region id.");
		while(input.hasNextLine()) {
			if(!input.hasNextInt()) {
				break;
			}
			int regionId = input.nextInt();
			int x = (regionId >> 8) << 6;
			int y = (regionId & 0xFF) << 6;
			System.out.println("Coords : " + x + ": " + y + " Region ID: " + regionId);
			writer.append("Coords : " + x + ": " + y + " Region ID: " + regionId);
			writer.newLine();
			writer.flush();
		}
		writer.close();
	}
}