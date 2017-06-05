package net.edge.util;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	public static List<Object> getClassesInDirectory(String directory) {
		List<Object> classes = new ArrayList<>();
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
	public static List<String> getSubDirectories(Class<?> clazz) {
		String directory = "./bin/" + clazz.getPackage().getName().replace(".", "/");
		File file = new File(directory);
		String[] directories = file.list((current, name) -> new File(current, name).isDirectory());
		return Arrays.asList(directories);
	}
}
