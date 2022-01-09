package com.rageps.util;

import com.rageps.world.locale.Position;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

public final class Utility {
	
	/**
	 * Formats a double value to the given decimals.
	 * @param value the value to format.
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
	 * Gets all of the classes in a directory
	 * @param directory The directory to iterate through
	 * @return The list of classes
	 */
	public static ObjectList<Object> getClassesInDirectory(String directory) {
		ObjectList<Object> classes = new ObjectArrayList<>();
		for(File file : new File(directory.replace(".", "/")).listFiles()) {
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
	 * Returns all the classes in a {@link Set} inside a {@link ObjectList}
	 * @param clazzSet the set of classes
	 * @return the list of classes
	 */
	public static ObjectList<Object> getClassesInSet(Set<Class<?>> clazzSet) {
		ObjectList<Object> classes = new ObjectArrayList<>();
		try {
			for(Class<?> clazz : clazzSet ) {
				Object obj = clazz.newInstance();
				classes.add(obj);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return classes;
	}

	/**
	 * Gets all of the sub directories of a folder
	 */
	public static ObjectList<String> getSubDirectories(Class<?> clazz) {
		File file = new File(clazz.getProtectionDomain().getCodeSource().getLocation().getFile());
		String[] directories = file.list((current, name) -> new File(current, name).isDirectory());
		return new ObjectArrayList<>(directories);
	}
	
	public static boolean inside(Position source, int sourceWidth, int sourceLength, Position target, int targetWidth, int targetLength) {
		if(sourceWidth <= 0)
			sourceWidth = 1;
		if(sourceLength <= 0)
			sourceLength = 1;
		if(targetWidth <= 0)
			targetWidth = 1;
		if(targetLength <= 0)
			targetLength = 1;
		Position sourceTopRight = source.move(sourceWidth - 1, sourceLength - 1, 0);
		Position targetTopRight = target.move(targetWidth - 1, targetLength - 1, 0);
		if(source.equals(target) || sourceTopRight.equals(targetTopRight)) {
			return true;
		}
		if(source.getX() > targetTopRight.getX() || sourceTopRight.getX() < target.getX()) {
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


	public static int clampSigned(int value) {
		return clamp(value, 0, Integer.MAX_VALUE);
	}

	public static int clamp(int value, int min, int max) {
		if (value < min) {
			return min;
		}
		return Math.min(value, max);
	}

	public static float clamp(float value, float min, float max) {
		if (value < min) {
			return min;
		}
		return Math.min(value, max);
	}


	public static long clamp(long value, long min, long max) {
		if (value < min) {
			return min;
		}
		return Math.min(value, max);
	}
	/** Gets a basic time based off seconds. */
	public static String getTimeTicks(long ticks) {
		long secs = ticks * 3 / 5;

		if (secs < 60) {
			return  (secs < 10 ? "0s" : secs+"s ");
		}

		long mins = secs / 60;
		long remainderSecs = secs - (mins * 60);
		if (mins < 60) {
			return mins + "m " + (remainderSecs < 10 ? "0s " : remainderSecs + "s ");
		}

		long hours = mins / 60;
		long remainderMins = mins - (hours * 60);
		if (hours < 24) {
			return hours + "h " + (remainderMins < 10 ? "0m " : remainderMins + "m ")
					+ (remainderSecs < 10 ? "0s " : remainderSecs + "s ");
		}

		long days = hours / 24;
		long remainderHrs = hours - (days * 24);
		return days + "d " + (remainderHrs < 10 ? "0h " : remainderHrs + "h ") + (remainderMins < 10 ? "0m " : remainderMins + "m ");
	}
	public static String getTime(long duration) {

		long second = (duration / 1000) % 60;
		long minute = (duration / (1000 * 60)) % 60;
		long hour = (duration / (1000 * 60 * 60)) % 24;

		return hour > 0 ? String.format("%02dh %02dm %02ds", hour, minute, second) :
				String.format("%02dm %02ds", minute, second);
	}

	/**
	 * Converts milliseconds into a time format (HH:MM:SS)
	 * @param value the milliseconds to convert.
	 * @return the time format.
	 */
	public static String convertTime(long time) {
		int secs = (int) Math.floor(time / 1000);
		int mins = (int) Math.floor(secs / 60);
		secs = secs % 60;
		int hrs = (int) Math.floor(mins / 60);
		mins = mins % 60;
		int days = (int) Math.floor(hrs / 24);
		hrs = hrs % 24;
		if (days > 0) {
			String postFix = hrs > 0 ? (hrs > 1 ? "hours" : "hour") : mins > 0 ? (mins > 1 ? "minutes" : "minute") : secs > 0 ? "seconds" : "second";
			return String.format("%d " + (days > 1 ? "days and " : " day and ") + "%02d:%02d:%02d " + postFix, days, hrs, mins, secs);
		} else if (hrs > 0) {
			return String.format("%02d:%02d:%02d " + (hrs > 1 ? "hours" : "hour"), hrs, mins, secs);
		} else if (mins > 0) {
			return String.format("%02d:%02d " + (mins > 1 ? "minutes" : "minute"), mins, secs);
		} else {
			return String.format("%02d " + (secs > 1 ? "seconds" : "second"), secs);
		}
	}
}