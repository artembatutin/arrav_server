package net.edge.game;

import net.edge.net.codec.login.LoginCredentialsMessage;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PlayerIO implements Runnable {
	
	/**
	 * Login requests received.
	 */
	Queue<LoginCredentialsMessage> logins = new ConcurrentLinkedQueue<>();
	
	@Override
	public void run() {
	
	}
	
	
}
