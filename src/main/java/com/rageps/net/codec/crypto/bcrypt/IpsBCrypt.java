package com.rageps.net.codec.crypto.bcrypt;

import java.security.SecureRandom;
import java.util.Random;

public final class IpsBCrypt {
	private static final int DEFAULT_LOG_ROUNDS = 13;

	private IpsBCrypt() {
	}

	public static String hashpw(String password, String salt) {
		return hashpw(DEFAULT_LOG_ROUNDS, password, salt);
	}

	public static String hashpw(int logRounds, String password, String salt) {
		return BCrypt.hashpw(password, gensaltPrefix(logRounds) + salt);
	}

	public static String gensaltPrefix(int logRounds) {
		StringBuilder builder = new StringBuilder();
		builder.append("$2a$");
		if (logRounds < 10) {
			builder.append("0");
		}
		if (logRounds > 30) {
			throw new IllegalArgumentException("log_rounds exceeds maximum (30)");
		}
		builder.append(Integer.toString(logRounds));
		builder.append("$");
		return builder.toString();
	}

	public static String gensalt() {
		return gensalt(new SecureRandom());
	}

	public static String gensalt(Random random) {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < 22; i++) {
			char character;
			do {
				character = (char) (48 + random.nextInt(75));
			} while ((character >= 58 && character <= 64) || (character >= 91 && character <= 96));
			builder.append(character);
		}

		return builder.toString();
	}
}
