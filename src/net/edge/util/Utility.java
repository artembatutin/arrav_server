package net.edge.util;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.world.locale.Position;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public final class Utility {
	
	/**
	 * Formats a double value to the given decimals.
	 * @param value  the value to format.
	 * @param places the amount of decimals to format.
	 * @return the value which has been formatted.
	 */
	public static double round(double value, int places) {
		if(places < 0)
			throw new IllegalArgumentException();
		
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	
	/**
	 * Converts minutes to days, hours and minutes.
	 * @param l the minutes to convert.
	 * @return an alphabetic value representing the format.
	 */
	public static String timeConvert(long l) {
		return l / 24 / 60 + "d, " + l / 60 % 24 + "h, " + l % 60 + "m";
	}
	
	/**
	 * Converts milliseconds into a time format (HH:MM:SS)
	 * @param value the milliseconds to convert.
	 * @return the time format.
	 */
	public static String convertTime(long value) {
		long milliseconds = value / 1000;
		int h = (int) (milliseconds / (3600));
		int m = (int) ((milliseconds - (h * 3600)) / 60);
		int s = (int) (milliseconds - (h * 3600) - m * 60);
		
		return String.format("%02d:%02d:%02d", h, m, s);
	}
	
	/**
	 * Gets all of the classes in a directory
	 * @param directory The directory to iterate through
	 * @return The list of classes
	 */
	public static ObjectList<Object> getClassesInDirectory(String directory) {
		ObjectList<Object> classes = new ObjectArrayList<>();
		for(File file : new File("./bin/" + directory.replace(".", "/")).listFiles()) {
			if(file.getName().contains("$")) {
				continue;
			}
			try {
				Object objectEvent = (Class.forName(directory + "." + file.getName().replace(".class", "")).newInstance());
				classes.add(objectEvent);
			} catch(InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				//e.printStackTrace();
			}
		}
		return classes;
	}
	
	/**
	 * Gets all of the sub directories of a folder
	 */
	public static ObjectList<String> getSubDirectories(Class<?> clazz) {
		String directory = "./bin/" + clazz.getPackage().getName().replace(".", "/");
		File file = new File(directory);
		String[] directories = file.list((current, name) -> new File(current, name).isDirectory());
		return new ObjectArrayList<>(directories);
	}
	
	public static boolean inside(Position source, int sourceWidth, int sourceLength, Position target, int targetWidth, int targetLength) {
		if (sourceWidth <= 0) sourceWidth = 1;
		if (sourceLength <= 0) sourceLength = 1;
		if (targetWidth <= 0) targetWidth = 1;
		if (targetLength <= 0) targetLength = 1;
		Position sourceTopRight = source.move(sourceWidth - 1, sourceLength - 1, 0);
		Position targetTopRight = target.move(targetWidth - 1, targetLength - 1, 0);
		if (source.equals(target) || sourceTopRight.equals(targetTopRight)) {
			return true;
		}
		if (source.getX() > targetTopRight.getX() || sourceTopRight.getX() < target.getX()) {
			return false;
		}
		return source.getY() <= targetTopRight.getY() && sourceTopRight.getY() >= target.getY();
	}
	
	private static int lines = 0;
	
	public static int linesInProject(File file) {
		for(final File fileEntry : file.listFiles()) {
			if(!fileEntry.isDirectory()) {
				try {
					lines += countLines(fileEntry);
				} catch(IOException e) {
					e.printStackTrace();
				}
			} else {
				linesInProject(fileEntry);
			}
		}
		return lines;
	}
	
	private static int countLines(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		int lines = 0;
		while(reader.readLine() != null)
			lines++;
		reader.close();
		return lines;
	}
}
